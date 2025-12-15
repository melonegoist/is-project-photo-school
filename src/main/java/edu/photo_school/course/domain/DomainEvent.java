package edu.photo_school.course.domain;

import java.time.Instant;

public abstract class DomainEvent {
    private final Long eventId;
    private final Instant occurredOn;

    protected DomainEvent() {
        this.eventId = (long) (Math.random() * 1000000000);
        this.occurredOn = Instant.now();
    }

    public Long getEventId() {
        return eventId;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }

    public abstract String getEventType();
}
