package reader;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ExcelReader {

  <T> List<T> read(int sheet, Class<T> type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;

  <T> List<T> read(String sheetName, Class<T> type) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;
}
