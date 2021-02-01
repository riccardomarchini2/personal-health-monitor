package com.project.personal_health_monitor.view;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.project.personal_health_monitor.R;
import com.project.personal_health_monitor.notification.Notification;
import com.project.personal_health_monitor.view.base.BaseActivity;
import com.project.personal_health_monitor.view.dialog.MyTimePickerDialog;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class SettingsActivity extends BaseActivity {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);

    public static final String NAME = "PersonalHealthMonitor";

    public static final String REMEMBER_REPORT_HOUR = "RememberReportHour";
    public static final String REMEMBER_REPORT_MINUTE = "RememberReportMinute";
    public static final int DEFAULT_REMEMBER_REPORT_HOUR = 8;
    public static final int DEFAULT_REMEMBER_REPORT_MINUTE = 0;
    public static final String POSTPONE_BY = "PostponeBy";
    public static final int DEFAULT_POSTPONE_BY = 10;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.report_local_time_text_view)
    TextView reportLocalTimeTextView;

    @BindView(R.id.report_postpone_by_edit_text)
    EditText reportPostponeByEditText;

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