package myapp.book.services;

import java.util.List;
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
import myapp.book.dto.book.BookSearchDto;
import myapp.book.entities.*;
import myapp.book.entities.Book.STATUS;
import myapp.book.exceptions.DatabaseException;
import myapp.book.exceptions.ResourceDuplicatedException;
import myapp.book.exceptions.ResourceNotFoundException;
import myapp.book.exceptions.ValidationException;
import myapp.book.repositories.BookRepository;
import myapp.book.repositories.UserRepository;
import myapp.book.specifications.BookSpecification;

@Service
public class BookService {

  @Autowired
  private UserRepository userRepo;

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
  public PaginationDto<Book> search(final BookSearchDto searchDto) {
    Objects.requireNonNull(searchDto, "the input search dto must not be null");

    logger.debug("search dto = {}", searchDto);

    int page = Integer.parseInt(searchDto.getPage()) - 1;
    int size = Integer.parseInt(searchDto.getSize());

    PageRequest pageRequest = PageRequest.of(page, size);
    Page<Book> result = Page.empty();

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

    return data;
  }

  /**
   * Create a book
   *
   * @throws NullPointerException when the input request dto is null
   * @throws ValidationException  when the code is already taken
   * @throws DatabaseException    when error from database
   */
  public void create(final Book book) {
    Objects.requireNonNull(book, "the input object must not be null");

    logger.debug("want to create a new book = {}", book);

    if (bookRepo.existsByCode(book.getCode())) {
      throw new ValidationException(
          String.format("book code = %s is already taken", book.getCode()));
    }
    Book newBook;

    // save new object
    try {
      newBook = bookRepo.save(book);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    logger.debug("book just created = {}", newBook);
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
          "the input book id must not be negative");
    }

    logger.debug("want to fetch book id = {}", id);

    Optional<Book> optionalOfBook = Optional.empty();

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
   * @throws NullPointerException      when the given newBook is null
   * @throws IllegalArgumentException  when the given book id is negative
   * @throws DatabaseException         when error from database
   * @throws ResourceNotFoundException when there is no book by the given id
   */
  public void update(final int id, final Book newBook) {
    Objects.requireNonNull(newBook, "the input book must not be null");

    if (id <= 0) {
      throw new IllegalArgumentException(
          "the input book id must not be negative");
    }

    logger.debug("wanted to update book id = {} with values = {}", id, newBook);

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
    String newCode = book.getCode();
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

    // update title if given
    String newTitle = book.getTitle();
    if (newTitle != null && !newTitle.isEmpty()) {
      book.setTitle(newTitle);
    }

    // update author if given
    String newAuthor = book.getAuthor();
    if (newAuthor != null && !newAuthor.isEmpty()) {
      book.setAuthor(newAuthor);
    }

    // update category if given
    String newCategory = book.getCategory();
    if (newCategory != null && !newCategory.isEmpty()) {
      book.setCategory(newCategory);
    }

    // update description if given
    String newDesc = book.getDescription();
    if (newDesc != null && !newDesc.isEmpty()) {
      book.setDescription(newDesc);
    }

    Book updatedBook;

    // save
    try {
      updatedBook = bookRepo.save(book);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    logger.debug("updated book = {}", updatedBook);
  }

  /**
   * Soft delete the book
   *
   * @param id an id
   * @throws IllegalArgumentException when the given book id is negative
   * @throws DatabaseException        when error from database
   */
  public void delete(final int id) {
    if (id <= 0) {
      throw new IllegalArgumentException(
          "the input book id must not be negative");
    }

    logger.debug("wanted to delete a book id = {}", id);

    Book book = this.detail(id);
    book.setStatus(STATUS.DELETED);

    Book updatedBook;
    try {
      updatedBook = bookRepo.save(book);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    logger.debug("deleted book = {}", updatedBook);
  }

  /**
   * Hold a book for a user
   *
   * @throws IllegalArgumentException when the given bookId or userId is negative
   * @throws ResourceNotFoundException when there is no such userId or bookId
   * @throws ValidationException  when the validation has error
   * @throws DatabaseException    when error from database
   */
  public User hold(final int bookId, final int userId) {
    if (bookId <= 0) {
      throw new IllegalArgumentException(
          "the input bookId must not be negative");
    }

    if (userId <= 0) {
      throw new IllegalArgumentException(
          "the input userId must not be negative");
    }

    logger.debug("wanted to hold book id = {} for user id = {}", bookId, userId);

    Book book = detail(bookId);

    User user = userRepo
        .findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("user by id = %d is not found", userId)));

    List<Book> books = user.getBooks();

    // check is that user already hold that book
    if (books.contains(book)) {
      throw new ValidationException(
          String.format("user = %s already hold book = %s", user, book));
    }

    // one user can hold maximum of 3 books
    int count = books.size();
    if (count >= 3) {
      throw new ValidationException(
          String.format("user = %s already hold 3 books", user));
    }

    User updatedUser;

    try {
      books.add(book);
      updatedUser = userRepo.save(user);
      books = updatedUser.getBooks();
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    logger.debug("user = %s is now holding books = %s", books);

    return updatedUser;
  }

  /**
   * Release a book from a user
   *
   * @throws IllegalArgumentException when the given bookId or userId is negative
   * @throws ResourceNotFoundException when the userId is not found
   * @throws ValidationException  when the validation has error
   * @throws DatabaseException    when error from database
   */
  public User unhold(final int bookId, final int userId) {
    if (bookId <= 0) {
      throw new IllegalArgumentException(
          "the input bookId must not be negative");
    }

    if (userId <= 0) {
      throw new IllegalArgumentException(
          "the input userId must not be negative");
    }

    logger.debug(
        "want to release book id = {} from user id = {}",
        bookId,
        userId);

    User user = userRepo
        .findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("user id = %d is not found", userId)));

    Book book = detail(bookId);

    List<Book> books = user.getBooks();

    // check is that user hold that book
    if (!books.contains(book)) {
      throw new ValidationException(
          String.format("user = %s didn't held the book = %s", user, book));
    }

    // now it is ok to unhold
    User updatedUser;
    try {
      books.remove(book);
      updatedUser = userRepo.save(user);
      books = updatedUser.getBooks();
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    logger.debug("user = {} is holding books = {}", updatedUser, books);

    return updatedUser;
  }
}
