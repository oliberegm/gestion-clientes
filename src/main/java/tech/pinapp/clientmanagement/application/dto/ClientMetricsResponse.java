package tech.pinapp.clientmanagement.application.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ClientMetricsResponse {
    BigDecimal averageAge;
    BigDecimal standardDeviation;
    Long totalClients;

}
