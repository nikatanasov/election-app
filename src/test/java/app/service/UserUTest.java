package app.service;

import app.exception.UserAlreadyExistsException;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.RegisterRequest;
import app.web.dto.UpdateUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserUTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_happyPath(){
        /*User user = User.builder()
                .id(UUID.randomUUID())
                .username("nikatanasov22")
                .password("123123")
                .email("nikolay@abv.bg")
                .userRole(UserRole.USER)
                .isActive(true)
                .build();*/
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("nikatanasov22")
                .email("nikolay@abv.bg")
                .password("123123")
                .confirmPassword("123123")
                .build();

        when(userRepository.findByUsernameOrEmail(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        User user = userService.registerUser(registerRequest);
        assertEquals(user.getUsername(), registerRequest.getUsername());
        assertEquals(user.getEmail(), registerRequest.getEmail());
        assertEquals(user.getPassword(), registerRequest.getPassword());
        assertEquals(user.getPassword(), registerRequest.getConfirmPassword());

        verify(userRepository, times(1)).findByUsernameOrEmail(any(String.class), any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_wrongPath(){
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("nikatanasov22")
                .password("123123")
                .email("nikolay@abv.bg")
                .userRole(UserRole.USER)
                .isActive(true)
                .build();

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("nikatanasov22")
                .email("nikolay@abv.bg")
                .password("123123")
                .confirmPassword("123123")
                .build();

        when(userRepository.findByUsernameOrEmail(any(String.class), any(String.class))).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(registerRequest));

        verify(userRepository, times(1)).findByUsernameOrEmail(anyString(), anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserInfo_happyPath(){
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("nikatanasov22")
                .password("123123")
                .email("nikolay@abv.bg")
                .userRole(UserRole.USER)
                .isActive(true)
                .build();

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .username("evatanasova")
                .password("321321")
                .email("evatanasova@abv.bg")
                .build();

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        User updatedUser = userService.updateUserInfo(userId, updateUserRequest);
        assertEquals(updatedUser.getUsername(), updateUserRequest.getUsername());
        assertEquals(updatedUser.getPassword(), updateUserRequest.getPassword());
        assertEquals(updatedUser.getEmail(), updateUserRequest.getEmail());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any());
    }
}
