
import com.weather.api.entity.User;
import com.weather.api.exceptions.CityNotFoundException;
import com.weather.api.exceptions.UserNotFoundException;
import com.weather.api.models.dto.*;
import com.weather.api.models.payload.CityPayload;
import com.weather.api.models.payload.CityWeatherPayload;
import com.weather.api.models.payload.UserDetailsPayload;
import com.weather.api.models.payload.UserPayload;
import com.weather.api.repository.UserRepo;
import com.weather.api.security.jwt.JwtTokenProvider;
import com.weather.api.services.CityService;
import com.weather.api.services.RoleService;
import com.weather.api.services.SubscribeService;
import com.weather.api.services.UserService;
import com.weather.api.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AppTests {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleService roleService;

    @Mock
    private CityService cityService;

    @Mock
    private SubscribeService subscribeService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(
                this.userRepo,
                this.roleService,
                this.cityService
        );
    }

    @Test
    User register() {
        CreateUserDto dto = new CreateUserDto("John Doe", "john@gmail.com", "12345");

        User user = userService.createUser(dto);

        String token = jwtTokenProvider.generateAccessToken(user);

        System.out.println(user);
        System.out.println(token);

        return user;
    }

    @Test
    void getUserList() {
        List<UserPayload> list = userService.getUserList();
        System.out.println(Utils.convertToString(list));
    }

    @Test
    void getUserDetails() throws UserNotFoundException {
        UUID id = UUID.fromString("14a1d527-4d19-4106-857c-25b8f0cf068c");
        UserDetailsPayload userDetailsPayload = userService.getUserDetails(id);
        System.out.println(Utils.convertToString(userDetailsPayload));
    }

    @Test
    void editUser() throws UserNotFoundException {
        EditUserDto dto = new EditUserDto(UUID.fromString("14a1d527-4d19-4106-857c-25b8f0cf068c"), "Leo Messi", "messi@gmail.com");
        UserPayload userPayload = userService.editUser(dto);
        System.out.println(Utils.convertToString(userPayload));
    }

    @Test
    List<CityPayload> getCitiesList() {
        List<CityPayload> list = cityService.getCitiesList();
        System.out.println(Utils.convertToString(list));
        return list;
    }

    @Test
    void editCity() throws CityNotFoundException {
        EditCityDto dto = new EditCityDto(UUID.fromString("14a1d527-4d19-4106-857c-25b8f0cf068c"), "Tashkent");
        CityPayload cityPayload = cityService.editCity(dto);
        System.out.println(Utils.convertToString(cityPayload));
    }

    @Test
    void updateCityWeather() throws CityNotFoundException {
        UpdateCityWeatherDto dto = new UpdateCityWeatherDto(UUID.fromString("14a1d527-4d19-4106-857c-25b8f0cf068c"), "+37");
        CityWeatherPayload cityWeatherPayload = cityService.updateCityWeather(dto);
        System.out.println(Utils.convertToString(cityWeatherPayload));
    }

    @Test
    void subscribeToCity() throws CityNotFoundException {
        List<CityPayload> cityPayloadList = getCitiesList();
        List<UUID> list = new ArrayList<>();

        cityPayloadList.forEach(e -> list.add(e.getId()));

        SubscribeToCityDto dto = new SubscribeToCityDto(list);
        subscribeService.subscribeToCity(dto);
    }

    @Test
    void getSubscriptions() {
        List<CityWeatherPayload> list = subscribeService.getSubscriptions();
        System.out.println(Utils.convertToString(list));
    }
}
