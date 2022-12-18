package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.dictionary.UriParam;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.regex.Pattern;

public class FilmorateUtils {
    public static int validateParseInt(String value, UriParam uriParam) {
        Pattern pattern = Pattern.compile("^-?\\d+$");
        if (value == null || !pattern.matcher(value).matches()) {
            throw new ValidationException(String.format("%s не валидное: %s", uriParam.getLabel(), value));
        } else {
            return Integer.parseInt(value);
        }
    }
}
