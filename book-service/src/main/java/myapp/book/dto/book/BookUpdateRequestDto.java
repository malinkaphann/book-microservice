/**
 * This is the book update request dto.
 *
 * @author Phann Malinka
 */
package myapp.book.dto.book;

import javax.validation.constraints.Size;
import lombok.Data;
import myapp.book.entities.Book.STATUS;
import myapp.book.utils.CategoryEnum;
import myapp.book.utils.ValidationUtil;
import myapp.book.validators.ValueOfEnum;

@Data
public class BookUpdateRequestDto {

  @Size(min = ValidationUtil.MIN_LEN_BOOK_CODE, max = ValidationUtil.MAX_LEN_BOOK_CODE, 
    message = "the code = '${validatedValue}' must be between {min} and {max} characters")
  String code;

  @Size(min = ValidationUtil.MIN_LEN_BOOK_TITLE, max = ValidationUtil.MAX_LEN_BOOK_TITLE, 
    message = "the title = '${validatedValue}' must be between {min} and {max} characters")
  String title;

  @Size(min = ValidationUtil.MIN_LEN_BOOK_AUTHOR, max = ValidationUtil.MAX_LEN_BOOK_AUTHOR, 
    message = "the author = '${validatedValue}' must be between {min} and {max} characters")
  String author;

  @ValueOfEnum(enumClass = CategoryEnum.class, message = "category must be one of NOVEL, STUDY, COMICS")
  String category;

  @ValueOfEnum(enumClass = STATUS.class, message = "status must be one of GOOD, OLD, DELETED")
  String status;

  @Size(max = ValidationUtil.MAX_LEN_BOOK_DESCRIPTION, 
    message = "the description = '${validatedValue}' must be shorter than {max} characters")
  String description;
}
