/**
 * This is the role service.
 * 
 * @author Phann Malinka
 */
package library.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import library.entities.User;
import library.exceptions.DatabaseException;
import library.exceptions.ResourceNotFoundException;
import library.repositories.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepo;

  private final Logger logger = LoggerFactory.getLogger(UserService.class);

  /**
   * Fetch the user's detail
   *
   * @param id an id
   * @return User a user that is just fetched
   * @exception NullPointerException when the input request dto is null
   * @exception ResourceNotFoundException when the resource is found
   * @exception DatabaseException when error from database
   */
  public User detail(final int id) {
    if (id <= 0) {
      throw new IllegalArgumentException(
        "the input book id must not be negative"
      );
    }

    logger.info("want to fetch book id = {}", id);

    Optional<User> optionalOfUser = Optional.empty();

    // find
    try {
      optionalOfUser = userRepo.findById(id);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    // check
    if (optionalOfUser.isEmpty()) {
      throw new ResourceNotFoundException(
        String.format("user id = %d is not found", id)
      );
    }

    User foundUser = optionalOfUser.get();

    logger.info("found user = {}", foundUser);

    return foundUser;
  }


}
