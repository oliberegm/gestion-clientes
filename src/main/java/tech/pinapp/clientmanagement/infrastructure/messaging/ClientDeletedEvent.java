package tech.pinapp.clientmanagement.infrastructure.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDeletedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long clientId;
    private LocalDateTime deletionTimestamp;

}