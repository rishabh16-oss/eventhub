package com.rishabh.eventhub.controller;

import com.rishabh.eventhub.dto.EventRequest;
import com.rishabh.eventhub.dto.EventResponse;
import com.rishabh.eventhub.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(request));
    }

    @GetMapping
    public List<EventResponse> getAll() {
        return eventService.getAll();
    }

    @GetMapping("/{id}")
    public EventResponse getById(@PathVariable Long id) {
        return eventService.getById(id);
    }

    @PutMapping("/{id}")
    public EventResponse update(@PathVariable Long id, @Valid @RequestBody EventRequest request) {
        return eventService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}