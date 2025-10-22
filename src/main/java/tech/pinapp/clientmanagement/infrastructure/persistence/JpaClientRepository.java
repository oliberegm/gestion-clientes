package tech.pinapp.clientmanagement.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import tech.pinapp.clientmanagement.domain.model.Client;
import tech.pinapp.clientmanagement.domain.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the domain's ClientRepository port using JpaClientRepository.
 * This class ensures that the Domain layer remains decoupled from the JPA implementation.
 */
@Repository
@RequiredArgsConstructor
public class JpaClientRepository implements ClientRepository {

    private final ClientJpaRepository clientJpaRepository;

    @Override
    public Client save(Client client) {
        return clientJpaRepository.save(client);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientJpaRepository.findById(id);
    }

    @Override
    public List<Client> findAll() {
        return clientJpaRepository.findAll();
    }

    @Override
    public List<Integer> findAllAges() {
        return clientJpaRepository.findAllAges();
    }

    @Override
    public Page<Client> findAll(Specification<Client> spec, Pageable pageable) {
        return clientJpaRepository.findAll(spec, pageable);
    }

    @Override
    public void deleteById(Long id) {
        clientJpaRepository.deleteById(id);
    }
}
