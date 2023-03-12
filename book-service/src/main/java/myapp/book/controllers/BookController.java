/**
 * This is the book controller.
 *
 * @author Phann Malinka
 */
package myapp.book.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import myapp.book.dto.ApiResponseDto;
import myapp.book.dto.DataApiResponseDto;
import myapp.book.dto.PaginationDto;
import myapp.book.dto.SearchDto;
import myapp.book.dto.book.BookCreateRequestDto;
import myapp.book.dto.book.BookUpdateRequestDto;
import myapp.book.entities.Book;
import myapp.book.services.BookService;
import myapp.book.utils.AttributeUtil;
import myapp.book.utils.StatusEnum;

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
    final SearchDto searchDto
  ) {

    logger.debug("search request dto = {}", searchDto);

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
      final @Validated @RequestBody BookCreateRequestDto requestDto) {

    logger.debug("book create request dto = {}", requestDto);

    // build object out of the request dto
    //String status = requestDto.getStatus();
    Book book = new Book(
        requestDto.getCode(),
        requestDto.getTitle(),
        requestDto.getAuthor(),
        requestDto.getCategory(),
        requestDto.getStatus(),
        requestDto.getDescription());

    bookService.create(book);
    
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
      final @PathVariable int id,
      final @Validated @RequestBody BookUpdateRequestDto requestDto) {
  
    logger.debug("requested to update book id = {} with new values = {}",
        id, requestDto);

    Book updatedObject = new Book(
        requestDto.getCode(),
        requestDto.getTitle(),
        requestDto.getAuthor(),
        requestDto.getCategory(),
        requestDto.getStatus(),
        requestDto.getDescription());

    // update resource
    bookService.update(id, updatedObject);

    // build the response
    ApiResponseDto response = new ApiResponseDto(
        StatusEnum.STATUS_SUCCESS.getValue(),
        String.format("the resource id = %s was just updated successfully", id),
        MDC.get(AttributeUtil.REQUEST_ID));

    logger.debug("update response dto: {}", response);

    return ResponseEntity.ok(response);
  }

  /**
   * Fetch the book detail
   *
   * @param id an id of the book
   * @return DataApiResponseDto
   */
  @GetMapping(value = "{id}")
  public ResponseEntity<DataApiResponseDto<Book>> detail(
    final @PathVariable int id
  ) {

    logger.debug("request to fetch detail of book id = {}", id);

    // fetch the detail
    Book book = bookService.detail(id);

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
   * Delete the book
   *
   * @param id id of the book
   * @return ApiResponseDto an api response dto
   */
  @DeleteMapping("{id}")
  public ResponseEntity<ApiResponseDto> delete(
    final @PathVariable int id
  ) {

    logger.debug("request to delete book id = {}", id);

    // soft delete the resource
    bookService.delete(id);

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
