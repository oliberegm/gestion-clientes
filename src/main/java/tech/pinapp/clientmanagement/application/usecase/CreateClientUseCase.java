package tech.pinapp.clientmanagement.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.pinapp.clientmanagement.application.dto.ClientCreationRequest;
import tech.pinapp.clientmanagement.application.dto.ClientResponse;
import tech.pinapp.clientmanagement.application.mapper.ClientMapper;
import tech.pinapp.clientmanagement.domain.model.Client;
import tech.pinapp.clientmanagement.domain.outbound.ClientEventPublisher;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

/**
 * Use Case to handle the creation of a new client.
 * This class orchestrates the business logic: persistence and asynchronous messaging.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateClientUseCase {

    private final ClientRepository clientRepository;
    private final ClientEventPublisher clientEventPublisher;
    private final GetClientMetricsUseCase getClientMetricsUseCase;
    private final ClientMapper clientMapper;


    /**
     * Executes the client creation process.
     * @param request The validated data transfer object with client details.
     * @return The response DTO containing the details of the newly created client.
     */
    @Transactional
    public ClientResponse execute(ClientCreationRequest request) {
        log.info("INICIO: Ejecutando caso de uso para crear nuevo cliente: {} {}", request.getFirstName(), request.getLastName());

        Client newClient = clientMapper.toClientEntity(request);

        Client savedClient = clientRepository.save(newClient);

        log.info("Cliente registrado exitosamente con ID: {}", savedClient.getId());

        clientEventPublisher.publishClientCreated(savedClient);
        log.info("Evento de creación de cliente enviado al sistema de mensajería (asíncrono).");

        getClientMetricsUseCase.evictMetricsCache();

        return clientMapper.toClientResponse(savedClient);
    }
}