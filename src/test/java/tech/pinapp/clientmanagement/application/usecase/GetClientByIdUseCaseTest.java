package tech.pinapp.clientmanagement.application.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.pinapp.clientmanagement.application.dto.ClientResponse;
import tech.pinapp.clientmanagement.application.mapper.ClientMapper;
import tech.pinapp.clientmanagement.domain.exception.ClientNotFoundException;
import tech.pinapp.clientmanagement.domain.model.Client;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetClientByIdUseCaseTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private CalculatedEstimatedDeathDateUseCase calculatedEstimatedDeathDateUseCase;

    @InjectMocks
    private GetClientByIdUseCase getClientByIdUseCase;

    private final Long CLIENT_ID = 1L;
    private Client client;
    private ClientResponse clientResponse;
    private LocalDate birthDate;
    private LocalDate estimatedDeathDate;

    @BeforeEach
    void setUp() {
        birthDate = LocalDate.of(1990, 5, 15);
        estimatedDeathDate = LocalDate.of(2075, 5, 15); // 1990 + 85 years

        client = Client.builder()
                .id(CLIENT_ID)
                .firstName("John")
                .lastName("Doe")
                .age(34)
                .dateOfBirth(birthDate)
                .build();

        // The base response from mapper (without estimatedDeathDate initially)
        clientResponse = ClientResponse.builder()
                .id(CLIENT_ID)
                .firstName("John")
                .lastName("Doe")
                .age(34)
                .dateOfBirth(birthDate)
                .build();
    }

    @Test
    @DisplayName("Should return ClientResponse with estimated death date when client exists")
    void execute_whenClientExists_shouldReturnClientResponseWithEstimatedDeathDate() {
        // Arrange
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
        when(clientMapper.toClientResponse(client)).thenReturn(clientResponse);
        when(calculatedEstimatedDeathDateUseCase.execute(birthDate)).thenReturn(estimatedDeathDate);

        // Act
        ClientResponse result = getClientByIdUseCase.execute(CLIENT_ID);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(CLIENT_ID);
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getEstimatedDeathDate()).isEqualTo(estimatedDeathDate);

        // Verify interactions
        verify(clientRepository, times(1)).findById(CLIENT_ID);
        verify(clientMapper, times(1)).toClientResponse(client);
        verify(calculatedEstimatedDeathDateUseCase, times(1)).execute(birthDate);
    }

    @Test
    @DisplayName("Should throw ClientNotFoundException when client does not exist")
    void execute_whenClientDoesNotExist_shouldThrowClientNotFoundException() {
        // Arrange
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> getClientByIdUseCase.execute(CLIENT_ID))
                .isInstanceOf(ClientNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado con ID: " + CLIENT_ID);

        // Verify interactions
        verify(clientRepository, times(1)).findById(CLIENT_ID);
        verify(clientMapper, never()).toClientResponse(any(Client.class)); // Mapper should not be called
        verify(calculatedEstimatedDeathDateUseCase, never()).execute(any(LocalDate.class)); // Calculation should not be called
    }
}