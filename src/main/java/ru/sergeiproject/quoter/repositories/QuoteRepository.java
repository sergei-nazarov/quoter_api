package ru.sergeiproject.quoter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sergeiproject.quoter.data.Quote;
import ru.sergeiproject.quoter.data.Role;
import ru.sergeiproject.quoter.data.User;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    List<Quote> findAllByAuthor(User author);
    List<Quote> findAllByAuthor_Username(String author_username);

}
