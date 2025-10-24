package com.example.reservation.reservations.aviliability;

import com.example.reservation.reservations.ReservationRepository;
import com.example.reservation.reservations.ReservationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationAvailabilityService
{
    private final ReservationRepository reservationRepository;
    private final Logger logger = LoggerFactory.getLogger(ReservationAvailabilityService.class);

    public ReservationAvailabilityService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    public Boolean isReservationAvailable(
            Long roomId,
            LocalDate startDate,
            LocalDate endDate
    )
    {
        logger.info("isReservationAvailable");

        if(endDate.isAfter(startDate))
        {
            throw new IllegalArgumentException("Start date must be 1 day earlier than end date");
        }
        List<Long> allReservations = reservationRepository.findConflictReservationIds(
                roomId, startDate, endDate, ReservationStatus.APPROVED
        );
        if (allReservations.isEmpty())
        {
            return true;
        }
        logger.info("Conflict with reservation id = {}" + allReservations);
        return false;

    }
}
