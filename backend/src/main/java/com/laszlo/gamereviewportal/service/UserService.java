package com.laszlo.gamereviewportal.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.laszlo.gamereviewportal.entity.RoleEntity;
import com.laszlo.gamereviewportal.entity.UserEntity;
import com.laszlo.gamereviewportal.repository.RoleRepository;
import com.laszlo.gamereviewportal.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public UserEntity register(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        RoleEntity role = roleRepository.findById(2L)
                .orElseThrow(() -> new IllegalArgumentException("Default role not found"));
        user.setRole(role);
        return userRepository.save(user);
    }

    public String verify(UserEntity user) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());

        return "Login failed!";
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getByUsername(String username) {
        return userRepository.findOptionalByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public void updateUserRole(Long userId, Long newRoleId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        RoleEntity newRole = roleRepository.findById(newRoleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        user.setRole(newRole);
        userRepository.save(user);
    }
}
