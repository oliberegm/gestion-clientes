package tech.pinapp.clientmanagement.infrastructure.messaging;

import lombok.Value;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for the Client Creation Event (the message payload).
 */
@Value
public class ClientCreatedEvent {
    Long clientId;
    String fullName;
    LocalDateTime creationTimestamp = LocalDateTime.now();
    String eventSource = "client-management-service";
}