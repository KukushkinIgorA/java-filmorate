package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingMpa ratingMpa = (RatingMpa) o;
        return id == ratingMpa.id && name.equals(ratingMpa.name) && Objects.equals(description, ratingMpa.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
