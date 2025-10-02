package app.web.dto;

import app.user.model.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {
    private UUID userId;

    private String username;

    private String email;

    private UserRole userRole;
}
