package com.weather.api.repository;

import com.weather.api.entity.Subscribe;
import com.weather.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscribeRepo extends JpaRepository<Subscribe, UUID> {

    List<Subscribe> findAllByUser(User user);
}
