package com.weather.api.utils;

import com.weather.api.entity.User;
import com.weather.api.entity.enums.LangEnum;

public class Settings {

    private static User currentUser;
    private static LangEnum lang;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Settings.currentUser = currentUser;
    }

    public static LangEnum getLang() {
        return lang;
    }

    public static void setLang(LangEnum lang) {
        Settings.lang = lang;
    }
}
