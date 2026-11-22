package com.anirudh.demo;

public class SeatAlreadyBookedException extends RuntimeException{
    public SeatAlreadyBookedException( String seatNumber){
        super("Seat already booked: " + seatNumber);
    }
}
