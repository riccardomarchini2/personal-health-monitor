package com.project.personal_health_monitor.view.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;
import com.project.personal_health_monitor.view_model.HealthParameterNameViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class HealthParameterNameDialog extends DialogFragment {

    @BindView(R.id.health_parameter_value_edit_text)
    EditText healthParameterNameEditText;

    @BindView(R.id.health_parameter_name_priority_edit_text)
    EditText healthParameterNamePriorityEditText;

    private final HealthParameterNameViewModel healthParameterNameViewModel;

    public HealthParameterNameDialog(HealthParameterNameViewModel healthParameterNameViewModel) {
        this.healthParameterNameViewModel = healthParameterNameViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = getActivity().getLayoutInflater().inflate(R.layout.health_parameter_name_dialog, container);
        injectDependencies(root);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullWidthDialog);
    }

    private void injectDependencies(View root) {
        ButterKnife.bind(this, root);
    }

    @OnClick(R.id.health_parameter_name_dismiss_button)
    public void closeDialog() {
        dismiss();
    }

    @OnClick(R.id.health_parameter_name_save_button)
    public void saveHealthParameterName() {
        String name = healthParameterNameEditText.getText().toString();
        String priorityAsString = healthParameterNamePriorityEditText.getText().toString();

        if (!name.isEmpty() && !priorityAsString.isEmpty()) {
            Integer priority = Integer.parseInt(priorityAsString);

            HealthParameterName healthParameterName = new HealthParameterName();
            healthParameterName.name = name;
            healthParameterName.priority = priority;

            healthParameterNameViewModel.create(healthParameterName);

            dismiss();
        }
    }

}
