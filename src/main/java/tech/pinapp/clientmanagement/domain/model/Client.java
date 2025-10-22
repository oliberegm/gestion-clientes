package tech.pinapp.clientmanagement.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

/**
 * Represents the core domain entity for a client.
 * This class is also mapped as a JPA Entity for persistence.
 */
@Value
@Builder
@AllArgsConstructor
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Column(name = "age", nullable = false)
    Integer age;

    @Column(name = "date_of_birth", nullable = false)
    LocalDate dateOfBirth;
}
