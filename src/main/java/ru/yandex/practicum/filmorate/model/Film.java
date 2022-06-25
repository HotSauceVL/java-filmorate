package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class Film {
    private long id;
    @NotBlank
    private String title;
    @Size(min = 1, max = 200)
    @NotBlank
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @DurationMin (nanos = 1)
    @JsonFormat(pattern = "SECONDS")
    private Duration duration;
    private Set<Long> likes = new HashSet<>();

    public Film(long id, String title, String description, LocalDate releaseDate, Duration duration, Integer rate,
                String genre, String mpa) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.genre = genre;
        this.mpa = mpa;
    }

    private Integer rate = 0;
    private String genre;
    private String mpa;

    public void addLike(Long userId) {
        likes.add(userId);
        ++rate;
    }

    public void removeLike(Long userId) {
        if (likes.contains(userId)) {
            likes.remove(userId);
            --rate;
        } else
            throw new UserNotFoundException("Пользователь с таким id не ставил лайк этому фильму");
    }

    public long getDuration() {
        return duration.getSeconds();
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
