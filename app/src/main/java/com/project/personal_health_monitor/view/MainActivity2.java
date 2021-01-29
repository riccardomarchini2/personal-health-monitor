package com.project.personal_health_monitor.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CalendarView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.PersonalHealthMonitor;
import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.view.base.Activity;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;
import com.project.personal_health_monitor.persistence.model.Report;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;
import com.project.personal_health_monitor.view.adapter.ReportAdapter;
import com.project.personal_health_monitor.view_model.HealthParameterViewModel;
import com.project.personal_health_monitor.view_model.ReportViewModel;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class MainActivity2 extends Activity {

    @Inject ViewModelFactory viewModelFactory;

    private HealthParameterViewModel healthParameterViewModel;
    private ReportViewModel reportViewModel;

    @BindView(R.id.calendar_view)
    CalendarView calendarView;

    @BindView(R.id.report_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        injectDependencies();

        healthParameterViewModel = new ViewModelProvider(this, viewModelFactory).get(HealthParameterViewModel.class);
        reportViewModel = new ViewModelProvider(this, viewModelFactory).get(ReportViewModel.class);

        onDateChanged(LocalDate.now());

        calendarView.setOnDateChangeListener(
            (calendarView, year, month, dayOfMonth) -> onDateChanged(LocalDate.of(year, month + 1, dayOfMonth))
        );
    }

    private void injectDependencies() {
        ((PersonalHealthMonitor) getApplication()).applicationComponent().inject(this);
        ButterKnife.bind(this);
    }

    private void onDateChanged(LocalDate localDate) {
        reportViewModel.getBy(localDate).observe(this, this::initializeRecyclerView);
    }

    private void initializeRecyclerView(List<ReportWithHealthParameters> reportsWithHealthParameters) {
        ReportAdapter reportAdapter = new ReportAdapter(reportsWithHealthParameters);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reportAdapter);
    }

    @OnClick(R.id.add_new_report)
    public void onAddNewReportClicked() {
        Report report = new Report();
        report.date = LocalDate.now();
        report.notes = "Note";

        reportViewModel.create(report);

        HealthParameter healthParameter = new HealthParameter();
        healthParameter.reportId = report.id;
        healthParameter.name = HealthParameterName.BODY_PRESSURE;
        healthParameter.value = 37L;
    }

}