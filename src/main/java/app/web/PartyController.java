package app.web;

import app.party.model.Party;
import app.party.service.PartyService;
import app.web.dto.CreatePartyRequest;
import app.web.dto.PartyResponse;
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
public class PartyController {
    private final PartyService partyService;

    @Autowired
    public PartyController(PartyService partyService) {
        this.partyService = partyService;
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
}
