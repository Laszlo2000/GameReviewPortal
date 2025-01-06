package com.laszlo.gamereviewportal.controller;

import com.laszlo.gamereviewportal.entity.UserEntity;
import com.laszlo.gamereviewportal.repository.UserRepository;
import com.laszlo.gamereviewportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/current-user")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Map<String, Object>> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
        }

        String username = authentication.getName();
        UserEntity currentUser = userService.getByUsername(username);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }

        // Return the userId and roleId
        Map<String, Object> response = Map.of(
                "userId", currentUser.getId(),
                "username", currentUser.getUsername(),
                "roleId", currentUser.getRole().getId()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-role")
    @PreAuthorize("hasAuthority('admin')")
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
    @PreAuthorize("hasAnyAuthority('admin')")
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

    @PutMapping("/{id}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable Long id,
            @RequestBody Map<String, Long> request,
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal
    ) {
        Long newRoleId = request.get("roleId");

        // Az aktuális felhasználó lekérése
        UserEntity currentUser = userService.getByUsername(principal.getUsername());
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current user not found");
        }

        // Csak SuperAdmin módosíthat szerepköröket
        if (currentUser.getRole().getId() != 3) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only SuperAdmins can modify roles!");
        }

        // Célzott felhasználó lekérése
        UserEntity targetUser = userService.getUserById(id);
        if (targetUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Target user not found");
        }

        // A szerepkör frissítése
        userService.updateUserRole(id, newRoleId);
        return ResponseEntity.ok("Role updated successfully");
    }

}