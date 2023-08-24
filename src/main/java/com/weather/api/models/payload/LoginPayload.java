package com.weather.api.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginPayload {

    private String accessToken;
    private UserPayload user;
}
