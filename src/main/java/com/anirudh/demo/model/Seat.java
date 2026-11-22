package com.anirudh.demo.model;

import jakarta.persistence.*;

@Entity //tells the database the class maps to a database table Seat
public class Seat {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY) //It allows the postgres to create unique ids itself
    private Long id;
     @Column(unique = true, nullable = false)
    private String seatNumber;
    private boolean booked;
     //Optimistic approach to prevent conflicts Version is a number that updates everytime the row updates
     //in the SQL query we will have a condition version = 0, and it will update the moment first person books the
    // ticket and hence fails it for other user if they do so simultaneously
     @Version
    private Long version;
    //Empty constructor because jpa create object through reflection
    public Seat(){ //Reflection is java's ability to inspect and use classes at runtime without knowing about them at compile time

    }
    public Long getVersion() {
        return version;
    }
    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }
}
