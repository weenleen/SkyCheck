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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.skycheck.model.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int FINE_LOCATION_PERM_CODE = 100;

    private EditText citySearchBar;
    private FloatingActionButton addCityButton;

    private ImageButton refreshButton;
    private ViewPager modelViewPager;


    private List<Model> modelList;
    private ModelAdapter modelAdapter;
    private WeatherClient weatherClient;
    private StorageManager storageManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.storageManager = new StorageManager(this.getApplicationContext());

        this.modelList = this.storageManager.retrieveData();
        this.modelList.add(0, null); // set current location as unknown

        this.modelAdapter = new ModelAdapter(this.modelList, this);

        this.modelViewPager = findViewById(R.id.modelViewPager);
        this.modelViewPager.setAdapter(modelAdapter);

        this.weatherClient = new WeatherClient(this.getApplicationContext(), modelList, modelAdapter);


        // get current location
        Location location = accessFineLocation();
        if (location != null) {
            Log.e("", "LOCATION RETRIEVED");
            this.weatherClient.updateCurrLocation(
                    location.getLatitude(),
                    location.getLongitude());
        } else {
            Toast.makeText(this, "Unable to get current location data", Toast.LENGTH_SHORT).show();
        }

        this.citySearchBar = findViewById(R.id.citySearchBar);

        this.addCityButton = findViewById(R.id.addCityButton);
        this.addCityButton.setOnClickListener(v -> {
            String searchText = citySearchBar.getText().toString();
            if (TextUtils.isEmpty(searchText)) {
                citySearchBar.setError("Please enter a city");
            } else {
                this.weatherClient.addCity(searchText);
            }
        });

        this.refreshButton = findViewById(R.id.refreshButton);
        this.refreshButton.setOnClickListener(v -> {
            Location tmpLoc = accessFineLocation();
            if (tmpLoc != null) {
                Log.e("", "LOCATION RETRIEVED");
                this.weatherClient.updateCurrLocation(
                        tmpLoc.getLatitude(),
                        tmpLoc.getLongitude());
            } else {
                Toast.makeText(this, "Unable to get current location data", Toast.LENGTH_SHORT).show();
            }
            this.weatherClient.refreshModelList();
        });

    }


    private Location accessFineLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("", "ACCESS_FINE_LOCATION DENIED");
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERM_CODE);
        } else {
            // permission granted
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        }
        return null;
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
                Toast.makeText(MainActivity.this, "Fine Location Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "Fine Location Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        List<Model> newList = new ArrayList<>(this.modelList);
        newList.remove(0);
        this.storageManager.storeData(newList);
    }
}