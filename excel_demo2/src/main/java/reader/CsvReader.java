package reader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface CsvReader {

  <T> List<T> read(Class<T> type) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, NoSuchFieldException;
}
