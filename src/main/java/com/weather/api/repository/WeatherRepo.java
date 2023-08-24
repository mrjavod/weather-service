package com.weather.api.repository;

import com.weather.api.entity.City;
import com.weather.api.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

public interface WeatherRepo extends JpaRepository<Weather, UUID> {

    Optional<Weather> findByCityAndWeatherDate(City city, Date date);
}
