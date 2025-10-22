package tech.pinapp.clientmanagement.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import tech.pinapp.clientmanagement.domain.model.Client;
import java.util.List;
import java.util.Optional;

/**
 * Port (Output Adapter Interface) for data persistence operations of the Client entity.
 * This contract defines the necessary methods for the Use Cases layer.
 * It is technology-agnostic (not bound to JPA, JDBC, etc.).
 */
public interface ClientRepository {

    /**
     * Saves a new client or updates an existing one.
     * @param client The client entity to save.
     * @return The saved client entity with its ID.
     */
    Client save(Client client);

    /**
     * Finds a client by its unique ID.
     * @param id The client ID.
     * @return An Optional containing the client if found, or empty otherwise.
     */
    Optional<Client> findById(Long id);

    /**
     * Retrieves all registered clients.
     * @return A list of all clients.
     */
    List<Client> findAll();

    /**
     * Finds the ages of all clients, used for calculating metrics.
     * @return A list of all client ages.
     */
    List<Integer> findAllAges();

    /**
     * Finds all clients matching a specification, with pagination.
     * @param spec The specification to filter by.
     * @param pageable The pagination information.
     * @return A page of clients.
     */
    Page<Client> findAll(Specification<Client> spec, Pageable pageable);

    /**
     * Deletes a client by its unique ID.
     * @param id The client ID to delete.
     */
    void deleteById(Long id);
}
