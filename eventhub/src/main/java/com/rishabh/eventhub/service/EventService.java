package com.rishabh.eventhub.service;

import com.rishabh.eventhub.dto.EventRequest;
import com.rishabh.eventhub.dto.EventResponse;
import com.rishabh.eventhub.dto.PageResponse;
import com.rishabh.eventhub.entity.Event;
import com.rishabh.eventhub.exception.InvalidRequestException;
import com.rishabh.eventhub.exception.ResourceNotFoundException;
import com.rishabh.eventhub.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

    private static final Set<String> SORTABLE_FIELDS =
            Set.of("title", "startTime", "price", "venue");

    public PageResponse<EventResponse> search(String keyword, int page, int size,
                                              String sortBy, String direction) {
        if (!SORTABLE_FIELDS.contains(sortBy)) {
            throw new InvalidRequestException("Cannot sort by: " + sortBy);
        }
        size = Math.min(size, 50); // cap the page size

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Event> result = (keyword == null || keyword.isBlank())
                ? eventRepository.findAll(pageable)
                : eventRepository
                .findByTitleContainingIgnoreCaseOrVenueContainingIgnoreCase(
                        keyword, keyword, pageable);

        return new PageResponse<>(
                result.getContent().stream().map(this::toResponse).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast());
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