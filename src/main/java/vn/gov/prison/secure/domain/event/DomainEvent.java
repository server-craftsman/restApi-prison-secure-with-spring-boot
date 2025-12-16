package vn.gov.prison.secure.domain.event;

import java.time.LocalDateTime;

/**
 * Base interface for all domain events
 * Following Domain Event Pattern
 * 
 * Note: Methods use record accessor naming (no 'get' prefix)
 * to work seamlessly with Java records
 */
public interface DomainEvent {

    /**
     * Get unique event ID
     */
    String eventId();

    /**
     * Get event timestamp
     */
    LocalDateTime occurredAt();

    /**
     * Get event type
     */
    String eventType();

    /**
     * Get aggregate ID that generated this event
     */
    String aggregateId();
}
