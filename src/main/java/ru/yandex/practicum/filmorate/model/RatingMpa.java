package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingMpa {
    int id;
    String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String description;
}
