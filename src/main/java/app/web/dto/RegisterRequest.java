package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {


    @Size(min = 5, max = 15)
    @NotNull
    @NotBlank
    private String username;

    //@Email(message = "Invalid email")
    @NotNull
    @Email
    private String email;

    @Size(min = 5, max = 15)
    @NotNull
    @NotBlank
    private String password;


    @NotNull
    @NotBlank
    @Size(min = 5, max = 15)
    private String confirmPassword;

}
