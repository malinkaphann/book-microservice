package library.utils;

public class PaginationUtil {

  public enum ORDER {
    ASC("asc"),
    DESC("desc");

    private String value;

    private ORDER(String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }
  }

  public static Integer DEFAULT_PAGE_NUMBER = 1;
  public static Integer DEFAULT_PAGE_SIZE = 10;
}
