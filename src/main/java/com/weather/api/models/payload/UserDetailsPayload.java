package com.weather.api.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsPayload {
    private UUID id;
    private String username;
    private String fullName;
    private List<CityPayload> subscribes;
}
