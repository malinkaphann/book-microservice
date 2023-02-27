/**
 *
 * @author Phann Malinka
 */
package library.dto;

import library.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdRequestDto extends ApiRequestDto {

  private String id;

  public void validate() {
    if (this.id == null) {
      throw new ValidationException("id is required");
    }

    try {
      Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new ValidationException("id must be a number");
    }
  }

  @Override
  public String toString() {
    return String.format(
      "IdRequestDto(request id = %s, id = %s)",
      super.getRequestId(),
      id
    );
  }
}
