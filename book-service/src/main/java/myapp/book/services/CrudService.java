/**
 * This is the interface of crud servie.
 * 
 * @author Phann Malinka
 */
package myapp.book.services;

import java.io.Serializable;
import myapp.book.dto.PaginationDto;
import myapp.book.dto.SearchDto;

public interface CrudService<T, I extends Serializable> {
    public T findById(I id);
    public PaginationDto<T> search(SearchDto searchDto);
    public T create(T object);
    public T update(I id, T object);
    public void delete(I id);
}