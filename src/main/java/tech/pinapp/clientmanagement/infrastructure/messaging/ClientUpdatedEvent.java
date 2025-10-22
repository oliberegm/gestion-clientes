package tech.pinapp.clientmanagement.infrastructure.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.pinapp.clientmanagement.domain.model.Client;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdatedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long clientId;
    private String firstName;
    private String lastName;
    private Integer age;
    private LocalDateTime updateTimestamp;

}