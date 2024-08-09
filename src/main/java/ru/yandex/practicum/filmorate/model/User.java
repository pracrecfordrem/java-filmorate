package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data

public class User {
    @EqualsAndHashCode.Exclude
    private Long id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Email должен содержать символ @")
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
    private Set<Long> friendsIds = new HashSet<>();
    private Set<Long> outgoingFriendRequests = new HashSet<>();
    private Set<Long> incomingFriendRequests = new HashSet<>();
}
