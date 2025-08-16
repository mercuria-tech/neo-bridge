package com.neobridge.auth.service;

import com.neobridge.auth.dto.LoginRequest;
import com.neobridge.auth.dto.LoginResponse;
import com.neobridge.auth.dto.UserRegistrationRequest;
import com.neobridge.auth.entity.Role;
import com.neobridge.auth.entity.User;
import com.neobridge.auth.exception.AuthenticationException;
import com.neobridge.auth.exception.UserAlreadyExistsException;
import com.neobridge.auth.repository.RoleRepository;
import com.neobridge.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

/**
 * Authentication service for user registration, login, and management.
 * Handles user authentication and authorization in the NeoBridge platform.
 */
@Service
@Transactional
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Register a new user in the system.
     */
    public User registerUser(UserRegistrationRequest request) {
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        if (request.getPhone() != null && userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new UserAlreadyExistsException("User with phone " + request.getPhone() + " already exists");
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setNationality(request.getNationality());
        user.setCountryOfResidence(request.getCountryOfResidence());
        user.setAddressLine1(request.getAddressLine1());
        user.setAddressLine2(request.getAddressLine2());
        user.setCity(request.getCity());
        user.setState(request.getState());
        user.setPostalCode(request.getPostalCode());

        // Assign default USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found"));
        user.setRoles(Set.of(userRole));

        // Save user
        User savedUser = userRepository.save(user);

        // TODO: Send verification email
        // TODO: Publish user created event to Kafka

        return savedUser;
    }

    /**
     * Authenticate user and return JWT tokens.
     */
    public LoginResponse login(LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Check if user is active
            if (!user.isEnabled()) {
                throw new AuthenticationException("User account is not active");
            }

            // Update last login time
            user.setLastLoginAt(Instant.now());
            userRepository.save(user);

            // Generate JWT tokens
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            // TODO: Publish user login event to Kafka

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtService.getTokenExpirationTime())
                    .refreshExpiresIn(jwtService.getRefreshTokenExpirationTime())
                    .user(UserResponse.fromUser(user))
                    .build();

        } catch (Exception e) {
            throw new AuthenticationException("Invalid email or password");
        }
    }

    /**
     * Refresh access token using refresh token.
     */
    public LoginResponse refreshToken(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            throw new AuthenticationException("Invalid refresh token");
        }

        String email = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = loadUserByUsername(email);

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getTokenExpirationTime())
                .refreshExpiresIn(jwtService.getRefreshTokenExpirationTime())
                .build();
    }

    /**
     * Load user by username (email) for Spring Security.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * Get user by email.
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get user by ID.
     */
    public Optional<User> getUserById(String id) {
        return userRepository.findById(java.util.UUID.fromString(id));
    }

    /**
     * Update user profile.
     */
    public User updateUserProfile(String userId, UserRegistrationRequest request) {
        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Update allowed fields
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getDateOfBirth() != null) {
            user.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getNationality() != null) {
            user.setNationality(request.getNationality());
        }
        if (request.getCountryOfResidence() != null) {
            user.setCountryOfResidence(request.getCountryOfResidence());
        }
        if (request.getAddressLine1() != null) {
            user.setAddressLine1(request.getAddressLine1());
        }
        if (request.getAddressLine2() != null) {
            user.setAddressLine2(request.getAddressLine2());
        }
        if (request.getCity() != null) {
            user.setCity(request.getCity());
        }
        if (request.getState() != null) {
            user.setState(request.getState());
        }
        if (request.getPostalCode() != null) {
            user.setPostalCode(request.getPostalCode());
        }

        return userRepository.save(user);
    }

    /**
     * Change user password.
     */
    public void changePassword(String userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new AuthenticationException("Current password is incorrect");
        }

        // Update password
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // TODO: Publish password change event to Kafka
    }

    /**
     * Enable/disable two-factor authentication.
     */
    public void toggleTwoFactorAuthentication(String userId, boolean enabled) {
        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setTwoFactorEnabled(enabled);
        userRepository.save(user);

        // TODO: Publish 2FA toggle event to Kafka
    }

    /**
     * Verify user email.
     */
    public void verifyEmail(String userId) {
        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEmailVerified(true);
        if (user.getUserStatus() == User.UserStatus.PENDING) {
            user.setUserStatus(User.UserStatus.ACTIVE);
        }
        userRepository.save(user);

        // TODO: Publish email verification event to Kafka
    }

    /**
     * Verify user phone.
     */
    public void verifyPhone(String userId) {
        User user = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPhoneVerified(true);
        userRepository.save(user);

        // TODO: Publish phone verification event to Kafka
    }
}
