package com.anirudh.demo;

import com.anirudh.demo.dto.BookingResponse;
import com.anirudh.demo.exception.SeatAlreadyBookedException;
import com.anirudh.demo.exception.SeatNotFoundException;
import com.anirudh.demo.model.Seat;
import com.anirudh.demo.model.User;
import com.anirudh.demo.repository.BookingRepository;
import com.anirudh.demo.repository.SeatRepository;
import com.anirudh.demo.repository.UserRepository;
import com.anirudh.demo.service.BookingService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SeatRepository seatRepository;
    @InjectMocks
    private BookingService bookingService;

    @Test
    void bookSeat_success(){
        User user = new User();
        user.setUsername("anirudh");

        Seat seat = new Seat();
        seat.setSeatNumber("A1");
        seat.setBooked(false);

        when(userRepository.findByUsername("anirudh")).thenReturn(Optional.of(user));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        BookingResponse response = bookingService.bookSeat(1L,"anirudh");

        assertEquals("A1", response.getSeatNumber());
        assertEquals("anirudh", response.getUsername());
        assertTrue(seat.isBooked());
        verify(bookingRepository,times(1)).save(any());


    }
    @Test
    void bookSeat_alreadyBooked_throws(){
        User user = new User();
        user.setUsername("anirudh");
        Seat seat = new Seat();
        seat.setSeatNumber("A1");
        seat.setBooked(true);

        when(userRepository.findByUsername("anirudh")).thenReturn(Optional.of(user));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        assertThrows(SeatAlreadyBookedException.class,
                ()->bookingService.bookSeat(1L,"anirudh"));
        verify(bookingRepository,never()).save(any());
    }
    @Test
    void bookSeat_seatNotFound_throws(){
        User user = new User();
        user.setUsername("anirudh");

        when(userRepository.findByUsername("anirudh")).thenReturn(Optional.of(user));
        when(seatRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SeatNotFoundException.class,
                ()->bookingService.bookSeat(99L,"anirudh"));
    }

}
