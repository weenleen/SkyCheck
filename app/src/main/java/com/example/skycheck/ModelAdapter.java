package com.example.skycheck;

import static android.view.View.GONE;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.skycheck.model.Model;
import com.example.skycheck.model.Weather;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class ModelAdapter extends PagerAdapter {
    private final List<Model> storedList;
    private final List<Model> adapterList;
    private final Context context;
    private final LayoutInflater layoutInflater;

    private static final DecimalFormat df = new DecimalFormat("#.#");

    public ModelAdapter(List<Model> adapterList, List<Model> storedList, Context context) {
        this.adapterList = adapterList;
        this.storedList = storedList;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return adapterList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        Model currLocation = adapterList.get(0);
        Model currModel = adapterList.get(position);

        final View view;
        if (currModel == null) {
            view = layoutInflater.inflate(R.layout.item_error, container, false);
            container.addView(view, 0);
            return view;
        }


        Weather currWeather = currModel.getWeather().get(0);

        int weatherID1 = currWeather.getId() / 100;
        int weatherID2 = currWeather.getId() % 10;
        String weatherIconStr = currWeather.getIcon();
        boolean isNight = weatherIconStr.charAt(weatherIconStr.length()-1) == 'n';
        boolean isDark = false;
        int bg_id = R.drawable.bg_noon;

        if (isNight) {
            bg_id = R.drawable.bg_cloudy;
            isDark = true;
        } else {
            if (weatherID1 <= 5) { //ThunderStorm, Rain, Drizzle
                bg_id = R.drawable.bg_cloudy;
                isDark = true;
            } else if (weatherID1 == 7) {
                bg_id = R.drawable.bg_morning;
            } else if (weatherID1 == 6) { // Snow
                bg_id = R.drawable.bg_snow;
            } else if (weatherID1 == 8 && weatherID2 != 0) { // cloudy
                bg_id = R.drawable.bg_evening;
                isDark = true;
            }
        }

        // get view
        if (isDark) {
            view = layoutInflater.inflate(R.layout.item_weather_dark, container, false);
        } else {
            view = layoutInflater.inflate(R.layout.item_weather, container, false);
        }

        ImageView cityImage, weatherIconImg;
        TextView cityName, cityTemp, timeCalculated, cityWeatherDesc, currentLocationFlag;
        ImageButton itemDeleteButton = view.findViewById(R.id.itemDeleteButton);

        cityImage = view.findViewById(R.id.cityImage);
        weatherIconImg = view.findViewById(R.id.weatherIcon);
        cityName = view.findViewById(R.id.cityName);
        cityTemp = view.findViewById(R.id.cityTemp);
        cityWeatherDesc = view.findViewById(R.id.cityWeatherDesc);
        timeCalculated = view.findViewById(R.id.timeCalculated);
        // if the city is not the current location, hide the flag
        currentLocationFlag = view.findViewById(R.id.currentLocation);
        if (position != 0) currentLocationFlag.setVisibility(GONE);

        cityImage.setImageResource(bg_id);

        Resources res = this.context.getResources();

        int drawable_id = res.getIdentifier(
                "wic_" + weatherIconStr,
                "drawable", this.context.getPackageName());
        weatherIconImg.setImageResource(drawable_id);

        cityName.setText(currModel.getCityName());
        cityTemp.setText(
                String.format(
                        res.getString(R.string.celsius),
                        df.format(currModel.getTemp())));
        cityWeatherDesc.setText(currWeather.getDesc());
        timeCalculated.setText(
                String.format(
                        res.getString(R.string.update_date_time),
                        currModel.getTimeCalculated()));

        if (position == 0) {
            itemDeleteButton.setVisibility(GONE);
        } else {
            // Set animation on deletion
            Animation anim = AnimationUtils.loadAnimation(this.context, R.anim.anim_dismiss);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationRepeat(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(GONE);
                    container.postDelayed(() -> {
                        adapterList.remove(currModel);
                        storedList.remove(currModel); // update stored data
                        notifyDataSetChanged();
                    }, 200);
                }
            });

            itemDeleteButton.setOnClickListener(v -> view.startAnimation(anim));
        }

        if (currLocation != null
                && position != 0
                && Objects.equals(currLocation.getCityName(), currModel.getCityName())) {
            view.setVisibility(GONE);
        }

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
