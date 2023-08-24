package com.weather.api.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SubscribesPayload {
    private List<CityPayload> cities;
}
