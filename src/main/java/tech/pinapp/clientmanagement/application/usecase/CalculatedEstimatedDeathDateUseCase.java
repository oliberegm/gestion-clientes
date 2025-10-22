package tech.pinapp.clientmanagement.application.usecase;

import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CalculatedEstimatedDeathDateUseCase {
    private static final int AVERAGE_LIFESPAN_YEARS = 85;

    /**
     * Calcula la fecha de muerte estimada de un cliente basándose en su fecha de nacimiento
     * y una esperanza de vida promedio.
     *
     * @param dateOfBirth La fecha de nacimiento del cliente.
     * @return La fecha de muerte estimada.
     */
    @Named("calculateEstimatedDeathDate")
    public LocalDate execute(java.time.LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return null;
        }
        return dateOfBirth.plusYears(AVERAGE_LIFESPAN_YEARS);
    }

}
