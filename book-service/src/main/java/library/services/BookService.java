package library.services;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import library.entities.*;
import library.exceptions.DatabaseException;
import library.exceptions.ValidationException;
import library.repositories.BookRepository;
import library.repositories.UserRepository;
import library.specifications.BookSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService extends CrudService<Book> {

  @Autowired
  private UserRepository userRepo;

  private BookRepository bookRepo;

  public BookService(BookRepository bookRepo, BookSpecification bookSpec) {
    super(bookRepo, bookSpec);
    this.bookRepo = bookRepo;
  }

  /**
   * Exists by code
   *
   * @param id an id
   */
  public Boolean existsBookByCode(final String code) {
    return this.bookRepo.existsByCode(code);
  }

  /**
   * Exists by code and id not
   *
   * @param code a code
   * @param id an id
   */
  public Optional<Book> findByCodeAndIdNot(
    final String code,
    final Integer id
  ) {
    return this.bookRepo.findByCodeAndIdNot(code, id);
  }

  /**
   * A user hold a book.
   */
  public void hold(final User user, final Book book) {
    Objects.requireNonNull(user, "the input user must not be null");
    Objects.requireNonNull(user, "the input book must not be null");

    // check is that user already interact with that book with the same status
    if (user.getBooks().contains(book)) {
      throw new ValidationException(
        String.format(
          "user[id = %d, username = %s] already hold book[id = %d, title = %s]",
          user.getId(),
          user.getUsername(),
          book.getId(),
          book.getTitle()
        )
      );
    }

    Set<Book> books = user.getBooks();

    // one user can hold maximum of 3 books
    int count = books.size();
    if (count >= 3) {
      throw new ValidationException(
        String.format(
          "user[id = %d, username = %s] already hold 3 books",
          user.getId(),
          user.getUsername()
        )
      );
    }

    try {
      books.add(book);
      userRepo.save(user);

    } catch (Exception e) {
      throw new DatabaseException(e.getMessage(), e);
    }
  }

    /**
   * A user unhold a book.
   */
  public void unhold(final User user, final Book book) {
    Objects.requireNonNull(user, "the input user must not be null");
    Objects.requireNonNull(user, "the input book must not be null");

    // check is that user already interact with that book with the same status
    if (!user.getBooks().contains(book)) {
      throw new ValidationException(
        String.format(
          "user[id = %d, username = %s] didn't held the book[id = %d, title = %s]",
          user.getId(),
          user.getUsername(),
          book.getId(),
          book.getTitle()
        )
      );
    }

    try {
  
      Set<Book> books = user.getBooks();
      books.remove(book);
      userRepo.save(user);

    } catch (Exception e) {
      throw new DatabaseException(e.getMessage(), e);
    }
  }
}
