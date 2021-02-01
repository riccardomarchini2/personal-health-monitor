package com.project.personal_health_monitor.view.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.project.personal_health_monitor.R;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class MyTimePickerDialog extends DialogFragment {

    private TimePickerCallback timePickerCallback;

    private final SublimeListenerAdapter sublimeListener = new SublimeListenerAdapter() {

        @Override
        public void onCancelled() {
            dismiss();
        }

        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimePicker, SelectedDate selectedDate, int hour, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
            if (timePickerCallback != null) {
                LocalTime localTime = LocalTime.of(hour, minute);
                timePickerCallback.onTimeSelected(localTime);
            }
            dismiss();
        }

    };

    public void setCallback(TimePickerCallback timePickerCallback) {
        this.timePickerCallback = timePickerCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SublimePicker sublimePicker = (SublimePicker) getActivity().getLayoutInflater().inflate(R.layout.sublime_picker, container);

        SublimeOptions sublimeOptions = new SublimeOptions();
        sublimeOptions.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
        sublimeOptions.setDisplayOptions(SublimeOptions.ACTIVATE_TIME_PICKER);

        sublimePicker.initializePicker(sublimeOptions, sublimeListener);
        return sublimePicker;
    }

    public interface TimePickerCallback {

        public void onTimeSelected(LocalTime localTime);

    }

}
