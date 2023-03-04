/**
 * This is the book controller that handle the book module's endpoints.
 *
 * @author Phann Malinka
 */
package book.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import book.dto.ApiResponseDto;
import book.dto.DataApiResponseDto;
import book.dto.PaginationDto;
import book.dto.book.BookCreateRequestDto;
import book.dto.book.BookSearchDto;
import book.dto.book.BookUpdateRequestDto;
import book.entities.Book;
import book.entities.User;
import book.entities.Book.STATUS;
import book.exceptions.DatabaseException;
import book.exceptions.ValidationException;
import book.services.BookService;
import book.utils.AttributeUtil;
import book.utils.StatusEnum;

@RestController
@RequestMapping(value = "/api/v1/book")
public class BookController {

  private final Logger logger = LoggerFactory.getLogger(BookController.class);

  @Autowired
  private BookService bookService;


  /**
   * Search for books
   *
   * @param searchDto a search dto
   * @return DataApiResponseDto a list of books.
   */
  @GetMapping(value = "")
  public ResponseEntity<DataApiResponseDto<PaginationDto<Book>>> search(
    final BookSearchDto searchDto
  ) {

    logger.debug("search request dto = {}", searchDto);

    // validate
    searchDto.validate();

    // search
    PaginationDto<Book> data = bookService.search(searchDto);

    // build response
    DataApiResponseDto<PaginationDto<Book>> response = new DataApiResponseDto<>(
      StatusEnum.STATUS_SUCCESS.getValue(),
      "the search is done successfully",
      MDC.get(AttributeUtil.REQUEST_ID),
      data
    );

    logger.debug("search result dto = {}", response);

    return ResponseEntity.ok(response);
  }

  /**
   * Create a book
   *
   * @param requestDto a create book request dto
   * @return ApiResponseDto an api response dto
   */
  @PostMapping("")
  public ResponseEntity<ApiResponseDto> create(
      final @RequestBody BookCreateRequestDto requestDto) {

    logger.debug("book create request dto = {}", requestDto);

    requestDto.validate();

    // build object out of the request dto
    String status = requestDto.getStatus();
    Book book = new Book(
        requestDto.getCode(),
        requestDto.getTitle(),
        requestDto.getAuthor(),
        requestDto.getCategory(),
        // default = GOOD
        status == null || status.isEmpty() ? STATUS.GOOD : STATUS.valueOf(status),
        requestDto.getDescription());

    try {
      bookService.create(book);
    } catch (Exception e) {
      throw new DatabaseException(e.getMessage(), e);
    }

    // build the response
    ApiResponseDto response = new ApiResponseDto(
        StatusEnum.STATUS_SUCCESS.getValue(),
        "the new book was just created successfully",
        MDC.get(AttributeUtil.REQUEST_ID));

    logger.debug("book create response dto: {}", response);
    return ResponseEntity.ok(response);
  }

  /**
   * Update the resource
   *
   * @param id               an id of the resource,
   * @param updateRequestDto an update request dto.
   * @return ApiResponseDto an api response dto
   */
  @PutMapping("{id}")
  public ResponseEntity<ApiResponseDto> update(
      final @PathVariable String id,
      final @RequestBody BookUpdateRequestDto requestDto) {
    int intId;
    try {
      intId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new ValidationException(
          String.format("id = %s is not a number", id));
    }

    logger.debug("requested to update book id = {} with new values = {}",
        id, requestDto);

    Book updatedObject = new Book(
        requestDto.getCode(),
        requestDto.getTitle(),
        requestDto.getAuthor(),
        requestDto.getCategory(),
        STATUS.valueOf(requestDto.getStatus()),
        requestDto.getDescription());

    // update resource
    bookService.update(intId, updatedObject);

    // build the response
    ApiResponseDto response = new ApiResponseDto(
        StatusEnum.STATUS_SUCCESS.getValue(),
        String.format("the resource id = %s was just updated successfully", id),
        MDC.get(AttributeUtil.REQUEST_ID));

    logger.debug("update response dto: {}", response);

    return ResponseEntity.ok(response);
  }

