package tech.pinapp.clientmanagement.application.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class ClientUpdateRequest {
    Long id;
    String firstName;
    String lastName;
    Integer age;
    LocalDate dateOfBirth;
}
