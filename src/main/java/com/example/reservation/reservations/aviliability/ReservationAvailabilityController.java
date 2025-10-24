package com.example.reservation.reservations.aviliability;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reservation/availability")
public class ReservationAvailabilityController {
    private final ReservationAvailabilityService service;
    private final Logger logger = LoggerFactory.getLogger(ReservationAvailabilityController.class);
    public ReservationAvailabilityController(ReservationAvailabilityService service) {
        this.service = service;
    }

    @PostMapping("/check")
    public ResponseEntity<CheckAvailablityResponse> checkAvailability(
            @Valid CheckAvailabilityRequest request)
    {
        logger.info("Received check availability request");
        boolean isAvailable = service.isReservationAvailable(
                request.roomId(),
                request.startDate(),
                request.endDate()
                );
        var message = isAvailable ? "Reservation is available" : "Reservation is not available";
        var status = isAvailable ? AvailabilityStatus.AVAILABLE : AvailabilityStatus.RESERVED;
        return ResponseEntity.status(HttpStatus.OK).body(new CheckAvailablityResponse(message, status));
    }
}
