/**
 * This is the interface of all create request dto.
 *
 * @author Phann Malinka
 */
package library.dto;

public interface DataRequestDto<E> {
    public void setRequestId(String requestId);
    public E setDeleted(E object);
    public E build();
    public E build(E object);
    public void validate();
}