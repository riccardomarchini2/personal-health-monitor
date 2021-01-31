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

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DatePickerDialog extends DialogFragment {

    private DatePickerCallback datePickerCallback;

    private final SublimeListenerAdapter sublimeListener = new SublimeListenerAdapter() {

        @Override
        public void onCancelled() {
            dismiss();
        }

        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimePicker, SelectedDate selectedDate, int hour, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
            if (datePickerCallback != null) {
                Calendar calendar = selectedDate.getFirstDate();
                long milliseconds = calendar.getTimeInMillis();
                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), calendar.getTimeZone().toZoneId());
                datePickerCallback.onDateSelected(localDateTime.toLocalDate());
            }
            dismiss();
        }

    };

    public void setCallback(DatePickerCallback datePickerCallback) {
        this.datePickerCallback = datePickerCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SublimePicker sublimePicker = (SublimePicker) getActivity().getLayoutInflater().inflate(R.layout.sublime_picker, container);

        SublimeOptions sublimeOptions = new SublimeOptions();
        sublimeOptions.setCanPickDateRange(false);
        sublimeOptions.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER);

        sublimePicker.initializePicker(sublimeOptions, sublimeListener);
        return sublimePicker;
    }

    public interface DatePickerCallback {

        public void onDateSelected(LocalDate localDate);

    }

}
