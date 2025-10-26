package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {

    @Size(min = 5, max = 15)
    private String username;

    @Size(min = 5, max = 15)
    private String password;

    @Email
    private String email;
}
