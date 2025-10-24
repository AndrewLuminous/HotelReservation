package com.example.reservation.reservations;

import com.example.reservation.reservations.aviliability.ReservationAvailabilityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

@Service
public class ReservationService
{
    private final ReservationRepository reservationRepository;
    private final ReservationMapper mapper;
    private final ReservationAvailabilityService availabilityService;
    public ReservationService(ReservationRepository reservationRepository, ReservationMapper mapper, ReservationAvailabilityService availabilityService)
    {
        this.reservationRepository = reservationRepository;
        this.mapper = mapper;
        this.availabilityService = availabilityService;
    }

    private final Logger log = Logger.getLogger(ReservationService.class.getName());
    public Reservation getReservationServiceById(Long id)
    {
        reservationRepository.findAllByStatusIs(ReservationStatus.PENDING);
        log.info("getReservationServiceById");
        ReservationEntity reservationEntity = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found by id = " + id));
        return mapper.toDomain(reservationEntity);
    }

    public List<Reservation> searchAllByFilter(
            ReservationSearchFilter filter
    )
    {
        log.info("searchAllByFilter");
        int PageSize = filter.pageSize() != null ? filter.pageSize() : 10;
        int PageNumber = filter.pageNumber() != null ? filter.pageNumber() : 0;
        Pageable pageable = Pageable
                .ofSize(PageSize)
                .withPage(PageNumber);

         List <ReservationEntity> allEntites = reservationRepository.allByFilter(
                 filter.roomId(),
                 filter.userId(),
                 pageable
         );

        return allEntites.stream()
                .map(mapper::toDomain)
                .toList();
    }

    public Reservation createNewReservation(Reservation reservation)
    {
        log.info("createNewReservation");
        if (reservation.status() != null)
        {
            throw new IllegalArgumentException("Reservation already exists");
        }
        if(!reservation.endDate().isAfter(reservation.startDate()))
        {
            throw new IllegalArgumentException("Start date must be 1 day earlier than end date");
        }
        var EntityToSave = mapper.toEntity(reservation);
        EntityToSave.setStatus(ReservationStatus.PENDING);
        return mapper.toDomain(reservationRepository.save(EntityToSave));
    }

    public Reservation updateReservation(Reservation reservationToUpdate, Long id)
    {
        log.info("updateReservation");

        var reservationEntity = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reservation not found = " + id));

        if(reservationEntity.getStatus() != ReservationStatus.PENDING)
        {
            throw new IllegalArgumentException("cannot modify reservation status: status = " + reservationEntity.getStatus() + ", id = " + id);
        }
        if(!reservationToUpdate.endDate().isAfter(reservationToUpdate.startDate()))
        {
            throw new IllegalArgumentException("Start date must be 1 day earlier than end date");
        }
        var reservation = mapper.toEntity(reservationToUpdate);
        reservation.setId(reservationEntity.getId());
        reservation.setStatus(ReservationStatus.PENDING);
        var updated = reservationRepository.save(reservation);
        return mapper.toDomain(updated);
    }
    @Transactional
    public void cancelReservation(Long id)
    {
        if(!reservationRepository.existsById(id))
        {
            throw new EntityNotFoundException("Reservation not found");
        }
        log.info("Sucsess cancel Reservation id = " + id);
        var reservation = reservationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Reservation not found = " + id));
        if(ReservationStatus.APPROVED.equals(reservation.getStatus()))
        {
            throw new IllegalStateException("Cannot cancel approved reservation. Contact with manager, please");
        }
        if(ReservationStatus.CANCELLED.equals(reservation.getStatus()))
        {
            throw new IllegalStateException("Cannot cancel cancelled reservation. Reservation was already cancelled");
        }
        reservationRepository.setStatus(id, ReservationStatus.CANCELLED);
    }

    public Reservation approve(Long id)
    {
        log.info("aprove");
        ReservationEntity reservationEntity =
                reservationRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException("not found by id = " + id));

        if (reservationEntity.getStatus() != ReservationStatus.PENDING)
        {
            throw new IllegalArgumentException("cannot approve reservation status = " + reservationEntity.getStatus() + ", id = " + id);
        }
        var isAvailable = availabilityService.isReservationAvailable(
                reservationEntity.getRoomId(),
                reservationEntity.getStartDate(),
                reservationEntity.getEndDate());
        if (!isAvailable)
        {
            throw new IllegalArgumentException("cannot approve reservation status cuz conflict");
        }
        reservationEntity.setStatus(ReservationStatus.APPROVED);
        reservationRepository.save(reservationEntity);

        return mapper.toDomain(reservationEntity);

    }

}
