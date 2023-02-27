/**
 * This is the role service.
 * 
 * @author Phann Malinka
 */
package library.services;

import library.entities.*;
import library.repositories.UserRepository;
import library.specifications.UserSpecification;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CrudService<User> {

  public UserService(
    UserRepository userRepo,
    UserSpecification userSpec
  ) {
    super(userRepo, userSpec);
  }
}
