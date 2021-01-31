package com.project.personal_health_monitor.view.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;

import java.util.ArrayList;
import java.util.List;

public class HealthParameterAdapter extends RecyclerView.Adapter<HealthParameterAdapter.ViewHolder>{

    private final List<HealthParameter> data;

    public HealthParameterAdapter() {
        data = new ArrayList<>();
        addHealthParameter();

    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.health_parameter_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        HealthParameter healthParameter = data.get(position);

        for (int i = 0; i < viewHolder.healthParameterSpinner.getAdapter().getCount() ; i++) {
            if (viewHolder.healthParameterSpinner.getAdapter().getItem(i) == healthParameter.name) {
                viewHolder.healthParameterSpinner.setSelection(i);
            }
        }

        viewHolder.healthParameterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (HealthParameterName healthParameterName : HealthParameterName.values()) {
                    if (healthParameterName.ordinal() == position) {
                        healthParameter.name = healthParameterName;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        viewHolder.healthParameterValueText.setText(healthParameter.value.toString());
        viewHolder.healthParameterValueText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = s.toString();

                if (value.isEmpty()) {
                    value = "0";
                }

                healthParameter.value = Long.parseLong(value);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        viewHolder.deleteHealthParameterButton.setOnClickListener((view)->{
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, data.size());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Spinner healthParameterSpinner;
        private final EditText healthParameterValueText;
        private final ImageButton deleteHealthParameterButton;

        ViewHolder(View view) {
            super(view);


            healthParameterSpinner = view.findViewById(R.id.health_parameter_spinner);
            healthParameterValueText = view.findViewById(R.id.health_parameter_value_text);
            deleteHealthParameterButton = view.findViewById(R.id.delete_health_parameter_button);

            initializeHealthParameterSpinner(view);
        }

        private void initializeHealthParameterSpinner(View view) {
            ArrayAdapter<HealthParameterName> spinnerArrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, HealthParameterName.values());
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            healthParameterSpinner.setAdapter(spinnerArrayAdapter);
        }
    }

    public void addHealthParameter() {
        HealthParameter healthParameter = new HealthParameter();
        healthParameter.value = 0L;
        data.add(healthParameter);
        notifyItemInserted(data.size() - 1);
    }

    public List<HealthParameter> getHealthParameters () {
        return data;
    }



}