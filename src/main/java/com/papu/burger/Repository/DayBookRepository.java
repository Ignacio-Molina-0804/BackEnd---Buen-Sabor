package com.papu.burger.Repository;

import com.papu.burger.Model.DayBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DayBookRepository extends JpaRepository<DayBook, UUID> {
    Optional<DayBook> findByCreatedAt(Date createdAt);
}