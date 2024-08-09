package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    static final LocalDate MIN_DATE = LocalDate.of(1895,12,28);
    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    private String genre;
    private String MPArating;
    @Size(max = 200, message = "Длина описания не может быть более 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма не может быть отрицательным значением.")
    private Long duration;
    private Set<Long> likes = new HashSet<>();
}
