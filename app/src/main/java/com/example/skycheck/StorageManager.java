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
import java.util.List;

public class StorageManager {

    static private final String STORAGE_FILENAME = "storage";

    private final Context context;
    private final Gson gson = new Gson();

    public StorageManager(Context context) {
        this.context = context;
    }

    public List<Model> retrieveData() {

        List<Model> modelList = new ArrayList<>();

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

        return modelList;
    }

    public void storeData(List<Model> modelList) {
        Log.d("", "WRITING TO STORAGE... " + modelList.size());
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    this.context.openFileOutput(STORAGE_FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(modelList.toString());
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }
}
