package Main;

import Model.Appointment;
import Model.Doctor;
import Model.Patient;
import Model.TimeSlot;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * HospitalSystem Class
 * 
 * Central management system for the entire hospital.
 * Manages all patients, doctors, appointments, and schedules.
 * Provides centralized business logic for appointment booking, validation, and management.
 * 
 * Key Responsibilities:
 * 1. Manage Patient collection (ArrayList<Patient>)
 * 2. Manage Doctor collection (ArrayList<Doctor>)
 * 3. Manage Appointment collection (ArrayList<Appointment>)
 * 4. Validate and book appointments
 * 5. Prevent duplicate/conflicting appointments
 * 6. Track appointment history and status
 * 7. Provide system-wide reporting
 * 
 * Access Modifiers:
 * - Private: patients, doctors, appointments, appointmentMap, appointmentCounter (encapsulated data)
 * - Public: All management methods (interface for Main or UI components)
 */
public class HospitalSystem {
    // Hospital name constant
    private static final String HOSPITAL_NAME = "City General Hospital";
    
    // Private collections - encapsulated data
    private List<Patient> patients;              // All patients in the system
    private List<Doctor> doctors;                // All doctors in the system
    private List<Appointment> appointments;      // All appointments in the system
    private Map<String, Appointment> appointmentMap;  // Fast lookup by ID
    private int appointmentCounter;              // For generating unique appointment IDs

    /**
     * Constructor: Initializes the hospital system with empty collections
     */
    public HospitalSystem() {
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.appointmentMap = new HashMap<>();
        this.appointmentCounter = 0;
    }

    // --- PATIENT MANAGEMENT ---

    /**
     * Registers a new patient in the system
     * @param patient The patient to register
     * @return true if patient was registered successfully
     */
    public boolean registerPatient(Patient patient) {
        if (patient == null) {
            System.out.println("Error: Cannot register null patient.");
            return false;
        }
        if (patients.contains(patient)) {
            System.out.println("Error: Patient already registered.");
            return false;
        }
        patients.add(patient);
        System.out.println("Patient registered: " + patient.getName());
        return true;
    }

    /**
     * Removes a patient from the system
     * @param patient The patient to remove
     * @return true if patient was removed
     */
    public boolean removePatient(Patient patient) {
        if (patient == null || !patients.contains(patient)) {
            return false;
        }
        patients.remove(patient);
        return true;
    }

