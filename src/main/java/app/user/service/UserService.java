package app.user.service;

import app.exception.UserAlreadyExistsException;
import app.user.model.User;
import app.user.model.UserRole;
import app.user.repository.UserRepository;
import app.web.dto.RegisterRequest;
import app.web.dto.UpdateUserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(RegisterRequest registerRequest) {
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(registerRequest.getUsername(), registerRequest.getEmail());
        if(optionalUser.isPresent()){
            throw new UserAlreadyExistsException("User with id ["+optionalUser.get().getId()+"] already exist");
        }
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .email(registerRequest.getEmail())
                .userRole(UserRole.USER)
                .isActive(true)
                .build();
        return userRepository.save(user);
    }

    public User getById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(()-> new RuntimeException("No user ith this id"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUserInfo(UUID userId, @Valid UpdateUserRequest updateUserRequest) {
        User user = getById(userId);
        user.setUsername(updateUserRequest.getUsername());
        user.setPassword(updateUserRequest.getPassword());
        user.setEmail(updateUserRequest.getEmail());

        return userRepository.save(user);
    }
}
