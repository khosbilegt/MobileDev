package com.example.weatherapp;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hourlyTemperature;
        public TextView hourlyTime;
        public TextView typeText;
        public ImageView imageView;
        public Resources resources;

        public ViewHolder(View itemView) {
            super(itemView);
            hourlyTime = itemView.findViewById(R.id.text_daily_day);
            hourlyTemperature = itemView.findViewById(R.id.text_daily_temp);
            imageView = itemView.findViewById(R.id.image_daily);
            resources = itemView.getResources();
        }
    }
    private List<Weather> weatherList;
    HourlyFragment fragment;

    public HourlyAdapter(List<Weather> weatherList, HourlyFragment fragment) {
        this.weatherList = weatherList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public HourlyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View weatherView = inflater.inflate(R.layout.item_hourly, parent, false);
        ViewHolder viewHolder = new ViewHolder(weatherView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyAdapter.ViewHolder holder, int position) {
        setTemperature(holder, position);
        setIcon(holder, position);
        setTime(holder, position);
    }

    private void setTemperature(@NonNull HourlyAdapter.ViewHolder holder, int position) {
        TextView hourlyTemp = holder.hourlyTemperature;
        hourlyTemp.setText(String.valueOf(weatherList.get(position).getTempInt()));
        if(position == 0) {
            TextView currentTemp = fragment.getActivity().findViewById(R.id.current_temp_text);
            currentTemp.setText(String.valueOf(weatherList.get(0).getTempInt()) + "°");
            TextView typeText = fragment.getActivity().findViewById(R.id.type_text);
            typeText.setText(weatherList.get(0).getDescription());
        }
    }

    private void setTime(@NonNull HourlyAdapter.ViewHolder holder, int position) {
        TextView hourlyTime = holder.hourlyTime;
        if(position == 0) {
            hourlyTime.setText("Now");
            return;
        }
        //hourlyTime.setText(String.valueOf(weatherList.get(position).getTime()));
        hourlyTime.setText(weatherList.get(position).getTimeAmPm());
    }

    private void setIcon(@NonNull HourlyAdapter.ViewHolder holder, int position) {
        String icon = weatherList.get(position).getIcon();
        String imageName = "w" + icon;
        Context context = fragment.getContext();
        int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
        ImageView imageView = holder.imageView;
        imageView.setImageDrawable(context.getResources().getDrawable(resourceId));
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
