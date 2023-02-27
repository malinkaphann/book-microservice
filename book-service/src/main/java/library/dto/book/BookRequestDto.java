/**
 * This is a request dto used to create a new book.
 * The validation is done right when this object is created.
 *
 * @author Phann Malinka
 */
package library.dto.book;

import java.util.UUID;
import library.dto.ApiRequestDto;
import library.dto.DataRequestDto;
import library.entities.Book;
import library.exceptions.ValidationException;
import library.utils.BookEnum;
import library.utils.CategoryEnum;
import library.utils.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class BookRequestDto
  extends ApiRequestDto
  implements DataRequestDto<Book> {

  protected String id;
  protected String code;
  protected String title;
  protected String author;
  protected String category;
  protected String status;
  protected String description;

  /**
   * This is when creating a new book.
   * @return Book a new book
   */
  @Override
  public Book build() {
    this.status =
      this.status == null || this.status.isEmpty()
        ? BookEnum.GOOD.name()
        : this.status;

    this.code =
      this.code == null || this.code.isEmpty()
        ? UUID.randomUUID().toString()
        : this.code;

    // make sure these value are not null and empty
    if (this.title == null || this.title.isEmpty()) {
      throw new ValidationException("title is required");
    }

    if (this.category == null || this.category.isEmpty()) {
      throw new ValidationException("category is required");
    }

    if (this.author == null || this.author.isEmpty()) {
      throw new ValidationException("author is required");
    }

    // validate the rest
    this.validate();

    return new Book(
      this.code,
      this.title,
      this.author,
      this.category,
      this.status,
      this.description
    );
  }

  /**
   * This is when updating a book.
   * @return Book an updated book
   */
  @Override
  public Book build(Book book) {
    this.validate();
    
    if (this.code != null) {
      book.setCode(this.code);
    }

    if (this.title != null) {
      book.setTitle(this.title);
    }
    if (this.author != null) {
      book.setAuthor(this.author);
    }
    if (this.category != null) {
      book.setCategory(this.category);
    }
    if (this.status != null) {
      book.setStatus(this.status);
    }
    if (this.description != null) {
      book.setDescription(this.description);
    }
    return book;
  }

  /**
   * This is to validate
   */
  @Override
  public void validate() {
    if (this.code != null) {
      ValidationUtil.validateSize(
        "code",
        this.code,
        ValidationUtil.MIN_LEN_BOOK_CODE,
        ValidationUtil.MAX_LEN_BOOK_CODE
      );
    }

    // validate title
    if (this.title != null) {
      ValidationUtil.validateSize(
        "title",
        this.title,
        ValidationUtil.MIN_LEN_BOOK_TITLE,
        ValidationUtil.MAX_LEN_BOOK_TITLE
      );
    }

    // validate author
    if (this.author != null) {
      ValidationUtil.validateSize(
        "author",
        this.author,
        ValidationUtil.MIN_LEN_BOOK_AUTHOR,
        ValidationUtil.MAX_LEN_BOOK_AUTHOR
      );
    }

    // validate category
    if (this.category != null) {
      if (!EnumUtils.isValidEnum(CategoryEnum.class, this.category)) {
        throw new ValidationException(
          String.format(
            "category = %s is invalid, the correct values = %s",
            this.category,
            EnumUtils.getEnumList(CategoryEnum.class).toString()
          )
        );
      }
    }

    // validate status
    if (this.status != null && !this.status.isEmpty()) {
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

  /**
   * This is to set the request id
   */
  @Override
  public void setRequestId(String requestId) {
    super.setRequestId(requestId);
  }

  /**
   * toString
   */
  public String toString() {
    return String.format(
      "BookRequestDto(request id = %s, code = %s, title = %s, " +
      "author = %s, category = %s, status = %s, description = %s)",
      this.getRequestId(),
      this.code,
      this.title,
      this.author,
      this.category,
      this.status,
      this.description
    );
  }

  /**
   * delete a book
   */
  @Override
  public Book setDeleted(Book book) {
    book.setStatus(BookEnum.DELETED.name());
    return book;
  }
}
