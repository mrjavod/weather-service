package com.weather.api.security.jwt;

import com.weather.api.entity.Role;
import com.weather.api.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getLogin(),
                user.getFullName(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRole()),
                true,
                new Date()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Role role) {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(role.getLabel().toString()));
        return list;
    }
}
