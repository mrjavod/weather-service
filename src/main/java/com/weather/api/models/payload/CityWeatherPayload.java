package com.weather.api.models.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
public class CityWeatherPayload {
    private CityPayload city;
    private String degree;
    private Date weatherDate;
}
