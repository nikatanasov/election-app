package app.party.service;

import app.exception.PartyCreationDeniedException;
import app.exception.PartyDeleteDeniedException;
import app.party.model.Party;
import app.party.repository.PartyRepository;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import app.vote.repository.VoteRepository;
import app.vote.service.VoteService;
import app.web.dto.CreatePartyRequest;
import app.web.dto.PartyResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PartyService {
    private final UserService userService;
    private final PartyRepository partyRepository;
    private final VoteRepository voteRepository;

    @Autowired
    public PartyService(UserService userService, PartyRepository partyRepository, VoteRepository voteRepository) {
        this.userService = userService;
        this.partyRepository = partyRepository;
        this.voteRepository = voteRepository;
    }

    public Party createNewParty(CreatePartyRequest createPartyRequest, UUID userId) {
        User user = userService.getById(userId);
        if(user.getUserRole() == UserRole.USER){
            throw new PartyCreationDeniedException();
        }

        Party party = Party.builder()
                .name(createPartyRequest.getName())
                .abbreviation(createPartyRequest.getAbbreviation())
                .votes(new ArrayList<>())
                .build();

        return partyRepository.save(party);
    }

    public List<Party> getAllParties() {
        return partyRepository.findAll();
    }

    public Party getByName(String partyName) {
        return partyRepository.findByName(partyName).orElseThrow(() -> new RuntimeException("No such party!"));
    }

    public Party getById(UUID id) {
        return partyRepository.findById(id).orElseThrow(() -> new RuntimeException("No such party!"));
    }
}
