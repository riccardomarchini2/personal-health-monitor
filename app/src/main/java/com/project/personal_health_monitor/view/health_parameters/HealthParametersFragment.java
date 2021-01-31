package com.project.personal_health_monitor.view.health_parameters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;
import com.project.personal_health_monitor.view.adapter.HealthParameterNameAdapter;
import com.project.personal_health_monitor.view.base.BaseFragment;
import com.project.personal_health_monitor.view.dialog.HealthParameterNameDialog;
import com.project.personal_health_monitor.view_model.HealthParameterNameViewModel;
import com.project.personal_health_monitor.view_model.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class HealthParametersFragment extends BaseFragment {

    @BindView(R.id.health_parameter_name_recycler_view)
    RecyclerView healthParameterNameRecyclerView;

    @Inject ViewModelFactory viewModelFactory;

    private HealthParameterNameViewModel healthParameterNameViewModel;

    private HealthParameterNameAdapter healthParameterNameAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_health_parameters, container, false);

        injectDependencies(root);

        initializeHealthParameterNameRecyclerView();

        healthParameterNameViewModel = new ViewModelProvider(this, viewModelFactory).get(HealthParameterNameViewModel.class);
        healthParameterNameViewModel.getAll().observe(getActivity(), this::setHealthParameterNames);

        return root;
    }

    private void initializeHealthParameterNameRecyclerView() {
        healthParameterNameAdapter = new HealthParameterNameAdapter();
        healthParameterNameRecyclerView.setAdapter(healthParameterNameAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        healthParameterNameRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
            healthParameterNameRecyclerView.getContext(),
            linearLayoutManager.getOrientation()
        );

        healthParameterNameRecyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.SimpleCallback swipeTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDirection) {
                int position = viewHolder.getAdapterPosition();
                HealthParameterName healthParameterName = healthParameterNameAdapter.getItemAt(position);

                healthParameterNameViewModel.delete(healthParameterName);
                healthParameterNameAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeTouchHelper);
        itemTouchHelper.attachToRecyclerView(healthParameterNameRecyclerView);
    }

    private void setHealthParameterNames(List<HealthParameterName> healthParameterNames) {
       healthParameterNameAdapter.setHealthParameterNames(healthParameterNames);
    }

    private void injectDependencies(View root) {
        ButterKnife.bind(this, root);
        personalHealthMonitor().applicationComponent().inject(this);
    }

    @OnClick(R.id.health_parameter_name_button)
    public void createHealthParameterName() {
        HealthParameterNameDialog healthParameterNameDialog = new HealthParameterNameDialog(healthParameterNameViewModel);
        healthParameterNameDialog.show(getActivity().getSupportFragmentManager(), "Health parameter");
    }

}