package com.weather.api.services;

import com.weather.api.entity.City;
import com.weather.api.entity.Weather;
import com.weather.api.repository.WeatherRepo;
import com.weather.api.utils.Settings;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class WeatherService {

    private final WeatherRepo weatherRepo;

    public WeatherService(WeatherRepo weatherRepo) {
        this.weatherRepo = weatherRepo;
    }

    public Weather updateTodaysCityWeather(City city, String degree) {

        Date currentDate = new Date(Timestamp.valueOf(LocalDateTime.now()).getTime());

        Optional<Weather> optionalWeather = weatherRepo.findByCityAndWeatherDate(city, currentDate);
        if (optionalWeather.isPresent()) {
            Weather weather = optionalWeather.get();
            weather.setDegree(degree);
            weather.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
            weather.setChangeUser(Settings.getCurrentUser());
            weatherRepo.save(weather);

            return weather;
        }

        Weather weather = new Weather();
        weather.setId(UUID.randomUUID());
        weather.setCity(city);
        weather.setDegree(degree);
        weather.setWeatherDate(currentDate);
        weather.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
        weather.setCreateUser(Settings.getCurrentUser());
        weatherRepo.save(weather);

        return weather;
    }

    public Weather getTodaysCityWeather(City city) {
        Date currentDate = new Date(Timestamp.valueOf(LocalDateTime.now()).getTime());

        return weatherRepo.findByCityAndWeatherDate(city, currentDate)
                .orElse(null);
    }
}
