package com.example.reservation.reservations.aviliability;

public record CheckAvailabilityResponse
        (
                String message,
                AvailabilityStatus status

        )
{
}
