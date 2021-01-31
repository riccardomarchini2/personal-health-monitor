package com.project.personal_health_monitor.view.reports;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;
import com.project.personal_health_monitor.view.adapter.ReportAdapter;

import java.util.List;

import butterknife.BindView;

public class ReportsFragment extends Fragment {

    @BindView(R.id.report_recycler_view)
    RecyclerView recyclerView;

    private void updateRecyclerView(List<ReportWithHealthParameters> reportsWithHealthParameters) {
        ReportAdapter reportAdapter = new ReportAdapter(reportsWithHealthParameters);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(reportAdapter);
    }

}
