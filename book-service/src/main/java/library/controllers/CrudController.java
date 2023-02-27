/**
 * This file is for the crud controller class.
 *
 * @author Phann Malinka
 */
package library.controllers;

import library.dto.ApiResponseDto;
import library.dto.DataApiResponseDto;
import library.dto.DataRequestDto;
import library.dto.IdRequestDto;
import library.dto.PaginationDto;
import library.dto.SearchDto;
import library.entities.BaseEntity;
import library.services.CrudService;
import library.utils.AttributeUtil;
import library.utils.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is the parent class of all controllers.
 * It is abstract because
 * 1- all the common functions are implemented here.
 * 2- it is not supposed to be instantiated.
 *
 * @param <E> entity
 * @param <DRD> a child of data request dto
 */
public abstract class CrudController<
  E extends BaseEntity<Integer>,
  SDT extends SearchDto,
  DRD extends DataRequestDto<E>> {

  private final Logger logger = LoggerFactory.getLogger(BookController.class);

  /**
   * This crud service will be injected from the children class.
   * It is immutable, hence it is final.
   */
  private final CrudService<E> service;

  protected final DataRequestDto<E> deleteRequestDto;

  public CrudController(CrudService<E> service, DataRequestDto<E> deleteRequestDto) {
    this.service = service;
    this.deleteRequestDto = deleteRequestDto;
  }

  /**
   * Search for resources
   *
   * @param requestId a request id
   * @param searchDto a search dto
   * @return DataApiResponseDto a list of books.
   */
  @GetMapping(value = "")
  public ResponseEntity<DataApiResponseDto<PaginationDto<E>>> search(
    final @RequestAttribute(AttributeUtil.REQUEST_ID) String requestId,
    final SDT searchDto
  ) {

    // put the request id into the request dto
    searchDto.setRequestId(requestId);

    logger.debug("search request dto : {}", searchDto);

    // validate
    searchDto.validate();

    // search
    PaginationDto<E> data = service.search(searchDto);

    // build response
    DataApiResponseDto<PaginationDto<E>> response = new DataApiResponseDto<PaginationDto<E>>(
      StatusEnum.STATUS_SUCCESS.getValue(),
      "the search is done successfully",
      searchDto.getRequestId(),
      data
    );

    logger.debug("search result dto: {}", response);

    return ResponseEntity.ok(response);
  }

  /**
   * Fetch the resource's detail
   *
   * @param requestId a request id
   * @param id an id of the resource
   * @return DataApiResponseDto
   */
  @GetMapping(value = "{id}")
  public ResponseEntity<DataApiResponseDto<E>> detail(
    final @RequestAttribute(AttributeUtil.REQUEST_ID) String requestId,
    final IdRequestDto requestDto
  ) {

    // put the request id into the request dto
    requestDto.setRequestId(requestId);

    logger.debug("fetch detail request dto : {}", requestDto);

    requestDto.validate();

    // fetch the detail
    E object = service.detail(Integer.parseInt(requestDto.getId()));

    // build the response
    DataApiResponseDto<E> response = new DataApiResponseDto<E>(
      StatusEnum.STATUS_SUCCESS.getValue(),
      String.format("the resource id = %s is fetched successfully", 
        requestDto.getId()),
      requestId,
      object
    );

    logger.debug("detail fetch response dto = {}", response);

    return ResponseEntity.ok(response);
  }

  
  /**
   * Delete the resource
   *
   * @param id id of entity that needs to be removed
   * @return ApiResponseDto an api response dto
   */
  @DeleteMapping("{id}")
  public ResponseEntity<ApiResponseDto> delete(
    final @RequestAttribute(AttributeUtil.REQUEST_ID) String requestId,
    final @PathVariable String id
  ) {
    Integer intId = Integer.parseInt(id);

    logger.debug("request to delete object id = : {}", id);
    deleteRequestDto.setRequestId(requestId);

    E object = service.detail(intId);
    E updatedObject = deleteRequestDto.setDeleted(object);

    // soft delete the resource
    service.update(intId, updatedObject);

    // build the response
    ApiResponseDto response = new ApiResponseDto(
      StatusEnum.STATUS_SUCCESS.getValue(),
      String.format("the resource id = %s was just deleted successfully", id),
      requestId
    );

    logger.debug("delete response dto: {}", response);

    return ResponseEntity.ok(response);
  }
}
