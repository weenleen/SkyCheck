package com.example.skycheck;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.skycheck.model.Model;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

public class WeatherClient {

    static private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?";
    static private final String API_KEY = "d83b956a4812baff16774a1528ddaa50";
    static private final String MEASURE_UNITS = "metric";

    private final Locale locale = Locale.getDefault();
    private final Gson gson = new Gson();

    private final List<Model> modelList;
    private final ModelAdapter modelAdapter;
    private final Context context;

    private final RequestQueue requestQueue;

    public WeatherClient(Context context, List<Model> modelList, ModelAdapter modelAdapter) {
        this.context = context;
        this.modelList = modelList;
        this.modelAdapter = modelAdapter;
        this.requestQueue = Volley.newRequestQueue(context);
    }


    public void addCity(String cityName) {
        String apiCall = String.format(
                locale,
                "%sq=%s&appid=%s&units=%s",
                BASE_URL, cityName, API_KEY, MEASURE_UNITS);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
                apiCall,
                null,
                response -> {
                    try {
                        Model responseModel = this.gson.fromJson(response.toString(), Model.class);
                        refreshModelList();
                        modelList.add(responseModel);
                        modelAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                });

        this.requestQueue.add(jor);
    }

    public void updateCurrLocation(double lat, double lon) {
        String apiCall = String.format(
                locale,
                "%slat=%f&lon=%f&appid=%s&units=%s",
                BASE_URL, lat, lon, API_KEY, MEASURE_UNITS);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
                apiCall,
                null,
                response -> {
                    try {
                        Model responseModel = this.gson.fromJson(response.toString(), Model.class);
                        modelList.set(0, responseModel);
                        modelAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Unable to obtain current location data", Toast.LENGTH_SHORT).show();
                });

        this.requestQueue.add(jor);
    }

    public void refreshModelList() {
        for (Model m : this.modelList) {
            updateItem(m);
        }
    }


    private void updateItem(Model m) {

        String apiCall = String.format(
                locale,
                "%sq=%s&appid=%s&units=%s",
                BASE_URL, m.getCityName(), API_KEY, MEASURE_UNITS);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
                apiCall,
                null,
                response -> {
                    try {
                        Model responseModel = this.gson.fromJson(response.toString(), Model.class);
                        modelList.set(this.modelList.indexOf(m), responseModel);
                        modelAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                });

        this.requestQueue.add(jor);
    }
}
