package app.web;

import app.user.model.User;
import app.user.model.UserRole;
import app.user.service.UserService;
import app.web.dto.RegisterRequest;
import app.web.dto.UpdateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {

    @MockitoBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postWithBodyToRegisterUser_thenReturnCorrectDtoAndStatusOK() throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Ivan123")
                .email("ivan123@abv.bg")
                .password("123123")
                .confirmPassword("123123")
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

        when(userService.registerUser(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(registerRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("userId").value(userId.toString()))
                .andExpect(jsonPath("username").value("Ivan123"))
                .andExpect(jsonPath("email").value("ivan123@abv.bg"));
    }

    @Test
    void putRequestToUpdateUserInfo_thenReturnStatusOKAndUserResponse() throws Exception {
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .username("Ivan123")
                .password("123123")
                .email("ivan123@abv.bg")
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

        when(userService.updateUserInfo(any(UUID.class), any(UpdateUserRequest.class))).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/api/v1/users/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(updateUserRequest));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value(userId.toString()))
                .andExpect(jsonPath("username").value("Ivan123"))
                .andExpect(jsonPath("email").value("ivan123@abv.bg"))
                .andExpect(jsonPath("userRole").value("USER"));
    }

    @Test
    void getRequestToGetUserById_thenReturnStatusOKAndCorrectDtoStructure() throws Exception {
        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .username("Ivan123")
                .password("123123")
                .email("ivan123@abv.bg")
                .userRole(UserRole.USER)
                .isActive(true)
                .build();

        when(userService.getById(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/api/v1/users/{userId}", userId);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value(userId.toString()))
                .andExpect(jsonPath("username").value("Ivan123"))
                .andExpect(jsonPath("email").value("ivan123@abv.bg"))
                .andExpect(jsonPath("userRole").value("USER"));
    }
}
