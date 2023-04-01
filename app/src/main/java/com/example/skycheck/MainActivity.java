package com.example.skycheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skycheck.model.Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int FINE_LOCATION_PERM_CODE = 100;
    private static final int COARSE_LOCATION_PERM_CODE = 101;

    static private final String STORAGE_FILENAME = "storage";

    private final Gson gson = new Gson();

    private TextView cityTextView, tempTextView, dateTextView;
    private EditText citySearchBar;
    private Button searchButton;
    private ViewPager modelViewPager;


    private List<Model> modelList;
    private ModelAdapter modelAdapter;
    private WeatherClient weatherClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modelList = new ArrayList<>();

        // populate modelList
        try {
            FileInputStream fis = this.getApplicationContext().openFileInput(STORAGE_FILENAME);

            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                // Error occurred when opening raw file for reading.
            } finally {
                modelList = gson.fromJson(
                        stringBuilder.toString(),
                        new TypeToken<ArrayList<Model>>() {
                        }.getType());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        modelAdapter = new ModelAdapter(modelList, this);

        modelViewPager = findViewById(R.id.modelViewPager);
        modelViewPager.setAdapter(modelAdapter);

        this.weatherClient = new WeatherClient(this, modelList, modelAdapter);


        // get current location
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERM_CODE);
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERM_CODE);
        }

        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            this.weatherClient.updateCurrLocation(
                    location.getLatitude(),
                    location.getLongitude());
        }



        this.cityTextView = findViewById(R.id.cityTextView);
        this.tempTextView = findViewById(R.id.tempTextView);
        this.dateTextView = findViewById(R.id.dateTextView);


        this.citySearchBar = findViewById(R.id.citySearchBar);
        this.searchButton = findViewById(R.id.searchButton);
        this.searchButton.setOnClickListener(v -> {
            String searchText = citySearchBar.getText().toString();
            if (TextUtils.isEmpty(searchText)) {
                citySearchBar.setError("Please enter a city");
                return;
            }
            this.weatherClient.addCity(searchText);
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == FINE_LOCATION_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        } else if (requestCode == COARSE_LOCATION_PERM_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    openFileOutput(STORAGE_FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(this.modelList.toString());
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}