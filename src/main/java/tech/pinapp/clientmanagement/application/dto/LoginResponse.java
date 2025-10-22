package tech.pinapp.clientmanagement.application.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponse {
    private String token;
}