package com.weather.api.services;

import com.weather.api.entity.City;
import com.weather.api.entity.Subscribe;
import com.weather.api.entity.User;
import com.weather.api.entity.Weather;
import com.weather.api.exceptions.CityNotFoundException;
import com.weather.api.models.dto.SubscribeToCityDto;
import com.weather.api.models.payload.CityPayload;
import com.weather.api.models.payload.CityWeatherPayload;
import com.weather.api.repository.SubscribeRepo;
import com.weather.api.utils.Settings;
import com.weather.api.utils.Utils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SubscribeService {

    private final SubscribeRepo subscribeRepo;
    private final CityService cityService;
    private final WeatherService weatherService;

    public SubscribeService(SubscribeRepo subscribeRepo,
                            @Lazy CityService cityService,
                            @Lazy WeatherService weatherService) {
        this.subscribeRepo = subscribeRepo;
        this.cityService = cityService;
        this.weatherService = weatherService;
    }

    public List<Subscribe> getUserSubscribes(User user) {
        return subscribeRepo.findAllByUser(user);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void subscribeToCity(SubscribeToCityDto dto) throws CityNotFoundException {
        User user = Settings.getCurrentUser();

        List<Subscribe> subscribes = new ArrayList<>();
        for (UUID cityId : dto.getCities()) {
            City city = cityService.getCityById(cityId);

            Subscribe subscribe = new Subscribe(user, city);
            subscribe.setId(UUID.randomUUID());
            subscribe.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
            subscribe.setCreateUser(user);

            subscribes.add(subscribe);
        }

        subscribeRepo.saveAll(subscribes);
    }

    public List<CityWeatherPayload> getSubscriptions() {
        List<CityWeatherPayload> list = new ArrayList<>();
        getUserSubscribes(Settings.getCurrentUser()).forEach(e -> {
            City city = e.getCity();
            Weather weather = weatherService.getTodaysCityWeather(city);

            list.add(new CityWeatherPayload(
                    new CityPayload(city.getId(), city.getName()),
                    !Utils.isEmpty(weather) ? weather.getDegree() : null,
                    !Utils.isEmpty(weather) ? weather.getWeatherDate() : null
            ));
        });
        return list;
    }
}
