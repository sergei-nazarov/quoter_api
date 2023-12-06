package ru.sergeiproject.quoter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sergeiproject.quoter.data.*;
import ru.sergeiproject.quoter.errors.QuoteNotFoundException;
import ru.sergeiproject.quoter.repositories.QuoteRatingRepository;
import ru.sergeiproject.quoter.repositories.QuoteRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class QuoteService {
    final private QuoteRepository quoteRepository;
    final private QuoteRatingRepository quoteRatingRepository;


    @Autowired
    public QuoteService(QuoteRepository quoteRepository, QuoteRatingRepository quoteRatingRepository) {
        this.quoteRepository = quoteRepository;
        this.quoteRatingRepository = quoteRatingRepository;
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

    @Transactional
    public boolean deleteQuote(Long quoteId, User user) {
        Optional<Quote> quote = quoteRepository.findById(quoteId);
        if (quote.isEmpty()) {
            return true;
        }
        if (quote.get().getAuthor().equals(user)) {
            quoteRatingRepository.deleteByQuote(quote.get());
            quoteRepository.deleteById(quoteId);
            return true;
        }
        return false;
    }

    @Transactional
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

    public List<Quote> getQuotesByUser(User user, Pageable pageable) {
        return quoteRepository.findAllByAuthor(user, pageable);
    }

    public List<Quote> getQuotesByUser(String username, Pageable pageable) {
        return quoteRepository.findAllByAuthor_Username(username, pageable);
    }

    public Quote getRandomQuote() {
        long count = quoteRepository.count();
        return quoteRepository.findById(ThreadLocalRandom.current().nextLong(1, count + 1)).get();
    }

    public Page<Quote> getAllQuotes(Pageable pageable) {
        return quoteRepository.findAll(pageable);
    }

    @Transactional
    public boolean rateQuote(Long quoteId, User user, int grade) {
        Optional<Quote> quote = quoteRepository.findById(quoteId);
        if (quote.isEmpty()) {
            return false;
        }
        Optional<QuoteRate> quoteRatingOptional = quoteRatingRepository.findByUserAndQuote_Id(user, quoteId);
        QuoteRate quoteRating;
        if (quoteRatingOptional.isPresent()) {
            quoteRating = quoteRatingOptional.get();
            //delete previous rate
            if (quoteRating.getGrade() == 1) {
                quoteRepository.minusRate(quote.get());
            } else {
                quoteRepository.plusRate(quote.get());
            }

            quoteRating.setGrade(grade);
            quoteRating.setCreated(LocalDateTime.now());
        } else {
            quoteRating = QuoteRate.builder().user(user).quote(quote.get()).grade(grade).created(LocalDateTime.now()).build();
        }

        if (grade == 1) {
            quoteRepository.plusRate(quote.get());
            quoteRatingRepository.save(quoteRating);
        } else if (grade == -1) {
            quoteRepository.minusRate(quote.get());
            quoteRatingRepository.save(quoteRating);
        } else if (grade == 0) {
            quoteRatingRepository.delete(quoteRating);
        }

        return true;
    }

    public Page<QuoteRate> getUserLastVotes(User user, Pageable pageable) {
        return quoteRatingRepository.findByUser(user, pageable);
    }

    public List<Point<LocalDateTime, Long>> getQuoteRateGraph(Long quoteId) {
        return quoteRatingRepository.getQuoteRatingGraph(Quote.builder().id(quoteId).build()).stream().distinct().toList();
    }
}
