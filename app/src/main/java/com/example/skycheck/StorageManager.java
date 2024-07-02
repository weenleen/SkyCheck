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

    static private final String DEFAULT_DATA = "[{\"base\":\"stations\",\"id\":5128581,\"name\":\"New York\",\"clouds\":{\"all\":100},\"cod\":200,\"coord\":{\"lat\":40.7143,\"lon\":-74.006},\"main\":{\"feels_like\":10.32,\"humidity\":71,\"pressure\":997,\"temp\":11.28,\"temp_max\":12.31,\"temp_min\":9.73},\"rain\":{\"1h\":1.07},\"sys\":{\"country\":\"US\",\"id\":2039034,\"sunrise\":1680431891,\"sunset\":1680477647,\"type\":2},\"dt\":1680408035,\"timezone\":-14400,\"visibility\":10000,\"weather\":[{\"description\":\"moderate rain\",\"icon\":\"10n\",\"id\":501,\"main\":\"Rain\"}],\"wind\":{\"deg\":310.0,\"gust\":14.92,\"speed\":8.23}}, {\"base\":\"stations\",\"id\":1275339,\"name\":\"Mumbai\",\"clouds\":{\"all\":20},\"cod\":200,\"coord\":{\"lat\":19.0144,\"lon\":72.8479},\"main\":{\"feels_like\":28.76,\"humidity\":69,\"pressure\":1012,\"temp\":26.99,\"temp_max\":27.94,\"temp_min\":26.99},\"sys\":{\"country\":\"IN\",\"id\":9052,\"sunrise\":1680397328,\"sunset\":1680441735,\"type\":1},\"dt\":1680407989,\"timezone\":19800,\"visibility\":2500,\"weather\":[{\"description\":\"haze\",\"icon\":\"50d\",\"id\":721,\"main\":\"Haze\"}],\"wind\":{\"deg\":30.0,\"speed\":2.57}}, {\"base\":\"stations\",\"id\":1273294,\"name\":\"Delhi\",\"clouds\":{\"all\":40},\"cod\":200,\"coord\":{\"lat\":28.6667,\"lon\":77.2167},\"main\":{\"feels_like\":23.19,\"humidity\":68,\"pressure\":1011,\"temp\":23.05,\"temp_max\":23.05,\"temp_min\":23.05},\"sys\":{\"country\":\"IN\",\"id\":9165,\"sunrise\":1680396033,\"sunset\":1680440933,\"type\":1},\"dt\":1680408517,\"timezone\":19800,\"visibility\":1800,\"weather\":[{\"description\":\"haze\",\"icon\":\"50d\",\"id\":721,\"main\":\"Haze\"}],\"wind\":{\"deg\":220.0,\"speed\":1.54}}, {\"base\":\"stations\",\"id\":2147714,\"name\":\"Sydney\",\"clouds\":{\"all\":75},\"cod\":200,\"coord\":{\"lat\":-33.8679,\"lon\":151.2073},\"main\":{\"feels_like\":17.17,\"humidity\":89,\"pressure\":1018,\"temp\":17.08,\"temp_max\":19.75,\"temp_min\":15.97},\"rain\":{\"1h\":1.3},\"sys\":{\"country\":\"AU\",\"id\":2002865,\"sunrise\":1680379639,\"sunset\":1680421820,\"type\":2},\"dt\":1680407929,\"timezone\":36000,\"visibility\":9000,\"weather\":[{\"description\":\"light intensity shower rain\",\"icon\":\"09d\",\"id\":520,\"main\":\"Rain\"}],\"wind\":{\"deg\":30.0,\"speed\":5.66}}, {\"base\":\"stations\",\"id\":4163971,\"name\":\"Melbourne\",\"clouds\":{\"all\":0},\"cod\":200,\"coord\":{\"lat\":28.0836,\"lon\":-80.6081},\"main\":{\"feels_like\":23.49,\"humidity\":80,\"pressure\":1018,\"temp\":23.04,\"temp_max\":24.98,\"temp_min\":21.71},\"sys\":{\"country\":\"US\",\"id\":2007578,\"sunrise\":1680433898,\"sunset\":1680478809,\"type\":2},\"dt\":1680408254,\"timezone\":-14400,\"visibility\":10000,\"weather\":[{\"description\":\"clear sky\",\"icon\":\"01n\",\"id\":800,\"main\":\"Clear\"}],\"wind\":{\"deg\":259.0,\"gust\":5.36,\"speed\":4.47}}, {\"base\":\"stations\",\"id\":1880252,\"name\":\"Singapore\",\"clouds\":{\"all\":40},\"cod\":200,\"coord\":{\"lat\":1.2897,\"lon\":103.8501},\"main\":{\"feels_like\":35.38,\"humidity\":71,\"pressure\":1010,\"temp\":30.04,\"temp_max\":30.95,\"temp_min\":28.91},\"sys\":{\"country\":\"SG\",\"id\":9470,\"sunrise\":1680390267,\"sunset\":1680433918,\"type\":1},\"dt\":1680408530,\"timezone\":28800,\"visibility\":10000,\"weather\":[{\"description\":\"scattered clouds\",\"icon\":\"03d\",\"id\":802,\"main\":\"Clouds\"}],\"wind\":{\"deg\":0.0,\"speed\":2.06}}]";
    static private final String STORAGE_FILENAME = "storage";

    private final Context context;
    private final Gson gson = new Gson();

    public StorageManager(Context context) {
        this.context = context;
    }

    public List<Model> retrieveData() {

        List<Model> storedList = null;

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
                storedList = gson.fromJson(
                        stringBuilder.toString(),
                        new TypeToken<ArrayList<Model>>() {
                        }.getType());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (storedList == null) {
            storedList = gson.fromJson(
                    DEFAULT_DATA,
                    new TypeToken<ArrayList<Model>>() {
                    }.getType());
        }

        storedList.removeAll(Collections.singleton(null));

        return storedList;
    }

    public void storeData(List<Model> storedList) {
        Log.d("", "WRITING TO STORAGE... " + storedList.size());
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    this.context.openFileOutput(STORAGE_FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(storedList.toString());
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }
}
