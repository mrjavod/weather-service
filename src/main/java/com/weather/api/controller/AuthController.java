package com.weather.api.controller;

import com.weather.api.entity.User;
import com.weather.api.exceptions.UserNotFoundException;
import com.weather.api.models.dto.LoginDto;
import com.weather.api.models.dto.PasswordUpdateDto;
import com.weather.api.models.payload.LoginPayload;
import com.weather.api.models.payload.UserPayload;
import com.weather.api.security.jwt.JwtTokenProvider;
import com.weather.api.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginPayload> login(@RequestBody LoginDto loginDto) {

        try {
            String username = loginDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginDto.getPassword()));
            User user = userService.findUserByLogin(username);

            String token = jwtTokenProvider.generateAccessToken(user);

            return ResponseEntity.ok(
                    new LoginPayload(token,
                            new UserPayload(
                                    user.getId(),
                                    user.getLogin(),
                                    user.getFullName(),
                                    user.getRole().getLabel(),
                                    user.getRole().getName())));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/updatePassword")
    public void updatePassword(@Valid @RequestBody PasswordUpdateDto dto) {
        userService.updatePassword(dto);
    }
}
