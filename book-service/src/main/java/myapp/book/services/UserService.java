/**
 * This is the role service.
 * 
 * @author Phann Malinka
 */
package myapp.book.services;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import myapp.book.entities.Book;
import myapp.book.entities.User;
import myapp.book.exceptions.DatabaseException;
import myapp.book.exceptions.ResourceNotFoundException;
import myapp.book.exceptions.ValidationException;
import myapp.book.repositories.UserRepository;

@Service
public class UserService {

  /**
   * This is the return type of hold and unhold.
   */
  @Data
  @AllArgsConstructor
  public class UserHoldBook {
    User user;
    Book book;
  }

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private BookService bookService;

  private final Logger logger = LoggerFactory.getLogger(UserService.class);

  /**
   * Fetch the user's detail
   *
   * @param id an id
   * @return User a user that is just fetched
   * @throws IllegalArgumentException  when the input id is negative
   * @throws ResourceNotFoundException when that user id does not exist
   * @throws DatabaseException         when error from database
   */
  public User detail(final int id) {
    if (id <= 0) {
      throw new IllegalArgumentException(
          "the input book id must not be negative");
    }

    logger.debug("want to fetch book id = {}", id);

    Optional<User> optionalOfUser;

    // find
    try {
      optionalOfUser = userRepo.findById(id);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    // check
    if (optionalOfUser.isEmpty()) {
      throw new ResourceNotFoundException(
          String.format("user id = %d is not found", id));
    }

    User foundUser = optionalOfUser.get();

    logger.debug("found user = {}", foundUser);

    return foundUser;
  }

  /**
   * Hold a book for a user
   * 
   * @param userId a user to hold that book
   * @param bookId a book to be hold
   * @throws IllegalArgumentException  when the given bookId or userId is negative
   * @throws ResourceNotFoundException when there is no such userId or bookId
   * @throws ValidationException       when the validation has error
   * @throws DatabaseException         when error from database
   */
  public UserHoldBook hold(final int userId, final int bookId) {

    if (userId <= 0) {
      throw new IllegalArgumentException(
          "the input userId must not be zero or negative");
    }

    if (bookId <= 0) {
      throw new IllegalArgumentException(
          "the input bookId must not be zero or negative");
    }

    logger.debug("wanted to hold book id = {} for user id = {}",
        bookId, userId);

    Book book = bookService.detail(bookId);
    User user = detail(userId);

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

    UserHoldBook uhb = new UserHoldBook(updatedUser, book);

    logger.debug("user = {} just hold a new book = {}",
        uhb.getUser(), uhb.getBook());

    return uhb;
  }

  /**
   * Release a book from a user
   *
   * @param userId a user who releases the book
   * @param bookId a book that is going to be released from the user
   * @throws IllegalArgumentException  when the given bookId or userId is negative
   * @throws ResourceNotFoundException when the userId is not found
   * @throws ValidationException       when the validation has error
   * @throws DatabaseException         when error from database
   */
  public UserHoldBook unhold(final int userId, final int bookId) {

    if (userId <= 0) {
      throw new IllegalArgumentException(
          "the input userId must not be zero or negative");
    }

    if (bookId <= 0) {
      throw new IllegalArgumentException(
          "the input bookId must not be zero or negative");
    }

    logger.debug(
        "want to release book id = {} from user id = {}",
        bookId,
        userId);

    User user = userRepo
        .findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException(
            String.format("user id = %d is not found", userId)));

    Book book = bookService.detail(bookId);

    List<Book> books = user.getBooks();

    // check is that user never hold that book
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

    UserHoldBook uhb = new UserHoldBook(updatedUser, book);

    logger.debug("user = {} just released the book = {}",
        uhb.getUser(), uhb.getBook());

    return uhb;
  }

}
