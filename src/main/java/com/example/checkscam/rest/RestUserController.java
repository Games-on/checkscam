package com.example.checkscam.rest;

import com.example.checkscam.dto.ResCreateUserDTO;
import com.example.checkscam.entity.User;
import com.example.checkscam.response.CheckScamResponse;
import com.example.checkscam.service.UserService;
import com.example.checkscam.exception.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RestUserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping()
    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody User postManUser) {
        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
        postManUser.setPassword(hashPassword);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.handleCreateUser(postManUser));
    }

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<String> handleIdException(IdInvalidException idException) {
        return ResponseEntity.badRequest().body(idException.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id)
            throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("Id khong lon hown 1501");
        }

        this.userService.handleDeleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message","Delete user successfully");
        return ResponseEntity.ok(response);
        // return ResponseEntity.status(HttpStatus.OK).body("ericUser");
    }

    // fetch user by id
    @GetMapping("/{id}")
    public CheckScamResponse<User> getUserById(@PathVariable("id") long id) {
        return new CheckScamResponse<>(this.userService.fetchUserById(id));
    }

    // fetch all users
    @GetMapping()
    public ResponseEntity<List<User>> getAllUser() {
        // return ResponseEntity.ok(this.userService.fetchAllUser());
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User ericUser = this.userService.handleUpdateUser(user);
        return ResponseEntity.ok(ericUser);
    }

}