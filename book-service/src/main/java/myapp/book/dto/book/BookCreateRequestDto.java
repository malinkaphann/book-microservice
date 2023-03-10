/**
 * This is a request dto used to create a new myapp.book.
 * The validation is done right when this object is created.
 *
 * @author Phann Malinka
 */
package myapp.book.dto.book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.EnumUtils;
import myapp.book.dto.Validatable;
import myapp.book.exceptions.ValidationException;
import myapp.book.utils.BookEnum;
import myapp.book.utils.CategoryEnum;
import myapp.book.utils.ValidationUtil;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BookCreateRequestDto implements Validatable {
  String code;
  String title;
  String author;
  String category;
  String status;
  String description;

  @Override
  public void validate() {
    // validate code
    if (this.code == null) {
      throw new ValidationException("code is required");
    }

    ValidationUtil.validateSize(
      "code",
      this.code,
      ValidationUtil.MIN_LEN_BOOK_CODE,
      ValidationUtil.MAX_LEN_BOOK_CODE
    );

    // validate title
    if (this.title == null) {
      throw new ValidationException("title is required");
    }

    ValidationUtil.validateSize(
      "title",
      this.title,
      ValidationUtil.MIN_LEN_BOOK_TITLE,
      ValidationUtil.MAX_LEN_BOOK_TITLE
    );

    // validate author
    if (this.author == null) {
      throw new ValidationException("author is required");
    }

    ValidationUtil.validateSize(
      "author",
      this.author,
      ValidationUtil.MIN_LEN_BOOK_AUTHOR,
      ValidationUtil.MAX_LEN_BOOK_AUTHOR
    );

    // validate category
    if (this.category == null || this.category.isEmpty()) {
      throw new ValidationException("category is required");
    }

    if (!EnumUtils.isValidEnum(CategoryEnum.class, this.category)) {
      throw new ValidationException(
        String.format(
          "category = %s is invalid, the correct values = %s",
          this.category,
          EnumUtils.getEnumList(CategoryEnum.class).toString()
        )
      );
    }

    // validate status
    // status is optional
    if (this.status != null) {
      if (!EnumUtils.isValidEnum(BookEnum.class, this.status)) {
        throw new ValidationException(
          String.format(
            "status = %s is invalid, the correct values = %s",
            this.status,
            EnumUtils.getEnumList(BookEnum.class)
          )
        );
      }
    }

    // validate description
    if (this.description != null) {
      ValidationUtil.validateSize(
        "description",
        this.description,
        ValidationUtil.MIN_LEN_BOOK_DESCRIPTION,
        ValidationUtil.MAX_LEN_BOOK_DESCRIPTION
      );
    }
  }
}
