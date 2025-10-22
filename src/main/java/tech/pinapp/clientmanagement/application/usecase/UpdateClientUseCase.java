package tech.pinapp.clientmanagement.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.pinapp.clientmanagement.application.dto.ClientResponse;
import tech.pinapp.clientmanagement.application.dto.ClientUpdateRequest;
import tech.pinapp.clientmanagement.application.mapper.ClientMapper;
import tech.pinapp.clientmanagement.domain.exception.ClientNotFoundException;
import tech.pinapp.clientmanagement.domain.model.Client;
import tech.pinapp.clientmanagement.domain.outbound.ClientEventPublisher;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateClientUseCase {
    private final ClientRepository clientRepository;
    private final GetClientMetricsUseCase getClientMetricsUseCase;
    private final ClientEventPublisher clientEventPublisher;
    private final ClientMapper clientMapper;

    @Transactional
    public ClientResponse execute(ClientUpdateRequest request) {
        log.info("INICIO: Ejecutando caso de uso para actualizar cliente con ID: {}", request.getId());

        Client existingClient = clientRepository.findById(request.getId())
            .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado con ID: " + request.getId()));

        Integer originalAge = existingClient.getAge();
        Client clientToUpdate = clientMapper.toClientEntity(request, existingClient);

        Client updatedClient = clientRepository.save(clientToUpdate);

        log.info("Cliente con ID {} actualizado exitosamente.", updatedClient.getId());

        if (!Objects.equals(originalAge, updatedClient.getAge())) {
            getClientMetricsUseCase.evictMetricsCache();
        }
        
        clientEventPublisher.publishClientUpdated(updatedClient);

        return clientMapper.toClientResponse(updatedClient);
    }
}
