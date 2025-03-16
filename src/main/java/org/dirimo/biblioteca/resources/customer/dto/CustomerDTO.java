package org.dirimo.biblioteca.resources.customer.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
}
