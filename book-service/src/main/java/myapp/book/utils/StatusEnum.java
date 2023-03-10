package myapp.book.utils;

public enum StatusEnum {
    STATUS_SUCCESS(0),
    ERROR_UNEXPECTED(101),
    ERROR_DATABASE(102),
    ERROR_RESOURCE_NOT_FOUND(103),
    ERROR_RESOURCE_DUPLICATED(104),
    ERROR_VALIDATION(105),
    ERROR_UNSUPPORTED(106);

    private int status;
    private StatusEnum(int status) {
      this.status = status;
    }
    public int getValue() {
      return status;
    }
}