package app;

import app.exception.PartyCreationDeniedException;
import app.party.model.Party;
import app.party.service.PartyService;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.vote.model.Vote;
import app.vote.service.VoteService;
import app.web.dto.CreatePartyRequest;
import app.web.dto.RegisterRequest;
import app.web.dto.VoteRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class CreateNewVoteITest {

    @Autowired
    private UserService userService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteService voteService;

    @Mock
    private MailSender mailSender;

    @Test
    void testCreateNewVote_happyPath(){
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Ivan123")
                .email("ivanat@abv.bg")
                .password("123123")
                .confirmPassword("123123")
                .build();
        User user = userService.registerUser(registerRequest);
        user.setUserRole(UserRole.ADMIN);
        userRepository.save(user);

        CreatePartyRequest createPartyRequest = CreatePartyRequest.builder()
                .name("Ima takuv narod")
                .abbreviation("ITN")
                .build();
        Party party = partyService.createNewParty(createPartyRequest, user.getId());

        VoteRequest voteRequest = VoteRequest.builder()
                .partyName("Ima takuv narod")
                .abbreviation("ITN")
                .build();

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        Vote savedVote = voteService.createNewVote(voteRequest, user.getId());
        assertEquals("Ivan123", savedVote.getUser().getUsername());
        assertEquals("Ima takuv narod", savedVote.getParty().getName());
    }

    @Test
    void testCreateNewVoteWithUserWithRoleUserCreatingAParty_returnException(){
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Ivan123")
                .email("ivanat@abv.bg")
                .password("123123")
                .confirmPassword("123123")
                .build();
        User user = userService.registerUser(registerRequest);

        CreatePartyRequest createPartyRequest = CreatePartyRequest.builder()
                .name("Ima takuv narod")
                .abbreviation("ITN")
                .build();
        assertThrows(PartyCreationDeniedException.class, () -> partyService.createNewParty(createPartyRequest, user.getId()));
    }
}
