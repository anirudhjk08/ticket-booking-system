package com.anirudh.demo.service;

import com.anirudh.demo.dto.BookingResponse;
import com.anirudh.demo.exception.InvalidCredentialsException;
import com.anirudh.demo.exception.SeatAlreadyBookedException;
import com.anirudh.demo.exception.SeatNotFoundException;
import com.anirudh.demo.model.Booking;
import com.anirudh.demo.model.Seat;
import com.anirudh.demo.model.User;
import com.anirudh.demo.repository.BookingRepository;
import com.anirudh.demo.repository.SeatRepository;
import com.anirudh.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, SeatRepository seatRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public BookingResponse bookSeat(Long seatId, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(()->new SeatNotFoundException(seatId));
        if(seat.isBooked()){
            throw new SeatAlreadyBookedException(seat.getSeatNumber());

        }
        seat.setBooked(true);
        seatRepository.save(seat);
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSeat(seat);
        booking.setBookedAt(LocalDateTime.now());
        bookingRepository.save(booking);
        return toResponse(booking);
    }
    @Transactional
    public List<BookingResponse> getMyBookings(String username){
        return bookingRepository.findByUserUsername(username).stream().map(this::toResponse)
        .toList();
    }

    private BookingResponse toResponse(Booking b){
        return new BookingResponse(b.getId(),b.getSeat().getSeatNumber(),
                b.getUser().getUsername(),b.getBookedAt());
    }
}
