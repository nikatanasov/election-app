package app.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class PartyResponse {
    private UUID id;

    private String name;

    private String abbreviation;

    private int numberOfVotes;
}
