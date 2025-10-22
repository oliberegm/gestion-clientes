package tech.pinapp.clientmanagement.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.pinapp.clientmanagement.domain.exception.ClientNotFoundException;
import tech.pinapp.clientmanagement.domain.model.Client;
import tech.pinapp.clientmanagement.domain.outbound.ClientEventPublisher;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteClientUseCase {

    private final ClientRepository clientRepository;
    private final GetClientMetricsUseCase getClientMetricsUseCase;
    private final ClientEventPublisher clientEventPublisher;

    /**
     * Deletes a client by its ID.
     * It first verifies the client exists before deleting.
     * After deletion, it invalidates the client metrics cache.
     *
     * @param id The ID of the client to delete.
     * @throws ClientNotFoundException if no client with the given ID is found.
     */
    @Transactional
    public void execute(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("No se puede eliminar. Cliente no encontrado con ID: " + id));

        clientRepository.deleteById(id);

        log.info("Cliente eliminado exitosamente con ID: {}", id);

        clientEventPublisher.publishClientDeleted(client);
        log.info("Evento de eliminación de cliente enviado al sistema de mensajería (asíncrono).");


        getClientMetricsUseCase.evictMetricsCache();
        log.info("Caché de métricas invalidada tras la eliminación del cliente con ID: {}", id);
    }
}