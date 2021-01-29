package com.project.personal_health_monitor.repository;

import androidx.lifecycle.LiveData;

import com.project.personal_health_monitor.persistence.dao.Reports;
import com.project.personal_health_monitor.persistence.model.Report;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

public class ReportRepository {

    private final Reports reports;

    @Inject
    public ReportRepository(Reports reports) {
        this.reports = reports;
    }

    public void create(Report report) {
        reports.create(report);
    }

    public LiveData<List<ReportWithHealthParameters>> getAll() {
        return reports.getAll();
    }

    public LiveData<List<ReportWithHealthParameters>> getBy(LocalDate localDate) {
        return reports.getBy(localDate);
    }

    public void update(Report report) {
        reports.update(report);
    }

    public void delete(Report report) {
        reports.delete(report);
    }

}
