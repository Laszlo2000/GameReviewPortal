package com.laszlo.gamereviewportal.controller;

import com.laszlo.gamereviewportal.entity.UserEntity;
import com.laszlo.gamereviewportal.repository.UserRepository;
import com.laszlo.gamereviewportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('admin')")
public class AdminController {

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/protected")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<String> protectedEndpoint() {
        return ResponseEntity.ok("This is an admin-only endpoint.");
    }

    @GetMapping("/check-role")
    public ResponseEntity<String> checkRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized"); // Token nélkül
        }
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("admin")); // Vagy "ROLE_ADMIN" ha úgy tárolod
        if (isAdmin) {
            return ResponseEntity.ok("admin");
        } else {
            return ResponseEntity.ok("user");
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: User with id " + id + " does not exist.");
        }
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting user: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserEntity updatedUser) {
        return userRepository.findById(id).map(user -> {
            // Csak a nem null értékeket frissítjük
            if (updatedUser.getUsername() != null) {
                user.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                user.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getFirstName() != null) {
                user.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                user.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getDateOfBirth() != null) {
                user.setDateOfBirth(updatedUser.getDateOfBirth());
            }
            if (updatedUser.getCountry() != null) {
                user.setCountry(updatedUser.getCountry());
            }
            userRepository.save(user);
            return ResponseEntity.ok("User updated successfully!");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found."));
    }
}