package Model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Appointment Class
 * 
 * Represents a medical appointment in the hospital system.
 * Establishes relationships between Patient, Doctor, and TimeSlot.
 * 
 * Key Responsibilities:
 * - Link Patient, Doctor, and TimeSlot together
 * - Manage appointment status (BOOKED or CANCELLED)
 * - Check if a time slot is available for a specific doctor via isAvailable()
 * - Calculate appointment fees based on doctor's hourly rate
 * 
 * AVAILABILITY CHECKING: The isAvailable() method checks if the doctor has
 * any conflicting appointments at the given time slot. This centralizes availability
 * logic: don't check TimeSlot.isAvailable() - check Appointment.isAvailable() instead.
 * 
 * Key Relationships:
 * - Appointment → Patient (who is being treated)
 * - Appointment → Doctor (who provides treatment)
 * - Appointment → TimeSlot (when the appointment occurs)
 * - Appointment → AppointmentStatus (current state of appointment)
 * 
 * Access Modifiers:
 * - Private: id, patient, doctor, timeSlot, status, fee (encapsulated data)
 * - Public: Constructor, getters, setters, validation methods (interface)
 */
public class Appointment implements Displayable, StatusManageable {
    // Private fields - encapsulated data
    private String id;                          // Unique appointment reference
    private Patient patient;                    // Patient for this appointment
    private Doctor doctor;                      // Doctor assigned to this appointment
    private TimeSlot timeSlot;                  // When the appointment is scheduled
    private AppointmentStatus status;           // Current state of the appointment
    private double fee;                         // Cost of the appointment
    private static int totalAppointmentsCreated = 0;
    /**
     * Constructor: Creates an Appointment linking Patient, Doctor, and TimeSlot
     * @param id Unique appointment identifier
     * @param patient The patient for this appointment
     * @param doctor The doctor for this appointment
     * @param timeSlot The time slot for this appointment
     */
    public Appointment(String id, Patient patient, Doctor doctor, TimeSlot timeSlot) {
        setId(id);
        setPatient(patient);
        setDoctor(doctor);
        setTimeSlot(timeSlot);
        this.status = AppointmentStatus.BOOKED; // New appointments start as BOOKED
        // Fee is calculated based on the doctor's hourly rate at time of booking
        this.fee = (doctor != null) ? doctor.getHourlyRate() : 0.0;
        totalAppointmentsCreated++;
    }

    // --- SETTERS (Mutators) with Business Logic ---

    /**
     * Sets the appointment ID
     * @param id Unique identifier (typically set once at creation)
     */
    public void setId(String id) {
        if (id == null || id.isEmpty()) {
            this.id = "GEN-" + System.currentTimeMillis(); // Auto-generate if invalid
        } else {
            this.id = id;
        }
    }

    /**
     * Sets the patient for this appointment
     * Logic: Ensures a valid patient is assigned
     * @param patient The patient object
     */
    public void setPatient(Patient patient) {
        if (patient == null) {
            System.out.println("Error: Appointment must have a valid patient.");
        } else {
            this.patient = patient;
        }
    }

    /**
     * Sets the doctor for this appointment
     * Logic: Ensures a valid doctor is assigned
     * @param doctor The doctor object
     */
    public void setDoctor(Doctor doctor) {
        if (doctor == null) {
            System.out.println("Error: Appointment must have a valid doctor.");
        } else {
            this.doctor = doctor;
        }
    }

