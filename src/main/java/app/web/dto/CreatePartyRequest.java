package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePartyRequest {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String abbreviation;
}
