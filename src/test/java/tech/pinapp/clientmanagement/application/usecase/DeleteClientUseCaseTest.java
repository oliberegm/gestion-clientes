package tech.pinapp.clientmanagement.application.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.pinapp.clientmanagement.domain.exception.ClientNotFoundException;
import tech.pinapp.clientmanagement.domain.model.Client;
import tech.pinapp.clientmanagement.domain.outbound.ClientEventPublisher;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteClientUseCaseTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private GetClientMetricsUseCase getClientMetricsUseCase;
    @Mock
    private ClientEventPublisher clientEventPublisher;

    @InjectMocks
    private DeleteClientUseCase deleteClientUseCase;

    private final Long CLIENT_ID = 1L;

    @Test
    @DisplayName("Should delete client and evict metrics cache when client exists")
    void execute_whenClientExists_shouldDeleteClientAndEvictCache() {
        // Arrange
        Client existingClient = Client.builder().id(CLIENT_ID).firstName("Test").lastName("User").age(30).build();
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(existingClient));
        doNothing().when(clientRepository).deleteById(CLIENT_ID);
        doNothing().when(getClientMetricsUseCase).evictMetricsCache();
        doNothing().when(clientEventPublisher).publishClientDeleted(existingClient);

        // Act & Assert
        assertThatCode(() -> deleteClientUseCase.execute(CLIENT_ID))
                .doesNotThrowAnyException();

        // Verify
        verify(clientRepository, times(1)).findById(CLIENT_ID);
        verify(clientRepository, times(1)).deleteById(CLIENT_ID);
        verify(getClientMetricsUseCase, times(1)).evictMetricsCache();
        verify(clientEventPublisher, times(1)).publishClientDeleted(existingClient);
    }

    @Test
    @DisplayName("Should throw ClientNotFoundException when client does not exist")
    void execute_whenClientDoesNotExist_shouldThrowClientNotFoundException() {
        // Arrange
        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deleteClientUseCase.execute(CLIENT_ID))
                .isInstanceOf(ClientNotFoundException.class)
                .hasMessageContaining("Cliente no encontrado con ID: " + CLIENT_ID);

        // Verify
        verify(clientRepository, times(1)).findById(CLIENT_ID);
        verify(clientRepository, never()).deleteById(CLIENT_ID);
        verify(getClientMetricsUseCase, never()).evictMetricsCache();
    }
}