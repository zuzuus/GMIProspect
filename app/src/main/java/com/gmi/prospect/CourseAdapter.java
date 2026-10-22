package com.gmi.prospect;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmi.prospect.databinding.ItemCourseBinding;

import java.util.ArrayList;

// Adapter to show the courses in the RecyclerView
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private ArrayList<Course> courseList;

    public CourseAdapter(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    // Called when the filter button is pressed or when data arrives from the web
    public void updateList(ArrayList<Course> newList) {
        courseList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Data binding: inflate item_course.xml through its binding class
        ItemCourseBinding binding = ItemCourseBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.binding.tvCategory.setText(course.getCategory());
        holder.binding.tvName.setText(course.getName());
        holder.binding.tvDuration.setText("Duration: " + course.getDuration());

        // Show entry requirement when the course is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle(course.getName())
                        .setMessage("Entry Requirement (SPM):\n\n" + course.getRequirement())
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemCourseBinding binding;

        public ViewHolder(ItemCourseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}