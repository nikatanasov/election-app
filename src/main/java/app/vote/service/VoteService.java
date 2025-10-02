package app.vote.service;

import app.party.model.Party;
import app.party.service.PartyService;
import app.user.model.User;
import app.user.service.UserService;
import app.vote.model.Vote;
import app.vote.repository.VoteRepository;
import app.web.dto.VoteRequest;
import app.web.dto.VoteResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserService userService;
    private final PartyService partyService;

    @Autowired
    public VoteService(VoteRepository voteRepository, UserService userService, PartyService partyService) {
        this.voteRepository = voteRepository;
        this.userService = userService;
        this.partyService = partyService;
    }

    public Vote createNewVote(VoteRequest voteRequest, UUID userId) {
        User user = userService.getById(userId);
        Party party = partyService.getByName(voteRequest.getPartyName());
        Vote vote = Vote.builder()
                .user(user)
                .party(party)
                .votedAt(LocalDateTime.now())
                .build();
        Vote savedVote = voteRepository.save(vote);
        party.getVotes().add(savedVote);
        return savedVote;
    }

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public Vote getVoteById(UUID voteId) {
        return voteRepository.findById(voteId).orElseThrow(() -> new RuntimeException("Vote does not exist!"));
    }
}
