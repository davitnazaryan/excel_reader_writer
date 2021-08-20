package model;

public class Book {

  private String title;
  private String author;
  private int pageCount;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public int getPageCount() {
    return pageCount;
  }

  public void setPageCount(int pageCount) {
    this.pageCount = pageCount;
  }

  @Override
  public String toString() {
    return "\nBook{" +
        "title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", pageCount=" + pageCount +
        "}";
  }
}
