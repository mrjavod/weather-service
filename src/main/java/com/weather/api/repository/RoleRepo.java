package com.weather.api.repository;

import com.weather.api.entity.Role;
import com.weather.api.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {

    Role getRolesByLabel(RoleEnum label);

}
