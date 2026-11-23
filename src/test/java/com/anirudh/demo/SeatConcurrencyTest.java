package com.anirudh.demo;

import com.anirudh.demo.model.Seat;
import com.anirudh.demo.model.User;
import com.anirudh.demo.repository.BookingRepository;
import com.anirudh.demo.repository.SeatRepository;
import com.anirudh.demo.repository.UserRepository;
import com.anirudh.demo.service.BookingService;
import com.anirudh.demo.service.SeatService;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.anirudh.demo.exception.SeatAlreadyBookedException;
import org.springframework.beans.factory.annotation.Autowired;
@SpringBootTest
public class SeatConcurrencyTest {
     @Autowired
    private SeatService seatService;
     @Autowired
    private SeatRepository seatRepository;
     @Autowired
     private BookingService bookingService;
     @Autowired
     private BookingRepository bookingRepository;
     @Autowired
     private UserRepository userRepository;
    @Test
    void onlyOneUserCanBookTheSameTicket() throws InterruptedException{
        Seat seat = seatService.addSeat("RACE-1");
        Long seatId = seat.getId();
        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        AtomicInteger success = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();
        if (!userRepository.existsByUsername("racer")) {
            User u = new User();
            u.setUsername("racer");
            u.setPassword("x");
            userRepository.save(u);
        }
        for( int i=0; i < threads; i++){
            executor.submit(()-> {
                try {
                    ready.countDown();
                    start.await();
                    bookingService.bookSeat(seatId,"racer");
                    success.incrementAndGet();
                } catch (OptimisticLockingFailureException | SeatAlreadyBookedException |DataIntegrityViolationException e) {
                    failed.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }
        ready.await();
        start.countDown();
        assertTrue(done.await(30, TimeUnit.SECONDS));
        executor.shutdown();

        System.out.println("Success = " + success.get()+",Failed = "+failed.get());

        assertEquals(1,success.get());
        assertEquals(threads-1,failed.get());
        bookingRepository.deleteAll();
        seatRepository.deleteById(seatId);
    }
    @AfterEach
    void cleanUp() {
        bookingRepository.deleteAll();
        seatRepository.findBySeatNumber("RACE-1")
                .ifPresent(s -> seatRepository.deleteById(s.getId()));
    }
}
