/**
 * This is the book service.
 * 
 * @author Phann Malinka
 */
package myapp.book.services;

import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import myapp.book.dto.PaginationDto;
import myapp.book.dto.SearchDto;
import myapp.book.entities.*;
import myapp.book.entities.Book.STATUS;
import myapp.book.exceptions.DatabaseException;
import myapp.book.exceptions.ResourceDuplicatedException;
import myapp.book.exceptions.ResourceNotFoundException;
import myapp.book.exceptions.ValidationException;
import myapp.book.repositories.BookRepository;
import myapp.book.specifications.BookSpecification;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepo;

  @Autowired
  private BookSpecification bookSpec;

  private final Logger logger = LoggerFactory.getLogger(BookService.class);

  /**
   * Search for books
   *
   * @param searchDto the search request dto
   * @return PaginationDto a pagination data
   * @throws NullPointerException when the input search dto is null
   * @throws DatabaseException    when error from database
   */
  public PaginationDto<Book> search(final SearchDto searchDto) {

    Objects.requireNonNull(searchDto, "the input search dto must not be null");

    logger.debug("search dto = {}", searchDto);

    PageRequest pageRequest = PageRequest.of(searchDto.getPage() - 1, 
      searchDto.getSize());
    Page<Book> result = Page.empty();

    // search
    try {
      Specification<Book> spec = bookSpec.search(searchDto);
      result = bookRepo.findAll(spec, pageRequest);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    // build data
    PaginationDto<Book> data = new PaginationDto<>(
        result.getNumber() + 1,
        result.getContent().size(),
        result.getTotalPages(),
        result.getTotalElements(),
        result.getContent());

    logger.debug("search result dto = {}", data);

    return data;
  }

  /**
   * Create a book
   * 
   * @param book a book to create
   * @return Book a book just created
   * @throws NullPointerException when the input request dto is null
   * @throws ValidationException  when the code is already taken
   * @throws DatabaseException    when error from database
   */
  public Book create(final Book book) {

    Objects.requireNonNull(book, "the input object must not be null");

    logger.debug("want to create a new book = {}", book);

    if (bookRepo.existsByCode(book.getCode())) {
      throw new ResourceDuplicatedException(
          String.format("book code = %s is already taken", book.getCode()));
    }

    // make sure that the status is getting the default value
    if (book.getStatus() == null) {
      book.setStatus(STATUS.GOOD.name());
    }

    Book newBook;

    // save new object
    try {
      newBook = bookRepo.save(book);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    logger.debug("book just created = {}", newBook);

    return newBook;
  }

  /**
   * Fetch the book's detail
   *
   * @param id an id
   * @return Book a book that is just fetched
   * @throws NullPointerException      when the input request dto is null
   * @throws ResourceNotFoundException when the resource is found
   * @throws DatabaseException when error from database
   */
  public Book detail(final int id) {

    if (id <= 0) {
      throw new IllegalArgumentException(
          "the input book id must not be zero or negative");
    }

    logger.debug("want to fetch book id = {}", id);

    Optional<Book> optionalOfBook;

    // find
    try {
      optionalOfBook = bookRepo.findById(id);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    // check
    if (optionalOfBook.isEmpty()) {
      throw new ResourceNotFoundException(
          String.format("book id = %d is not found", id));
    }

    Book foundBook = optionalOfBook.get();

    logger.debug("found book = {}", foundBook);

    return foundBook;
  }

  /**
   * Update the book
   *
   * @param id   an id of a book
   * @param book a new values
   * @return Book an updated book
   * @throws NullPointerException      when the given newBook is null
   * @throws IllegalArgumentException  when the given book id is negative
   * @throws DatabaseException         when error from database
   * @throws ResourceNotFoundException when there is no book by the given id
   */
  public Book update(final int id, final Book newBook) {

    Objects.requireNonNull(newBook, "the input book must not be null");

    if (id <= 0) {
      throw new IllegalArgumentException(
          "the input book id must not be zero or negative");
    }

    logger.debug("wanted to update book id = {} with values = {}", 
      id, newBook);

    // find it
    Optional<Book> optionalOfBook = Optional.empty();
    try {
      optionalOfBook = bookRepo.findById(id);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    if (optionalOfBook.isEmpty()) {
      throw new ResourceNotFoundException(
          String.format("book id = %d is not found", id));
    }

    Book book = optionalOfBook.get();

    // update code if given
    String newCode = newBook.getCode();
    if (newCode != null && !newCode.isEmpty()) {

      // new code must not be the same as the old code used by other books
      Optional<Book> optionalOfAnotherBook = bookRepo.findByCodeAndIdNot(
          book.getCode(), id);

      if (optionalOfAnotherBook.isPresent()) {
        Book foundBook = optionalOfBook.get();
        throw new ResourceDuplicatedException(
            String.format(
                "code = %s was already taken by book id = %d",
                foundBook.getCode(),
                foundBook.getId()));
      }
      
      // now it is ok to set a new code
      book.setCode(newCode);
    }

    // update title if it is given and it is new value.
    String newTitle = newBook.getTitle();
    if ((newTitle != null) && 
      (!newTitle.isEmpty()) &&
      (Objects.equals(newTitle, book.getTitle()))) {
      book.setTitle(newTitle);
    }

    // update author if it is given and it is new value.
    String newAuthor = newBook.getAuthor();
    if ((newAuthor != null) && 
        (!newAuthor.isEmpty()) &&
        (Objects.equals(newAuthor, book.getAuthor()))) {
      book.setAuthor(newAuthor);
    }

    // update category if it is given and it is new value
    String newCategory = newBook.getCategory();
    if ((newCategory != null) &&
        (!newCategory.isEmpty()) &&
        (Objects.equals(newCategory, book.getCategory()))) {
      book.setCategory(newCategory);
    }

    // update description if given
    String newDesc = newBook.getDescription();
    if (newDesc != null && !newDesc.isEmpty()) {
      book.setDescription(newDesc);
    }

    // update status
    String status = newBook.getStatus();
    if (status != null && !status.isEmpty()) {
      book.setStatus(status);
    }

    Book updatedBook;

    // save
    try {
      updatedBook = bookRepo.save(book);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    logger.debug("updated book = {}", updatedBook);

    return updatedBook;
  }

  /**
   * Soft delete the book
   *
   * @param id an id
   * @return Book a deleted book
   * @throws IllegalArgumentException when the given book id is negative
   * @throws DatabaseException        when error from database
   */
  public Book delete(final int id) {

    if (id <= 0) {
      throw new IllegalArgumentException(
          "the input book id must not be zero or negative");
    }

    logger.debug("wanted to delete a book id = {}", id);

    Book book = this.detail(id);
    book.setStatus(STATUS.DELETED.name());

    Book updatedBook;
    try {
      updatedBook = bookRepo.save(book);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    logger.debug("deleted book = {}", updatedBook);

    return updatedBook;
  }

}
