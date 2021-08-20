package writer.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import writer.ExcelWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class ExcelWriterImpl implements ExcelWriter {

  private String outputPath;

  public ExcelWriterImpl(String outputPath) {
    this.outputPath = outputPath;
  }

  @Override
  public void writeOne(List<?> data) throws NoSuchFieldException, IllegalAccessException, IOException {
    Workbook workbook = new XSSFWorkbook();
    createSheetAndWriteContent(workbook, data);
    writeExcelToFile(workbook, this.outputPath);
  }

  @Override
  public void writeMultiple(List<List<?>> data) throws NoSuchFieldException, IllegalAccessException, IOException {
    Workbook workbook = new XSSFWorkbook();
    for (List<?> d : data) {
      createSheetAndWriteContent(workbook, d);
    }
    writeExcelToFile(workbook, this.outputPath);
  }

  private static void writeExcelToFile(Workbook workbook, String outputPath) throws IOException {
    try (OutputStream out = new FileOutputStream(outputPath)) {
      workbook.write(out);
    }
  }

  private static void createSheetAndWriteContent(Workbook workbook, List<?> data) throws NoSuchFieldException, IllegalAccessException {
    Sheet sheet = workbook.createSheet(data.get(0).getClass().getSimpleName().toUpperCase());
    writeSheetData(workbook, sheet, data);
  }

  private static void writeSheetData(Workbook workbook, Sheet sheet, List<?> data) throws NoSuchFieldException, IllegalAccessException {

    List<String> header = createHeader(data.get(0).getClass());

    int i = 0;
    Row headerRow = sheet.createRow(i++);
    CellStyle cellStyle = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    font.setColor(IndexedColors.BLUE.index);
    cellStyle.setFont(font);
    cellStyle.setFillForegroundColor(IndexedColors.RED.index);
    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    writeRow(header, headerRow);
    for (Cell cell : headerRow) {
      cell.setCellStyle(cellStyle);
    }

    for (Object obj : data) {
      List<Object> rowContent = createContent(header, obj);
      writeRow(rowContent, sheet.createRow(i++));
    }
    int j = 0;
    for (Cell cell : headerRow) {
      sheet.autoSizeColumn(j++);
    }
  }

  private static void writeRow(List<?> data, Row row) {
    for (int i = 0; i < data.size(); i++) {
      Object cellValue = data.get(i);
      if (cellValue != null) {
        Cell cell = row.createCell(i);
        if (cellValue instanceof Number) {
          cell.setCellValue(((Number) cellValue).doubleValue());
        } else if (cellValue instanceof Boolean) {
          cell.setCellValue((Boolean) cellValue);
        } else {
          cell.setCellValue(cellValue.toString());
        }
      }
    }
  }

  private static List<String> createHeader(Class<?> clazz) {
    List<String> headers = new LinkedList<>();
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      headers.add(field.getName());
    }
    return headers;
  }

  private static List<Object> createContent(List<String> headers, Object obj) throws NoSuchFieldException, IllegalAccessException {
    List<Object> data = new LinkedList<>();
    Class<?> clazz = obj.getClass();
    for (String columnName : headers) {
      Field field = clazz.getDeclaredField(columnName);
      field.setAccessible(true);
      data.add(field.get(obj));
    }
    return data;
  }
}
