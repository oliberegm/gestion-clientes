package tech.pinapp.clientmanagement.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tech.pinapp.clientmanagement.application.dto.ClientMetricsResponse;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetClientMetricsUseCase {
    private static final int DECIMAL_PRECISION = 2;

    private final ClientRepository clientRepository;

    /**
     * Ejecuta el caso de uso para obtener las metricas de los clientes.
     * @return el objeto con el resumen de las metricas.
     */
    @Cacheable(value = "clientMetrics")
    public ClientMetricsResponse execute() {
        log.info("INICIO: Ejecutando caso de uso para obtener métricas de clientes.");
        List<Integer> ages = clientRepository.findAllAges();

        if (ages.isEmpty()) {
            log.warn("ADVERTENCIA: no hay clientes para generar estadisticas.");
            return ClientMetricsResponse.builder()
                .averageAge(BigDecimal.ZERO)
                .standardDeviation(BigDecimal.ZERO)
                .totalClients(0L)
                .build();
        }

        Long totalClients = (long) ages.size();

        BigDecimal sumAges = ages.stream()
            .map(BigDecimal::valueOf)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal total = BigDecimal.valueOf(totalClients);
        BigDecimal averageAge = sumAges.divide(total, DECIMAL_PRECISION, RoundingMode.HALF_UP);

        BigDecimal sumOfSquaredDifferences = ages.stream()
            .map(age -> BigDecimal.valueOf(age).subtract(averageAge))
            .map(diff -> diff.pow(2))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal variance = sumOfSquaredDifferences.divide(total, DECIMAL_PRECISION, RoundingMode.HALF_UP);

        BigDecimal standardDeviation = BigDecimal.valueOf(Math.sqrt(variance.doubleValue()))
            .setScale(DECIMAL_PRECISION, RoundingMode.HALF_UP);

        log.info("FIN: Métricas calculadas. Total clientes: {}. Promedio: {}", totalClients, averageAge);

        return ClientMetricsResponse.builder()
            .totalClients(totalClients)
            .averageAge(averageAge)
            .standardDeviation(standardDeviation)
            .build();
    }

    @CacheEvict(value = "clientMetrics", allEntries = true)
    public void evictMetricsCache() {
        log.info("CACHÉ INVÁLIDA: La caché de métricas de clientes ha sido eliminada debido a una actualización de datos.");
    }


}
