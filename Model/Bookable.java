package Model;

/**
 * Bookable Interface
 * 
 * Defines behavior for objects that can be booked or have availability status.
 * This interface is designed for time slots, schedules, and resources that
 * can be reserved or made unavailable.
 * 
 * Purpose:
 * - Standardize booking/availability behavior for time-related resources
 * - Allow flexible booking logic across different objects
 * - Centralize availability management patterns
 * 
 * Implemented by: TimeSlot, Schedule
 */
public interface Bookable {
    /**
     * Checks if this resource is available for booking
     * @return true if available, false otherwise
     */
    boolean isAvailable();
    
    /**
     * Marks this resource as booked/unavailable
     */
    void book();
    
    /**
     * Marks this resource as released/available for booking again
     */
    void release();
}
