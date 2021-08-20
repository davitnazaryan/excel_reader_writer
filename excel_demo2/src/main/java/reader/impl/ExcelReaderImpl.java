package reader.impl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import reader.ExcelReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExcelReaderImpl implements ExcelReader {

  private Workbook workbook;

  public ExcelReaderImpl(final String excelPath) throws IOException {
    File file = new File(excelPath);
    String fileName = file.getName();
    String extension = file.getName().substring(fileName.lastIndexOf(".") + 1);
    if ("xls".equalsIgnoreCase(extension)) {
      try (InputStream in = new FileInputStream(excelPath)) {
        this.workbook = new HSSFWorkbook(in);

      }
    } else if ("xlsx".equalsIgnoreCase(extension)) {
      this.workbook = new XSSFWorkbook(excelPath);
    }
  }

  public <T> List<T> read(int sheet, Class<T> type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    return this.read(workbook.getSheetAt(sheet), type);
  }

  public <T> List<T> read(String sheetName, Class<T> type) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    return this.read(workbook.getSheet(sheetName), type);
  }

  private <T> List<T> read(Sheet sh, Class<T> type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    List<T> data = new LinkedList<>();
    Map<String, Integer> header = this.readHeader(sh.getRow(0));
    boolean skipHeader = true;
    for (Row row : sh) {
      if (skipHeader) {
        skipHeader = false;
        continue;
      }
      T obj = this.transform(row, header, type);
      data.add(obj);
    }
    return data;
  }


  private <T> T transform(Row row, Map<String, Integer> header, Class<T> type) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<T> constructor = type.getConstructor();
    T obj = constructor.newInstance();
    Field[] fields = type.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      String fieldName = field.getName();
      Integer cellNum = header.get(fieldName);
      if (cellNum != null) {
        Cell cell = row.getCell(cellNum);
        CellType cellType = cell.getCellType();
        switch (cellType) {
          case STRING:
            String strValue = cell.getStringCellValue();
            field.set(obj, strValue);
            break;
          case NUMERIC:
            Double dValue = cell.getNumericCellValue();
            Class<?> fType = field.getType();
            if (fType == byte.class || fType == Byte.class) {
              field.set(obj, dValue.byteValue());
            } else if (fType == short.class || fType == Short.class) {
              field.set(obj, dValue.shortValue());
            } else if (fType == int.class || fType == Integer.class) {
              field.set(obj, dValue.intValue());
            } else if (fType == long.class || fType == Long.class) {
              field.set(obj, dValue.longValue());
            } else if (fType == float.class || fType == Float.class) {
              field.set(obj, dValue.floatValue());
            } else if (fType == double.class || fType == Double.class) {
              field.set(obj, dValue);
            }
            break;
          case _NONE:
            field.set(obj, null);
            break;
          case BLANK:
            field.set(obj, "");
            break;
          default:
            throw new RuntimeException("Unknown type.");
        }
      }
    }
    return obj;
  }

  private Map<String, Integer> readHeader(Row row) {
    Map<String, Integer> header = new HashMap<>();
    for (Cell cell : row) {
      header.put(cell.getStringCellValue(), cell.getColumnIndex());
    }
    return header;
  }
}
