package com.example.skycheck;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.skycheck.model.Model;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

        for (@Nullable Model m : modelList) {
            if (m != null && Objects.equals(m.getCityName(), cityName)) return;
        }

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
        Log.e("", "UPDATING CURRENT LOCATION");
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
                        if (modelList.size() < 1) {
                            modelList.add(responseModel);
                        } else {
                            modelList.set(0, responseModel);
                        }
                        modelAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("", "Json error");
                    }
                },
                error -> {
                    Log.e("", "Unable to obtain current location data");
                    Toast.makeText(context, "Unable to obtain current location data", Toast.LENGTH_SHORT).show();
                });

        this.requestQueue.add(jor);
    }

    public void refreshModelList() {
        for (int i = 1; i < this.modelList.size(); i++) {
            updateItem(this.modelList.get(i));
        }
    }


    private void updateItem(Model m) {

        if (m == null) return;

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
