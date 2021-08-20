import model.Book;
import model.User;
import reader.CsvReader;
import reader.ExcelReader;
import reader.impl.CsvReaderImpl;
import reader.impl.ExcelReaderImpl;
import writer.CsvWriter;
import writer.ExcelWriter;
import writer.impl.CsvWriterImpl;
import writer.impl.ExcelWriterImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
//    ExcelReader excelReader = new ExcelReaderImpl("/home/gnuni/Desktop/test.xls");
//    System.out.println(excelReader.read(0, User.class));
//    System.out.println(excelReader.read(1, Book.class));

    ExcelReader excelReader = new ExcelReaderImpl("/home/gnuni/Desktop/test.xlsx");
    List<User> users = excelReader.read(0, User.class);
    List<Book> books = excelReader.read(1, Book.class);

    ExcelWriter excelWriter = new ExcelWriterImpl("/home/gnuni/Desktop/books.xlsx");
    excelWriter.writeOne(books);

    excelWriter = new ExcelWriterImpl("/home/gnuni/Desktop/users.xlsx");
    excelWriter.writeOne(users);

    excelWriter = new ExcelWriterImpl("/home/gnuni/Desktop/users_books.xlsx");
    excelWriter.writeMultiple(Arrays.asList(users, books));

    CsvWriter csvWriter = new CsvWriterImpl("/home/gnuni/Desktop/users.csv");
    csvWriter.write(users);

    CsvReader csvReader = new CsvReaderImpl("/home/gnuni/Desktop/users.csv");
    users = csvReader.read(User.class);
    System.out.println(users);
  }
}
