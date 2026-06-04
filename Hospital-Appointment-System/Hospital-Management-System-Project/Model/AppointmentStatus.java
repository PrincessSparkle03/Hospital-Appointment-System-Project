package Model;

/**
 * AppointmentStatus Enum
 * 
 * Represents the possible states of an appointment.
 * Week 6 Feedback: Simplified to only track Booked and Cancelled states.
 * Availability control is managed at the Appointment class level.
 */
public enum AppointmentStatus {
    /**
     * BOOKED: Appointment has been successfully scheduled and confirmed.
     * Patient and Doctor have committed to this appointment time.
     */
    BOOKED("Booked"),
    
    /**
     * CANCELLED: Appointment has been cancelled by patient or doctor.
     * The time slot becomes available again for other patients/doctors.
     */
    CANCELLED("Cancelled");
    
    // Display value for status
    private final String displayValue;
    
    /**
     * Constructor for AppointmentStatus
     * @param displayValue Human-readable status name
     */
    AppointmentStatus(String displayValue) {
        this.displayValue = displayValue;
    }
    
    /**
     * Gets the display value of the status
     * @return Human-readable status string
     */
    public String getDisplayValue() {
        return displayValue;
    }
}
