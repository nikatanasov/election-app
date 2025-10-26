package app.web;

import app.party.model.Party;
import app.party.service.PartyService;
import app.web.dto.CreatePartyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartyController.class)
public class PartyControllerApiTest {

    @MockitoBean
    private PartyService partyService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postWithBodyToCreateNewParty_thenReturnCorrectDtoAndStatusCreated() throws Exception {
        CreatePartyRequest createPartyRequest = CreatePartyRequest.builder()
                .name("BSP")
                .abbreviation("Bulgarska soc partiq")
                .build();

        Party party = Party.builder()
                .id(UUID.randomUUID())
                .name("BSP")
                .abbreviation("Bulgarska soc partiq")
                .votes(new ArrayList<>())
                .build();

        when(partyService.createNewParty(any(CreatePartyRequest.class), any(UUID.class))).thenReturn(party);

        MockHttpServletRequestBuilder request = post("/api/v1/parties")
                .param("userId", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(createPartyRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(createPartyRequest.getName()))
                .andExpect(jsonPath("abbreviation").value(createPartyRequest.getAbbreviation()))
                .andExpect(jsonPath("numberOfVotes").value(0));
    }

    @Test
    void getRequestToGetAllParties_thenReturnStatusOkAndCorrectDto() throws Exception {
        Party party1 = Party.builder()
                .id(UUID.randomUUID())
                .name("BSP")
                .abbreviation("b.s.p.")
                .votes(new ArrayList<>())
                .build();

        Party party2 = Party.builder()
                .id(UUID.randomUUID())
                .name("ITN")
                .abbreviation("i.t.n.")
                .votes(new ArrayList<>())
                .build();

        when(partyService.getAllParties()).thenReturn(List.of(party1, party2));

        MockHttpServletRequestBuilder request = get("/api/v1/parties");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("BSP"))
                .andExpect(jsonPath("$[0].abbreviation").value("b.s.p."))
                .andExpect(jsonPath("$[0].numberOfVotes").value(0))
                .andExpect(jsonPath("$[1].id").isNotEmpty())
                .andExpect(jsonPath("$[1].name").value("ITN"))
                .andExpect(jsonPath("$[1].abbreviation").value("i.t.n."))
                .andExpect(jsonPath("$[1].numberOfVotes").value(0));
    }

    @Test
    void getRequestToGetPartyById_thenReturnStatusOKAndValidDto() throws Exception {
        UUID id = UUID.randomUUID();
        Party party = Party.builder()
                .id(id)
                .name("BSP")
                .abbreviation("b.s.p.")
                .votes(new ArrayList<>())
                .build();

        when(partyService.getById(any())).thenReturn(party);

        MockHttpServletRequestBuilder request = get("/api/v1/parties/{id}", id);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id.toString()))
                .andExpect(jsonPath("name").value("BSP"))
                .andExpect(jsonPath("abbreviation").value("b.s.p."))
                .andExpect(jsonPath("numberOfVotes").value(0));
    }
}
