package tech.pinapp.clientmanagement.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CalculatedEstimatedDeathDateUseCaseTest {

    private CalculatedEstimatedDeathDateUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CalculatedEstimatedDeathDateUseCase();
    }

    @Test
    @DisplayName("Should calculate estimated death date correctly for a valid birth date")
    void execute_givenValidBirthDate_shouldReturnCorrectEstimatedDeathDate() {
        // Arrange
        LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);
        LocalDate expectedDeathDate = LocalDate.of(2075, 5, 15);

        // Act
        LocalDate actualDeathDate = useCase.execute(dateOfBirth);

        // Assert
        assertThat(actualDeathDate).isEqualTo(expectedDeathDate);
    }

    @Test
    @DisplayName("Should return null when date of birth is null")
    void execute_givenNullBirthDate_shouldReturnNull() {
        // Arrange
        LocalDate dateOfBirth = null;

        // Act
        LocalDate actualDeathDate = useCase.execute(dateOfBirth);

        // Assert
        assertThat(actualDeathDate).isNull();
    }

}