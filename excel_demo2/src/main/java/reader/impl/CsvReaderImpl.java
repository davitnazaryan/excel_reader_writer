package reader.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import reader.CsvReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class CsvReaderImpl implements CsvReader {

  private String filePath;

  public CsvReaderImpl(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public <T> List<T> read(Class<T> type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
    List<T> data = new LinkedList<>();
    try (Reader reader = new FileReader(this.filePath)) {
      CSVFormat csvFormat = CSVFormat
          .newFormat(',')
          .withDelimiter(',')
          .withQuote('"')
          .withRecordSeparator("\n")
          .withFirstRecordAsHeader()
          .withQuoteMode(QuoteMode.NON_NUMERIC);
      CSVParser csvParser = CSVParser.parse(reader, csvFormat);
      List<String> headers = csvParser.getHeaderNames();
      List<CSVRecord> csvRecords = csvParser.getRecords();
      for (CSVRecord csvRecord : csvRecords) {
        data.add(read(csvRecord, headers, type));
      }
    }
    return data;
  }

  private static <T> T read(CSVRecord csvRecord, List<String> headers, Class<T> type) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
    Constructor<T> constructor = type.getConstructor();
    constructor.setAccessible(true);
    T t = constructor.newInstance();
    for (String header : headers) {
      Field field = type.getDeclaredField(header);
      field.setAccessible(true);

      String value = csvRecord.get(header);

      if (value != null) {
        Class<?> fType = field.getType();
        if (fType == byte.class || fType == Byte.class) {
          field.set(t, Byte.parseByte(value));
        } else if (fType == short.class || fType == Short.class) {
          field.set(t, Short.parseShort(value));
        } else if (fType == int.class || fType == Integer.class) {
          field.set(t, Integer.parseInt(value));
        } else if (fType == long.class || fType == Long.class) {
          field.set(t, Long.parseLong(value));
        } else if (fType == float.class || fType == Float.class) {
          field.set(t, Float.parseFloat(value));
        } else if (fType == double.class || fType == Double.class) {
          field.set(t, Double.parseDouble(value));
        } else if (fType == boolean.class || fType == Boolean.class) {
          field.set(t, Boolean.parseBoolean(value));
        } else {
          field.set(t, value);
        }
      }
    }
    return t;
  }
}
