package com.example.training_app.mvp.statistics;

import android.widget.Toast;

import com.example.training_app.common.interfaces.ICreateReportCallback;
import com.example.training_app.common.reports.PdfReport;

public class ExerciseStatisticsPresenter {

    private com.example.training_app.mvp.statistics.ExerciseStatisticsFragment view;

    public void attachView(com.example.training_app.mvp.statistics.ExerciseStatisticsFragment exerciseStatisticsFragment) {
        view = exerciseStatisticsFragment;
    }

    public void detachView() {
        view = null;
    }

    public void onDownloadPDF() {
        PdfReport pdfReport = new PdfReport(view.requireContext(), view.requireActivity(), view.getWorkingout());
        if (pdfReport.checkPermission()) {
            PdfReport.createPdf(new ICreateReportCallback() {
                @Override
                public void onCallback(String message) {
                    Toast.makeText(view.requireContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onBack() {
        view.onClose();
    }
}