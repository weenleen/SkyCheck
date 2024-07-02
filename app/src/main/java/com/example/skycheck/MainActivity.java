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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int FINE_LOCATION_PERM_CODE = 100;

    private final Gson gson = new Gson();

    private EditText citySearchBar;
    private FloatingActionButton addCityButton;

    private ImageButton refreshButton;
    private ViewPager modelViewPager;

    private List<Model> adapterList; // list used by adapter
    private ModelAdapter modelAdapter;
    private List<Model> storedList; // list of stored cities
    private WeatherClient weatherClient;
    private StorageManager storageManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.weatherClient = new WeatherClient(this.getApplicationContext());

        // retrieve stored data
        this.storageManager = new StorageManager(this.getApplicationContext());
        this.storedList = this.storageManager.retrieveData();

        this.adapterList = new ArrayList<>();
        this.adapterList.add(null);
        this.adapterList.addAll(this.storedList);

        this.modelAdapter = new ModelAdapter(this.adapterList, this.storedList, this);

        this.modelViewPager = findViewById(R.id.modelViewPager);
        this.modelViewPager.setAdapter(modelAdapter);

        updateCurrLocation(); // update current location and adapter list

        this.citySearchBar = findViewById(R.id.citySearchBar);

        this.addCityButton = findViewById(R.id.addCityButton);
        this.addCityButton.setOnClickListener(v -> {
            // get city name string
            String searchText = citySearchBar.getText().toString();
            if (TextUtils.isEmpty(searchText)) {
                citySearchBar.setError("Please enter a city");
                return;
            }

            // retrieve weather data
            this.weatherClient.onGetCityData(
                    searchText,
                    response -> {
                    try {
                        Model responseModel = this.gson.fromJson(response.toString(), Model.class);

                        // check for duplicates in stored list
                        if (!this.storedList.contains(responseModel)) {
                            // to add to stored list
                            this.storedList.add(responseModel);

                            // update adapterList
                            Model currLocModel = this.adapterList.isEmpty() ? null : this.adapterList.get(0);
                            if (!responseModel.equals(currLocModel)) {
                                // if new city is not the current location, add to adapter list
                                this.adapterList.add(responseModel);
                                this.modelAdapter.notifyDataSetChanged();
                                setPrimaryCity(responseModel);
                            } else {
                                // if the new city is the current location, scroll to it
                                // no change in adapterList
                                this.modelViewPager.setCurrentItem(0, true);
                            }
                        } else {
                            // if already stored, check the position in the adapter list
                            setPrimaryCity(responseModel);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        });

        // refresh current location
        this.refreshButton = findViewById(R.id.refreshButton);
        this.refreshButton.setOnClickListener(v -> updateCurrLocation());

    }

    private void setPrimaryCity(Model m) {
        for (int i = 0; i < this.adapterList.size(); i++) {
            Model tmp = this.adapterList.get(i);
            if (m.equals(tmp)) {
                this.modelViewPager.setCurrentItem(i);
                return;
            }
        }
    }
    
    private void updateCurrLocation() {
        Location location = accessFineLocation();
        if (location != null) {
            Log.e("", "LOCATION RETRIEVED");
            this.weatherClient.onGetCurrLocationData(
                    location.getLatitude(),
                    location.getLongitude(),
                    response -> {
                        adapterList.set(0, null);

                        try {
                            Model responseModel = this.gson.fromJson(response.toString(), Model.class);
                            if (responseModel != null) {
                                // check for duplicates
                                for (int i = 1; i < adapterList.size(); i++) {
                                    Model tmpModel = adapterList.get(i);
                                    if (tmpModel.equals(responseModel)) {
                                        adapterList.remove(tmpModel); // remove duplicate
                                        break;
                                    }
                                }
                                // set as current location
                                adapterList.set(0, responseModel);
                            }
                        } catch (Exception e) {
                            Log.e("", "Json error");
                            Toast.makeText(this, "Unable to refresh data", Toast.LENGTH_SHORT).show();
                        }

                        modelAdapter.notifyDataSetChanged();
                    });
        } else {
            Toast.makeText(this, "Unable to get current location data", Toast.LENGTH_SHORT).show();
        }
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
            return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
        // save data
        this.storageManager.storeData(this.storedList);
    }
}