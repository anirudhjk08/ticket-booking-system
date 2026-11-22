package com.anirudh.demo;

public class SeatNotFoundException extends RuntimeException{
    public SeatNotFoundException(Long id){
        super("Seat Not Found with id:"+id);
    }
}
