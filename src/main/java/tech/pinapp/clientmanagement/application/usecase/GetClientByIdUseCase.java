package tech.pinapp.clientmanagement.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.pinapp.clientmanagement.application.dto.ClientResponse;
import tech.pinapp.clientmanagement.application.mapper.ClientMapper;
import tech.pinapp.clientmanagement.domain.exception.ClientNotFoundException;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetClientByIdUseCase {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    /**
     * Busca un cliente por su ID. Los resultados se cachean para mejorar el rendimiento en consultas repetidas.
     * La primera vez que se llama con un ID, se ejecuta la consulta a la BD.
     * Las siguientes veces, el resultado se devuelve directamente desde la caché "clients".
     *
     * @param id El ID del cliente a buscar.
     * @return Un ClientResponse con los datos del cliente.
     */
    @Cacheable(value = "clients", key = "#id")
    @Transactional(readOnly = true)
    public ClientResponse execute(Long id) {
        log.info("Buscando cliente con ID: {} en la base de datos (no se encontró en caché)", id);
        return clientRepository.findById(id).map(clientMapper::toClientResponse)
            .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado con ID: " + id));
    }
}
