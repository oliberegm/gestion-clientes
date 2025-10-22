package tech.pinapp.clientmanagement.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.pinapp.clientmanagement.application.dto.ClientCreationRequest;
import tech.pinapp.clientmanagement.application.dto.ClientResponse;
import tech.pinapp.clientmanagement.application.mapper.ClientMapper;
import tech.pinapp.clientmanagement.domain.model.Client;
import tech.pinapp.clientmanagement.domain.outbound.ClientEventPublisher;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private GetClientMetricsUseCase getClientMetricsUseCase;
    @Mock
    private CalculatedEstimatedDeathDateUseCase calculatedEstimatedDeathDateUseCase;
    @Mock
    private ClientEventPublisher clientEventPublisher;
    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private CreateClientUseCase createClientUseCase;

    private ClientCreationRequest clientRequest;
    private Client clientToSave;
    private Client savedClient;
    private ClientResponse clientResponse;
    private LocalDate birthDate;
    private LocalDate estimatedDeathDate;

    @BeforeEach
    void setUp() {
        birthDate = LocalDate.of(1990, 5, 15);
        estimatedDeathDate = LocalDate.of(2070, 5, 15);

        clientRequest = ClientCreationRequest.builder()
                .firstName("FirstName_1")
                .lastName("LastName_1")
                .age(34)
                .dateOfBirth(birthDate)
                .build();

        clientToSave = Client.builder()
                .firstName("FirstName_1")
                .lastName("LastName_1")
                .age(34)
                .dateOfBirth(birthDate)
                .build();

        savedClient = Client.builder()
                .id(1L)
                .firstName("FirstName_1")
                .lastName("LastName_1")
                .age(34)
                .dateOfBirth(birthDate)
                .build();

        clientResponse = ClientResponse.builder()
                .id(1L)
                .firstName("FirstName_1")
                .lastName("LastName_1")
                .age(34)
                .dateOfBirth(birthDate)
                .build();
    }

    @Test
    @DisplayName("Should create client, publish event, and return response with estimated death date")
    void execute_whenGivenValidRequest_thenCreatesClientAndReturnsResponse() {
        // Arrange
        when(clientMapper.toClientEntity(any(ClientCreationRequest.class))).thenReturn(clientToSave);
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);
        when(calculatedEstimatedDeathDateUseCase.execute(any(LocalDate.class))).thenReturn(estimatedDeathDate);
        when(clientMapper.toClientResponse(any(Client.class))).thenReturn(clientResponse);

        // Act
        ClientResponse result = createClientUseCase.execute(clientRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("FirstName_1");
        assertThat(result.getEstimatedDeathDate()).isEqualTo(estimatedDeathDate);

        // Verify interactions
        verify(clientRepository, times(1)).save(clientToSave);
        verify(clientEventPublisher, times(1)).publishClientCreated(savedClient);
        verify(getClientMetricsUseCase, times(1)).evictMetricsCache();
        verify(calculatedEstimatedDeathDateUseCase, times(1)).execute(birthDate);
    }
}