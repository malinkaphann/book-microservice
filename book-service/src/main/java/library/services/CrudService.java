/**
 * This is the parent class of all service classes.
 * It is abstract because
 * 1- all the common functions are moved here
 * 2- it is not supposed to be instantiated
 *
 * @author Phann Malinka
 */
package library.services;

import java.util.Objects;
import library.dto.PaginationDto;
import library.dto.SearchDto;
import library.entities.BaseEntity;
import library.exceptions.DatabaseException;
import library.exceptions.ResourceNotFoundException;
import library.repositories.CrudRepository;
import library.specifications.CrudSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * @param <E> entity class
 */
@Component
public abstract class CrudService<E extends BaseEntity<Integer>> {

  private final CrudRepository<E> repository;
  private final CrudSpecification<E> specification;

  private final Logger logger = LoggerFactory.getLogger(CrudService.class);

  public CrudService(CrudRepository<E> repository, CrudSpecification<E> specification) {
    this.repository = repository;
    this.specification = specification;
  }

  /**
   * Search for resources
   *
   * @param searchDto the search request dto
   * @return PaginationDto a pagination data
   * @exception NullPointerException when the input search dto is null
   * @exception DatabaseException when error comes from running database query
   */
  public PaginationDto<E> search(final SearchDto searchDto) {
    
    // make sure the given search dto is ok
    Objects.requireNonNull(searchDto, "the input search dto must not be null");

    logger.debug("search dto = {}", searchDto);

    Specification<E> spec = this.specification.search(searchDto);

    int page = Integer.parseInt(searchDto.getPage()) - 1;
    int size = Integer.parseInt(searchDto.getSize());

    PageRequest pageRequest = PageRequest.of(
      page,
      size
    );

    Page<E> result = Page.empty();

    try {
      // search
      result =
        repository.findAll(spec, pageRequest);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }

    // build data
    PaginationDto<E> data = new PaginationDto<E>(
      result.getNumber() + 1,
      result.getContent().size(),
      result.getTotalPages(),
      result.getTotalElements(),
      result.getContent()
    );

    return data;
  }

  /**
   * Fetch the resource's detail
   *
   * @param id an id
   * @return E the resource to fetch
   * @exception NullPointerException when the input request dto is null
   * @exception ResourceNotFoundException when the resource is found
   * @exception DatabaseException when error comes from running database query
   */
  public E detail(final Integer id) {

    Objects.requireNonNull(
      id,
      "the input id must not be null"
    );

    logger.debug("fetch resource id  = {}", id);

    // find its detail by id
    E object = repository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException(
          String.format("resource id = %d is not found", id)
        )
      );

    return object;
  }

  /**
   * Create the resource
   *
   * @param requestDto a create request dto
   * @exception NullPointerException when the input request dto is null
   * @exception DatabaseException when error comes from running database query
   */
  public void create(final E object) {

    Objects.requireNonNull(
      object,
      "the input object must not be null"
    );

    logger.debug("object to create = {}", object);

    // save new object
    try {
      this.repository.save(object);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }
  }

  /**
   * Update the resource
   *
   * @param id an id of the object
   * @param object an object
   * @exception NullPointerException when the input request dto is null
   * @exception DatabaseException when error comes from running database query
   */
  public void update(final Integer id, final E object) {
    
    Objects.requireNonNull(
      id,
      "the input id must not be null"
    );

    Objects.requireNonNull(
      object,
      "the input object must not be null"
    );

    logger.debug("update object id = {} with value = {}", 
      id, object);

    // save new object
    try {
      this.repository.save(object);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }
  }

  /**
   * Delete the resource
   * This is a hard delete
   *
   * @param id an id
   * @exception NullPointerException      when the input request dto is null
   * @exception DatabaseException when error comes from running a database query
   */
  public void delete(final Integer id) {
    
    Objects.requireNonNull(
      id,
      "the id must not be null"
    );

    E object = this.detail(id);

    try {
      repository.delete(object);
    } catch (Exception e) {
      throw new DatabaseException(e);
    }
  }

}
