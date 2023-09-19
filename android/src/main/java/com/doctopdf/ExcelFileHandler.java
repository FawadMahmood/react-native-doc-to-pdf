package com.doctopdf;

import androidx.annotation.NonNull;


import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.CellStyle;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import android.os.Environment;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;


import java.util.Iterator;


public class ExcelFileHandler {
  ReactApplicationContext context;
  String filePath;
  String fileType;
  Promise promise;

  public ExcelFileHandler(String filePath, String fileType, ReactApplicationContext context, Promise promise){
    this.filePath = filePath;
    this.context = context;
    this.fileType = fileType;
    this.promise = promise;
  }

  void handleExcelFile(String filePath){
    try {
      FileInputStream fis = new FileInputStream(filePath.replace("file://", ""));

      Workbook workbook;
      if (filePath.endsWith(".xls")) {
        workbook = new HSSFWorkbook(fis);
      } else if (filePath.endsWith(".xlsx")) {
        workbook = new XSSFWorkbook(fis);
      } else {
        throw new IllegalArgumentException("Unsupported Excel file format");
      }

      // Get the first sheet
      Sheet sheet = workbook.getSheetAt(0);

      short availableColumns = sheet.getRow(0).getLastCellNum();

      Iterator<Row> rowIterator = sheet.iterator();

      String randomFileName = UUID.randomUUID().toString() + ".pdf";
      File imageFile = new File(this.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), randomFileName);

      Document iText_xls_2_pdf = new Document();
      PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream(imageFile));
      iText_xls_2_pdf.open();

      PdfPTable my_table = new PdfPTable(availableColumns);
      PdfPCell table_cell = null;

      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        Iterator<Cell> cellIterator = row.cellIterator();

        while (cellIterator.hasNext()) {
          Cell cell = cellIterator.next();

          switch (cell.getCellType()) {
            default:
              try {
                table_cell = new PdfPCell(new Phrase(cell.getStringCellValue()));
              } catch (IllegalStateException illegalStateException) {
                //TODO: Need to handle exceptions for different type too
                if (illegalStateException.getMessage().equals("Cannot get a STRING value from a NUMERIC cell")) {
                  table_cell = new PdfPCell(new Phrase(String.valueOf(cell.getNumericCellValue())));
                }
              }

              break;
          }


          // Get Excel cell style
          CellStyle cellStyle = cell.getCellStyle();
          // Apply Excel cell style to PdfPCell
          applyCellStyleToPdfCell(cellStyle, table_cell, workbook);

          // Add PdfPCell to PdfPTable
          my_table.addCell(table_cell);
        }
      }

      iText_xls_2_pdf.add(my_table);
      iText_xls_2_pdf.close();
      workbook.close();
      fis.close();
      this.promise.resolve(imageFile.getAbsolutePath());
    }catch(Exception e){
      e.printStackTrace();
      this.promise.reject("error generating pdf");
    }
  }

  private void applyCellStyleToPdfCell(CellStyle cellStyle, PdfPCell pdfCell,Workbook workbook) {
    // Get font
    com.itextpdf.text.Font font = new com.itextpdf.text.Font();
    org.apache.poi.ss.usermodel.Font excelFont = workbook.getFontAt(cellStyle.getFontIndex());
    font.setSize(excelFont.getFontHeightInPoints());
    font.setStyle(excelFont.getBold() ? com.itextpdf.text.Font.BOLD : com.itextpdf.text.Font.NORMAL);
    pdfCell.setPhrase(new Phrase(pdfCell.getPhrase().getContent(), font));

    // Get text alignment
    HorizontalAlignment alignment = HorizontalAlignment.LEFT;
    switch (cellStyle.getAlignment()) {
      case CENTER:
        alignment = HorizontalAlignment.CENTER;
        break;
      case RIGHT:
        alignment = HorizontalAlignment.RIGHT;
        break;
    }

    pdfCell.setHorizontalAlignment(alignment.getCode());

    VerticalAlignment verticalAlignment = VerticalAlignment.CENTER;
    switch (cellStyle.getVerticalAlignment()) {
      case CENTER:
        verticalAlignment = VerticalAlignment.CENTER;
        break;
      case TOP:
        verticalAlignment = VerticalAlignment.TOP;
        break;
      case BOTTOM:
        verticalAlignment = VerticalAlignment.BOTTOM;
        break;
    }
    pdfCell.setVerticalAlignment(verticalAlignment.getCode());
  }


}
