package com.weather.api.services;

import com.weather.api.entity.Role;
import com.weather.api.entity.User;
import com.weather.api.entity.enums.RoleEnum;
import com.weather.api.exceptions.UserNotFoundException;
import com.weather.api.models.dto.CreateUserDto;
import com.weather.api.models.dto.EditUserDto;
import com.weather.api.models.dto.PasswordUpdateDto;
import com.weather.api.models.payload.UserDetailsPayload;
import com.weather.api.models.payload.UserPayload;
import com.weather.api.repository.UserRepo;
import com.weather.api.utils.Message;
import com.weather.api.utils.Settings;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final RoleService roleService;
    private final CityService cityService;

    public UserService(UserRepo userRepo,
                       RoleService roleService,
                       CityService cityService) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.cityService = cityService;
    }


    public void createDefaultUser(List<Role> roles) {

        if (userRepo.count() == 0) {
            Role role = roles.stream().filter(e -> e.getLabel().equals(RoleEnum.ROLE_ADMIN))
                    .findFirst().orElse(null);

            User user = new User();
            user.setId(UUID.randomUUID());
            user.setLogin("admin");
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setFullName("Adminov Admin");
            user.setRole(role);
            user.setCreateUser(null);
            user.setChangeUser(null);
            user.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));

            this.userRepo.save(user);
        }
    }

    public User findUserByLogin(String login) throws UserNotFoundException {
        return userRepo.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private UserPayload getUserPayload(User u) {
        return new UserPayload(u.getId(),
                u.getLogin(),
                u.getFullName(),
                u.getRole().getLabel(),
                u.getRole().getName());
    }

    public void updatePassword(PasswordUpdateDto dto) {

        User user = Settings.getCurrentUser();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.INCORRECT_OLD_PASSWORD);
        }

        if (!dto.getNewPassword().equals(dto.getConfirm())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.INCORRECT_PASSWORD_CONFIRM);
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepo.save(user);
    }

    public User createUser(CreateUserDto dto) {

        Optional<User> optionalUser = userRepo.findByLogin(dto.getLogin());

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setLogin(dto.getLogin());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setFullName(dto.getFullName());
        user.setRole(roleService.getRoleByLabel(RoleEnum.ROLE_USER));
        user.setCreateUser(null);
        user.setCreateDate(Timestamp.valueOf(LocalDateTime.now()));

        return userRepo.save(user);
    }

    public List<UserPayload> getUserList() {
        List<UserPayload> list = new ArrayList<>();

        userRepo.findAll().forEach(u -> list.add(getUserPayload(u)));
        return list;
    }

    public UserDetailsPayload getUserDetails(UUID id) throws UserNotFoundException {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new UserDetailsPayload(
                user.getId(),
                user.getLogin(),
                user.getFullName(),
                cityService.getUserSubscribeCities(user));
    }

    public UserPayload editUser(EditUserDto dto) throws UserNotFoundException {
        User user = userRepo.findById(dto.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setLogin(dto.getLogin());
        user.setFullName(dto.getFullName());
        user.setChangeUser(Settings.getCurrentUser());
        user.setChangeDate(Timestamp.valueOf(LocalDateTime.now()));

        return getUserPayload(userRepo.save(user));
    }


}
