package tech.pinapp.clientmanagement.application.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * DTO for returning a paginated list of clients.
 */
@Data
@Builder
public class ClientListResponse {
    private List<ClientResponse> clients;
    private int currentPage;
    private long totalItems;
    private int totalPages;
}