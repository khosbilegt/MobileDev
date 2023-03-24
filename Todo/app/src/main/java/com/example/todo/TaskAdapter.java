package com.example.todo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Resources resources;
        ConstraintLayout layout;
        CheckBox checkBox;
        TextView textTitle;
        TextView textTimeLeft;

        public ViewHolder(View itemView) {
            super(itemView);
            resources = itemView.getResources();
            layout = itemView.findViewById(R.id.itemLayout);
            checkBox = itemView.findViewById(R.id.taskIsDone);
            textTitle = itemView.findViewById(R.id.taskTitle);
            textTimeLeft = itemView.findViewById(R.id.taskTimeLeft);

            layout.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {

                    return true;
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        textTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        textTimeLeft.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        return;
                    }
                    textTitle.setPaintFlags( textTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    textTimeLeft.setPaintFlags( textTimeLeft.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
            });

        }
    }

    List<Task> list;

    public TaskAdapter(List<Task> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.task_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        String itemText = list.get(position).getTitle();
        holder.textTitle.setText(itemText);
        holder.textTimeLeft.setText(Task.timeLeft(list.get(position).getFinishDate()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
