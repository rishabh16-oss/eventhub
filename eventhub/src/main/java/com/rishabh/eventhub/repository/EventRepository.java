package com.rishabh.eventhub.repository;

import com.rishabh.eventhub.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}