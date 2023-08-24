package com.weather.api.security;

import com.weather.api.entity.User;
import com.weather.api.exceptions.UserNotFoundException;
import com.weather.api.security.jwt.JwtUserFactory;
import com.weather.api.services.UserService;
import com.weather.api.utils.Settings;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            User user = userService.findUserByLogin(login);

            Settings.setCurrentUser(user);
            return JwtUserFactory.create(user);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("User with username: " + login + " not found");
        }
    }
}
