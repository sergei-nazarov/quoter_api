package ru.sergeiproject.quoter.controllers;


import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.sergeiproject.quoter.data.CustomUserPrincipal;
import ru.sergeiproject.quoter.data.Quote;
import ru.sergeiproject.quoter.data.QuoteDto;
import ru.sergeiproject.quoter.data.User;
import ru.sergeiproject.quoter.errors.QuoteNotFoundException;
import ru.sergeiproject.quoter.services.QuoteService;
import ru.sergeiproject.quoter.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
public class QuoteRestController {

    final private QuoteService quoteService;
    final private UserService userService;


    @Autowired
    public QuoteRestController(QuoteService quoteService, UserService userService) {
        this.quoteService = quoteService;
        this.userService = userService;
    }

    @GetMapping("/secret")
    @RolesAllowed({"USER"})
    String qwe() {
        return "ready";
    }

    @GetMapping("/quote/{id}")
    Quote getQuote(@PathVariable("id") Long id) {
        Optional<Quote> quote = quoteService.getQuoteById(id);
        if (quote.isPresent()) {
            return quote.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quote with id " + id + " not found");
        }
    }

    @DeleteMapping(value = "/quote/{id}")
    @RolesAllowed({"USER"})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    void deleteQuote(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserPrincipal userDetails) {
        boolean success = quoteService.deleteQuote(id, userDetails.getUser());
        if (!success) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of the quote");
        }
    }

    @PutMapping(value = "/quote/{id}")
    @RolesAllowed({"USER"})
    Quote updateQuote(@PathVariable("id") Long id, @RequestBody QuoteDto quoteDto,
                      @AuthenticationPrincipal CustomUserPrincipal userDetails) {
        try {
            return quoteService.updateQuote(id, quoteDto, userDetails.getUser());
        } catch (QuoteNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quote with id " + id + " not found");
        } catch (IllegalAccessException e) { //todo better move to controller advice
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of the quote");
        }
    }

    @PostMapping("/quote")
    @RolesAllowed({"USER"})
    Quote createQuote(@RequestBody @Valid QuoteDto quoteDto, @AuthenticationPrincipal CustomUserPrincipal userDetails) {
        return quoteService.saveQuote(quoteDto, userDetails.getUser());
    }

    @GetMapping("/user/quotes")
    @RolesAllowed({"USER"})
    List<Quote> getUsersQuotes(@AuthenticationPrincipal CustomUserPrincipal userDetails) {
        return quoteService.getQuotesByUser(userDetails.getUser());
    }

    @GetMapping("/user")
    @RolesAllowed({"USER"})
    User currentUser(@AuthenticationPrincipal CustomUserPrincipal userDetails) {
        return userDetails.getUser();
    }

    @GetMapping("/users/{username}")
    User getUserInfoByUsername(@PathVariable("username") String username) {
        Optional<User> userInfo = userService.getUserInfo(username);
        if (userInfo.isPresent()) {
            return userInfo.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username " + username + " not exists");
        }
    }

    @GetMapping("/users/{username}/quotes")
    List<Quote> getQuotesByUsername(@PathVariable("username") String username) {
        return quoteService.getQuotesByUser(username);
    }

    @GetMapping("/quotes/random")
    Quote getRandomQuote(){
        return quoteService.getRandomQuote();
    }
}
