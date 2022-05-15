package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

@Data
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    @NotBlank
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @DurationMin (nanos = 1)
    private Duration duration;

}
