package com.anirudh.demo.controller;

import com.anirudh.demo.dto.AddSeatRequest;
import com.anirudh.demo.model.Seat;
import com.anirudh.demo.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//RestContoller - It marks class as an HTTP handler and whatever method returns converts it automatically to json
@RestController
@RequestMapping("/api/seats") //sets the base method for every method in the class
public class SeatController {
    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }
    @PostMapping
    public ResponseEntity<Seat> addSeat(@Valid @RequestBody AddSeatRequest request){
      Seat seat = seatService.addSeat(request.getSeatNumber());
      return ResponseEntity.status(HttpStatus.CREATED).body(seat);
    }
    @GetMapping
    public List<Seat> getAllSeats(){
        return seatService.getAllSeats();
    }
    @GetMapping("/available")
    public List<Seat> getAvailableSeats(){
        return seatService.getAvailableSeats();
    }
}
