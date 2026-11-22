package com.anirudh.demo.service;

import com.anirudh.demo.SeatAlreadyBookedException;
import com.anirudh.demo.SeatAlreadyExistsException;
import com.anirudh.demo.SeatNotFoundException;
import com.anirudh.demo.model.Seat;
import com.anirudh.demo.repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
//tells spring create one object of this class and manage. It marks it as business logic layer
@Service
public class SeatService {
    private final SeatRepository seatRepository;


    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat addSeat(String seatNumber){
        if(seatRepository.existsBySeatNumber(seatNumber)){
            throw new SeatAlreadyExistsException(seatNumber);
        }
        Seat seat = new Seat();
        seat.setSeatNumber(seatNumber);;
        seat.setBooked(false);
        return seatRepository.save(seat);
    }
    public List<Seat> getAllSeats(){
        return seatRepository.findAll();
    }
    public List<Seat> getAvailableSeats(){
        return seatRepository.findByBookedFalse();
    }
    //Wraps the whole method as single database transaction either the whole method is executed or rollback
    @Transactional
    public Seat bookSeat(Long seatId){
        Seat seat = seatRepository.findById(seatId).orElseThrow(()->new SeatNotFoundException(seatId));
        if(seat.isBooked()){
            throw new SeatAlreadyBookedException(seat.getSeatNumber());
        }
        seat.setBooked(true);
        return seatRepository.save(seat);
    }
}
