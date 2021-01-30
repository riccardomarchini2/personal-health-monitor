package com.project.personal_health_monitor.view.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.project.personal_health_monitor.PersonalHealthMonitor;
import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.Report;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;
import com.project.personal_health_monitor.view.DateTimePicker;
import com.project.personal_health_monitor.view.adapter.HealthParameterAdapter;
import com.project.personal_health_monitor.view.adapter.ReportAdapter;
import com.project.personal_health_monitor.view_model.HealthParameterViewModel;
import com.project.personal_health_monitor.view_model.ReportViewModel;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class ReportActivity extends AppCompatActivity {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    @Inject
    ViewModelFactory viewModelFactory;

    @BindView(R.id.date_textview)
    TextView dateTextView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private HealthParameterViewModel healthParameterViewModel;
    private ReportViewModel reportViewModel;
    private HealthParameterAdapter healthParameterAdapter;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        injectDependencies();

        //View Model Provider serve per creare oggetti di tipo viewmodel (get()).
        //Prende in ingresso un oggetto ViewModelFactory
        healthParameterViewModel = new ViewModelProvider(this, viewModelFactory).get(HealthParameterViewModel.class);
        reportViewModel = new ViewModelProvider(this, viewModelFactory).get(ReportViewModel.class);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        report = new Report();

        onDateChange(LocalDate.now());
        initializeRecyclerView();
    }


    @OnClick(R.id.change_date_button)
    public void onDate() {
        // Instance of DateTimePicker with yy, mm, dd, hh, m and provides local datetime
        DateTimePicker picker = new DateTimePicker(this::onDateChange);

        // Choose the preferred dialog style
        picker.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        // Shows dialog for datetime picker
        picker.show(this.getSupportFragmentManager(), "Select Date and Time");
    }

    private void onDateChange(int year, int month, int day, int hour, int minute) {
        onDateChange(LocalDate.of(year,month,day));
    }

    private void onDateChange(LocalDate localDate) {
        dateTextView.setText(localDate.format(DATE_TIME_FORMATTER));
        report.date = localDate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void injectDependencies() {
        ((PersonalHealthMonitor) getApplication()).applicationComponent().inject(this);
        ButterKnife.bind(this);
    }

    private void initializeRecyclerView() {
        healthParameterAdapter = new HealthParameterAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(healthParameterAdapter);
    }

    @OnClick(R.id.add_health_parameter_button)
    public void addHealthParameter() {
        healthParameterAdapter.addHealthParameter();
    }

    @OnClick(R.id.save_health_paramater_button)
    public void saveReport() {
        Long id = reportViewModel.create(report);

        for (HealthParameter healthParameter: healthParameterAdapter.getHealthParameters()) {
            healthParameter.reportId = id;
            healthParameterViewModel.create(healthParameter);
        }
    }

}