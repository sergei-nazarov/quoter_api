package ru.sergeiproject.quoter.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sergeiproject.quoter.data.Point;
import ru.sergeiproject.quoter.data.Quote;
import ru.sergeiproject.quoter.data.QuoteRate;
import ru.sergeiproject.quoter.data.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface QuoteRatingRepository extends JpaRepository<QuoteRate, Long> {

    Optional<QuoteRate> findByUserAndQuote_Id(User user, Long quoteId);

    Page<QuoteRate> findByUser(User user, Pageable pageable);

    void deleteByQuote(Quote quote);

    @Query("SELECT DISTINCT new ru.sergeiproject.quoter.data.Point(q.created, SUM(q.grade) over(ORDER BY created)) from QuoteRate q where q.quote= :quote")
    List<Point<LocalDateTime, Long>> getQuoteRatingGraph(Quote quote);
}
