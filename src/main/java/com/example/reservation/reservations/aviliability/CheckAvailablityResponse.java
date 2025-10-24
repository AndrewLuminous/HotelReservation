package com.example.reservation.reservations.aviliability;

public record CheckAvailablityResponse
        (
                String message,
                AvailabilityStatus status

        )
{
}
