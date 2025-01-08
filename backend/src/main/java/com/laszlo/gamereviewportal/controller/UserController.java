package com.laszlo.gamereviewportal.controller;

import com.laszlo.gamereviewportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.laszlo.gamereviewportal.entity.UserEntity;
import com.laszlo.gamereviewportal.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository; // Szükséges a role ellenőrzéséhez

    @PostMapping("register")
    public UserEntity register(@RequestBody UserEntity user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserEntity user) {
        return userService.verify(user);
    }

    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }

        String username = authentication.getName();
        UserEntity currentUser = userService.getByUsername(username);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        Map<String, Object> response = Map.of(
                "userId", currentUser.getId(),
                "username", currentUser.getUsername(),
                "roleId", currentUser.getRole().getId()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getrole")
    public ResponseEntity<String> getUserRole(Authentication authentication) {
        // Az aktuális bejelentkezett felhasználó role-ját lekérjük
        UserEntity user = userRepository.findByUsername(authentication.getName());
        if (user != null && user.getRole() != null) {
            return ResponseEntity.ok(user.getRole().getRole()); // Role visszaküldése
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Role not found");
    }

//    @GetMapping("/users")
//    public List<UserEntity> getAllUsers() {
//        return userService.getAllUsers();
//    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
