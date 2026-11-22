package com.anirudh.demo.dto;

import jakarta.validation.constraints.NotBlank;

public class AddSeatRequest {
    public String seatNumber;
    @NotBlank(message = "Seat number is required")
    public AddSeatRequest() {

    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
