package com.example.skycheck;

import android.content.Context;
import android.util.Log;

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
import java.util.Collections;
import java.util.List;

public class StorageManager {

    static private final String DEFAULT_DATA = "[{\"base\":\"stations\",\"id\":5128581,\"name\":\"New York\",\"clouds\":{\"all\":100},\"cod\":200,\"coord\":{\"lat\":40.7143,\"lon\":-74.006},\"main\":{\"feels_like\":11.39,\"humidity\":70,\"pressure\":996,\"temp\":12.28,\"temp_max\":13.33,\"temp_min\":10.13},\"sys\":{\"country\":\"US\",\"id\":2039034,\"sunrise\":1680345590,\"sunset\":1680391184,\"type\":2},\"dt\":1680406059,\"timezone\":-14400,\"visibility\":10000,\"weather\":[{\"description\":\"overcast clouds\",\"icon\":\"04n\",\"id\":804,\"main\":\"Clouds\"}],\"wind\":{\"deg\":360.0,\"gust\":12.52,\"speed\":7.6}}, {\"base\":\"stations\",\"id\":1275339,\"name\":\"Mumbai\",\"clouds\":{\"all\":20},\"cod\":200,\"coord\":{\"lat\":19.0144,\"lon\":72.8479},\"main\":{\"feels_like\":28.76,\"humidity\":69,\"pressure\":1012,\"temp\":26.99,\"temp_max\":26.99,\"temp_min\":26.94},\"sys\":{\"country\":\"IN\",\"id\":9052,\"sunrise\":1680397328,\"sunset\":1680441735,\"type\":1},\"dt\":1680406389,\"timezone\":19800,\"visibility\":2500,\"weather\":[{\"description\":\"haze\",\"icon\":\"50d\",\"id\":721,\"main\":\"Haze\"}],\"wind\":{\"deg\":30.0,\"speed\":2.57}}, {\"base\":\"stations\",\"id\":1273294,\"name\":\"Delhi\",\"clouds\":{\"all\":20},\"cod\":200,\"coord\":{\"lat\":28.6667,\"lon\":77.2167},\"main\":{\"feels_like\":21.38,\"humidity\":83,\"pressure\":1011,\"temp\":21.05,\"temp_max\":21.05,\"temp_min\":21.05},\"sys\":{\"country\":\"IN\",\"id\":9165,\"sunrise\":1680396033,\"sunset\":1680440933,\"type\":1},\"dt\":1680406432,\"timezone\":19800,\"visibility\":1500,\"weather\":[{\"description\":\"mist\",\"icon\":\"50d\",\"id\":701,\"main\":\"Mist\"}],\"wind\":{\"deg\":0.0,\"speed\":0.0}}, {\"base\":\"stations\",\"id\":2147714,\"name\":\"Sydney\",\"clouds\":{\"all\":97},\"cod\":200,\"coord\":{\"lat\":-33.8679,\"lon\":151.2073},\"main\":{\"feels_like\":17.14,\"humidity\":90,\"pressure\":1018,\"temp\":17.03,\"temp_max\":19.31,\"temp_min\":15.76},\"rain\":{\"1h\":1.15},\"sys\":{\"country\":\"AU\",\"id\":2002865,\"sunrise\":1680379639,\"sunset\":1680421820,\"type\":2},\"dt\":1680405913,\"timezone\":36000,\"visibility\":10000,\"weather\":[{\"description\":\"moderate rain\",\"icon\":\"10d\",\"id\":501,\"main\":\"Rain\"}],\"wind\":{\"deg\":72.0,\"gust\":4.47,\"speed\":2.24}}, {\"base\":\"stations\",\"id\":4163971,\"name\":\"Melbourne\",\"clouds\":{\"all\":0},\"cod\":200,\"coord\":{\"lat\":28.0836,\"lon\":-80.6081},\"main\":{\"feels_like\":24.06,\"humidity\":78,\"pressure\":1018,\"temp\":23.61,\"temp_max\":25.54,\"temp_min\":22.24},\"sys\":{\"country\":\"US\",\"id\":2007578,\"sunrise\":1680347566,\"sunset\":1680392377,\"type\":2},\"dt\":1680406360,\"timezone\":-14400,\"visibility\":10000,\"weather\":[{\"description\":\"clear sky\",\"icon\":\"01n\",\"id\":800,\"main\":\"Clear\"}],\"wind\":{\"deg\":262.0,\"gust\":4.92,\"speed\":4.02}}]";

    static private final String STORAGE_FILENAME = "storage";

    private final Context context;
    private final Gson gson = new Gson();

    public StorageManager(Context context) {
        this.context = context;
    }

    public List<Model> retrieveData() {

        List<Model> modelList = null;

        try {
            FileInputStream fis = this.context.openFileInput(STORAGE_FILENAME);

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

        if (modelList == null) {
            modelList = gson.fromJson(
                    DEFAULT_DATA,
                    new TypeToken<ArrayList<Model>>() {
                    }.getType());
        }

        modelList.removeAll(Collections.singleton(null));

        return modelList;
    }

    public void storeData(List<Model> modelList) {
        Log.d("", "WRITING TO STORAGE... " + modelList.size());
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    this.context.openFileOutput(STORAGE_FILENAME, Context.MODE_PRIVATE));
            Log.e("", modelList.toString());
            outputStreamWriter.write(modelList.toString());
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }
}
