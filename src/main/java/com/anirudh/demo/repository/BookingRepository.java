package com.anirudh.demo.repository;

import com.anirudh.demo.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    public List<Booking> findByUserUsername(String username);
}
