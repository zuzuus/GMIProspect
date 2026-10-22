package com.gmi.prospect;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.gmi.prospect.databinding.ItemCourseBinding;

import java.util.ArrayList;

// Connects the course list to the cards in the RecyclerView
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private static final String TAG = "CourseAdapter";

    private ArrayList<Course> courseList;

    public CourseAdapter(ArrayList<Course> courseList) {
        this.courseList = courseList;
    }

    // Called by the filter buttons and when the web data arrives
    public void updateList(ArrayList<Course> newList) {
        courseList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCourseBinding binding = ItemCourseBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);
        Log.v(TAG, "Showing card " + position + ": " + course.getName());

        holder.binding.tvCategory.setText(course.getCategory());
        holder.binding.tvName.setText(course.getName());
        holder.binding.tvDuration.setText("Duration: " + course.getDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRequirementDialog(v, course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    private void showRequirementDialog(View v, Course course) {
        new AlertDialog.Builder(v.getContext())
                .setTitle(course.getName())
                .setMessage("Entry Requirement (SPM):\n\n" + course.getRequirement())
                .setPositiveButton("OK", null)
                .show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemCourseBinding binding;

        public ViewHolder(ItemCourseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
