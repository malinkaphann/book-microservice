/**
 * This is the book controller that handle the book module's endpoints.
 *
 * @author Phann Malinka
 */
package library.controllers;

import java.util.Optional;
import library.dto.ApiResponseDto;
import library.dto.book.BookRequestDto;
import library.dto.book.BookSearchDto;
import library.dto.book.HoldBookRequestDto;
import library.entities.Book;
import library.entities.User;
import library.exceptions.DatabaseException;
import library.exceptions.ResourceDuplicatedException;
import library.exceptions.ValidationException;
import library.services.BookService;
import library.services.UserService;
import library.utils.AttributeUtil;
import library.utils.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/book")
public class BookController
  extends CrudController<Book, BookSearchDto, BookRequestDto> {

  private final Logger logger = LoggerFactory.getLogger(BookController.class);

  private UserService userService;
  private BookService bookService;

  public BookController(BookService service, UserService userService, BookRequestDto deleteRequestDto) {
    super(service, deleteRequestDto);
    this.userService = userService;
    this.bookService = service;
    
  }

  /**
   * Update the resource
   *
   * @param requestId a request id
   * @param createRequestDto a create request dto
   * @return ApiResponseDto an api response dto
   */
  @PostMapping("")
  public ResponseEntity<ApiResponseDto> create(
    final @RequestAttribute(AttributeUtil.REQUEST_ID) String requestId,
    final @RequestBody BookRequestDto bookCreateRequestDto
  ) {
    // put the request id in the request dto
    bookCreateRequestDto.setRequestId(requestId);

    logger.debug("book create request dto: {}", bookCreateRequestDto);

    bookCreateRequestDto.validate();

    if (this.bookService.existsBookByCode(bookCreateRequestDto.getCode())) {
      throw new ValidationException(
        String.format(
          "book code = %s is already taken",
          bookCreateRequestDto.getCode()
        )
      );
    }

    // build object out of the request dto
    Book book = bookCreateRequestDto.build();

    try {
      bookService.create(book);
    } catch (Exception e) {
      throw new DatabaseException(e.getMessage(), e);
    }

    // build the response
    ApiResponseDto response = new ApiResponseDto(
      StatusEnum.STATUS_SUCCESS.getValue(),
      "the new book was just created successfully",
      requestId
    );

    logger.debug("book create response dto: {}", response);
    return ResponseEntity.ok(response);
  }

  /**
   * Update the resource
   *
   * @param requestId a request id
   * @param id an id of the resource,
   * @param updateRequestDto an update request dto.
   * @return ApiResponseDto an api response dto
   */
  @PutMapping("{id}")
  public ResponseEntity<ApiResponseDto> update(
    final @RequestAttribute(AttributeUtil.REQUEST_ID) String requestId,
    final @PathVariable String id,
    final @RequestBody BookRequestDto updateRequestDto
  ) {
    int intId;
    try {
      intId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new ValidationException(
        String.format("id = %s is not a number", id)
      );
    }
    // put the request id in the request dto
    updateRequestDto.setRequestId(requestId);

    logger.debug("update request dto: {}", updateRequestDto);

    Book object = bookService.detail(intId);

    Optional<Book> optionalBook = bookService.findByCodeAndIdNot(
      updateRequestDto.getCode(),
      intId
    );

    if (optionalBook.isPresent()) {
      throw new ResourceDuplicatedException(
        String.format(
          "code = %s was already taken by book id = %d",
          optionalBook.get().getCode(),
          optionalBook.get().getId()
        )
      );
    }

    Book updatedObject = updateRequestDto.build(object);

    // update resource
    bookService.update(intId, updatedObject);

    // build the response
    ApiResponseDto response = new ApiResponseDto(
      StatusEnum.STATUS_SUCCESS.getValue(),
      String.format("the resource id = %s was just updated successfully", id),
      requestId
    );

    logger.debug("update response dto: {}", response);

    return ResponseEntity.ok(response);
  }

  /**
   * User hold a book.
   *
   * @param requestId
   * @param requestDto
   * @return
   */
  @PostMapping("hold")
  public ResponseEntity<ApiResponseDto> hold(
    final @RequestAttribute(AttributeUtil.REQUEST_ID) String requestId,
    final @RequestBody HoldBookRequestDto requestDto
  ) {
    requestDto.validate();

    int userId = Integer.parseInt(requestDto.getUserId());
    int bookId = Integer.parseInt(requestDto.getBookId());

    // put the request id in the request dto
    requestDto.setRequestId(requestId);

    logger.debug("hold request dto: {}", requestDto);

    User user = userService.detail(userId);
    Book book = bookService.detail(bookId);

    bookService.hold(user, book);

    ApiResponseDto response = new ApiResponseDto(
      StatusEnum.STATUS_SUCCESS.getValue(),
      String.format(
        "user[id = %d, username = %s] hold book [id = %d, title = %s]",
        user.getId(),
        user.getUsername(),
        book.getId(),
        book.getTitle()
      ),
      requestId
    );

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
  @DeleteMapping("hold")
  public ResponseEntity<ApiResponseDto> unhold(
    final @RequestAttribute(AttributeUtil.REQUEST_ID) String requestId,
    final @RequestBody HoldBookRequestDto requestDto
  ) {
    requestDto.validate();

    int userId = Integer.parseInt(requestDto.getUserId());
    int bookId = Integer.parseInt(requestDto.getBookId());

    // put the request id in the request dto
    requestDto.setRequestId(requestId);

    logger.debug("unhold request dto: {}", requestDto);

    User user = userService.detail(userId);
    Book book = bookService.detail(bookId);

    bookService.unhold(user, book);

    ApiResponseDto response = new ApiResponseDto(
      StatusEnum.STATUS_SUCCESS.getValue(),
      String.format(
        "user[id = %d, username = %s] unhold the book [id = %d, title = %s]",
        user.getId(),
        user.getUsername(),
        book.getId(),
        book.getTitle()
      ),
      requestId
    );

    logger.debug("unhold response dto = {}", response);

    return ResponseEntity.ok(response);
  }

}
