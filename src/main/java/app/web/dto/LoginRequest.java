package app.web.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Size(min = 5, max = 15, message = "Username length must be between 5 and 15 symbols")
    private String username;

    @Size(min = 5, max = 15, message = "Password length must be between 5 and 15 symbols")
    private String password;
}
