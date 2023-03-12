/**
 * This is the role service.
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
import myapp.book.dto.PaginationDto;
import myapp.book.dto.SearchDto;
import myapp.book.entities.Role;
import myapp.book.exceptions.DatabaseException;
import myapp.book.exceptions.ResourceNotFoundException;
import myapp.book.repositories.RoleRepository;
import myapp.book.specifications.RoleSpecification;

public class RoleService {

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private RoleSpecification roleSpec;
    

    private final Logger logger = LoggerFactory.getLogger(RoleService.class);
    
    /**
     * Find a role by a given id.
     * 
     * @param id id of the role
     * @return Role a role
     * @throws NullPointerException when the input role id is null
     * @throws IllegalArgumentException when the given id is negative
     * @throws DatabaseException when there is an error coming from database
     */
    public Role findById(Integer id) {

        Objects.requireNonNull(id, "the input id must not be null");

        if (id <= 0) {
            throw new IllegalArgumentException("the input id must not be negative");
        }

        logger.debug("wanted to fetch the detail of role id = {}", id);

        Optional<Role> optionalOfRole;
        try {
            optionalOfRole = roleRepo.findById(id);
        } catch(Exception e) {
            throw new DatabaseException(e);
        }

        if (optionalOfRole.isEmpty()) {
            throw new ResourceNotFoundException(String.format(
                "role by id = %d is not found", id));
        }

        Role role = optionalOfRole.get();

        logger.debug("found role = {}", role);

        return role;
    }

    /**
     * Search for roles.
     * 
     * @param searchDto a search dto
     * @return PaginationDto a pagination dto
     * @throws NullPointerException when the input search dto is null
     * @throws DatabaseException when the error comes from database
     */
    public PaginationDto<Role> search(SearchDto searchDto) {

        Objects.requireNonNull(searchDto, "the input searchDto must not be null");

        PageRequest pageRequest = PageRequest.of(searchDto.getPage() - 1, searchDto.getSize());
        Page<Role> result = Page.empty();
    
        logger.debug("wanted to search for roles by processing the search dto = {}", 
            searchDto);

        try {
          Specification<Role> spec = roleSpec.search(searchDto);
          result = roleRepo.findAll(spec, pageRequest);
        } catch (Exception e) {
          throw new DatabaseException(e);
        }
    
        // build data
        PaginationDto<Role> data = new PaginationDto<>(
            result.getNumber() + 1,
            result.getContent().size(),
            result.getTotalPages(),
            result.getTotalElements(),
            result.getContent());
    
        logger.debug("search result = {}", data);

        return data;
    }
}
