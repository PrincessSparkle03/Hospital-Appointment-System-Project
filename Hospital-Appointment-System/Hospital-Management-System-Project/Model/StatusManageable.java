package Model;

/**
 * StatusManageable Interface
 * 
 * Defines behavior for objects that have status and status transitions.
 * This interface ensures that status changes follow a consistent pattern
 * and that status queries are standardized.
 * 
 * Purpose:
 * - Standardize status management across appointment-related classes
 * - Make status transitions explicit and controlled
 * - Allow status checking in a uniform way
 * 
 * Implemented by: Appointment
 */
public interface StatusManageable {
    /**
     * Gets the current status of this object
     * @return The current AppointmentStatus
     */
    AppointmentStatus getStatus();
    
    /**
     * Updates the status to a new state
     * @param newStatus The new AppointmentStatus to set
     */
    void setStatus(AppointmentStatus newStatus);
    
    /**
     * Checks if this object is in a specific status
     * @param status The status to check for
     * @return true if the object has this status, false otherwise
     */
    boolean hasStatus(AppointmentStatus status);
}
