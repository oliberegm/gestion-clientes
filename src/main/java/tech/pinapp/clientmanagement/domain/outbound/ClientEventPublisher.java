package tech.pinapp.clientmanagement.domain.outbound;

import tech.pinapp.clientmanagement.domain.model.Client;

/**
 * Outbound Port for publishing client-related events.
 * The application layer uses this port to send events without knowing the underlying messaging technology.
 */
public interface ClientEventPublisher {

    void publishClientCreated(Client client);

    void publishClientDeleted(Client client);

    void publishClientUpdated(Client client);

}