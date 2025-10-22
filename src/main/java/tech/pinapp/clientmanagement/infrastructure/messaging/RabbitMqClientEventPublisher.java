package tech.pinapp.clientmanagement.infrastructure.messaging;

import tech.pinapp.clientmanagement.domain.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.pinapp.clientmanagement.domain.outbound.ClientEventPublisher;

import java.time.LocalDateTime;

/**
 * Service to publish asynchronous messages to the message broker (RabbitMQ/AMQP).
 * This service is an infrastructure component used by the Use Cases layer.
 */
@Service
public class RabbitMqClientEventPublisher implements ClientEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(RabbitMqClientEventPublisher.class);

    // Se asume que el nombre del exchange y la routing key se obtienen de la configuración
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final AmqpTemplate rabbitTemplate;

    public RabbitMqClientEventPublisher(AmqpTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publishes an event after a client is successfully created.
     * @param client The client entity that was just saved.
     */
    public void publishClientCreationEvent(Client client) {

    }

    @Override
    public void publishClientCreated(Client client) {
        ClientCreatedEvent event = new ClientCreatedEvent(
            client.getId(),
            client.getFirstName() + " " + client.getLastName()
        );

        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, event);
            log.info("MENSAJE ENVIADO: Evento ClientCreated para el ID {} enviado a RabbitMQ.", client.getId());
        } catch (Exception e) {
            log.error("ERROR CRÍTICO: Fallo al enviar el evento de creación de cliente con ID {}. Causa: {}", client.getId(), e.getMessage());
        }
    }

    @Override
    public void publishClientDeleted(Client client) {
        ClientDeletedEvent event = new ClientDeletedEvent(
            client.getId(),
            LocalDateTime.now()
        );

        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, event);
            log.info("MESSAGE SENT: ClientDeleted event for ID {} sent to RabbitMQ.", client.getId());
        } catch (Exception e) {
            log.error("CRITICAL ERROR: Failed to send client deletion event for ID {}. Cause: {}", client.getId(), e.getMessage());
            // Consider an Outbox pattern or retry mechanism here.
        }
    }

    /**
     * Publishes an event after a client is successfully updated.
     * @param client The client entity that was just updated.
     */
    @Override
    public void publishClientUpdated(Client client) {
        ClientUpdatedEvent event = new ClientUpdatedEvent(
            client.getId(),
            client.getFirstName(),
            client.getLastName(),
            client.getAge(),
            LocalDateTime.now()
        );

        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, event);
            log.info("MESSAGE SENT: ClientUpdated event for ID {} sent to RabbitMQ.", client.getId());
        } catch (Exception e) {
            log.error("CRITICAL ERROR: Failed to send client update event for ID {}. Cause: {}", client.getId(), e.getMessage());
            // Consider an Outbox pattern or retry mechanism here.
        }
    }
}