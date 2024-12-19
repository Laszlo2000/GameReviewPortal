package com.laszlo.gamereviewportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('admin')")
public class AdminController {

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
        // Jogosultság ellenőrzése
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("admin")); // Vagy "ROLE_ADMIN" ha úgy tárolod
        if (isAdmin) {
            return ResponseEntity.ok("admin");
        } else {
            return ResponseEntity.ok("user");
        }
    }

}