package tech.pinapp.clientmanagement.application.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.pinapp.clientmanagement.application.dto.ClientMetricsResponse;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetClientMetricsUseCaseTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private GetClientMetricsUseCase getClientMetricsUseCase;

    @Test
    @DisplayName("Should calculate metrics correctly when clients exist")
    void execute_whenClientsExist_shouldReturnCorrectMetrics() {
        // Arrange
        List<Integer> ages = Arrays.asList(20, 30, 40);
        when(clientRepository.findAllAges()).thenReturn(ages);

        // Expected values
        BigDecimal expectedAverage = new BigDecimal("30.00");
        // ( (20-30)^2 + (30-30)^2 + (40-30)^2 ) / 3 = (100 + 0 + 100) / 3 = 66.666...
        // sqrt(66.666...) = 8.1649...
        BigDecimal expectedStdDeviation = new BigDecimal("8.16");

        // Act
        ClientMetricsResponse result = getClientMetricsUseCase.execute();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalClients()).isEqualTo(3L);
        assertThat(result.getAverageAge()).isEqualTo(expectedAverage);
        assertThat(result.getStandardDeviation()).isEqualTo(expectedStdDeviation);

        // Verify
        verify(clientRepository, times(1)).findAllAges();
    }

    @Test
    @DisplayName("Should return zero metrics when no clients exist")
    void execute_whenNoClientsExist_shouldReturnZeroMetrics() {
        // Arrange
        when(clientRepository.findAllAges()).thenReturn(Collections.emptyList());

        // Act
        ClientMetricsResponse result = getClientMetricsUseCase.execute();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalClients()).isEqualTo(0L);
        assertThat(result.getAverageAge()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getStandardDeviation()).isEqualTo(BigDecimal.ZERO);

        // Verify
        verify(clientRepository, times(1)).findAllAges();
    }

    @Test
    @DisplayName("Should evict cache when evictMetricsCache is called")
    void evictMetricsCache_shouldBeCallable() {
        // This test mainly exists to ensure the method can be called without error.
        // The caching behavior itself is tested in integration tests.
        
        // Act & Assert
        // We just call the method. If it throws an exception, the test will fail.
        getClientMetricsUseCase.evictMetricsCache();

        // No verification is needed here for a unit test, as it only contains an annotation and a log statement.
    }
}