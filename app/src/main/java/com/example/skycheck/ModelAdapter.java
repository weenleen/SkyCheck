package com.example.skycheck;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.skycheck.model.Model;

import java.util.List;

public class ModelAdapter extends PagerAdapter {
    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public ModelAdapter(List<Model> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_weather, container, false);

        ImageView cityImage;
        TextView cityName, cityTemp, timeCalculated;

        cityImage = view.findViewById(R.id.cityImage);
        cityName = view.findViewById(R.id.cityName);
        cityTemp = view.findViewById(R.id.cityTemp);
        timeCalculated = view.findViewById(R.id.timeCalculated);

//        imageView.setImageResource(models.get(position).getImage());
        cityName.setText(models.get(position).getCityName());
        cityTemp.setText(String.valueOf(models.get(position).getMain().getTemp()));
        timeCalculated.setText(String.valueOf(models.get(position).getTimeCalculated()));

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, DetailActivity.class);
//                intent.putExtra("param", models.get(position).getTitle());
//                context.startActivity(intent);
//                // finish();
//            }
//        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
