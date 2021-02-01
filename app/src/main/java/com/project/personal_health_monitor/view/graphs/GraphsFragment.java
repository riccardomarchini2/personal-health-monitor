package com.project.personal_health_monitor.view.graphs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;
import com.project.personal_health_monitor.view.base.BaseFragment;
import com.project.personal_health_monitor.view_model.HealthParameterNameViewModel;
import com.project.personal_health_monitor.view_model.ReportViewModel;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class GraphsFragment extends BaseFragment {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    @BindView(R.id.health_parameter_name_spinner)
    Spinner healthParameterNameSpinner;

    @BindView(R.id.health_parameter_bar_chart)
    BarChart healthParameterBarChart;

    @BindView(R.id.report_count_bar_chart)
    BarChart reportCountBarChart;

    @Inject ViewModelFactory viewModelFactory;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_graphs, container, false);
        injectDependencies(root);

        ReportViewModel reportViewModel = new ViewModelProvider(this, viewModelFactory).get(ReportViewModel.class);

        LocalDate lastWeek = LocalDate.now().minusDays(7);
        reportViewModel.greaterThan(lastWeek).observe(getViewLifecycleOwner(), this::updateGraphs);

        return root;
    }

    private void injectDependencies(View root) {
        personalHealthMonitor().applicationComponent().inject(this);
        ButterKnife.bind(this, root);
    }

    private void updateGraphs(List<ReportWithHealthParameters> reportsWithHealthParameters) {
        initializeHealthParameterBarChart(reportsWithHealthParameters);
        initializeReportCountBarChart(reportsWithHealthParameters);
    }

    private void initializeHealthParameterBarChart(List<ReportWithHealthParameters> reportsWithHealthParameters) {
        HealthParameterNameViewModel healthParameterNameViewModel = new ViewModelProvider(this, viewModelFactory).get(HealthParameterNameViewModel.class);
        healthParameterNameViewModel.getAll().observe(getViewLifecycleOwner(), healthParameterNames -> doInitializeHealthParameterBarChart(healthParameterNames, reportsWithHealthParameters));
    }

    private void doInitializeHealthParameterBarChart(List<HealthParameterName> healthParameterNames, List<ReportWithHealthParameters> reportsWithHealthParameters) {
        ArrayAdapter<HealthParameterName> spinnerArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, healthParameterNames);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        healthParameterNameSpinner.setAdapter(spinnerArrayAdapter);

        healthParameterNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HealthParameterName healthParameterName = healthParameterNames.get(position);

                List<BarEntry> barEntries = new ArrayList<>();

                for (int i = 0; i < 7; i++) {
                    LocalDate localDate = LocalDate.now().minusDays(6 - i);

                    Float averageHealthParameterValue = (float) reportsWithHealthParameters.stream()
                        .filter(reportWithHealthParameters -> reportWithHealthParameters.report.localDate.equals(localDate))
                        .flatMap(reportWithHealthParameters -> reportWithHealthParameters.healthParameters.stream())
                        .filter(healthParameter -> Objects.equals(healthParameter.healthParameterName, healthParameterName.name))
                        .mapToDouble(healthParameter -> healthParameter.value)
                        .average()
                        .orElse(0);

                    BarEntry barEntry = new BarEntry(localDate.toEpochDay(), averageHealthParameterValue);
                    barEntries.add(barEntry);
                }

                BarDataSet barDataSet = new BarDataSet(barEntries, "Reports");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                BarData barData = new BarData(barDataSet);

                healthParameterBarChart.setData(barData);
                Description description = new Description();
                description.setText("");
                healthParameterBarChart.setDescription(description);

                healthParameterBarChart.getXAxis().setValueFormatter(new XAxisValueFormatter());
                healthParameterBarChart.getAxisRight().setEnabled(false);
                healthParameterBarChart.getAxisLeft().setDrawGridLines(false);
                healthParameterBarChart.getXAxis().setDrawGridLines(false);
                healthParameterBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                healthParameterBarChart.getLegend().setEnabled(false);
                healthParameterBarChart.setTouchEnabled(false);
                healthParameterBarChart.setDragEnabled(false);
                healthParameterBarChart.setPinchZoom(false);

                healthParameterBarChart.animateY(1000);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void initializeReportCountBarChart(List<ReportWithHealthParameters> reportsWithHealthParameters) {
        List<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate localDate = LocalDate.now().minusDays(6 - i);
            Long count = reportsWithHealthParameters.stream().filter(reportWithHealthParameters -> reportWithHealthParameters.report.localDate.equals(localDate)).count();
            BarEntry barEntry = new BarEntry(localDate.toEpochDay(), count);
            barEntries.add(barEntry);
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Reports");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData barData = new BarData(barDataSet);

        reportCountBarChart.setData(barData);
        Description description = new Description();
        description.setText("");
        reportCountBarChart.setDescription(description);

        reportCountBarChart.getXAxis().setValueFormatter(new XAxisValueFormatter());
        reportCountBarChart.getAxisRight().setEnabled(false);
        reportCountBarChart.getAxisLeft().setDrawGridLines(false);
        reportCountBarChart.getXAxis().setDrawGridLines(false);
        reportCountBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        reportCountBarChart.getLegend().setEnabled(false);
        reportCountBarChart.setTouchEnabled(false);
        reportCountBarChart.setDragEnabled(false);
        reportCountBarChart.setPinchZoom(false);

        reportCountBarChart.animateY(1000);
    }

    private static class XAxisValueFormatter extends ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            LocalDate localDate = LocalDate.ofEpochDay((long) value);
            return formatDate(localDate);
        }

        public String formatDate(LocalDate localDate) {
            return localDate.format(DATE_TIME_FORMATTER);
        }

    }

}