package com.weather.api.controller;

import com.weather.api.entity.User;
import com.weather.api.exceptions.CityNotFoundException;
import com.weather.api.exceptions.UserNotFoundException;
import com.weather.api.models.dto.*;
import com.weather.api.models.payload.*;
import com.weather.api.security.jwt.JwtTokenProvider;
import com.weather.api.services.CityService;
import com.weather.api.services.SubscribeService;
import com.weather.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Api")
public class ApiController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final CityService cityService;
    private final SubscribeService subscribeService;

    public ApiController(JwtTokenProvider jwtTokenProvider,
                         UserService userService,
                         CityService cityService,
                         SubscribeService subscribeService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.cityService = cityService;
        this.subscribeService = subscribeService;
    }

    @GetMapping("/user-list")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public List<UserPayload> getUserList() {
        return userService.getUserList();
    }

    @GetMapping("/user-details/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public UserDetailsPayload getUserDetails(@PathVariable("id") UUID id) throws UserNotFoundException {
        return userService.getUserDetails(id);
    }

    @PostMapping("/edit-user")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public UserPayload editUser(@Valid @RequestBody EditUserDto dto) throws UserNotFoundException {
        return userService.editUser(dto);
    }

    @GetMapping("/cities-list")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public List<CityPayload> getCitiesList() {
        return cityService.getCitiesList();
    }

    @PostMapping("/edit-city")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public CityPayload editCity(@Valid @RequestBody EditCityDto dto) throws CityNotFoundException {
        return cityService.editCity(dto);
    }

    @PostMapping("/update-city-weather")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public CityWeatherPayload updateCityWeather(@Valid @RequestBody UpdateCityWeatherDto dto) throws CityNotFoundException {
        return cityService.updateCityWeather(dto);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginPayload> register(@Valid @RequestBody CreateUserDto dto) {
        User user = userService.createUser(dto);

        String token = jwtTokenProvider.generateAccessToken(user);

        return ResponseEntity.ok(
                new LoginPayload(token,
                        new UserPayload(
                                user.getId(),
                                user.getLogin(),
                                user.getFullName(),
                                user.getRole().getLabel(),
                                user.getRole().getName())));
    }

    @PostMapping("/subscribe-to-city")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_USER")
    public void subscribeToCity(@Valid @RequestBody SubscribeToCityDto dto) throws CityNotFoundException {
        subscribeService.subscribeToCity(dto);
    }

    @PostMapping("/get-subscriptions")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_USER")
    public List<CityWeatherPayload> getSubscriptions() {
        return subscribeService.getSubscriptions();
    }

}
