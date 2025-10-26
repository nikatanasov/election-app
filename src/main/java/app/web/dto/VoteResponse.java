package app.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VoteResponse {
    private String username;

    private String partyName;

    private LocalDateTime votedAt;
}