  /**
   * Hold a book for a user.
   *
   * @param requestId
   * @param requestDto
   * @return
   */
  @PostMapping("/hold/book/{bookId}/user/{userId}")
  public ResponseEntity<ApiResponseDto> hold(
      final @PathVariable String bookId,
      final @PathVariable String userId) {

    int intBookId;
    try {
      intBookId = Integer.parseInt(bookId);
    } catch (NumberFormatException e) {
      throw new ValidationException(String.format(
          "bookId = %s is not a number"));
    }

    int intUserId;
    try {
      intUserId = Integer.parseInt(userId);
    } catch (NumberFormatException e) {
      throw new ValidationException(String.format(
          "userId = %s is not a number", userId));
    }

    logger.debug("requested to hold a book id = {} for user id = {}",
        bookId, userId);

    Book book = bookService.detail(intBookId);
    User user = bookService.hold(intBookId, intUserId);

    ApiResponseDto response = new ApiResponseDto(
        StatusEnum.STATUS_SUCCESS.getValue(),
        String.format(
            "user = %s successfully hold the book = %s",
            user,
            book),
        MDC.get(AttributeUtil.REQUEST_ID));

    logger.debug("hold response dto = {}", response);

    return ResponseEntity.ok(response);
  }

  /**
   * User hold a book.
   *
   * @param requestId
   * @param requestDto
   * @return
   */
  @DeleteMapping("/hold/book/{bookId}/user/{userId}")
  public ResponseEntity<ApiResponseDto> unhold(
      final @PathVariable String bookId,
      final @PathVariable String userId
  ) {
    
    int intBookId;
    try {
      intBookId = Integer.parseInt(bookId);
    } catch (NumberFormatException e) {
      throw new ValidationException(String.format(
          "bookId = %s is not a number"));
    }

    int intUserId;
    try {
      intUserId = Integer.parseInt(userId);
    } catch (NumberFormatException e) {
      throw new ValidationException(String.format(
          "userId = %s is not a number", userId));
    }

    logger.debug("requested to release a book id = {} from user id = {}",
        bookId, userId);

    Book book = bookService.detail(intBookId);
    User user = bookService.unhold(intBookId, intUserId);

    ApiResponseDto response = new ApiResponseDto(
        StatusEnum.STATUS_SUCCESS.getValue(),
        String.format(
            "user = %s successfully release the book = %s",
            user, book),
        MDC.get(AttributeUtil.REQUEST_ID));

    logger.debug("unhold response dto = {}", response);

    return ResponseEntity.ok(response);
  }

    /**
   * Fetch the resource's detail
   *
   * @param id an id of the resource
   * @return DataApiResponseDto
   */
  @GetMapping(value = "{id}")
  public ResponseEntity<DataApiResponseDto<Book>> detail(
    final @PathVariable String id
  ) {

    int intId;
    try {
      intId = Integer.parseInt(id);
    } catch(NumberFormatException e) {
      throw new ValidationException(String.format("id = %s not a number"));
    }

    logger.debug("request to fetch detail of book id = {}", id);

    // fetch the detail
    Book book = bookService.detail(intId);

    // build the response
    DataApiResponseDto<Book> response = new DataApiResponseDto<Book>(
      StatusEnum.STATUS_SUCCESS.getValue(),
      String.format("the resource id = %s is fetched successfully", id),
      MDC.get(AttributeUtil.REQUEST_ID),
      book
    );

    logger.debug("detail fetch response dto = {}", response);

    return ResponseEntity.ok(response);
  }

  /**
   * Delete the resource
   *
   * @param id id of entity that needs to be removed
   * @return ApiResponseDto an api response dto
   */
  @DeleteMapping("{id}")
  public ResponseEntity<ApiResponseDto> delete(
    final @PathVariable String id
  ) {

    int intId;
    try {
       intId = Integer.parseInt(id);
    } catch(NumberFormatException e) {
      throw new ValidationException(String.format(
        "book id = %s is not a number", id));
    }

    logger.debug("request to delete book id = {}", id);

    // soft delete the resource
    bookService.delete(intId);

    // build the response
    ApiResponseDto response = new ApiResponseDto(
      StatusEnum.STATUS_SUCCESS.getValue(),
      String.format("the resource id = %s was just deleted successfully", id),
      MDC.get(AttributeUtil.REQUEST_ID)
    );

    logger.debug("delete response dto = {}", response);

    return ResponseEntity.ok(response);
  }
}
