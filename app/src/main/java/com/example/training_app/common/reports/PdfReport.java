package com.example.training_app.common.reports;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.training_app.common.interfaces.ICreateReportCallback;
import com.example.training_app.common.models.Workingout;
import com.example.training_app.common.storage.ConstantStorage;
import com.example.training_app.mvp.main.MainActivity;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.example.training_app.R;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PdfReport {

    private static String MESSAGE = "";

    private static String FILENAME = "";
    private static String DESTINATION = "";

    private static String TITLE = "";
    private static String DATE = "";
    private static Workingout workingout = null;

    private static Context context = null;
    private static Activity activity = null;

    public PdfReport(Context context, Activity activity, Workingout workingout) {
        PdfReport.context = context;
        PdfReport.activity = activity;
        PdfReport.MESSAGE = context.getResources().getString(R.string.messageAfterCreatePdfReport);
        PdfReport.DATE = context.getSharedPreferences(MainActivity.APP_NAME, Context.MODE_PRIVATE).getString(MainActivity.SELECTED_DATE, "");
        PdfReport.TITLE = ConstantStorage.TITLE;
        PdfReport.FILENAME = String.format("Report_%s_%s.pdf", DATE, TITLE);
        PdfReport.DESTINATION = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + FILENAME;
        PdfReport.workingout = workingout;
    }

    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(activity, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, R.integer.PERMISSION_WRITE_EXTERNAL_STORAGE);
            }
        }
        return true;
    }

    @SuppressLint("SimpleDateFormat")
    private static String getDate() {
        SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat simpleDateFormatOut = new SimpleDateFormat("dd MMMM, yyyy");
        Date date = new Date();
        try {
            date = simpleDateFormatInput.parse(DATE);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return simpleDateFormatOut.format(date);
    }

    @SuppressLint("ResourceType")
    public static void createPdf(ICreateReportCallback callback) {
        try {
            Rectangle small = new Rectangle(300,500);

            BaseFont baseFont = BaseFont.createFont(ConstantStorage.helveticaFontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            Font titleFont = new Font(baseFont, 24, Font.BOLD);
            Font subTitleFont = new Font(baseFont, 16, Font.BOLD);
            Font dateFont = new Font(baseFont, 10, Font.NORMAL);
            Font simpleFont = new Font(baseFont, 10, Font.BOLD);

            Document document = new Document(small, 10, 10, 10, 10);
            PdfWriter.getInstance(document, new FileOutputStream(DESTINATION));

            document.open();

            PdfPTable table = new PdfPTable(2);
            table.setTotalWidth(new float[] { 200, 80 } );
            table.setLockedWidth(true);

            PdfPCell cell = new PdfPCell(new Phrase(TITLE, titleFont));
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            table.addCell(cell);

            PdfPCell totalCell = new PdfPCell(new Phrase(context.getResources().getString(R.string.totalPhrase), simpleFont));
            totalCell.setFixedHeight(20);
            totalCell.setBorder(Rectangle.NO_BORDER);
            totalCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell madeCell = new PdfPCell(new Phrase(String.format("%s %s", context.getResources().getString(R.string.workedoutForPhrase), TITLE), simpleFont));
            madeCell.setFixedHeight(20);
            madeCell.setBorder(Rectangle.NO_BORDER);
            madeCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell leftCell = new PdfPCell(new Phrase(context.getResources().getString(R.string.leftPhrase), simpleFont));
            leftCell.setFixedHeight(20);
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell cellTopBorder = new PdfPCell();
            cellTopBorder.setFixedHeight(20);
            cellTopBorder.setBorder(Rectangle.TOP);
            cellTopBorder.setColspan(2);

            cell = new PdfPCell(new Phrase(PdfReport.getDate(), dateFont));
            cell.setFixedHeight(30);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setColspan(2);
            table.addCell(cell);

            // # start Calories
            cell = new PdfPCell(new Phrase(context.getResources().getString(R.string.caloriesPhrase), subTitleFont));
            cell.setFixedHeight(25);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);

            table.addCell(totalCell);

            cell = new PdfPCell(new Phrase(String.valueOf(workingout.getAllCaloriesWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(madeCell);

            cell = new PdfPCell(new Phrase(String.valueOf(workingout.getCurrentCaloriesWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(leftCell);

            cell = new PdfPCell(new Phrase(String.valueOf(ConstantStorage.maxCalories - workingout.getAllCaloriesWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(cellTopBorder);
            // # end Calories

            // # start Cards
            cell = new PdfPCell(new Phrase(context.getResources().getString(R.string.cardioPhrase), subTitleFont));
            cell.setFixedHeight(25);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);

            table.addCell(totalCell);

            cell = new PdfPCell(new Phrase(String.valueOf(workingout.getAllCardsWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(madeCell);

            cell = new PdfPCell(new Phrase(String.valueOf(workingout.getCurrentCardsWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(leftCell);

            cell = new PdfPCell(new Phrase(String.valueOf(ConstantStorage.maxCards - workingout.getAllCardsWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(cellTopBorder);
            // # end Cards

            // # start Strengths
            cell = new PdfPCell(new Phrase(context.getResources().getString(R.string.strengthPhrase), subTitleFont));
            cell.setFixedHeight(25);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);

            table.addCell(totalCell);

            cell = new PdfPCell(new Phrase(String.valueOf(workingout.getAllStrengthsWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(madeCell);

            cell = new PdfPCell(new Phrase(String.valueOf(workingout.getCurrentStrengthsWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(leftCell);

            cell = new PdfPCell(new Phrase(String.valueOf(ConstantStorage.maxStrengths - workingout.getAllStrengthsWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(cellTopBorder);
            // # end Strengths

            // # start Agilitys
            cell = new PdfPCell(new Phrase(context.getResources().getString(R.string.agilityPhrase), subTitleFont));
            cell.setFixedHeight(25);
            cell.setBorder(Rectangle.BOTTOM);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setColspan(2);
            table.addCell(cell);

            table.addCell(totalCell);

            cell = new PdfPCell(new Phrase(String.valueOf(workingout.getAllAgilitysWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(madeCell);

            cell = new PdfPCell(new Phrase(String.valueOf(workingout.getCurrentAgilitysWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(leftCell);

            cell = new PdfPCell(new Phrase(String.valueOf(ConstantStorage.maxAgilitys - workingout.getAllAgilitysWorkingout()), simpleFont));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);

            table.addCell(cellTopBorder);
            // # end Agilitys

            document.add(table);
            document.close();
            callback.onCallback(MESSAGE);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }
}
