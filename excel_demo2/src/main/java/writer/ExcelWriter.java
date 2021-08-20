package writer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface ExcelWriter {

  void writeOne(List<?> data) throws NoSuchFieldException, IllegalAccessException, IOException;

  void writeMultiple(List<List<?>> data) throws NoSuchFieldException, IllegalAccessException, IOException;


}
