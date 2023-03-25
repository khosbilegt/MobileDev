package com.example.todo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
        Button sideView;
        private static final int ADD_CODE = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            resources = itemView.getResources();
            layout = itemView.findViewById(R.id.itemLayout);
            checkBox = itemView.findViewById(R.id.taskIsDone);
            textTitle = itemView.findViewById(R.id.taskTitle);
            textTimeLeft = itemView.findViewById(R.id.taskTimeLeft);
            sideView = itemView.findViewById(R.id.sideView);
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
        holder.textTitle.setTextColor(holder.resources.getColor(R.color.white));
        holder.textTimeLeft.setText(Task.timeLeft(list.get(position).getFinishDate()));

        int staticPosition = position;
        Task task = list.get(staticPosition);

        // Long Click Edit
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), AddActivity.class);
                intent.putExtra("id", task.getId());
                ((Activity)holder.itemView.getContext()).startActivityForResult(intent, holder.ADD_CODE);
                return true;
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseHelper dbHandler = new DatabaseHelper(holder.itemView.getContext());
                task.setDone(isChecked);
                dbHandler.modifyTask(task.getId(), task);
                if(isChecked) {
                    holder.textTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.textTimeLeft.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.sideView.setBackground(holder.resources.getDrawable(R.drawable.side_done));
                    return;
                }
                holder.textTitle.setPaintFlags( holder.textTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                holder.textTimeLeft.setPaintFlags( holder.textTimeLeft.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                holder.sideView.setBackground(holder.resources.getDrawable(R.drawable.side_rest));
            }
        });

        // CheckBox Logic
        holder.checkBox.setChecked(task.isDone());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
