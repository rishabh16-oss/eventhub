package com.rishabh.eventhub.service;

import com.rishabh.eventhub.dto.EventRequest;
import com.rishabh.eventhub.dto.EventResponse;
import com.rishabh.eventhub.entity.Event;
import com.rishabh.eventhub.exception.InvalidRequestException;
import com.rishabh.eventhub.exception.ResourceNotFoundException;
import com.rishabh.eventhub.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventResponse create(EventRequest request) {
        Event event = Event.builder()
                .title(request.title())
                .description(request.description())
                .venue(request.venue())
                .startTime(request.startTime())
                .capacity(request.capacity())
                .availableSeats(request.capacity())
                .price(request.price())
                .build();
        return toResponse(eventRepository.save(event));
    }

    public List<EventResponse> getAll() {
        return eventRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public EventResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public EventResponse update(Long id, EventRequest request) {
        Event event = findOrThrow(id);
        int booked = event.getCapacity() - event.getAvailableSeats();

        if (request.capacity() < booked) {
            throw new InvalidRequestException(
                    "Capacity cannot be less than already booked seats (" + booked + ")");
        }

        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setVenue(request.venue());
        event.setStartTime(request.startTime());
        event.setCapacity(request.capacity());
        event.setAvailableSeats(request.capacity() - booked);
        event.setPrice(request.price());
        return toResponse(eventRepository.save(event));
    }

    public void delete(Long id) {
        eventRepository.delete(findOrThrow(id));
    }

    private Event findOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
    }

    private EventResponse toResponse(Event e) {
        return new EventResponse(e.getId(), e.getTitle(), e.getDescription(), e.getVenue(),
                e.getStartTime(), e.getCapacity(), e.getAvailableSeats(), e.getPrice());
    }
}