package com.example.skycheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.skycheck.model.Model;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?";
    static private final String API_KEY = "d83b956a4812baff16774a1528ddaa50";
    static private final String MEASURE_UNITS = "metric";

    private final Locale locale = Locale.getDefault();

    private final Gson gson = new Gson();


    private TextView cityTextView, tempTextView, dateTextView;
    private EditText citySearchBar;
    private Button searchButton;
    private ViewPager modelViewPager;


    private List<Model> modelList;
    private ModelAdapter modelAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

            findWeather(searchText);
        });


        modelList = new ArrayList<>();
        // add all models to the list

        modelAdapter = new ModelAdapter(modelList, this);

        modelViewPager = findViewById(R.id.modelViewPager);
        modelViewPager.setAdapter(modelAdapter);

        loadCityCards();

    }

    public void loadCityCards() {

    }

    public void findWeather(String cityName) {
        String apiCall = String.format(
                locale,
                "%sq=%s&appid=%s&units=%s",
                BASE_URL, cityName, API_KEY, MEASURE_UNITS);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
                apiCall,
                null,
                response -> {
                    try {
//                        String cityName = response.getString("name");
//                        Log.e("", response.toString());

                        Model responseModel = this.gson.fromJson(response.toString(), Model.class);
//                        String temp = String.valueOf(responseModel.getMain().getTemp());
//                        String dt = String.valueOf(responseModel.getTimeCalculated());
//                        String responseCity = responseModel.getCityName();
//
//                        tempTextView.setText(temp);
//                        cityTextView.setText(responseCity);
//                        dateTextView.setText(dt);

                        modelList.add(responseModel);
                        this.modelAdapter.notifyDataSetChanged();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    error.printStackTrace();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jor);
    }

    public void findWeather(float lat, float lon) {
        String apiCall = String.format(
                locale,
                "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&appid=%s&units=%s",
                lat, lon,
                API_KEY, MEASURE_UNITS);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
                apiCall,
                null,
                response -> {
                    try {
                        String cityName = response.getString("name");
                        JSONObject mainObj = response.getJSONObject("main");
                        String temp = String.valueOf(mainObj.getDouble("temp"));
                        String dt = String.valueOf(response.getLong("dt"));

                        tempTextView.setText(temp);
                        cityTextView.setText(cityName);
                        dateTextView.setText(dt);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    error.printStackTrace();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jor);
    }
}