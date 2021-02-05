package com.project.personal_health_monitor.view;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.notification.Notification;
import com.project.personal_health_monitor.persistence.model.HealthParameterName;
import com.project.personal_health_monitor.view.base.BaseActivity;
import com.project.personal_health_monitor.view.dialog.MyTimePickerDialog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class SettingsActivity extends BaseActivity {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);

    public static final String NAME = "PersonalHealthMonitor";

    public static final String REMEMBER_REPORT_HOUR = "RememberReportHour";
    public static final String REMEMBER_REPORT_MINUTE = "RememberReportMinute";
    public static final int DEFAULT_REMEMBER_REPORT_HOUR = 20;
    public static final int DEFAULT_REMEMBER_REPORT_MINUTE = 0;
    public static final String POSTPONE_BY = "PostponeBy";
    public static final int DEFAULT_POSTPONE_BY = 10;
    public static final List<Integer> POSTPONE_BY_VALUES = new ArrayList<>();

    public static final String THRESHOLD = "Threshold";
    public static final Float THRESHOLD_DEFAULT = 5.0F;
    public static final List<Float> THRESHOLD_VALUES = new ArrayList<>();

    public static final String THRESHOLD_CONTROL = "Threshold Control";
    public static final Integer THRESHOLD_CONTROL_DEFAULT = 7;
    public static final List<Integer> THRESHOLD_CONTROL_VALUES = new ArrayList<>();

    static {
        POSTPONE_BY_VALUES.add(10);
        POSTPONE_BY_VALUES.add(20);
        POSTPONE_BY_VALUES.add(30);
        POSTPONE_BY_VALUES.add(40);
        POSTPONE_BY_VALUES.add(50);
        POSTPONE_BY_VALUES.add(60);

        THRESHOLD_VALUES.add(5.0F);
        THRESHOLD_VALUES.add(5.5F);
        THRESHOLD_VALUES.add(6.0F);
        THRESHOLD_VALUES.add(6.5F);
        THRESHOLD_VALUES.add(7.0F);
        THRESHOLD_VALUES.add(7.5F);
        THRESHOLD_VALUES.add(8.0F);
        THRESHOLD_VALUES.add(8.5F);
        THRESHOLD_VALUES.add(9.0F);
        THRESHOLD_VALUES.add(9.5F);
        THRESHOLD_VALUES.add(10.0F);

        THRESHOLD_CONTROL_VALUES.add(2);
        THRESHOLD_CONTROL_VALUES.add(3);
        THRESHOLD_CONTROL_VALUES.add(4);
        THRESHOLD_CONTROL_VALUES.add(5);
        THRESHOLD_CONTROL_VALUES.add(6);
        THRESHOLD_CONTROL_VALUES.add(7);
        THRESHOLD_CONTROL_VALUES.add(14);
        THRESHOLD_CONTROL_VALUES.add(21);
        THRESHOLD_CONTROL_VALUES.add(28);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.report_local_time_text_view)
    TextView reportLocalTimeTextView;

    @BindView(R.id.report_postpone_by_spinner)
    Spinner reportPostponeBySpinner;

    @BindView(R.id.threshold_spinner)
    Spinner thresholdSpinner;

    @BindView(R.id.threshold_control_spinner)
    Spinner thresholdControlSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        injectDependencies();
        initializeToolbar();
        initializeSettings();
    }

    private void injectDependencies() {
        ButterKnife.bind(this);
        personalHealthMonitor().applicationComponent().inject(this);
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.NAME, MODE_PRIVATE);

        int hour = sharedPreferences.getInt(SettingsActivity.REMEMBER_REPORT_HOUR, SettingsActivity.DEFAULT_REMEMBER_REPORT_HOUR);
        int minute = sharedPreferences.getInt(SettingsActivity.REMEMBER_REPORT_MINUTE, SettingsActivity.DEFAULT_REMEMBER_REPORT_MINUTE);

        LocalTime localTime = LocalTime.of(hour, minute);
        setLocalTime(localTime);

        ArrayAdapter<Integer> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, POSTPONE_BY_VALUES);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportPostponeBySpinner.setAdapter(spinnerArrayAdapter);

        int minutes = sharedPreferences.getInt(SettingsActivity.POSTPONE_BY, SettingsActivity.DEFAULT_POSTPONE_BY);

        for (int i=0; i<spinnerArrayAdapter.getCount(); i++){
            int value = spinnerArrayAdapter.getItem(i);
            if (value == minutes) {
                reportPostponeBySpinner.setSelection(i);
            }
        }

        reportPostponeBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt(SettingsActivity.POSTPONE_BY, SettingsActivity.POSTPONE_BY_VALUES.get(position));

                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //DA FINIRE
        ArrayAdapter<Float> spinnerArrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, THRESHOLD_VALUES);
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thresholdSpinner.setAdapter(spinnerArrayAdapter2);

        float threshold = sharedPreferences.getFloat(SettingsActivity.THRESHOLD, SettingsActivity.THRESHOLD_DEFAULT);

        for (int i=0; i<spinnerArrayAdapter2.getCount(); i++){
            float value = spinnerArrayAdapter2.getItem(i);
            if (value == threshold) {
                thresholdSpinner.setSelection(i);
            }
        }

        thresholdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putFloat(SettingsActivity.THRESHOLD, SettingsActivity.THRESHOLD_VALUES.get(position));

                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<Integer> spinnerArrayAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, THRESHOLD_CONTROL_VALUES);
        spinnerArrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thresholdControlSpinner.setAdapter(spinnerArrayAdapter3);

        int thresholdControl = sharedPreferences.getInt(SettingsActivity.THRESHOLD_CONTROL, SettingsActivity.THRESHOLD_CONTROL_DEFAULT);

        for (int i=0; i<spinnerArrayAdapter3.getCount(); i++){
            int value = spinnerArrayAdapter3.getItem(i);
            if (value == thresholdControl) {
                thresholdControlSpinner.setSelection(i);
            }
        }

        thresholdControlSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt(SettingsActivity.THRESHOLD_CONTROL, SettingsActivity.THRESHOLD_CONTROL_VALUES.get(position));

                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick(R.id.change_report_local_time_button)
    public void onChangeTimeClicked() {
        MyTimePickerDialog timePickerDialog = new MyTimePickerDialog();
        timePickerDialog.setCallback(this::onTimeChange);
        timePickerDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        timePickerDialog.show(this.getSupportFragmentManager(), "Select time");
    }

    private void onTimeChange(LocalTime localTime) {
        int hour = localTime.getHour();
        int minute = localTime.getMinute();

        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(SettingsActivity.REMEMBER_REPORT_HOUR, hour);
        editor.putInt(SettingsActivity.REMEMBER_REPORT_MINUTE, minute);

        editor.apply();


        LocalDateTime nextAlarmDateTime = LocalDateTime.of(LocalDate.now(), localTime);
        if (nextAlarmDateTime.isBefore(LocalDateTime.now())) {
            nextAlarmDateTime = LocalDateTime.of(LocalDate.now().plusDays(1), localTime);
        }

        Notification.setRememberToCreateReportNotification(this, nextAlarmDateTime);

        setLocalTime(localTime);
    }

    private void setLocalTime(LocalTime localTime) {
        reportLocalTimeTextView.setText(localTime.format(DATE_TIME_FORMATTER));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}