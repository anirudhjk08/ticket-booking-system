package com.anirudh.demo.controller;

import com.anirudh.demo.dto.BookingResponse;
import com.anirudh.demo.service.BookingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/seat/{id}/book")
    public BookingResponse bookSeat(@PathVariable Long id, Authentication authentication){
        return bookingService.bookSeat(id,authentication.getName());
    }
    @GetMapping("/bookings/my")
    public List<BookingResponse> myBookings(Authentication authentication){
        return bookingService.getMyBookings(authentication.getName());
    }
}
