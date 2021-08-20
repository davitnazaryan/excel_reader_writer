package writer;

import java.io.IOException;
import java.util.List;

public interface CsvWriter {

  void write(List<?> data) throws IOException, NoSuchFieldException, IllegalAccessException;
}
