package book.utils;

import book.exceptions.ValidationException;

public class ValidationUtil {

    public static final int MAX_LEN_SEARCH = 8;
    public static final int MIN_LEN_USERNAME = 3;
    public static final int MAX_LEN_USERNAME = 16;

    public static final int MIN_LEN_PASSWORD = 3;
    public static final int MAX_LEN_PASSWORD = 10;

    public static final int MIN_LEN_STUDENT_ID = 3;
    public static final int MAX_LEN_STUDENT_ID = 10;

    public static final int MIN_LEN_ROLENAME = 3;
    public static final int MAX_LEN_ROLENAME = 10;

    public static final int MIN_LEN_NAME = 3;
    public static final int MAX_LEN_NAME = 10;

    public static final int MIN_LEN_PHONE = 5;
    public static final int MAX_LEN_PHONE = 10;

    // book
    public static final int MIN_LEN_BOOK_CODE = 9;
    public static final int MAX_LEN_BOOK_CODE = 32;

    public static final int MIN_LEN_BOOK_TITLE = 3;
    public static final int MAX_LEN_BOOK_TITLE = 64;

    public static final int MIN_LEN_BOOK_AUTHOR = 3;
    public static final int MAX_LEN_BOOK_AUTHOR = 64;

    public static final int MIN_LEN_BOOK_CATEGORY = 3;
    public static final int MAX_LEN_BOOK_CATEGORY = 32;

    public static final int MIN_LEN_BOOK_DESCRIPTION = 5;
    public static final int MAX_LEN_BOOK_DESCRIPTION = 1024;

    /**
     * validate the size of string data.
     * 
     * @param name
     * @param value
     * @param minLen
     * @param maxLen
     */
    public static void validateSize(String name, String value, int minLen, int maxLen) {
        if ((value.length() < minLen) ||
                (value.length() > maxLen)) {
            throw new ValidationException(
                    String.format("length of %s is not in range [%d, %d]",
                            name, minLen, maxLen));
        }
    }
}
