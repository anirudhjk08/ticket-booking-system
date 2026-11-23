package com.anirudh.demo.dto;

import java.time.LocalDateTime;

public class BookingResponse {
    private Long id;
    private String seatNumber;
    private String username;
    private LocalDateTime bookedAt;

    public BookingResponse(Long id, String seatNumber, String username, LocalDateTime bookedAt) {
        this.id = id;
        this.seatNumber = seatNumber;
        this.username = username;
        this.bookedAt = bookedAt;
    }

    public Long getId() {
        return id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }
}
