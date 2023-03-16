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

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dailyDay;
        public TextView dailyTemperature;
        public ImageView imageView;
        public Resources resources;

        public ViewHolder(View itemView) {
            super(itemView);
            dailyDay = itemView.findViewById(R.id.text_daily_day);
            dailyTemperature = itemView.findViewById(R.id.text_daily_temp);
            imageView = itemView.findViewById(R.id.image_daily);
            resources = itemView.getResources();
        }
    }
    private List<Weather> weatherList;
    DailyFragment fragment;

    public DailyAdapter(List<Weather> weatherList, DailyFragment fragment) {
        this.weatherList = weatherList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public DailyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View weatherView = inflater.inflate(R.layout.item_daily, parent, false);
        ViewHolder viewHolder = new ViewHolder(weatherView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DailyAdapter.ViewHolder holder, int position) {
        setTemperature(holder, position);
        setDay(holder, position);
        setIcon(holder, position);
    }

    private void setTemperature(@NonNull DailyAdapter.ViewHolder holder, int position) {
        TextView dailyTemp = holder.dailyTemperature;
        dailyTemp.setText(String.valueOf(weatherList.get(position).getTempInt()));
    }

    private void setDay(@NonNull DailyAdapter.ViewHolder holder, int position) {
        TextView dailyDay = holder.dailyDay;
        dailyDay.setText(String.valueOf(weatherList.get(position).getDayOfWeek()));
    }

    private void setIcon(@NonNull DailyAdapter.ViewHolder holder, int position) {
        String icon = weatherList.get(position).getIcon();
        String url = "https://openweathermap.org/img/wn/" + icon + ".png";
        ImageView imageView = holder.imageView;
        new AsyncDownloadImage((ImageView) imageView).execute(url);
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
