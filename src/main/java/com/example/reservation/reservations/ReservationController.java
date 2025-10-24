package com.example.reservation.reservations;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController
{
    Logger logger = LoggerFactory.getLogger(ReservationController.class);
    ReservationService reservationService;

    public ReservationController(ReservationService reservationService)
    {
        this.reservationService = reservationService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable Long id
            )
    {
        logger.info("getReservationByIdController");
        return ResponseEntity.status(HttpStatus.OK).body(reservationService.getReservationServiceById(id));
    }
    @GetMapping("/all")
        public ResponseEntity<List<Reservation>> getAllReservation(
                @RequestParam("roomId") Long roomId,
                @RequestParam("userId") Long userId,
                @RequestParam("pageSize") Integer pageSize,
                @RequestParam("pageNumber") Integer pageNumber
    )
        {
            logger.info("findAllReservationController");
            ReservationSearchFilter reservationSearchFilter = new ReservationSearchFilter(
                    roomId,
                    userId,
                    pageSize,
                    pageNumber);
            return ResponseEntity.ok(reservationService.searchAllByFilter(reservationSearchFilter));
        }
       @PostMapping
        public ResponseEntity<Reservation> createReservation(@RequestBody @Valid Reservation reservation)
        {
            logger.info("createReservationController");
            return ResponseEntity.status(HttpStatus.CREATED).header("test-header","123").body(reservationService.createNewReservation(reservation));
        }
        @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@RequestBody @Valid Reservation reservationToUpdate, @PathVariable Long id)
        {
            logger.info("updateReservationController");
            return ResponseEntity.status(202).body(reservationService.updateReservation(reservationToUpdate,id));
        }
        @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id)
        {
            logger.info("deleteReservationController id = {}", id);
            reservationService.cancelReservation(id);
            return ResponseEntity.status(HttpStatus.OK).build();


        }
        @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservation(@PathVariable Long id)
        {
            logger.info("approveReservationController");
            var reservation = reservationService.approve(id);
            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        }
}
