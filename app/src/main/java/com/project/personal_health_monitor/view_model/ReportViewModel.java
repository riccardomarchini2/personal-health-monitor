package com.project.personal_health_monitor.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.personal_health_monitor.persistence.model.Report;
import com.project.personal_health_monitor.persistence.model.ReportWithHealthParameters;
import com.project.personal_health_monitor.repository.ReportRepository;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

public class ReportViewModel extends ViewModel {

    private final ReportRepository reportRepository;

    @Inject
    public ReportViewModel(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Long create(Report report) {
        return reportRepository.create(report);
    }

    public LiveData<List<ReportWithHealthParameters>> getAll() {
        return reportRepository.getAll();
    }

    public LiveData<List<ReportWithHealthParameters>> getBy(LocalDate localDate) {
        return reportRepository.getBy(localDate);
    }

    public void update(Report report) {
        reportRepository.update(report);
    }

    public void delete(Report report) {
        reportRepository.delete(report);
    }

}
