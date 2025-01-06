package com.laszlo.gamereviewportal.controller;

import com.laszlo.gamereviewportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.laszlo.gamereviewportal.entity.UserEntity;
import com.laszlo.gamereviewportal.service.UserService;

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
