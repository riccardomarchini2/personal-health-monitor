package com.project.personal_health_monitor.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.Report;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private List<ReportWithHealthParameters> reportsWithHealthParameters;

    public ReportAdapter() {
        reportsWithHealthParameters = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ReportWithHealthParameters reportWithHealthParameters = reportsWithHealthParameters.get(position);

        Report report = reportWithHealthParameters.report;

        viewHolder.reportNotesTextView.setText(!report.notes.trim().isEmpty() ? report.notes : "---");
        viewHolder.reportHealthParametersTextView.setText(formatHealthParameters(reportWithHealthParameters.healthParameters));
    }

    private String formatHealthParameters(List<HealthParameter> healthParameters) {
        return healthParameters.stream()
            .map(healthParameter -> String.format(Locale.getDefault(), "%s (priority %d): %.2f", healthParameter.healthParameterName, healthParameter.healthParameterPriority, healthParameter.value))
            .collect(Collectors.joining("\n"));
    }

    public List<ReportWithHealthParameters> getReportsWithHealthParameters() {
        return reportsWithHealthParameters;
    }

    public void setReportsWithHealthParameters(List<ReportWithHealthParameters> reportsWithHealthParameters) {
        this.reportsWithHealthParameters = reportsWithHealthParameters;

        notifyDataSetChanged();
    }

    public ReportWithHealthParameters getItemAt(int position) {
        return getReportsWithHealthParameters().get(position);
    }

    @Override
    public int getItemCount() {
        return reportsWithHealthParameters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView reportNotesTextView;
        private final TextView reportHealthParametersTextView;

        ViewHolder(View view) {
            super(view);

            reportNotesTextView = view.findViewById(R.id.report_notes_text_view);
            reportHealthParametersTextView = view.findViewById(R.id.report_health_parameters_text_view);
        }
    }

}