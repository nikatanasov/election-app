package app.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RegisterResponse {
    private UUID userId;

    private String username;

    private String email;
}
