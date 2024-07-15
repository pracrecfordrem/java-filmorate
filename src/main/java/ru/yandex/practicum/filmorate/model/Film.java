package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film implements Comparator<Film> {
    static final LocalDate MIN_DATE = LocalDate.of(1895,12,28);
    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Длина описания не может быть более 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма не может быть отрицательным значением.")
    private Long duration;
    private Set<Long> likes = new HashSet<>();

    @Override
    public int compare(Film o1, Film o2) {
        return o2.getLikes().size() - o1.getLikes().size();
    }
}
