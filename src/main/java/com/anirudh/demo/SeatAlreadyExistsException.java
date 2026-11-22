package com.anirudh.demo;

public class SeatAlreadyExistsException extends RuntimeException {
    public SeatAlreadyExistsException(String seatNumber){
        super("Seat already exists: "+seatNumber);
    }
}
