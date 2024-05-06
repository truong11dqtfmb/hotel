package com.dqt.hotel.controller;

import  com.dqt.hotel.dto.request.LoginRequest;
import  com.dqt.hotel.dto.request.RefreshTokenRequest;
import  com.dqt.hotel.dto.request.SignupRequest;
import  com.dqt.hotel.dto.response.JwtResponse;
import  com.dqt.hotel.dto.response.ResponseMessage;
import  com.dqt.hotel.entity.RefreshToken;
import  com.dqt.hotel.repository.RefreshTokenRepository;
import  com.dqt.hotel.security.JwtService;
import  com.dqt.hotel.service.RefreshTokenService;
import  com.dqt.hotel.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("${api.prefix}/auth")

@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUpUser(@RequestBody SignupRequest signUpRequest) {
        try {
            ResponseMessage responseMessage = userService.signUp(signUpRequest);
            if (responseMessage.isStatus()) return new ResponseEntity<>(responseMessage, HttpStatus.OK);
            return ResponseEntity.badRequest().body(responseMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(principal);
        String email = principal.getUsername();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(email);
        return ResponseEntity.ok(new JwtResponse(token, refreshToken.getToken(), email));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refresh = refreshTokenRequest.getRefreshToken();
        Optional<RefreshToken> optional = refreshTokenRepository.findByToken(refresh);
        if (optional.isPresent()) {
            RefreshToken refreshToken = optional.get();
            String username = refreshTokenService.verifyExpiration(refreshToken).getEmail();

            String token = jwtUtils.generateToken(username);
            return ResponseEntity.ok(new JwtResponse(token, refresh, username));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseMessage.error("Refresh token not found"));
        }
    }

    @PostMapping("/log-out")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logoutUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        refreshTokenService.deleteByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ResponseMessage.ok("Log out successfully"));
    }

}
