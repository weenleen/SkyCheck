package com.example.skycheck;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Locale;

public class WeatherClient {

    static private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?";
    static private final String API_KEY = "d83b956a4812baff16774a1528ddaa50";
    static private final String MEASURE_UNITS = "metric";

    private final Locale locale = Locale.getDefault();

    private final Context context;

    private final RequestQueue requestQueue;

    public WeatherClient(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void onGetCityData(String cityName, Response.Listener<JSONObject> listener) {
        String apiCall = String.format(
                locale,
                "%sq=%s&appid=%s&units=%s",
                BASE_URL, cityName, API_KEY, MEASURE_UNITS);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
                apiCall,
                null,
                listener,
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Invalid City Name", Toast.LENGTH_SHORT).show();
                });

        this.requestQueue.add(jor);
    }

    public void onGetCurrLocationData(double lat, double lon, Response.Listener<JSONObject> listener) {
        String apiCall = String.format(
                locale,
                "%slat=%f&lon=%f&appid=%s&units=%s",
                BASE_URL, lat, lon, API_KEY, MEASURE_UNITS);

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
                apiCall,
                null,
                listener,
                error -> {
                    Log.e("", "Unable to obtain current location data");
                    Toast.makeText(context, "Unable to obtain current location data", Toast.LENGTH_SHORT).show();
                });

        this.requestQueue.add(jor);

    }
}
