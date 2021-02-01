package com.project.personal_health_monitor.view.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HealthParameterAdapter extends RecyclerView.Adapter<HealthParameterAdapter.ViewHolder>{
    private final List<HealthParameterName> healthParameterNames;

    private List<HealthParameter> healthParameters;

    public HealthParameterAdapter(List<HealthParameterName> healthParameterNames) {
        this.healthParameterNames = healthParameterNames;
        healthParameters = new ArrayList<>();
        createHealthParameter();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.health_parameter_list_item, parent, false);

        return new ViewHolder(view, healthParameterNames);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        HealthParameter healthParameter = healthParameters.get(position);
        initializeViewHolder(viewHolder, healthParameter);

        viewHolder.healthParameterNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HealthParameterName healthParameterName = healthParameterNames.get(position);

                healthParameter.healthParameterNameId = healthParameterName.id;
                healthParameter.healthParameterName = healthParameterName.name;
                healthParameter.healthParameterPriority = healthParameterName.priority;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        viewHolder.healthParameterValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String value = charSequence.toString();

                if (value.isEmpty()) {
                    value = "0";
                }

                healthParameter.value = Double.parseDouble(value);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void initializeViewHolder(ViewHolder viewHolder, HealthParameter healthParameter) {
        if (Objects.nonNull(healthParameter.healthParameterNameId)) {
            for (int i = 0; i < viewHolder.healthParameterNameSpinner.getAdapter().getCount() ; i++) {
                HealthParameterName healthParameterName = (HealthParameterName) viewHolder.healthParameterNameSpinner.getAdapter().getItem(i);
                if (Objects.equals(healthParameterName.id, healthParameter.healthParameterNameId)) {
                    viewHolder.healthParameterNameSpinner.setSelection(i);
                }
            }
        }

        if (Objects.nonNull(healthParameter.value)) {
            viewHolder.healthParameterValueEditText.setText(formatLong(healthParameter.value));
        }
    }

    private String formatLong(Double value) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        return numberFormat.format(value);
    }

    public List<HealthParameter> getHealthParameters() {
        return healthParameters;
    }

    public void setHealthParameters(List<HealthParameter> healthParameters) {
        this.healthParameters = healthParameters;

        notifyDataSetChanged();
    }

    public void createHealthParameter() {
        HealthParameter healthParameter = new HealthParameter();
        healthParameters.add(healthParameter);

        notifyItemInserted(healthParameters.size() - 1);
    }

    public void removeHealthParameter(int position) {
        healthParameters.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, healthParameters.size());
    }

    @Override
    public int getItemCount() {
        return healthParameters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Spinner healthParameterNameSpinner;
        private final EditText healthParameterValueEditText;

        ViewHolder(View view, List<HealthParameterName> healthParameterNames) {
            super(view);

            healthParameterNameSpinner = view.findViewById(R.id.health_parameter_name_spinner);
            healthParameterValueEditText = view.findViewById(R.id.health_parameter_value_edit_text);

            initializeHealthParameterNameSpinner(view, healthParameterNames);
        }

        private void initializeHealthParameterNameSpinner(View view, List<HealthParameterName> healthParameterNames) {
            ArrayAdapter<HealthParameterName> spinnerArrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, healthParameterNames);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            healthParameterNameSpinner.setAdapter(spinnerArrayAdapter);
        }
    }

}