/**
 * This is the role service.
 * 
 * @author Phann Malinka
 */
package library.services;

import library.entities.*;
import library.repositories.RoleRepository;
import library.specifications.RoleSpecification;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends CrudService<Role> {

  public RoleService(
    RoleRepository roleRepo,
    RoleSpecification roleSpec
  ) {
    super(roleRepo, roleSpec);
  }
}
