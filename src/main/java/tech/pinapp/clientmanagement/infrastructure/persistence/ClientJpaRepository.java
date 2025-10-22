package tech.pinapp.clientmanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import tech.pinapp.clientmanagement.domain.model.Client;

import java.util.List;

/**
 * Spring Data JPA specific interface. It uses the convention-over-configuration
 * approach to provide basic CRUD operations without explicit implementation.
 */
interface ClientJpaRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    // Custom query to efficiently retrieve only the ages for metric calculation
    @Query("SELECT c.age FROM Client c")
    List<Integer> findAllAges();
}
