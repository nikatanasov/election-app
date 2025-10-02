package app.web;

import app.vote.model.Vote;
import app.vote.service.VoteService;
import app.web.dto.VoteRequest;
import app.web.dto.VoteResponse;
import app.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/")
public class VoteController {
    private final VoteService voteService;

    @Autowired
    public VoteController(VoteService voteService) {
        this.voteService = voteService;
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

    @GetMapping("/votes/{voteId}")
    public ResponseEntity<VoteResponse> getVoteById(@PathVariable UUID voteId){
        Vote vote = voteService.getVoteById(voteId);
        VoteResponse voteResponse = DtoMapper.fromVote(vote);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(voteResponse);
    }
}
