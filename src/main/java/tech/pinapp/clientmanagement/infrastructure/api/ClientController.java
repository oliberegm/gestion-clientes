package tech.pinapp.clientmanagement.infrastructure.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.pinapp.clientmanagement.application.dto.*;
import tech.pinapp.clientmanagement.application.usecase.CreateClientUseCase;
import tech.pinapp.clientmanagement.application.usecase.DeleteClientUseCase;
import tech.pinapp.clientmanagement.application.usecase.FindClientsUseCase;
import tech.pinapp.clientmanagement.application.usecase.GetClientByIdUseCase;
import tech.pinapp.clientmanagement.application.usecase.GetClientMetricsUseCase;
import tech.pinapp.clientmanagement.application.usecase.UpdateClientUseCase;

import java.util.List;

/**
 * REST Controller (Input Adapter) for client management operations.
 */
@RestController
@RequestMapping("/clients")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gestión completa de clientes y consulta de métricas.")
public class ClientController {

    private final CreateClientUseCase createClientUseCase;
    private final GetClientMetricsUseCase getClientMetricsUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final GetClientByIdUseCase getClientByIdUseCase;
    private final FindClientsUseCase findClientsUseCase;
    private final DeleteClientUseCase deleteClientUseCase;

    /**
     * Endpoint para crear un nuevo cliente (POST /clients).
     */
    @Operation(
        summary = "Registra un nuevo cliente",
        description = "Crea un cliente y dispara un evento asíncrono de notificación.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Cliente creado exitosamente",
                content = @Content(schema = @Schema(implementation = ClientResponse.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida (errores de validación de campos)"
            )
        }
    )
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientCreationRequest request) {
        log.info("PETICIÓN RECIBIDA: Solicitud de creación de nuevo cliente. Nombre: {} {}",
            request.getFirstName(), request.getLastName());

        // 1. Delegación: Llama al Caso de Uso para ejecutar la lógica de negocio
        ClientResponse response = createClientUseCase.execute(request);

        log.info("RESPUESTA ENVIADA: Cliente creado exitosamente con ID: {}", response.getId());

        // 2. Respuesta: Devuelve el DTO de respuesta con el código de estado 201 CREATED
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Obtiene métricas de clientes",
        description = "Proporciona el número total de clientes registrados.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Métricas obtenidas exitosamente",
                content = @Content(schema = @Schema(implementation = ClientMetricsResponse.class))
            )
        }
    )
    @GetMapping("/metrics")
    public ResponseEntity<ClientMetricsResponse> getClientMetrics() {
        log.info("PETICIÓN RECIBIDA: Solicitud de métricas de clientes.");
        ClientMetricsResponse response = getClientMetricsUseCase.execute();
        log.info("RESPUESTA ENVIADA: Métricas de clientes obtenidas exitosamente: {}.", response);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Actualiza un cliente existente",
        description = "Actualiza la información de un cliente por su ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cliente actualizado exitosamente",
                content = @Content(schema = @Schema(implementation = ClientResponse.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida (errores de validación de campos o ID no encontrado)"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Cliente no encontrado"
            )
        }
    )
    @PutMapping
    public ResponseEntity<ClientResponse> updateClient(@Valid @RequestBody ClientUpdateRequest request) {
        log.info("PETICIÓN RECIBIDA: Solicitud de actualización de cliente con ID: {}", request.getId());

        ClientResponse response = updateClientUseCase.execute(request);

        log.info("RESPUESTA ENVIADA: Cliente con ID {} actualizado exitosamente.", response.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtiene un cliente por ID",
        description = "Busca y devuelve la información de un cliente específico por su ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Cliente encontrado exitosamente",
                content = @Content(schema = @Schema(implementation = ClientResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Cliente no encontrado"
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        log.info("PETICIÓN RECIBIDA: Solicitud para obtener cliente con ID: {}", id);
        ClientResponse response = getClientByIdUseCase.execute(id);
        log.info("RESPUESTA ENVIADA: Cliente con ID {} encontrado.", id);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Obtiene todos los clientes",
        description = "Devuelve una lista paginada y filtrable de todos los clientes. " +
                      "Permite filtrar por `firstName`, `lastName` y `age`. " +
                      "Soporta paginación con los parámetros `page`, `size` y `sort` (ej: `sort=lastName,asc`).",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de clientes obtenida exitosamente",
                content = @Content(schema = @Schema(implementation = ClientListResponse.class)) // Actualizar el schema si es necesario
            )
        }
    )
    @GetMapping
    public ResponseEntity<ClientListResponse> findClients(
            ClientFilter filter,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        log.info("PETICIÓN RECIBIDA: Solicitud para buscar clientes con filtros {} y paginación {}", filter, pageable);
        ClientListResponse response = findClientsUseCase.execute(filter, pageable);
        log.info("RESPUESTA ENVIADA: Se han recuperado {} clientes en la página {} de un total de {}.",
                response.getClients().size(), response.getCurrentPage(), response.getTotalItems());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Elimina un cliente por ID",
        description = "Elimina un cliente del sistema utilizando su ID.",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Cliente eliminado exitosamente (sin contenido)"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Cliente no encontrado"
            )
        }
    )
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.info("PETICIÓN RECIBIDA: Solicitud para eliminar cliente con ID: {}", id);
        deleteClientUseCase.execute(id);
        log.info("RESPUESTA ENVIADA: Cliente con ID {} eliminado exitosamente.", id);
        return ResponseEntity.noContent().build();
    }


}