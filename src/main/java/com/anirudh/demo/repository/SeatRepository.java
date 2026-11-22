package com.anirudh.demo.repository;

import com.anirudh.demo.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat,Long> { //JPA Repository provides many functions implicityly without writing them
    Optional<Seat> findBySeatNumber(String seatNumber); // a derived query whose format is findBy+fieldname
    List<Seat> findByBookedFalse();
     boolean existsBySeatNumber(String seatNumber);
}
