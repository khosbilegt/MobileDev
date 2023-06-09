package com.example.todo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        public Resources resources;
        TextView dayOfMonthText;
        TextView dayOfWeekText;

        public ViewHolder(View itemView) {
            super(itemView);
            resources = itemView.getResources();
            layout = itemView.findViewById(R.id.calendarLayout);
            dayOfMonthText = itemView.findViewById(R.id.dayOfMonthText);
            dayOfWeekText = itemView.findViewById(R.id.dayOfWeekText);
        }
    }

    List<Date> list;
    List<Boolean> activeList;

    public CalendarAdapter(List<Date> list) {
        this.list = list;
        activeList = new ArrayList<Boolean>();
        activeList.add(true);
        for(int i = 1; i < list.size(); i++) {
            activeList.add(false);
        }
    }

    @NonNull
    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.calendar_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        handleColors(holder, position);
        handleText(holder, position);
    }

    private void handleColors(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        int staticPosition = position;
        if(activeList.get(staticPosition)) {
            holder.layout.setBackground(holder.resources.getDrawable(R.drawable.cal_active));
            holder.dayOfMonthText.setTextColor(holder.resources.getColor(R.color.active_text));
            holder.dayOfWeekText.setTextColor(holder.resources.getColor(R.color.active_text));
            return;
        }
        holder.layout.setBackgroundColor(Color.parseColor("#00000000"));
        holder.dayOfMonthText.setTextColor(Color.parseColor("#FFFFFF"));
        holder.dayOfWeekText.setTextColor(Color.parseColor("#FFFFFF"));
        holder.layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i = 0; i < activeList.size(); i++) {
                            if(i == staticPosition) {
                                activeList.set(i, true);
                            } else {
                                activeList.set(i, false);
                            }
                        }
                        MainActivity main = ((MainActivity)holder.itemView.getContext());
                        main.changeDate(list.get(position));
                        notifyDataSetChanged();
                    }
                }
        );
    }
    private void handleText(@NonNull CalendarAdapter.ViewHolder holder, int position) {
        Date date = list.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        holder.dayOfMonthText.setText(String.valueOf(day));
        holder.dayOfWeekText.setText(DateHelper.getDayOfWeek(date));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
