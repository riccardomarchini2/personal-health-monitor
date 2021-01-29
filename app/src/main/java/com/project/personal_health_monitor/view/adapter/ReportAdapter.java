package com.project.personal_health_monitor.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.Report;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    private final List<ReportWithHealthParameters> data;

    public ReportAdapter(List<ReportWithHealthParameters> data) {
        this.data = data;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.report_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ReportWithHealthParameters reportWithHealthParameters = data.get(position);

        Report report = reportWithHealthParameters.report;

        viewHolder.dateTextView.setText(report.date.format(DATE_TIME_FORMATTER));
        viewHolder.notesTextView.setText(report.notes);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTextView;
        private final TextView notesTextView;

        ViewHolder(View view) {
            super(view);

            dateTextView = view.findViewById(R.id.date_text_view);
            notesTextView = view.findViewById(R.id.notes_text_view);
        }
    }

}