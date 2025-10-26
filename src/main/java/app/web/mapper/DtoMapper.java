package app.web.mapper;

import app.party.model.Party;
import app.user.model.User;
import app.vote.model.Vote;
import app.web.dto.PartyResponse;
import app.web.dto.RegisterResponse;
import app.web.dto.UserResponse;
import app.web.dto.VoteResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static RegisterResponse fromUser(User user){
        return RegisterResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public static UserResponse fromUser2(User user){
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .userRole(user.getUserRole())
                .build();
    }

    public static PartyResponse fromParty(Party party) {
        return PartyResponse.builder()
                .id(party.getId())
                .name(party.getName())
                .abbreviation(party.getAbbreviation())
                .numberOfVotes(party.getVotes().size())
                .build();
    }

    public static VoteResponse fromVote(Vote vote) {
        return VoteResponse.builder()
                .username(vote.getUser().getUsername())
                .partyName(vote.getParty().getName())
                .votedAt(vote.getVotedAt())
                .build();
    }
}
