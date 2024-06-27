package ru.yandex.practicum.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public class User {
    @EqualsAndHashCode.Exclude
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
