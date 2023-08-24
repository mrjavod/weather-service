package com.weather.api.exceptions;

public class CityNotFoundException extends Throwable {

    public CityNotFoundException(String message) {
        super(message);
    }
}
