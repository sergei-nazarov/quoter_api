package ru.sergeiproject.quoter.controllers;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.sergeiproject.quoter.data.User;
import ru.sergeiproject.quoter.data.UserRegistrationDto;
import ru.sergeiproject.quoter.services.UserService;

@RestController
@RequestMapping("/user")
public class RegistrationController {


    final private UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registration(@Valid @RequestBody UserRegistrationDto userDto) {
        try {
            return userService.saveUser(userDto);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
