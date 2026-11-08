package com.rishabh.eventhub.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String title,
        String description,
        String venue,
        LocalDateTime startTime,
        int capacity,
        int availableSeats,
        BigDecimal price
) {}