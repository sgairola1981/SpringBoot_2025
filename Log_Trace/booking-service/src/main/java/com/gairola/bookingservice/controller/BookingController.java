package com.gairola.bookingservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class BookingController {

    @GetMapping("/bookings")
    public List<String> getBookings() {
        return List.of("Booking1", "Booking2", "Booking3");
    }
}
