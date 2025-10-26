package app.vote.service;

import app.party.model.Party;
import app.party.service.PartyService;
import app.user.model.User;
import app.user.service.UserService;
import app.vote.model.Vote;
import app.vote.repository.VoteRepository;
import app.web.dto.VoteRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserService userService;
    private final PartyService partyService;
    private final MailSender mailSender;

    @Autowired
    public VoteService(VoteRepository voteRepository, UserService userService, PartyService partyService, MailSender mailSender) {
        this.voteRepository = voteRepository;
        this.userService = userService;
        this.partyService = partyService;
        this.mailSender = mailSender;
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
        //party.getVotes().add(savedVote);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("User "+user.getUsername()+" voted successfully!");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        message.setText("User "+user.getUsername()+" voted for "+party.getName()+" at "+vote.getVotedAt().format(dateTimeFormatter)+" time!");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.warn("There was an issue sending email to "+user.getEmail()+" due to "+e.getMessage()+"!");
        }

        return savedVote;
    }

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public Vote getVoteById(UUID voteId) {
        return voteRepository.findById(voteId).orElseThrow(() -> new RuntimeException("Vote does not exist!"));
    }
}
