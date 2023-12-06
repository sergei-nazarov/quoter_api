package ru.sergeiproject.quoter.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.sergeiproject.quoter.data.Quote;
import ru.sergeiproject.quoter.data.QuoteRate;
import ru.sergeiproject.quoter.data.User;
import ru.sergeiproject.quoter.data.UserRegistrationDto;
import ru.sergeiproject.quoter.security.CustomUserPrincipal;
import ru.sergeiproject.quoter.services.QuoteService;
import ru.sergeiproject.quoter.services.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@Tag(name = "Current user", description = "get current user info/quotes/votes")
public class UserController {


    final private UserService userService;
    final private QuoteService quoteService;


    public UserController(UserService userService, QuoteService quoteService) {
        this.userService = userService;
        this.quoteService = quoteService;
    }

    /**
     * Register new user
     */
    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public User registration(@Valid @RequestBody UserRegistrationDto userDto) {
        try {
            return userService.saveUser(userDto);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Get current user info
     */
    @GetMapping("/")
    @RolesAllowed({"USER"})
    @Operation(summary = "Get current user info")
    User currentUser(@AuthenticationPrincipal CustomUserPrincipal userDetails) {
        return userDetails.getUser();
    }

    /**
     * Get current user quotes
     */
    @GetMapping("/quotes")
    @RolesAllowed({"USER"})
    @Operation(summary = "Get current user quotes")
    Page<Quote> getCurrentUsersQuotes(@AuthenticationPrincipal CustomUserPrincipal userDetails,
                                      @PageableDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
        return quoteService.getQuotesByUser(userDetails.getUser(), pageable);
    }

    /**
     * Get current user votes
     */
    @GetMapping(value = "/votes")
    @RolesAllowed({"USER"})
    @Operation(summary = "Get current user votes")
    Page<QuoteRate> getCurrentUserLastVotes(@AuthenticationPrincipal CustomUserPrincipal userDetails,
                                            @PageableDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return quoteService.getUserLastVotes(userDetails.getUser(), pageable);
    }


}
