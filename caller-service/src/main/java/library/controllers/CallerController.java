/**
 * This is the caller controller.
 * 
 * @author Phann Malinka
 */
package library.controllers;

import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import library.dto.BookDataApiResponseDto;
import library.services.BookService;

@RestController
@RequestMapping(value = "/api/v1/caller/book")
public class CallerController {

  private BookService bookService;

  public CallerController(BookService bookService) {
    this.bookService = bookService;
  }
  
  @GetMapping(value = "{id}")
  public ResponseEntity<?> fetchBookDetail(
    final @PathVariable String id
  ) throws RestClientException, URISyntaxException {
      ResponseEntity<BookDataApiResponseDto> response = bookService.detail(id);
      return response;
    }
}
