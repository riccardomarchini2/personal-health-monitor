package com.project.personal_health_monitor.view.reports;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;
import com.project.personal_health_monitor.view.ReportActivity;
import com.project.personal_health_monitor.view.adapter.ReportAdapter;
import com.project.personal_health_monitor.view.base.BaseFragment;
import com.project.personal_health_monitor.view_model.ReportViewModel;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class ReportsFragment extends BaseFragment {

    @BindView(R.id.reports_calendar_view)
    CalendarView reportsCalendarView;

    @BindView(R.id.reports_recycler_view)
    RecyclerView reportsRecyclerView;

    @BindView(R.id.edit_report_button)
    ImageButton edit_report_button;

    @Inject ViewModelFactory viewModelFactory;

    private ReportViewModel reportViewModel;

    private ReportAdapter reportAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reports, container, false);

        injectDependencies(root);
        initializeReportRecyclerView();

        reportViewModel = new ViewModelProvider(this, viewModelFactory).get(ReportViewModel.class);

        onDateChanged(LocalDate.now());

        reportsCalendarView.setOnDateChangeListener(
            (calendarView, year, month, dayOfMonth) -> onDateChanged(LocalDate.of(year, month + 1, dayOfMonth))
        );

        return root;
    }

    private void injectDependencies(View root) {
        ButterKnife.bind(this, root);
        personalHealthMonitor().applicationComponent().inject(this);
    }

    private void initializeReportRecyclerView() {
        reportAdapter = new ReportAdapter();
        reportsRecyclerView.setAdapter(reportAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        reportsRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
            reportsRecyclerView.getContext(),
            linearLayoutManager.getOrientation()
        );

        reportsRecyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.SimpleCallback swipeTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int position = viewHolder.getAdapterPosition();
                ReportWithHealthParameters reportWithHealthParameters = reportAdapter.getItemAt(position);

                reportViewModel.delete(reportWithHealthParameters.report);
                reportAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeTouchHelper);
        itemTouchHelper.attachToRecyclerView(reportsRecyclerView);
    }

    public void onEditReportClicked(View view) {
        //mi dovrebbe portare alla'activity report con i campi gia avvalorati
    }

    private void onDateChanged(LocalDate localDate) {
        reportViewModel.getBy(localDate).observe(getViewLifecycleOwner(), this::updateReportRecyclerView);
    }

    private void updateReportRecyclerView(List<ReportWithHealthParameters> reportsWithHealthParameters) {
        reportAdapter.setReportsWithHealthParameters(reportsWithHealthParameters);
    }

    @OnClick(R.id.create_new_report_floating_button)
    public void createReport() {
        Intent intent = new Intent(getActivity(), ReportActivity.class);
        startActivity(intent);
    }

}
