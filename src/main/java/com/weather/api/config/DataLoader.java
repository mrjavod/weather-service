package com.weather.api.config;

import com.weather.api.entity.Role;
import com.weather.api.services.RoleService;
import com.weather.api.services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    public DataLoader(UserService userService,
                      RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String status;

    @Override
    public void run(String... args) {

        List<Role> roles = roleService.generateRoles();

        userService.createDefaultUser(roles);
    }
}
