package com.project.personal_health_monitor.view.graphs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.personal_health_monitor.PersonalHealthMonitor;
import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.view_model.HealthParameterViewModel;
import com.project.personal_health_monitor.view_model.ReportViewModel;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class GraphsFragment extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;

    private HealthParameterViewModel healthParameterViewModel;
    private ReportViewModel reportViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_graphs, container, false);
        injectDependencies(root);

        healthParameterViewModel = new ViewModelProvider(this, viewModelFactory).get(HealthParameterViewModel.class);
        reportViewModel = new ViewModelProvider(this, viewModelFactory).get(ReportViewModel.class);

        return root;
    }

    private void injectDependencies(View root) {
        ((PersonalHealthMonitor) getActivity().getApplication()).applicationComponent().inject(this);
        ButterKnife.bind(root);
    }
    
}