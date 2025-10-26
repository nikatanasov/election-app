package app.web;

import app.party.model.Party;
import app.user.model.User;
import app.user.model.UserRole;
import app.vote.model.Vote;
import app.vote.service.VoteService;
import app.web.dto.VoteRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VoteController.class)
public class VoteControllerApiTest {

    @MockitoBean
    private VoteService voteService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postRequestToMakeVote_thenReturnStatusCreatedAndCorrectDto() throws Exception {
        VoteRequest voteRequest = VoteRequest.builder()
                .partyName("Ima takuv narod")
                .abbreviation("ITN")
                .build();

        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .username("Ivan123")
                .password("123123")
                .email("ivan123@abv.bg")
                .userRole(UserRole.USER)
                .isActive(true)
                .build();

        Party party = Party.builder()
                .id(UUID.randomUUID())
                .name("Ima takuv narod")
                .abbreviation("ITN")
                .votes(new ArrayList<>())
                .build();

        Vote vote = Vote.builder()
                .id(UUID.randomUUID())
                .user(user)
                .party(party)
                .votedAt(LocalDateTime.now())
                .build();

        when(voteService.createNewVote(any(VoteRequest.class), any(UUID.class))).thenReturn(vote);

        MockHttpServletRequestBuilder request = post("/api/v1/votes")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(voteRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("username").value("Ivan123"))
                .andExpect(jsonPath("partyName").value("Ima takuv narod"))
                .andExpect(jsonPath("votedAt").isNotEmpty());
    }

    @Test
    void getRequestToGetVoteById_returnStatusOKAndCorrectDto() throws Exception {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("Ivan123")
                .password("123123")
                .email("ivan123@abv.bg")
                .userRole(UserRole.USER)
                .isActive(true)
                .build();

        Party party = Party.builder()
                .id(UUID.randomUUID())
                .name("Ima takuv narod")
                .abbreviation("ITN")
                .votes(new ArrayList<>())
                .build();

        UUID voteId = UUID.randomUUID();

        LocalDateTime time = LocalDateTime.now();
        Vote vote = Vote.builder()
                .id(voteId)
                .user(user)
                .party(party)
                .votedAt(time)
                .build();

        when(voteService.getVoteById(any())).thenReturn(vote);

        MockHttpServletRequestBuilder request = get("/api/v1/votes/{voteId}", voteId);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value("Ivan123"))
                .andExpect(jsonPath("partyName").value("Ima takuv narod"))
                .andExpect(jsonPath("votedAt").isNotEmpty());
    }
}
