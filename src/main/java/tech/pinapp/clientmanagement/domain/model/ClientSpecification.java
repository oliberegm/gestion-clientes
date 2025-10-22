package tech.pinapp.clientmanagement.domain.model;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import tech.pinapp.clientmanagement.application.dto.ClientFilter;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientSpecification {

    public Specification<Client> fromFilter(ClientFilter filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter == null) {
                return criteriaBuilder.conjunction(); // No filters, return all
            }

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getFirstName() != null && !filter.getFirstName().isBlank()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("firstName")),
                    "%" + filter.getFirstName().toLowerCase() + "%"
                ));
            }

            if (filter.getLastName() != null && !filter.getLastName().isBlank()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("lastName")),
                    "%" + filter.getLastName().toLowerCase() + "%"
                ));
            }

            if (filter.getAge() != null) {
                predicates.add(criteriaBuilder.equal(root.get("age"), filter.getAge()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}