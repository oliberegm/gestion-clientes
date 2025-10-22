package tech.pinapp.clientmanagement.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * Data Transfer Object for creating a new client.
 * Includes necessary validation constraints for input data.
 */
@Value
@Builder
public class ClientCreationRequest {

    @NotBlank(message = "El nombre del cliente no puede estar vacío.")
    String firstName;

    @NotBlank(message = "El apellido del cliente no puede estar vacío.")
    String lastName;

    @NotNull(message = "La edad es obligatoria.")
    @Min(value = 18, message = "El cliente debe ser mayor de 18 años.")
    Integer age;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser en el pasado.")
    LocalDate dateOfBirth;
}
