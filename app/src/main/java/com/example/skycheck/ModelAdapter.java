package com.example.skycheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.skycheck.model.Model;
import com.example.skycheck.model.Weather;

import java.util.List;

public class ModelAdapter extends PagerAdapter {
    private final List<Model> modelList;
    private final Context context;
    private final LayoutInflater layoutInflater;

    public ModelAdapter(List<Model> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.item_weather, container, false);

        ImageView cityImage, weatherIconImg;
        TextView cityName, cityTemp, timeCalculated;
        ImageButton itemDeleteButton = view.findViewById(R.id.itemDeleteButton);

        cityImage = view.findViewById(R.id.cityImage);
        weatherIconImg = view.findViewById(R.id.weatherIcon);
        cityName = view.findViewById(R.id.cityName);
        cityTemp = view.findViewById(R.id.cityTemp);
        timeCalculated = view.findViewById(R.id.timeCalculated);

        Model currModel = modelList.get(position);

        Weather currWeather = currModel.getWeather().get(0);

        int weatherID1 = currWeather.getId() / 100;
        int weatherID2 = currWeather.getId() % 10;
        String weatherIconStr = currWeather.getIcon();
        boolean isNight = weatherIconStr.charAt(weatherIconStr.length()-1) == 'n';

        if (isNight) {
            cityImage.setImageResource(R.drawable.bg_night);
        } else {
            if (weatherID1 <= 5) { //ThunderStorm, Rain, Drizzle
                cityImage.setImageResource(R.drawable.bg_cloudy);
            } else if (weatherID1 == 7) {
                cityImage.setImageResource(R.drawable.bg_morning);
            } else if (weatherID1 == 6) { // Snow
                cityImage.setImageResource(R.drawable.bg_snow);
            } else if (weatherID1 == 8 && weatherID2 == 0) { // clear
                cityImage.setImageResource(R.drawable.bg_evening);
            } else if (weatherID1 == 8) { // cloudy
                cityImage.setImageResource(R.drawable.bg_evening);
            }
        }

        int drawable_id = this.context.getResources()
                .getIdentifier(
                        "wic_" + weatherIconStr,
                        "drawable", this.context.getPackageName());
        weatherIconImg.setImageResource(drawable_id);

        cityName.setText(currModel.getCityName());
        cityTemp.setText(String.valueOf(currModel.getMain().getTemp()));
        timeCalculated.setText(currModel.getTimeCalculated());

        itemDeleteButton.setOnClickListener(v -> {
            modelList.remove(position);
            this.notifyDataSetChanged();
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // refresh all items when data set changed
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
