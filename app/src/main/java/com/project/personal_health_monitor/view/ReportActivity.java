package com.project.personal_health_monitor.view;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameter;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;
import com.project.personal_health_monitor.persistence.model.Report;
import com.project.personal_health_monitor.view.base.BaseActivity;
import com.project.personal_health_monitor.view.dialog.MyDatePickerDialog;
import com.project.personal_health_monitor.view.adapter.HealthParameterAdapter;
import com.project.personal_health_monitor.view_model.HealthParameterNameViewModel;
import com.project.personal_health_monitor.view_model.HealthParameterViewModel;
import com.project.personal_health_monitor.view_model.ReportViewModel;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class ReportActivity extends BaseActivity {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    @BindView(R.id.report_local_date_text_view)
    TextView reportLocalDateTextView;

    @BindView(R.id.report_notes_edit_text)
    TextView reportNotesEditText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.health_parameters_recycler_view)
    RecyclerView healthParametersRecyclerView;

    @Inject ViewModelFactory viewModelFactory;

    private HealthParameterViewModel healthParameterViewModel;
    private ReportViewModel reportViewModel;

    private HealthParameterAdapter healthParameterAdapter;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        injectDependencies();
        initializeToolbar();

        HealthParameterNameViewModel healthParameterNameViewModel = new ViewModelProvider(this, viewModelFactory).get(HealthParameterNameViewModel.class);
        healthParameterViewModel = new ViewModelProvider(this, viewModelFactory).get(HealthParameterViewModel.class);
        reportViewModel = new ViewModelProvider(this, viewModelFactory).get(ReportViewModel.class);

        healthParameterNameViewModel.getAll().observe(this, this::initializeReport);
    }

    private void injectDependencies() {
        ButterKnife.bind(this);
        personalHealthMonitor().applicationComponent().inject(this);
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeReport(List<HealthParameterName> healthParameterNames) {
        report = new Report();

        initializeReportLocalDate();
        initializeHealthParametersRecyclerView(healthParameterNames);
    }

    private void initializeReportLocalDate() {
        onDateChange(LocalDate.now());
    }

    @OnClick(R.id.change_report_local_date_button)
    public void onChangeDateClicked() {
        MyDatePickerDialog datePickerDialog = new MyDatePickerDialog();
        datePickerDialog.setCallback(this::onDateChange);
        datePickerDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        datePickerDialog.show(this.getSupportFragmentManager(), "Select date");
    }

    private void onDateChange(LocalDate localDate) {
        reportLocalDateTextView.setText(localDate.format(DATE_TIME_FORMATTER));
        report.localDate = localDate;
    }

    private void initializeHealthParametersRecyclerView(List<HealthParameterName> healthParameterNames) {
        healthParameterAdapter = new HealthParameterAdapter(healthParameterNames);
        healthParametersRecyclerView.setAdapter(healthParameterAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        healthParametersRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
            healthParametersRecyclerView.getContext(),
            linearLayoutManager.getOrientation()
        );

        healthParametersRecyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.SimpleCallback swipeTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int position = viewHolder.getAdapterPosition();
                healthParameterAdapter.removeHealthParameter(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeTouchHelper);
        itemTouchHelper.attachToRecyclerView(healthParametersRecyclerView);
    }

    @OnClick(R.id.create_health_parameter_button)
    public void createHealthParameter() {
        healthParameterAdapter.createHealthParameter();
    }

    @OnClick(R.id.save_health_parameter_button)
    public void saveHealthReport() {
        report.notes = reportNotesEditText.getText().toString();

        if (isValidHealthReport()) {
            Long reportId = reportViewModel.create(report);

            List<HealthParameter> healthParameters = healthParameterAdapter.getHealthParameters();
            healthParameters.forEach(healthParameter -> {
                healthParameter.reportId = reportId;
                healthParameterViewModel.create(healthParameter);
            });
            //send notification if value > threshold
            finish();
        }
    }

    private boolean isValidHealthReport() {
        List<HealthParameter> healthParameters = healthParameterAdapter.getHealthParameters();
        return Objects.nonNull(report.localDate) && healthParameters.size() > 1 && healthParameters.stream().allMatch(this::isValidHealthParameter);
    }

    private boolean isValidHealthParameter(HealthParameter healthParameter) {
        return Objects.nonNull(healthParameter.healthParameterNameId) &&
            Objects.nonNull(healthParameter.healthParameterName) &&
            Objects.nonNull(healthParameter.healthParameterPriority) &&
            Objects.nonNull(healthParameter.value);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}