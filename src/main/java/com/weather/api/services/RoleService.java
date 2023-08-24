package com.weather.api.services;

import com.weather.api.entity.Role;
import com.weather.api.entity.enums.RoleEnum;
import com.weather.api.repository.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Role> generateRoles() {
        if (roleRepo.count() == 0) {
            List<Role> list = new ArrayList<>();
            list.add(new Role(UUID.randomUUID(), "Administrator", RoleEnum.ROLE_ADMIN));
            list.add(new Role(UUID.randomUUID(), "User", RoleEnum.ROLE_USER));

            return roleRepo.saveAll(list);
        }
        return new ArrayList<>();
    }

    public Role getRoleByLabel(RoleEnum label) {
        return roleRepo.getRolesByLabel(label);
    }
}
