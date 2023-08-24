package com.weather.api.services;

import com.weather.api.entity.City;
import com.weather.api.entity.User;
import com.weather.api.entity.Weather;
import com.weather.api.exceptions.CityNotFoundException;
import com.weather.api.models.dto.EditCityDto;
import com.weather.api.models.dto.UpdateCityWeatherDto;
import com.weather.api.models.payload.CityPayload;
import com.weather.api.models.payload.CityWeatherPayload;
import com.weather.api.repository.CityRepo;
import com.weather.api.utils.Settings;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CityService {

    private final CityRepo cityRepo;
    private final WeatherService weatherService;
    private final SubscribeService subscribeService;

    public CityService(CityRepo cityRepo,
                       WeatherService weatherService,
                       SubscribeService subscribeService) {
        this.cityRepo = cityRepo;
        this.weatherService = weatherService;
        this.subscribeService = subscribeService;
    }

    public City getCityById(UUID id) throws CityNotFoundException {
        return cityRepo.findById(id)
                .orElseThrow(() -> new CityNotFoundException("City not found"));
    }

    public List<CityPayload> getUserSubscribeCities(User user) {
        List<CityPayload> list = new ArrayList<>();
        subscribeService.getUserSubscribes(user).forEach(e -> {
            City city = e.getCity();
            list.add(new CityPayload(city.getId(), city.getName()));
        });

        return list;
    }

    public List<CityPayload> getCitiesList() {
        List<CityPayload> list = new ArrayList<>();
        cityRepo.findAll().forEach(e -> list.add(
                new CityPayload(
                        e.getId(),
                        e.getName())));
        return list;
    }

    public CityPayload editCity(EditCityDto dto) throws CityNotFoundException {
        City city = cityRepo.findById(dto.getId())
                .orElseThrow(() -> new CityNotFoundException("City not found"));

        city.setName(dto.getName());
        city.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));
        city.setChangeUser(Settings.getCurrentUser());
        city = cityRepo.save(city);

        return new CityPayload(city.getId(), city.getName());
    }

    public CityWeatherPayload updateCityWeather(UpdateCityWeatherDto dto) throws CityNotFoundException {

        City city = cityRepo.findById(dto.getCity_id())
                .orElseThrow(() -> new CityNotFoundException("City not found"));

        Weather weather = weatherService.updateTodaysCityWeather(city, dto.getDegree());

        return new CityWeatherPayload(
                new CityPayload(city.getId(), city.getName()),
                weather.getDegree(),
                weather.getWeatherDate());
    }
}
