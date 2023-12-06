package ru.sergeiproject.quoter.controllers;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;
import ru.sergeiproject.quoter.data.*;
import ru.sergeiproject.quoter.security.CustomUserPrincipal;
import ru.sergeiproject.quoter.errors.QuoteNotFoundException;
import ru.sergeiproject.quoter.services.QuoteService;
import ru.sergeiproject.quoter.services.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;

@Tag(name = "Quoter", description = "get quote info in different ways")
@RestController
public class QuoteRestController {

    final private QuoteService quoteService;
    final private UserService userService;


    @Autowired
    public QuoteRestController(QuoteService quoteService, UserService userService) {
        this.quoteService = quoteService;
        this.userService = userService;
    }

    @GetMapping
    @Hidden
    RedirectView mainPage() {
        return new RedirectView("/swagger-ui/index.html");
    }

    /**
     * Get quote by id
     */
    @GetMapping("/quote/{id}")
    @Operation(summary = "Get quote by id")
    Quote getQuote(@PathVariable("id") Long id) {
        Optional<Quote> quote = quoteService.getQuoteById(id);
        if (quote.isPresent()) {
            return quote.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quote with id " + id + " not found");
        }
    }

    /**
     * Delete quote by id. Allowed only if current user = quote author
     */
    @DeleteMapping(value = "/quote/{id}")
    @RolesAllowed({"USER"})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete quote by id")
    void deleteQuote(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserPrincipal userDetails) {
        boolean success = quoteService.deleteQuote(id, userDetails.getUser());
        if (!success) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of the quote");
        }
    }

    /**
     * Update quote by id. Allowed only if current user = quote author
     */
    @PutMapping(value = "/quote/{id}")
    @RolesAllowed({"USER"})
    @Operation(summary = "Update quote by id")
    Quote updateQuote(@PathVariable("id") Long id, @RequestBody QuoteDto quoteDto,
                      @AuthenticationPrincipal CustomUserPrincipal userDetails) {
        try {
            return quoteService.updateQuote(id, quoteDto, userDetails.getUser());
        } catch (QuoteNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quote with id " + id + "doesn't found");
        } catch (IllegalAccessException e) { //todo better move to controller advice
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of the quote");
        }
    }

    /**
     * Create new quote
     */
    @PostMapping("/quote")
    @RolesAllowed({"USER"})
    @Operation(summary = "Create new quote")
    Quote createQuote(@RequestBody @Valid QuoteDto quoteDto, @AuthenticationPrincipal CustomUserPrincipal userDetails) {
        return quoteService.saveQuote(quoteDto, userDetails.getUser());
    }

    /**
     * Get quotes of a user by username
     */
    @GetMapping("/users/{username}/quotes")
    @Operation(summary = "Get quotes of a user by username")
    Page<Quote> getQuotesByUsername(@PathVariable("username") String username,
                                    @PageableDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
        return quoteService.getQuotesByUser(username, pageable);
    }

    /**
     * Get random quote
     */
    @GetMapping("/quotes/random")
    @Operation(summary = "Get random quote")
    Quote getRandomQuote() {
        return quoteService.getRandomQuote();
    }

    /**
     * Get quotes in different order (last, top, worst)
     */
    @GetMapping("/quotes")
    @Operation(summary = "Get quotes in different order (last, top, worst)")
    Page<Quote> getLastQuotes(@PageableDefault(sort = "created",
            direction = Sort.Direction.DESC) Pageable pageable) {
        return quoteService.getAllQuotes(pageable);
    }

    /**
     * Rate quote
     *
     * @param grade 1 - positive, -1 - negative, 0 - delete rate
     */
    @PostMapping(value = "/quote/{id}/rate")
    @RolesAllowed({"USER"})
    @Operation(summary = "Rate quote. 1 - positive, -1 - negative, 0 - delete rate")
    ResponseEntity<String> rateQuote(@PathVariable("id") Long id,
                                     @RequestParam(name = "grade") int grade,
                                     @AuthenticationPrincipal CustomUserPrincipal userDetails) {
        if (Math.abs(grade) - 1 != 0 && grade != 0) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Only 1, 0, -1 are available as a grade");
        }
        boolean success = quoteService.rateQuote(id, userDetails.getUser(), grade);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quote not found");
        }
    }

    /**
     * Get data for the "time-rating" graph
     */
    @GetMapping(value = "/quote/{id}/graph")
    @Operation(summary = "Get data for the \"time-rating\" graph")
    List<Point<LocalDateTime, Long>> rateQuote(@PathVariable("id") Long id) {
        return quoteService.getQuoteRatesGraph(id);
    }

    /**
     * Get user info by username
     */
    @GetMapping("/users/{username}")
    @Operation(summary = "Get user info by username")
    User getUserInfoByUsername(@PathVariable("username") String username) {
        Optional<User> userInfo = userService.getUserInfo(username);
        if (userInfo.isPresent()) {
            return userInfo.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username " + username + " doesn't exist");
        }
    }
}
