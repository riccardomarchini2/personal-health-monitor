package com.project.personal_health_monitor.view.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.PersonalHealthMonitor;
import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;
import com.project.personal_health_monitor.view.adapter.ReportAdapter;
import com.project.personal_health_monitor.view_model.HealthParameterViewModel;
import com.project.personal_health_monitor.view_model.ReportViewModel;
import com.project.personal_health_monitor.view_model.SummaryHealthParameter;
import com.project.personal_health_monitor.view_model.SummaryReport;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class HomeFragment extends Fragment {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
    @Inject
    ViewModelFactory viewModelFactory;

    private HealthParameterViewModel healthParameterViewModel;
    private ReportViewModel reportViewModel;

    @BindView(R.id.calendar_view)
    CalendarView calendarView;

    @BindView(R.id.summary_report_date_text)
    TextView summaryReportDate;

    @BindView(R.id.body_pressure_text)
    TextView bodyPressure;

    @BindView(R.id.body_temperature_text)
    TextView bodyTemperature;

    @BindView(R.id.glycemic_index_text)
    TextView glycemicIndex;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        injectDependencies(root);

        healthParameterViewModel = new ViewModelProvider(this, viewModelFactory).get(HealthParameterViewModel.class);
        reportViewModel = new ViewModelProvider(this, viewModelFactory).get(ReportViewModel.class);

        onDateChanged(LocalDate.now());

        calendarView.setOnDateChangeListener(
                (calendarView, year, month, dayOfMonth) -> onDateChanged(LocalDate.of(year, month + 1, dayOfMonth))
        );

        return root;
    }

    private void injectDependencies(View root) {
        ((PersonalHealthMonitor) getActivity().getApplication()).applicationComponent().inject(this);
        ButterKnife.bind(this, root);
    }

    private void onDateChanged(LocalDate localDate) {
        reportViewModel.getBy(localDate).observe(getActivity(), (reportWithHealthParameters) -> updateUI(localDate, reportWithHealthParameters));
    }

    private void updateUI(LocalDate localDate, List<ReportWithHealthParameters> reportsWithHealthParameters) {
        SummaryReport summaryReport = createSummaryReport(localDate, reportsWithHealthParameters);
        updateSummaryReport(summaryReport);
    }

    private SummaryReport createSummaryReport(LocalDate localDate, List<ReportWithHealthParameters> reportsWithHealthParameters) {
        SummaryReport summaryReport = new SummaryReport(localDate);

        for (ReportWithHealthParameters reportWithHealthParameter : reportsWithHealthParameters) {
            for (HealthParameter healthParameter : reportWithHealthParameter.healthParameters) {
                SummaryHealthParameter summaryHealthParameter = summaryReport.getSummaryHealthParameter(healthParameter.name);
                summaryHealthParameter.values.add(healthParameter.value);
            }
        }

        return summaryReport;
    }

    private void updateSummaryReport(SummaryReport summaryReport) {
        summaryReportDate.setText(summaryReport.date.format(DATE_TIME_FORMATTER));
        bodyPressure.setText(summaryReport.getSummaryHealthParameterValue(HealthParameterName.BODY_PRESSURE));
        bodyTemperature.setText(summaryReport.getSummaryHealthParameterValue(HealthParameterName.BODY_TEMPERATURE));
        glycemicIndex.setText(summaryReport.getSummaryHealthParameterValue(HealthParameterName.GLYCEMIC_INDEX));
    }


}