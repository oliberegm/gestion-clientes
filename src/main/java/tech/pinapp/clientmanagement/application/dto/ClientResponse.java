package tech.pinapp.clientmanagement.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * Data Transfer Object for returning client details (including the derived calculation).
 */
@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class ClientResponse {

    Long id;
    String firstName;
    String lastName;
    Integer age;
    LocalDate dateOfBirth;

    // Campo para el requisito de "cálculo derivado" (esperanza de vida estimada)
    LocalDate estimatedDeathDate;
}
