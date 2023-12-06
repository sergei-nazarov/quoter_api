package ru.sergeiproject.quoter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sergeiproject.quoter.data.QuoteRate;
import ru.sergeiproject.quoter.repositories.QuoteRatingRepository;
import ru.sergeiproject.quoter.repositories.QuoteRepository;

import java.util.List;

@Component
public class DbInitializer implements CommandLineRunner {

    final QuoteRepository quoteRepository;
    final QuoteRatingRepository quoteRatingRepository;

    @Autowired
    public DbInitializer(QuoteRepository quoteRepository, QuoteRatingRepository quoteRatingRepository) {
        this.quoteRepository = quoteRepository;
        this.quoteRatingRepository = quoteRatingRepository;
    }


    @Override
    @Transactional
    public void run(String... args) {
        initRating();
    }

    void initRating() {
        List<QuoteRate> allRating = quoteRatingRepository.findAll();
        for (QuoteRate quoteRating : allRating) {
            if (quoteRating.getGrade() == 1) {
                quoteRepository.plusRate(quoteRating.getQuote());
            } else {
                quoteRepository.minusRate(quoteRating.getQuote());
            }
        }
    }
}
