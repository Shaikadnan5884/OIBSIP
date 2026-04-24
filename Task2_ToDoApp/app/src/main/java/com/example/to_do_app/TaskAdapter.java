package com.example.to_do_app;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onEditClick(Task task);
        void onDeleteClick(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnTaskClickListener listener) {
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvDescription.setText(task.getDescription());
        holder.tvDate.setText(task.getTaskDate());
        holder.tvCategory.setText(task.getCategory());

        if (task.getTaskTime() != null && !task.getTaskTime().isEmpty()) {
            holder.tvTime.setText(task.getTaskTime());
            holder.tvTime.setVisibility(View.VISIBLE);
        } else {
            holder.tvTime.setVisibility(View.GONE);
        }

        // Set priority indicator color
        int priorityColor;
        switch (task.getPriority()) {
            case "High":
                priorityColor = Color.parseColor("#FF5252");
                break;
            case "Medium":
                priorityColor = Color.parseColor("#FFB300");
                break;
            default:
                priorityColor = Color.parseColor("#4CAF50");
                break;
        }
        holder.viewPriorityIndicator.setBackgroundColor(priorityColor);

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(task));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvDate, tvTime, tvCategory;
        MaterialButton btnEdit, btnDelete;
        View viewPriorityIndicator;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            viewPriorityIndicator = itemView.findViewById(R.id.viewPriorityIndicator);
        }
    }
}
