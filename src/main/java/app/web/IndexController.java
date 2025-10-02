package app.web;

import app.party.model.Party;
import app.party.service.PartyService;
import app.user.model.User;
import app.user.service.UserService;
import app.vote.model.Vote;
import app.vote.service.VoteService;
import app.web.dto.*;
import app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/")
public class IndexController {
    private final UserService userService;
    private final PartyService partyService;
    private final VoteService voteService;

    @Autowired
    public IndexController(UserService userService, PartyService partyService, VoteService voteService) {
        this.userService = userService;
        this.partyService = partyService;
        this.voteService = voteService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        User user = userService.registerUser(registerRequest);
        RegisterResponse registerResponse = DtoMapper.fromUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registerResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<List<RegisterResponse>> getAllUsers(){
        List<RegisterResponse> allUsers = userService.getAllUsers().stream().map(DtoMapper::fromUser).collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allUsers);
    }

    @PostMapping("/parties")
    public ResponseEntity<PartyResponse> createNewParty(@Valid @RequestBody CreatePartyRequest createPartyRequest, @RequestParam(name = "userId") UUID userId){
        Party party = partyService.createNewParty(createPartyRequest, userId);
        PartyResponse partyResponse = DtoMapper.fromParty(party);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(partyResponse);
    }

    @GetMapping("/parties")
    public ResponseEntity<List<PartyResponse>> getAllParties(){
        List<PartyResponse> parties = partyService.getAllParties().stream().map(DtoMapper::fromParty).collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(parties);
    }

    @GetMapping("/parties/{id}")
    public ResponseEntity<PartyResponse> getPartyById(@PathVariable UUID id){
        Party party = partyService.getById(id);
        PartyResponse partyResponse = DtoMapper.fromParty(party);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(partyResponse);
    }

    @PostMapping("/votes")
    public ResponseEntity<VoteResponse> makeVote(@Valid @RequestBody VoteRequest voteRequest, @RequestParam(name = "userId") UUID userId){
        Vote vote = voteService.createNewVote(voteRequest, userId);
        VoteResponse voteResponse = DtoMapper.fromVote(vote);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(voteResponse);
    }

    @GetMapping("/votes")
    public ResponseEntity<List<VoteResponse>> getAllVotes(){
        List<VoteResponse> voteResponses = voteService.getAllVotes().stream().map(DtoMapper::fromVote).collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(voteResponses);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId){
        User user = userService.getById(userId);
        UserResponse userResponse = DtoMapper.fromUser2(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponse);
    }

    @GetMapping("/votes/{voteId}")
    public ResponseEntity<VoteResponse> getVoteById(@PathVariable UUID voteId){
        Vote vote = voteService.getVoteById(voteId);
        VoteResponse voteResponse = DtoMapper.fromVote(vote);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(voteResponse);
    }

}