    /**
     * Gets all patients in the system
     * @return List of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);  // Return copy
    }

    /**
     * Gets a patient by name
     * @param name Patient's name
     * @return Patient object or null if not found
     */
    public Patient findPatientByName(String name) {
        if (name == null) {
            return null;
        }
        for (Patient p : patients) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Gets total number of patients
     * @return Patient count
     */
    public int getPatientCount() {
        return patients.size();
    }

    // --- DOCTOR MANAGEMENT ---

    /**
     * Registers a new doctor in the system
     * @param doctor The doctor to register
     * @return true if doctor was registered successfully
     */
    public boolean registerDoctor(Doctor doctor) {
        if (doctor == null) {
            System.out.println("Error: Cannot register null doctor.");
            return false;
        }
        if (doctors.contains(doctor)) {
            System.out.println("Error: Doctor already registered.");
            return false;
        }
        doctors.add(doctor);
        System.out.println("Doctor registered: " + doctor.getName());
        return true;
    }

    /**
     * Removes a doctor from the system
     * @param doctor The doctor to remove
     * @return true if doctor was removed
     */
    public boolean removeDoctor(Doctor doctor) {
        if (doctor == null || !doctors.contains(doctor)) {
            return false;
        }
        doctors.remove(doctor);
        return true;
    }

    /**
     * Gets all doctors in the system
     * @return List of all doctors
     */
    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors);  // Return copy
    }

    public Doctor findDoctorByName(String name) {
        if (name == null) {
            return null;
        }
        for (Doctor d : doctors) {
            String doctorName = d.getName();
            if (doctorName.equalsIgnoreCase(name) || doctorName.equalsIgnoreCase("Dr. " + name) || ("Dr. " + doctorName).equalsIgnoreCase(name)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Gets doctors by specialty
     * @param specialty Medical specialty
     * @return List of doctors with that specialty
     */
    public List<Doctor> findDoctorsBySpecialty(String specialty) {
        List<Doctor> matchingDoctors = new ArrayList<>();
        if (specialty == null) {
            return matchingDoctors;
        }
        for (Doctor d : doctors) {
            if (d.getSpecialist().equalsIgnoreCase(specialty.toUpperCase())) {
                matchingDoctors.add(d);
            }
        }
        return matchingDoctors;
    }

    /**
     * Gets total number of doctors
     * @return Doctor count
     */
    public int getDoctorCount() {
        return doctors.size();
    }

    // --- APPOINTMENT MANAGEMENT ---

    /**
     * Books an appointment for a patient with a doctor at a specific time slot
     * 
     * AVAILABILITY CHECK: Before booking, checks if the doctor has any conflicting 
     * appointments at this time slot using Doctor.hasConflictingAppointment().
     * Multiple doctors CAN share the same time slot; each doctor's availability is independent.
     * 
     * @param patient The patient to book
     * @param doctor The doctor to book with
     * @param timeSlot The time slot to book
     * @return Appointment object if successful, null if booking failed
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor, TimeSlot timeSlot) {
        // Validate inputs
        if (patient == null || doctor == null || timeSlot == null) {
            System.out.println("Error: Invalid patient, doctor, or time slot.");
            return null;
        }

        // Check for conflicting appointments with THIS DOCTOR only
        // (Other doctors can use the same time slot)
        if (doctor.hasConflictingAppointment(timeSlot)) {
            System.out.println("Error: Doctor is already booked at this time.");
            return null;
        }

        // Create the appointment
        appointmentCounter++;
        String appointmentId = String.format("%05d", appointmentCounter);
        Appointment appointment = new Appointment(appointmentId, patient, doctor, timeSlot);

        // Add to doctor's schedule
        if (!doctor.addAppointment(appointment)) {
            System.out.println("Error: Failed to add appointment to doctor's schedule.");
            return null;
        }

        // Add to patient's appointment history
        if (!patient.addAppointment(appointment)) {
            // Rollback if patient add fails
            doctor.removeAppointment(appointment);
            System.out.println("Error: Failed to add appointment to patient record.");
            return null;
        }

        // Add to system collections
        appointments.add(appointment);
        appointmentMap.put(appointmentId, appointment);

        return appointment;
    }

    /**
     * Overloaded method: Books an appointment with a specific doctor on a specific date and start time.
     * Searches the doctor's schedule for a matching TimeSlot.
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor, java.time.LocalDate date, java.time.LocalTime startTime) {
        if (doctor == null || doctor.getSchedule() == null || doctor.getSchedule().getSlots() == null) {
            System.out.println("Error: Doctor has no available schedule.");
            return null;
        }
        for (TimeSlot slot : doctor.getSchedule().getSlots()) {
            if (slot.getDate().equals(date) && slot.getStartTime().equals(startTime)) {
                return bookAppointment(patient, doctor, slot);
            }
        }
        System.out.println("Error: Specified date and time slot not found in doctor's schedule.");
        return null;
    }

    /**
     * Overloaded method: Books an appointment with a specific doctor on a specific date (first available time slot).
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor, java.time.LocalDate date) {
        if (doctor == null || doctor.getSchedule() == null || doctor.getSchedule().getSlots() == null) {
            System.out.println("Error: Doctor has no available schedule.");
            return null;
        }
        for (TimeSlot slot : doctor.getSchedule().getSlots()) {
            if (slot.getDate().equals(date)) {
                // Check if the doctor doesn't have a conflict
                if (!doctor.hasConflictingAppointment(slot)) {
                    return bookAppointment(patient, doctor, slot);
                }
            }
        }
        System.out.println("Error: No available slots for the doctor on the specified date.");
        return null;
    }

    /**
     * Overloaded method: Books an appointment with a specific doctor at a specific start time on any day (first available day).
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor, java.time.LocalTime startTime) {
        if (doctor == null || doctor.getSchedule() == null || doctor.getSchedule().getSlots() == null) {
            System.out.println("Error: Doctor has no available schedule.");
            return null;
        }
        for (TimeSlot slot : doctor.getSchedule().getSlots()) {
            if (slot.getStartTime().equals(startTime)) {
                if (!doctor.hasConflictingAppointment(slot)) {
                    return bookAppointment(patient, doctor, slot);
                }
            }
        }
        System.out.println("Error: No available slots starting at " + startTime + " for the doctor on any day.");
        return null;
    }

    /**
     * Overloaded method: Books an appointment with a specific doctor on any day and any time (first available slot).
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor) {
        if (doctor == null || doctor.getSchedule() == null || doctor.getSchedule().getSlots() == null) {
            System.out.println("Error: Doctor has no available schedule.");
            return null;
        }
        for (TimeSlot slot : doctor.getSchedule().getSlots()) {
            if (!doctor.hasConflictingAppointment(slot)) {
                return bookAppointment(patient, doctor, slot);
            }
        }
        System.out.println("Error: No available slots at all for the doctor.");
        return null;
    }

    /**
     * Overloaded method: Books an appointment with ANY doctor of a specific specialty at a specific TimeSlot.
     */
    public Appointment bookAppointment(Patient patient, String specialty, TimeSlot timeSlot) {
        if (specialty == null || timeSlot == null) {
            System.out.println("Error: Invalid specialty or time slot.");
            return null;
        }
        List<Doctor> specialists = findDoctorsBySpecialty(specialty);
        if (specialists.isEmpty()) {
            System.out.println("Error: No doctors registered under the specialty: " + specialty);
            return null;
        }
        for (Doctor doc : specialists) {
            if (doc.getSchedule() != null && doc.getSchedule().getSlots() != null && doc.getSchedule().getSlots().contains(timeSlot)) {
                if (!doc.hasConflictingAppointment(timeSlot)) {
                    return bookAppointment(patient, doc, timeSlot);
                }
            }
        }
        System.out.println("Error: No doctors of specialty " + specialty + " are available at the given time slot.");
        return null;
    }


    /**
     * Cancels an existing appointment
     * @param appointment The appointment to cancel
     * @return true if cancellation was successful
     */
    public boolean cancelAppointment(Appointment appointment) {
        if (appointment == null || !appointments.contains(appointment)) {
            System.out.println("Error: Appointment not found in system.");
            return false;
        }

        if (!appointment.canBeCancelled()) {
            System.out.println("Error: Appointment cannot be cancelled in its current state.");
            return false;
        }

        // Cancel the appointment (which also frees the time slot)
        appointment.cancel();

        System.out.println("Appointment " + appointment.getId() + " has been cancelled.");
        return true;
    }

    /**
     * Marks an appointment as completed
     * @param appointment The appointment to complete
     * @return true if marking as completed was successful
     * NOTE: Week 6 Update - COMPLETED status removed. Use cancelAppointment instead.
     */
    public boolean completeAppointment(Appointment appointment) {
        // COMPLETED status no longer exists per Week 6 feedback
        // Appointments are either BOOKED or CANCELLED
        System.out.println("Note: COMPLETED status has been removed. Use cancelAppointment() instead.");
        return false;
    }

    /**
     * Gets an appointment by ID
     * @param appointmentId The appointment ID
     * @return Appointment object or null if not found
     */
    public Appointment findAppointmentById(String appointmentId) {
        if (appointmentId == null) {
            return null;
        }
        return appointmentMap.get(appointmentId);
    }

    /**
     * Gets all appointments in the system
     * @return List of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);  // Return copy
    }

    /**
     * Gets appointments for a specific patient
     * @param patient The patient
     * @return List of patient's appointments
     */
    public List<Appointment> getPatientAppointments(Patient patient) {
        if (patient == null) {
            return new ArrayList<>();
        }
        return patient.getAppointments();
    }

    /**
     * Gets appointments for a specific doctor
     * @param doctor The doctor
     * @return List of doctor's appointments
     */
    public List<Appointment> getDoctorAppointments(Doctor doctor) {
        if (doctor == null) {
            return new ArrayList<>();
        }
        return doctor.getAppointments();
    }

    /**
     * Gets total number of appointments
     * @return Appointment count
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    /**
     * Gets count of active appointments
     * @return Number of active appointments
     */
    public int getActiveAppointmentCount() {
        return (int) appointments.stream()
                .filter(Appointment::isActive)
                .count();
    }

    // --- REPORTING AND STATISTICS ---

    /**
     * Generates a system status report
     * @return Formatted report string
     */
    public String generateSystemReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n========================================\n");
        report.append(HOSPITAL_NAME + " - SYSTEM REPORT\n");
        report.append("========================================\n");
        report.append("Total Patients: ").append(getPatientCount()).append("\n");
        report.append("Total Doctors: ").append(getDoctorCount()).append("\n");
        report.append("Total Appointments: ").append(getAppointmentCount()).append("\n");
        report.append("Active Appointments: ").append(getActiveAppointmentCount()).append("\n");
        report.append("========================================\n");
        return report.toString();
    }

    /**
     * Displays all patients in the system using displayInfo()
     */
    public void displayAllPatients() {
        System.out.println("\n--- PATIENTS ---");
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
            return;
        }
        for (Patient p : patients) {
            p.displayInfo();
            System.out.println();
        }
    }

    /**
     * Displays all doctors in the system using displayInfo()
     */
    public void displayAllDoctors() {
        System.out.println("\n--- DOCTORS ---");
        if (doctors.isEmpty()) {
            System.out.println("No doctors registered.");
            return;
        }
        for (Doctor d : doctors) {
            d.displayInfo();
            System.out.println();
        }
    }

    /**
     * Displays all appointments in the system using displayInfo()
     */
    public void displayAllAppointments() {
        System.out.println("\n--- APPOINTMENTS ---");
        if (appointments.isEmpty()) {
            System.out.println("No appointments booked.");
            return;
        }
        for (Appointment a : appointments) {
            a.displayInfo();
            System.out.println();
        }
    }

    // --- SEARCHABLE INTERFACE METHODS ---

    /**
     * Searches for a patient or doctor by name
     * Implements Searchable interface
     * @param name The name to search for
     * @return A Patient or Doctor object if found, or null if not found
     */
    public Object searchByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        
        // First try to find a patient
        Patient patient = findPatientByName(name);
        if (patient != null) {
            return patient;
        }
        
        // If no patient found, try to find a doctor
        Doctor doctor = findDoctorByName(name);
        if (doctor != null) {
            return doctor;
        }
        
        // If neither found, return null
        return null;
    }

    /**
     * Gets all records in the system (patients, doctors, appointments)
     * Implements Searchable interface
     * @return A combined list of all records
     */
    public List<?> getAllRecords() {
        List<Object> allRecords = new ArrayList<>();
        allRecords.addAll(patients);
        allRecords.addAll(doctors);
        allRecords.addAll(appointments);
        return allRecords;
    }

    /**
     * Gets the total count of all records in the system
     * Implements Searchable interface
     * @return Total count of patients + doctors + appointments
     */
    public int getRecordCount() {
        return patients.size() + doctors.size() + appointments.size();
    }

    /**
     * Gets the hospital name
     * @return Hospital name
     */
    public static String getHospitalName() {
        return HOSPITAL_NAME;
    }
}