    /**
     * Sets the TimeSlot for this appointment
     * Logic: Ensures a valid TimeSlot is assigned
     * Note: Availability is managed by HospitalSystem during booking process
     * @param timeSlot The TimeSlot object containing date and time
     */
    public void setTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null) {
            System.out.println("Error: Appointment must have a valid time slot.");
        } else {
            this.timeSlot = timeSlot;
            // Do NOT set availability here - let HospitalSystem manage it
        }
    }

    /**
     * Updates the appointment status
     * Logic: Allows transitioning between valid states
     * @param status The new AppointmentStatus
     */
    public void setStatus(AppointmentStatus status) {
        if (status == null) {
            System.out.println("Error: Status cannot be null.");
        } else {
            this.status = status;
            // Note: TimeSlot availability is now managed through Doctor.hasConflictingAppointment()
            // When appointment is cancelled, the doctor's conflicting appointment check automatically
            // reflects that the slot is available (since cancelled appointments are skipped)
        }
    }

    // --- GETTERS (Accessors) ---

    /**
     * Gets the appointment ID in a formatted way
     * @return Formatted ID like "REF-12345"
     */
    public String getId() {
        return "REF-" + id.toUpperCase();
    }

    /**
     * Gets the raw appointment ID (without formatting)
     * @return The unformatted ID
     */
    public String getRawId() {
        return id;
    }

    /**
     * Gets the patient associated with this appointment
     * @return The Patient object
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Gets the doctor associated with this appointment
     * @return The Doctor object
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Gets the TimeSlot for this appointment
     * @return The TimeSlot object
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Gets the date of this appointment
     * @return Date in YYYY-MM-DD format
     */
    public String getDate() {
        return (timeSlot != null) ? timeSlot.getDate().toString() : "Not Set";
    }

    /**
     * Gets the date as LocalDate
     * @return Date as LocalDate object
     */
    public LocalDate getDateAsLocalDate() {
        return (timeSlot != null) ? timeSlot.getDate() : null;
    }

    /**
     * Gets the time of this appointment
     * @return Time with formatting (e.g., "09:00 hrs (Scheduled)")
     */
    public String getTime() {
        return (timeSlot != null) ? timeSlot.getStart() + " (Scheduled)" : "Not Set";
    }

    /**
     * Gets the time as LocalTime
     * @return Time as LocalTime object
     */
    public LocalTime getTimeAsLocalTime() {
        return (timeSlot != null) ? timeSlot.getStartTime() : null;
    }

    /**
     * Gets the appointment status
     * @return The current AppointmentStatus
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Gets the status display value
     * @return Human-readable status string
     */
    public String getStatusDisplay() {
        return (status != null) ? status.getDisplayValue() : "Unknown";
    }

    /**
     * Gets the appointment fee with hospital tax
     * Logic: Automatically adds 10% hospital service tax to the doctor's rate
     * @return Total fee including tax
     */
    public double getFee() {
        return fee * 1.10;
    }
    
    @Override
    public void displayInfo() {
        System.out.println("==== APPOINTMENT DETAILS ====");
        System.out.println("Reference: " + getId());
        if (patient != null) System.out.println("Patient: " + patient.getName());
        if (doctor != null) System.out.println("Doctor: " + doctor.getName());
        if (timeSlot != null) System.out.println("Date & Time: " + timeSlot.getDate() + " at " + timeSlot.getStart());
        System.out.println("Status: " + getStatusDisplay());
        System.out.println("Fee: $" + String.format("%.2f", getFee()));
        System.out.println("==============================");
    }

    // --- STATUS MANAGEABLE INTERFACE METHODS ---

    /**
     * Checks if this appointment has a specific status
     * Implements StatusManageable interface
     * @param appointmentStatus The status to check for
     * @return true if the appointment has this status, false otherwise
     */
    public boolean hasStatus(AppointmentStatus appointmentStatus) {
        return this.status == appointmentStatus;
    }
    public static int getTotalAppointmentsCreated() { 
        return totalAppointmentsCreated;
    }
    /**
     * Gets the appointment fee without tax
     * @return Base fee from doctor's hourly rate
     */
    public double getBaseFee() {
        return fee;
    }

    // --- BUSINESS LOGIC METHODS ---

    /**
     * Checks if this appointment can be rescheduled
     * Week 6 Feedback: Rescheduling is managed through cancel and rebook
     * @return false - rescheduling not supported in simplified model
     */
    public boolean canBeRescheduled() {
        return false;  // Rescheduling handled through cancel + new appointment
    }

    /**
     * Checks if this appointment can be cancelled
     * Logic: Only BOOKED appointments can be cancelled
     * @return true if appointment can be cancelled
     */
    public boolean canBeCancelled() {
        return status == AppointmentStatus.BOOKED;
    }

    /**
     * Checks if the time slot for this appointment is available for the assigned doctor
     * 
     * IMPORTANT: This is the ONLY method you should use to check availability.
     * The Doctor checks their own appointments to prevent double-booking.
     * 
     * Logic: A slot is available if and only if the doctor has no other BOOKED 
     * appointments at that time. Cancelled appointments don't count as conflicts.
     * 
     * @return true if the time slot is available for this doctor, false otherwise
     */
    public boolean isAvailable() {
        if (doctor == null || timeSlot == null) {
            return false;
        }
        // Slot is available if this doctor doesn't have a conflicting appointment
        return !doctor.hasConflictingAppointment(timeSlot);
    }

    /**
     * Checks if this appointment is still active (booked, not cancelled)
     * Week 6 Feedback: Only BOOKED appointments are active (COMPLETE and RESCHEDULE removed)
     * @return true if appointment is BOOKED
     */
    public boolean isActive() {
        return status == AppointmentStatus.BOOKED;
    }

    /**
     * Cancels this appointment
     * Logic: Updates status to CANCELLED
     * Week 6 Feedback: Simplified - only BOOKED and CANCELLED states exist
     * @return true if cancellation was successful
     */
    public boolean cancel() {
        if (canBeCancelled()) {
            setStatus(AppointmentStatus.CANCELLED);
            return true;
        }
        return false;
    }

    // --- STRING REPRESENTATION ---

    /**
     * Provides a detailed appointment summary
     * @return Formatted appointment receipt
     */
    @Override
    public String toString() {
        String patientName = (patient != null) ? patient.getName() : "Unknown Patient";
        String doctorName = (doctor != null) ? doctor.getName() : "Unknown Doctor";
        String appointmentDate = getDate();
        String appointmentTime = (timeSlot != null) ? timeSlot.getStartRaw() : "Not Set";
        String statusInfo = getStatusDisplay();
        
        return "Receipt " + getId() + 
               " | Patient: " + patientName + 
               " | Doctor: " + doctorName + 
               " | Date: " + appointmentDate +
               " | Time: " + appointmentTime +
               " | Status: " + statusInfo +
               " | Fee: $" + String.format("%.2f", getFee());
    }

}