package com.weather.api.repository;

import com.weather.api.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CityRepo extends JpaRepository<City, UUID> {
}
