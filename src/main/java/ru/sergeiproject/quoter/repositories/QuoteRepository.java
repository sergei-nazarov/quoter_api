package ru.sergeiproject.quoter.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.sergeiproject.quoter.data.Quote;
import ru.sergeiproject.quoter.data.Role;
import ru.sergeiproject.quoter.data.User;

import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    List<Quote> findAllByAuthor(User author, Pageable pageable);

    List<Quote> findAllByAuthor_Username(String author_username, Pageable pageable);

    @Modifying
    @Query("update Quote q set q.rating = q.rating + 1 where q=:quote")
    void plusRate(@Param(value = "quote") Quote quote);

    @Modifying
    @Query("update Quote q set q.rating = q.rating - 1 where q=:quote")
    void minusRate(@Param(value = "quote") Quote quote);

}
