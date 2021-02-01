package com.project.personal_health_monitor.view.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;
import com.project.personal_health_monitor.view.ReportActivity;
import com.project.personal_health_monitor.view.base.BaseFragment;
import com.project.personal_health_monitor.view_model.ReportViewModel;
import com.project.personal_health_monitor.view_model.model.SummaryHealthParameter;
import com.project.personal_health_monitor.view_model.model.SummaryReport;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class HomeFragment extends BaseFragment {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    @BindView(R.id.reports_calendar_view)
    CalendarView calendarView;

    @BindView(R.id.report_local_date_text_view)
    TextView reportLocalDateTextView;

    @BindView(R.id.report_count_text_view)
    TextView reportCountTextView;

    @BindView(R.id.report_health_parameters_text_view)
    TextView reportHealthParametersTextView;

    @Inject ViewModelFactory viewModelFactory;

    private ReportViewModel reportViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        injectDependencies(root);

        reportViewModel = new ViewModelProvider(this, viewModelFactory).get(ReportViewModel.class);

        onDateChanged(LocalDate.now());

        calendarView.setOnDateChangeListener(
            (calendarView, year, month, dayOfMonth) -> onDateChanged(LocalDate.of(year, month + 1, dayOfMonth))
        );

        return root;
    }

    private void injectDependencies(View root) {
        ButterKnife.bind(this, root);
        personalHealthMonitor().applicationComponent().inject(this);
    }

    private void onDateChanged(LocalDate localDate) {
        reportViewModel.getBy(localDate).observe(getViewLifecycleOwner(), (reportWithHealthParameters) -> updateUI(localDate, reportWithHealthParameters));
    }

    private void updateUI(LocalDate localDate, List<ReportWithHealthParameters> reportsWithHealthParameters) {
        SummaryReport summaryReport = createSummaryReport(localDate, reportsWithHealthParameters);
        updateSummaryReport(reportsWithHealthParameters, summaryReport);
    }

    private SummaryReport createSummaryReport(LocalDate localDate, List<ReportWithHealthParameters> reportsWithHealthParameters) {
        SummaryReport summaryReport = new SummaryReport(localDate);

        for (ReportWithHealthParameters reportWithHealthParameter : reportsWithHealthParameters) {
            for (HealthParameter healthParameter : reportWithHealthParameter.healthParameters) {
                SummaryHealthParameter summaryHealthParameter = summaryReport.getSummaryHealthParameter(healthParameter.healthParameterName, healthParameter.healthParameterPriority);
                summaryHealthParameter.values.add(healthParameter.value);
            }
        }

        return summaryReport;
    }

    private void updateSummaryReport(List<ReportWithHealthParameters> reportsWithHealthParameters, SummaryReport summaryReport) {
        reportLocalDateTextView.setText(summaryReport.localDate.format(DATE_TIME_FORMATTER));
        reportCountTextView.setText(String.valueOf(reportsWithHealthParameters.size()));
        reportHealthParametersTextView.setText(formatHealthParameters(summaryReport.summaryHealthParameters));
    }

    private String formatHealthParameters(List<SummaryHealthParameter> healthParameters) {
        return healthParameters.stream()
            .map(healthParameter -> String.format(Locale.getDefault(), "%s (priority %d): %.2f", healthParameter.name, healthParameter.priority, healthParameter.getAverageValue()))
            .collect(Collectors.joining("\n"));
    }

    @OnClick(R.id.create_new_report_floating_button)
    public void createReport() {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        startActivity(intent);
    }

}