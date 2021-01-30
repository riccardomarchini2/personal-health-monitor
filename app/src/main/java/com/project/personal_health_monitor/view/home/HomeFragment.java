package com.project.personal_health_monitor.view.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.PersonalHealthMonitor;
import com.project.personal_health_monitor.R;
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
public class HomeFragment extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;

    private HealthParameterViewModel healthParameterViewModel;
    private ReportViewModel reportViewModel;

    @BindView(R.id.calendar_view)
    CalendarView calendarView;

    @BindView(R.id.report_recycler_view)
    RecyclerView recyclerView;

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
        reportViewModel.getBy(localDate).observe(getActivity(), this::initializeRecyclerView);
    }

    private void initializeRecyclerView(List<ReportWithHealthParameters> reportsWithHealthParameters) {
        ReportAdapter reportAdapter = new ReportAdapter(reportsWithHealthParameters);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(reportAdapter);

        createSummaryReport(reportsWithHealthParameters);
    }

    private void createSummaryReport(List<ReportWithHealthParameters> reportsWithHealthParameters) {
        //Media

        SummaryReport summaryReport = new SummaryReport();

        for (ReportWithHealthParameters reportWithHealthParameter : reportsWithHealthParameters) {
            for (HealthParameter healthParameter : reportWithHealthParameter.healthParameters) {
                SummaryHealthParameter summaryHealthParameter = summaryReport.getSummaryHealthParameter(healthParameter.name);
                summaryHealthParameter.values.add(healthParameter.value);
            }
        }

    }

    private static class SummaryReport {
        public LocalDate date;
        public List<SummaryHealthParameter> summaryHealthParameters;

        public SummaryHealthParameter getSummaryHealthParameter(HealthParameterName name) {
            for (SummaryHealthParameter summaryHealthParameter : summaryHealthParameters) {
                if (summaryHealthParameter.name == name) {
                    return summaryHealthParameter;
                }
            }

            SummaryHealthParameter summaryHealthParameter = new SummaryHealthParameter();
            summaryHealthParameter.name = name;
            summaryHealthParameters.add(summaryHealthParameter);

            return summaryHealthParameter;
        }
    }

    private static class SummaryHealthParameter {
        public HealthParameterName name;
        public List<Long> values;

        public Double averageValue() {
            if (values.size() == 0) {
                return (double) 0;
            } else {
                Long sum = 0L;
                for (Long value : values) {
                    sum += value;
                }
                return (double) sum / (double) values.size();
            }
        }

    }

}