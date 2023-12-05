package ru.sergeiproject.quoter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sergeiproject.quoter.data.Quote;
import ru.sergeiproject.quoter.data.QuoteDto;
import ru.sergeiproject.quoter.data.User;
import ru.sergeiproject.quoter.errors.QuoteNotFoundException;
import ru.sergeiproject.quoter.repositories.QuoteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class QuoteService {
    final private QuoteRepository quoteRepository;

    @Autowired
    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Quote saveQuote(QuoteDto quoteDto, User user) {
        Quote quote = Quote.builder()
                .author(user)
                .content(quoteDto.getContent())
                .created(LocalDateTime.now())
                .build();
        return quoteRepository.save(quote);
    }

    public Optional<Quote> getQuoteById(Long id) {
        return quoteRepository.findById(id);
    }

    public boolean deleteQuote(Long quoteId, User user) {
        Optional<Quote> quote = quoteRepository.findById(quoteId);
        if (quote.isEmpty() || quote.get().getAuthor().equals(user)) {
            quoteRepository.deleteById(quoteId);
            return true;
        }
        return false;
    }

    public Quote updateQuote(Long quoteId, QuoteDto quoteDto, User user)
            throws QuoteNotFoundException, IllegalAccessException {
        Optional<Quote> quoteOptional = quoteRepository.findById(quoteId);
        if (quoteOptional.isEmpty()) {
            throw new QuoteNotFoundException();
        }
        Quote quote = quoteOptional.get();
        if (!quote.getAuthor().equals(user)) {
            throw new IllegalAccessException();
        }
        quote.setContent(quoteDto.getContent());
        quote.setCreated(LocalDateTime.now());
        quoteRepository.save(quote);
        return quote;

    }

    public List<Quote> getQuotesByUser(User user) {
        return quoteRepository.findAllByAuthor(user);
    }

    public List<Quote> getQuotesByUser(String username) {
        return quoteRepository.findAllByAuthor_Username(username);
    }

    public Quote getRandomQuote() {
        long count = quoteRepository.count();
        return quoteRepository.findById(ThreadLocalRandom.current().nextLong(1, count + 1)).get();
    }
}
