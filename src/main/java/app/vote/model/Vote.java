package app.vote.model;

import app.party.model.Party;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "party_id", nullable = false)
    private Party party;

    @Column(nullable = false)
    private LocalDateTime votedAt;
}
