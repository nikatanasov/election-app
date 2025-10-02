package app.web;

import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.RegisterRequest;
import app.web.dto.RegisterResponse;
import app.web.dto.UpdateUserRequest;
import app.web.dto.UserResponse;
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
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
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

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserResponse> updateUserInfo(@PathVariable UUID userId, @Valid @RequestBody UpdateUserRequest updateUserRequest){
        User user = userService.updateUserInfo(userId, updateUserRequest);
        UserResponse userResponse = DtoMapper.fromUser2(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponse);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId){
        User user = userService.getById(userId);
        UserResponse userResponse = DtoMapper.fromUser2(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userResponse);
    }
}
