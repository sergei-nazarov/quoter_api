package ru.sergeiproject.quoter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sergeiproject.quoter.data.QuoteRate;
import ru.sergeiproject.quoter.data.Role;
import ru.sergeiproject.quoter.data.User;
import ru.sergeiproject.quoter.repositories.QuoteRatingRepository;
import ru.sergeiproject.quoter.repositories.QuoteRepository;
import ru.sergeiproject.quoter.repositories.RoleRepository;
import ru.sergeiproject.quoter.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class DbInitializer implements CommandLineRunner {

    final QuoteRepository quoteRepository;
    final QuoteRatingRepository quoteRatingRepository;
    final UserRepository userRepository;
    final RoleRepository roleRepository;

    @Autowired
    public DbInitializer(QuoteRepository quoteRepository, QuoteRatingRepository quoteRatingRepository,
                         UserRepository userRepository, RoleRepository roleRepository) {
        this.quoteRepository = quoteRepository;
        this.quoteRatingRepository = quoteRatingRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    @Transactional
    public void run(String... args) {
        initRoles();
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

    void initRoles() {
        List<User> all = userRepository.findAll();
        ArrayList<Role> roles = new ArrayList<>() {{
            add(roleRepository.findByName("ROLE_USER"));
        }};
        for (User user : all) {
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

}
