package writer.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import writer.CsvWriter;

import java.io.*;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class CsvWriterImpl implements CsvWriter {

  private String outputPath;

  public CsvWriterImpl(String outputPath) {
    this.outputPath = outputPath;
  }

  @Override
  public void write(List<?> data) throws IOException, NoSuchFieldException, IllegalAccessException {
    List<String> headers = createHeader(data.get(0).getClass());
    try (Writer writer = new FileWriter(this.outputPath);
         CSVPrinter csvPrinter = CSVFormat
             .newFormat(',')
             .withDelimiter(',')
             .withQuote('"')
             .withRecordSeparator("\n")
             .withQuoteMode(QuoteMode.NON_NUMERIC)
             .withHeader(headers.toArray(new String[headers.size()]))
             .print(writer)) {
      for (Object obj : data) {
        csvPrinter.printRecord(createContent(headers, obj));
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
