package Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Patient Class - Subclass of Person
 * 
 * Represents a patient in the hospital system.
 * Inherits common attributes from Person (name, phone).
 * Stores patient-specific demographics and maintains appointment history.
 * 
 * Inheritance:
 * - Extends abstract Person: Patient IS a Person
 * - Inherits: name, phone, getName(), getPhone()
 * - Implements abstract method: getRole()
 * - Adds: age, gender, dateOfBirth, appointments
 * 
 * Key Relationships:
 * - Patient → ArrayList<Appointment> (appointment history)
 * 
 * Access Modifiers:
 * - Protected (inherited): name, phone (from Person)
 * - Private: age, gender, date_of_birth, appointments (encapsulated data)
 * - Private Static: patientCount (static counter)
 * - Public: Getters, setters, appointment management methods (interface)
 */
public class Patient extends Person {
    // Private fields - patient-specific encapsulated data
    private int age;
    private String gender;
    private LocalDate dateOfBirth;              // Using LocalDate for proper date handling
    private List<Appointment> appointments;     // Patient's appointment history
    
    // Private static field - tracks total patients created
    private static int patientCount = 0;

    /**
     * Constructor: Creates a new patient with basic information
     * @param id Patient's unique identifier
     * @param name Patient's full name
     * @param age Patient's age
     * @param gender Patient's gender
     * @param dateOfBirth Patient's date of birth (LocalDate)
     * @param phone Patient's contact phone number
     */
    public Patient(String id, String name, int age, String gender, LocalDate dateOfBirth, String phone) {
        // Call superclass constructor to initialize inherited fields
        super(id, name, phone);
        
        // Initialize patient-specific fields using setters to trigger validation logic
        setAge(age);
        setGender(gender);
        setDateOfBirth(dateOfBirth);
        this.appointments = new ArrayList<>();  // Initialize empty appointment list
        patientCount++;
    }

    // --- SETTERS (Mutators) with Business Logic ---

    /**
     * Sets the patient age
     * Logic: Age must be within realistic hospital bounds
     * @param age The patient's age
     */
    public void setAge(int age) {
        // Logic: Age cannot be negative, and logically in a hospital, 
        // someone over 150 is likely a data entry error.
        if (age < 0 || age > 150) {
            System.out.println("Error: Age is invalid. Setting to 0.");
            this.age = 0;
        } else {
            this.age = age;
        }
    }

    /**
     * Sets the patient gender
     * Logic: Restrict to specific medical categories
     * @param gender The patient's gender
     */
    public void setGender(String gender) {
        // Logic: Restrict to specific medical categories
        String g = gender.toLowerCase();
        if (g.equals("male") || g.equals("female") || g.equals("other")) {
            this.gender = gender;
        } else {
            this.gender = "Unknown";
        }
    }

    /**
     * Sets the patient date of birth
     * Logic: Ensures date is not in the future and is a valid LocalDate
     * @param dateOfBirth The date of birth (LocalDate)
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        // Logic: Check if date is valid and not in the future
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            System.out.println("Error: Invalid date of birth. Must be today or in the past.");
            this.dateOfBirth = LocalDate.of(1900, 1, 1);
        } else {
            this.dateOfBirth = dateOfBirth;
        }
    }

    // --- GETTERS (Accessors) with Business Logic ---

    /**
     * Gets the patient age
     * @return The patient's age
     */
    public int getAge() {
        // Logic: If the patient is a minor (under 18), we could trigger a warning
        if (age < 18) {
            System.out.println("[Note: Patient is a minor]");
        }
        return age;
    }

    /**
     * Gets the patient gender in standardized format
     * @return The gender formatted as first letter capital
     */
    public String getGender() {
        // Logic: Return a standardized format
        return gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
    }

    /**
     * Gets the patient date of birth
     * @return Date of birth as LocalDate
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Gets the patient date of birth formatted for display
     * @return Date of birth formatted as string
     */
    public String getDateOfBirthDisplay() {
        return "DOB: " + dateOfBirth;
    }

    /**
     * Gets the total count of patients created
     * @return Static patient count
     */
    public static int getPatientCount() {
        return patientCount;
    }

    /**
     * Implements the abstract getRole() method from Person.
     * @return Patient role label
     */
    @Override
    public String getRole() {
        return "Patient";
    }

    // --- APPOINTMENT MANAGEMENT METHODS ---

    // --- DISPLAYABLE INTERFACE METHODS ---

    /**
     * Displays detailed patient information.
     * Overrides displayInfo from Person to reuse superclass implementation
     * and print patient-specific details.
     */
    @Override
    public void displayInfo() {
        System.out.println("==== PATIENT DETAILS ====");
        super.displayInfo();
        System.out.println("Age: " + age + " years");
        System.out.println("Gender: " + getGender());
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Total Appointments: " + appointments.size());
        System.out.println("========================");
    }

    // --- APPOINTMENT MANAGEMENT METHODS ---

    /**
     * Adds an appointment to this patient's appointment history
     * @param appointment The appointment to add
     * @return true if appointment was added successfully
     */
    public boolean addAppointment(Appointment appointment) {
        if (appointment == null) {
            System.out.println("Error: Cannot add null appointment to patient record.");
            return false;
        }
        appointments.add(appointment);
        return true;
    }

    /**
     * Removes an appointment from this patient's history
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
     * Gets all appointments for this patient
     * @return List of all appointments
     */
    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);  // Return copy to prevent external modification
    }

    /**
     * Gets count of total appointments for this patient
     * @return Number of appointments
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Gets count of active (not cancelled) appointments
     * @return Number of active appointments
     */
    public int getActiveAppointmentCount() {
        return (int) appointments.stream()
                .filter(Appointment::isActive)
                .count();
    }

    /**
     * Checks if patient has any appointments
     * @return true if patient has at least one appointment
     */
    public boolean hasAppointments() {
        return !appointments.isEmpty();
    }

    /**
     * Gets the most recent appointment for this patient
     * @return The most recent appointment or null if none exist
     */
    public Appointment getLastAppointment() {
        if (appointments.isEmpty()) {
            return null;
        }
        return appointments.get(appointments.size() - 1);
    }

    // --- STRING REPRESENTATION ---

}
