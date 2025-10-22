package tech.pinapp.clientmanagement.application.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for filtering clients. All fields are optional.
 */
@Data
@Builder
public class ClientFilter {
    private String firstName;
    private String lastName;
    private Integer age;
}