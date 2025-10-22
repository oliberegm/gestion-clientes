package tech.pinapp.clientmanagement.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.pinapp.clientmanagement.application.dto.ClientFilter;
import tech.pinapp.clientmanagement.application.dto.ClientListResponse;
import tech.pinapp.clientmanagement.application.dto.ClientResponse;
import tech.pinapp.clientmanagement.application.mapper.ClientMapper;
import tech.pinapp.clientmanagement.domain.model.Client;
import tech.pinapp.clientmanagement.domain.model.ClientSpecification;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FindClientsUseCase {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ClientSpecification clientSpecification;

    @Transactional(readOnly = true)
    public ClientListResponse execute(ClientFilter filter, Pageable pageable) {
        log.info("Executing FindClientsUseCase with filter: {} and pageable: {}", filter, pageable);
        var spec = clientSpecification.fromFilter(filter);

        Page<Client> clientPage = clientRepository.findAll(spec, pageable);

        List<ClientResponse> clientResponses = clientPage.getContent().stream()
                .map(clientMapper::toClientResponse)
                .toList();

        log.info("Found {} clients for page {} of {} total items.", clientResponses.size(), clientPage.getNumber(), clientPage.getTotalElements());
        return ClientListResponse.builder()
                .clients(clientResponses)
                .currentPage(clientPage.getNumber())
                .totalItems(clientPage.getTotalElements())
                .totalPages(clientPage.getTotalPages())
                .build();
    }
}