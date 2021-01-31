package com.project.personal_health_monitor.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class HealthParameterNameAdapter extends RecyclerView.Adapter<HealthParameterNameAdapter.ViewHolder>{

    private List<HealthParameterName> healthParameterNames;

    public HealthParameterNameAdapter() {
        healthParameterNames = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_parameter_name_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        HealthParameterName healthParameterName = healthParameterNames.get(position);

        viewHolder.healthParameterNameTextView.setText(healthParameterName.name);
        viewHolder.healthParameterNamePriorityTextView.setText(formatInteger(healthParameterName.priority));
    }

    private String formatInteger(Integer value) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format(value);
    }

    public List<HealthParameterName> getHealthParameterNames() {
        return this.healthParameterNames;
    }

    public void setHealthParameterNames(List<HealthParameterName> healthParameterNames) {
        this.healthParameterNames = healthParameterNames;
        notifyDataSetChanged();
    }

    public HealthParameterName getItemAt(int position) {
        return getHealthParameterNames().get(position);
    }

    @Override
    public int getItemCount() {
        return healthParameterNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView healthParameterNameTextView;
        private final TextView healthParameterNamePriorityTextView;

        ViewHolder(View view) {
            super(view);

            healthParameterNameTextView = view.findViewById(R.id.health_parameter_name_text_view);
            healthParameterNamePriorityTextView = view.findViewById(R.id.health_parameter_name_priority_text_view);
        }
    }

}