package com.anirudh.demo.exception;

public class SeatAlreadyExistsException extends RuntimeException {
    public SeatAlreadyExistsException(String seatNumber){
        super("Seat already exists: "+seatNumber);
    }
}
