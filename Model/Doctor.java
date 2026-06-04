package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Doctor Class - Subclass of Person
 * 
 * Represents a doctor in the hospital system.
 * Inherits common attributes from Person (name, phone).
 * Manages doctor-specific information, availability schedule, and appointments.
 * 
 * Inheritance:
 * - Extends Person: Doctor IS a Person
 * - Inherits: name, phone, getName(), getPhone()
 * - Adds: specialist, hourlyRate, schedule, appointments
 * 
 * Key Relationships:
 * - Doctor → Schedule (doctor's working availability)
 * - Doctor → ArrayList<Appointment> (doctor's scheduled appointments)
 * 
 * Access Modifiers:
 * - Protected (inherited): name, phone (from Person)
 * - Private: specialist, hourlyRate, schedule, appointments (encapsulated data)
 * - Private Static: doctorCount (static counter)
 * - Public: Getters, setters, appointment management methods (interface)
 */
public class Doctor extends Person {
    // Private fields - doctor-specific encapsulated data
    private String specialist;
    private double hourlyRate;
    private Schedule schedule;       // Doctor's working schedule
    private List<Appointment> appointments;    // Doctor's scheduled appointments
    
    // Private static field - tracks total doctors created
    private static int doctorCount = 0;

    /**
     * Constructor: Creates a new doctor with specialization and schedule
     * @param id Doctor's unique identifier
     * @param name Doctor's full name
     * @param specialist Doctor's medical specialty
     * @param hourlyRate Doctor's hourly rate (requires admin password to modify)
     * @param schedule Schedule object showing doctor's availability
     * @param phone Doctor's contact phone number
     */
    public Doctor(String id, String name, String specialist, double hourlyRate, Schedule schedule, String phone) {
        // Call superclass constructor to initialize inherited fields
        super(id, name, phone);
        
        // Initialize doctor-specific fields using setters to trigger validation logic
        setSpecialist(specialist);
        this.hourlyRate = hourlyRate;
        setSchedule(schedule);
        this.appointments = new ArrayList<>();  // Initialize empty appointment list
        doctorCount++;
    }
    
    /**
     * Constructor (overloaded): Creates a new doctor with specialization and schedule (no phone)
     * @param id Doctor's unique identifier
     * @param name Doctor's full name
     * @param specialist Doctor's medical specialty
     * @param hourlyRate Doctor's hourly rate (requires admin password to modify)
     * @param schedule Schedule object showing doctor's availability
     */
    public Doctor(String id, String name, String specialist, double hourlyRate, Schedule schedule) {
        this(id, name, specialist, hourlyRate, schedule, "No Phone");
    }

    // --- SETTERS (Mutators) with Business Logic ---

    /**
     * Sets the doctor's medical specialty
     * Condition: Ensures the doctor has a valid department
     * @param specialist Medical specialty
     */
    public void setSpecialist(String specialist) {
        // Condition: Ensures the doctor has a valid department
        if (specialist == null || specialist.trim().isEmpty()) {
            System.out.println("Error: Specialist field required. Setting to 'General Practice'.");
            this.specialist = "General Practice";
        } else {
            this.specialist = specialist;
        }
    }

    /**
     * Sets the doctor's hourly rate (requires admin password)
     * Security Condition: Only allows modification with correct admin code
     * @param hourlyRate The new hourly rate
     * @param accessCode Admin password for authorization
     */
    public void setHourlyRate(double hourlyRate, String accessCode) {
        // Condition 1: Security check
        if (!accessCode.equals("ADMIN123")) {
            System.out.println("Access Denied: Incorrect password to modify salary.");
            return;
        }
        // Condition 2: Data Integrity (A doctor cannot pay the hospital to work)
        if (hourlyRate < 0) {
            System.out.println("Error: Rate cannot be negative.");
        } else {
            this.hourlyRate = hourlyRate;
        }
    }

    /**
     * Sets the doctor's availability schedule
     * Condition: A doctor must have at least an empty list to avoid NullPointerException
     * @param availability List of Schedule objects
     */
    public void setSchedule(Schedule schedule) {
        if (schedule == null) {
            System.out.println("Warning: Doctor has no assigned schedule.");
        } 
        this.schedule = schedule;
    }

    // --- GETTERS (Accessors) with Business Logic ---


    /**
     * Gets the doctor's specialty in standardized format
     * Condition: Logic to return a readable string
     * @return Specialty in uppercase
     */
    public String getSpecialist() {
        // Condition: Logic to return a readable string
        return this.specialist.toUpperCase(); 
    }

    /**
     * Gets the doctor's hourly rate
     * @return Hourly rate in dollars
     */
    public double getHourlyRate() {
        // Logic: You could add a condition here to check if the user is authorized 
        // to see the salary, otherwise return 0.0
        return hourlyRate;
    }

    /**
     * Gets the doctor's availability schedule
     * Condition: Logic to ensure we don't return a null list that might crash the app
     * @return List of Schedule objects
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Gets the total count of doctors created
     * @return Static doctor count
     */
    public static int getDoctorCount() {
        return doctorCount;
    }

    // --- DISPLAYABLE INTERFACE METHODS ---

    /**
     * Displays detailed doctor information.
     * Overrides displayInfo from Person to reuse superclass implementation
     * and print doctor-specific details.
     */
    @Override
    public void displayInfo() {
        System.out.println("==== DOCTOR DETAILS ====");
        super.displayInfo();
        System.out.println("Specialty: " + getSpecialist());
        System.out.println("Hourly Rate: $" + hourlyRate);
        if (schedule != null) {
            System.out.println("Schedule: " + schedule.getDay() + " (" + schedule.getShiftDuration() + ")");
        }
        System.out.println("Total Appointments: " + appointments.size());
        System.out.println("Active Appointments: " + getActiveAppointmentCount());
        System.out.println("========================");
    }

    // --- APPOINTMENT MANAGEMENT METHODS ---

    /**
     * Gets all appointments for this doctor
     * @return List of doctor's appointments
     */
    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);  // Return copy to prevent external modification
    }

    /**
     * Gets count of total appointments for this doctor
     * @return Number of appointments
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Gets count of active (booked) appointments
     * @return Number of active appointments
     */
    public int getActiveAppointmentCount() {
        return (int) appointments.stream()
                .filter(Appointment::isActive)
                .count();
    }

    /**
     * Checks if this doctor has any appointments
     * @return true if doctor has at least one appointment
     */
    public boolean hasAppointments() {
        return !appointments.isEmpty();
    }

    // --- DUPLICATE APPOINTMENT VALIDATION ---

    /**
     * Checks if doctor has a conflicting appointment at the given time slot
     * Business Logic: Prevents double-booking for THIS DOCTOR (doctor cannot see two patients at once)
     * Week 6 Feedback: Simplified to check only if THIS DOCTOR has an appointment at this time,
     * not the time slot's overall availability.
     * @param timeSlot The time slot to check
     * @return true if this doctor already has an appointment during this time slot
     */
    public boolean hasConflictingAppointment(TimeSlot timeSlot) {
        if (timeSlot == null) {
            return false;
        }
        
        // Check all active appointments for THIS DOCTOR at this time slot
        for (Appointment apt : appointments) {
            // Only check BOOKED appointments (not cancelled)
            if (apt.getStatus() == AppointmentStatus.CANCELLED) {
                continue;
            }
            
            TimeSlot existingSlot = apt.getTimeSlot();
            if (existingSlot == null) {
                continue;
            }
            
            // If same time slot is already booked for this doctor, conflict exists
            if (existingSlot.getDate().equals(timeSlot.getDate()) &&
                existingSlot.getStart().equals(timeSlot.getStart())) {
                return true; // This doctor is already booked at this time
            }
        }
        return false;
    }

    /**
     * Validates if an appointment can be booked for this doctor
     * Business Logic: Ensures appointment doesn't violate business rules
     * Week 6 Feedback: Check only if THIS DOCTOR already has an appointment,
     * not the time slot's overall availability (that's managed by Appointment class)
     * @param appointment The appointment to validate
     * @return true if appointment can be booked, false otherwise
     */
    public boolean canBookAppointment(Appointment appointment) {
        if (appointment == null || appointment.getTimeSlot() == null) {
            System.out.println("Error: Invalid appointment or time slot.");
            return false;
        }
        
        if (hasConflictingAppointment(appointment.getTimeSlot())) {
            System.out.println("Error: Doctor already has an appointment at this time.");
            return false;
        }
        
        return true;
    }

    /**
     * Adds an appointment to this doctor's appointment list
     * Business Logic: Validates appointment before adding and prevents duplicates
     * @param appointment The appointment to add
     * @return true if appointment was added successfully
     */
    public boolean addAppointment(Appointment appointment) {
        if (appointment == null) {
            System.out.println("Error: Cannot add null appointment to doctor record.");
            return false;
        }
        
        if (!canBookAppointment(appointment)) {
            return false;
        }
        
        appointments.add(appointment);
        System.out.println("Appointment successfully booked with " + this.getName());
        return true;
    }

    /**
     * Removes an appointment from this doctor's list
     * @param appointment The appointment to remove
     * @return true if appointment was removed
     */
    public boolean removeAppointment(Appointment appointment) {
        if (appointment == null) {
            return false;
        }
        return appointments.remove(appointment);
    }

    /**
     * Cancels an appointment for this doctor
     * Business Logic: Cancels the appointment and frees up the time slot
     * @param appointment The appointment to cancel
     * @return true if cancellation was successful
     */
    public boolean cancelAppointment(Appointment appointment) {
        if (appointment == null) {
            return false;
        }
        
        if (appointment.cancel()) {
            System.out.println("Appointment cancelled successfully. Time slot is now available.");
            return true;
        }
        
        return false;
    }

    // --- STRING REPRESENTATION ---


    /**
     * Gets count of available time slots across all schedules
     * Logic: A slot is available if this doctor has no BOOKED appointments at that time
     * @return Number of available slots
     */
    public int getAvailableSlotCount() {
        int count = 0;
        if (schedule != null && schedule.getSlots() != null) {
            for (TimeSlot slot : schedule.getSlots()) {
                // A slot is available if this doctor doesn't have a conflicting appointment
                if (!hasConflictingAppointment(slot)) {
                    count++;
                }
            }
        }
        return count;
    }
}