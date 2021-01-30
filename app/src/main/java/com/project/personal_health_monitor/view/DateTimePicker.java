package com.project.personal_health_monitor.view;

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
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * Used to write apps that run on platforms prior to Android 3.0.
 */
public class DateTimePicker extends DialogFragment {

    /*
     * It is recommended to create separate format instances for each thread.
     * If multiple threads access a format concurrently, it must be synchronized
     * externally.
     */
    private DateFormat dateFormat;

    /*
     * A customizable view that provisions picking of a date,
     * time and recurrence option, all from a single user-interface.
     */
    private SublimePicker sublimePicker;

    private Callback callback;

    // SublimeListenerAdapter constructor
    SublimeListenerAdapter sublimeListener = new SublimeListenerAdapter() {

        /**
         * Cancel button or icon clicked
         */
        @Override
        public void onCancelled() {

            // Dismiss the fragment and its dialog
            dismiss();
        }

        /**
         * Gets the datetime if a date in calendar is selected.
         * @param sublimePicker
         * @param selectedDate
         * @param hour
         * @param minute
         * @param recurrenceOption
         * @param recurrenceRule
         */
        @Override
        public void onDateTimeRecurrenceSet(
            SublimePicker sublimePicker,
            SelectedDate selectedDate,
            int hour,
            int minute,
            // Not in use
            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
            // Not in use
            String recurrenceRule
        ) {
            if (callback != null) {

                // Returns calendar of selectedDate
                Calendar calendar = selectedDate.getFirstDate();

                // Gets time of calendar in milliseconds
                long calendarTimeInMillis = calendar.getTimeInMillis();

                // Gets local date time of now in calendarTimeInMillis
                LocalDateTime millis = LocalDateTime.ofInstant(Instant.ofEpochMilli(calendarTimeInMillis), calendar.getTimeZone().toZoneId());

                // get millis of year, month, day
                int year = millis.getYear();
                int month = millis.getMonth().getValue();
                int day = millis.getDayOfMonth();

                // Sets callback to datetime or null
                callback.selected(year, month, day, hour, minute);
            }

            // Dismiss the fragment and its dialog
            dismiss();
        }

    };

    // Standard default constructor
    public DateTimePicker() {
        this(null);
    }

    // Constructor with callback input param
    public DateTimePicker(Callback callback) {
        dateFormat = DateFormat.getTimeInstance( DateFormat.SHORT, Locale.getDefault() );
        dateFormat.setTimeZone( TimeZone.getDefault() );

        // Sets callback
        setCallback(callback);
    }

    // Setter callback
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    /**
     * Creates the view to select date and time
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sublimePicker = (SublimePicker) getActivity().getLayoutInflater().inflate(R.layout.sublime_picker, container);

        SublimeOptions options = new SublimeOptions();
        options.setCanPickDateRange(false);
        options.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER | SublimeOptions.ACTIVATE_TIME_PICKER);

        sublimePicker.initializePicker(options, sublimeListener);
        return sublimePicker;
    }

    /**
     * Callback interface.
     * Gets the selected day.
     */
    public interface Callback {

        void selected(int year, int month, int day, int hour, int minute);

    }

}
