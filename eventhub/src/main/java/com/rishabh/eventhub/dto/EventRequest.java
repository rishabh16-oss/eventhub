package com.rishabh.eventhub.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventRequest(
        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotBlank(message = "Venue is required")
        String venue,

        @NotNull(message = "Start time is required")
        @Future(message = "Start time must be in the future")
        LocalDateTime startTime,

        @Min(value = 1, message = "Capacity must be at least 1")
        int capacity,

        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price cannot be negative")
        BigDecimal price
) {}