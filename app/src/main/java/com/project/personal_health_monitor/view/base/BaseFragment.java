package com.project.personal_health_monitor.view.base;

import androidx.fragment.app.Fragment;

import com.project.personal_health_monitor.PersonalHealthMonitor;

public class BaseFragment extends Fragment {

    public PersonalHealthMonitor personalHealthMonitor() {
        return (PersonalHealthMonitor) getActivity().getApplication();
    }
}
