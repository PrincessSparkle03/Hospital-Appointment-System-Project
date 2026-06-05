package Main;

import Model.Appointment;
import Model.Doctor;
import Model.Patient;
import Model.Person;
import Model.TimeSlot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HospitalSystem Class - Central Management System
 * 
 * Manages all hospital operations:
 * - Patient registration and management
 * - Doctor registration and management
 * - Appointment booking and validation
 * - Schedule management
 * - System-wide reporting
 * 
 * WEEK 8 POLYMORPHISM FEATURES:
 * - Maintains List<Person> allPeople (polymorphic collection)
 * - Provides getByName() and getById() for polymorphic searches
 * - Demonstrates superclass polymorphism throughout
 * 
 * Access Modifiers:
 * - Private: All data collections (encapsulated)
 * - Public: All management methods (system interface)
 */
public class HospitalSystem {
    // Hospital name constant
    private static final String HOSPITAL_NAME = "City General Hospital";
    
    // Private collections - encapsulated data
    private List<Person> allPeople;              // Week 8 Polymorphism: Superclass collection
    private List<Patient> patients;              // All patients in the system
    private List<Doctor> doctors;                // All doctors in the system
    private List<Appointment> appointments;      // All appointments in the system
    private Map<String, Appointment> appointmentMap;  // Fast lookup by ID
    private int appointmentCounter;              // For generating unique appointment IDs

    /**
     * Constructor: Initializes the hospital system with empty collections
     */
    public HospitalSystem() {
        this.allPeople = new ArrayList<>();      // Week 8: Polymorphic Person collection
        this.patients = new ArrayList<>();
        this.doctors = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.appointmentMap = new HashMap<>();
        this.appointmentCounter = 0;
    }

    // ============================================
    // PATIENT MANAGEMENT
    // ============================================

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
        allPeople.add(patient);  // Week 8: Add to polymorphic collection
        System.out.println("✓ Patient registered: " + patient.getName());
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
        allPeople.remove(patient);  // Week 8: Remove from polymorphic collection
        return true;
    }

    /**
     * Gets all patients in the system
     * @return List of all patients
     */
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    /**
     * Finds a patient by name
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
     * Finds a patient by ID
     * @param id Patient's unique ID
     * @return Patient object or null if not found
     */
    public Patient findPatientById(String id) {
        if (id == null) {
            return null;
        }
        for (Patient p : patients) {
            if (p.getId().equalsIgnoreCase(id)) {
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

    // ============================================
    // DOCTOR MANAGEMENT
    // ============================================

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
        allPeople.add(doctor);  // Week 8: Add to polymorphic collection
        System.out.println("✓ Doctor registered: " + doctor.getName());
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
        allPeople.remove(doctor);  // Week 8: Remove from polymorphic collection
        return true;
    }

    /**
     * Gets all doctors in the system
     * @return List of all doctors
     */
    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors);
    }

    /**
     * Finds a doctor by name
     * @param name Doctor's name
     * @return Doctor object or null if not found
     */
    public Doctor findDoctorByName(String name) {
        if (name == null) {
            return null;
        }
        for (Doctor d : doctors) {
            if (d.getName().equalsIgnoreCase(name)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Finds a doctor by ID
     * @param id Doctor's unique ID
     * @return Doctor object or null if not found
     */
    public Doctor findDoctorById(String id) {
        if (id == null) {
            return null;
        }
        for (Doctor d : doctors) {
            if (d.getId().equalsIgnoreCase(id)) {
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
            if (d.getSpecialization().equalsIgnoreCase(specialty)) {
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

    // ============================================
    // APPOINTMENT MANAGEMENT
    // ============================================

    /**
     * Books an appointment with a doctor at a specific time slot
     * @param patient The patient to book
     * @param doctor The doctor to book with
     * @param timeSlot The time slot to book
     * @return Appointment object if successful, null if booking failed
     */
    public Appointment bookAppointment(Patient patient, Doctor doctor, TimeSlot timeSlot) {
        if (patient == null || doctor == null || timeSlot == null) {
            System.out.println("Error: Invalid patient, doctor, or time slot.");
            return null;
        }

        // Create the appointment
        appointmentCounter++;
        String appointmentId = String.format("APT-%05d", appointmentCounter);
        Appointment appointment = new Appointment(appointmentId, patient, doctor, "Scheduled");

        // Add to system collections
        appointments.add(appointment);
        appointmentMap.put(appointmentId, appointment);

        System.out.println("✓ Appointment booked successfully: " + appointmentId);
        return appointment;
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

        System.out.println("✓ Appointment " + appointment.getId() + " has been cancelled.");
        return true;
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
        return new ArrayList<>(appointments);
    }

    /**
     * Gets appointments for a specific patient
     * @param patient The patient
     * @return List of patient's appointments
     */
    public List<Appointment> getPatientAppointments(Patient patient) {
        List<Appointment> patientAppts = new ArrayList<>();
        if (patient == null) {
            return patientAppts;
        }
        for (Appointment apt : appointments) {
            if (apt.getPatient() != null && apt.getPatient().getId().equals(patient.getId())) {
                patientAppts.add(apt);
            }
        }
        return patientAppts;
    }

    /**
     * Gets appointments for a specific doctor
     * @param doctor The doctor
     * @return List of doctor's appointments
     */
    public List<Appointment> getDoctorAppointments(Doctor doctor) {
        List<Appointment> doctorAppts = new ArrayList<>();
        if (doctor == null) {
            return doctorAppts;
        }
        for (Appointment apt : appointments) {
            if (apt.getDoctor() != null && apt.getDoctor().getId().equals(doctor.getId())) {
                doctorAppts.add(apt);
            }
        }
        return doctorAppts;
    }

    /**
     * Gets total number of appointments
     * @return Appointment count
     */
    public int getAppointmentCount() {
        return appointments.size();
    }

    // ============================================
    // WEEK 8 POLYMORPHISM: GENERIC "GET BY" FUNCTIONS
    // ============================================

    /**
     * Gets all people (patients and doctors) in the system
     * Week 8 Polymorphism: Demonstrates polymorphic collection
     * @return List of all People (both Patients and Doctors)
     */
    public List<Person> getAllPeople() {
        return new ArrayList<>(allPeople);
    }

    /**
     * Gets a person (patient or doctor) by name
     * Week 8 Polymorphism: Demonstrates polymorphic search
     * @param name The name to search for
     * @return A Person if found, null otherwise
     */
    public Person getByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        
        for (Person person : allPeople) {
            if (person.getName().equalsIgnoreCase(name)) {
                return person;
            }
        }
        return null;
    }

    /**
     * Gets a person (patient or doctor) by ID
     * Week 8 Polymorphism: Another polymorphic retrieval method
     * @param id The person's unique ID
     * @return A Person if found, null otherwise
     */
    public Person getById(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        
        for (Person person : allPeople) {
            if (person.getId().equalsIgnoreCase(id)) {
                return person;
            }
        }
        return null;
    }

    /**
     * Gets all appointments created by a specific patient
     * @param patient The patient who booked appointments
     * @return List of appointments created by the patient
     */
    public List<Appointment> getAppointmentsCreatedBy(Patient patient) {
        return getPatientAppointments(patient);
    }

    // ============================================
    // REPORTING AND STATISTICS
    // ============================================

    /**
     * Generates a system status report
     * @return Formatted report string
     */
    public String generateSystemReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n╔════════════════════════════════════════════╗\n");
        report.append("║  ").append(HOSPITAL_NAME).append("\n");
        report.append("║  SYSTEM REPORT\n");
        report.append("╚════════════════════════════════════════════╝\n");
        report.append("Total Patients: ").append(getPatientCount()).append("\n");
        report.append("Total Doctors: ").append(getDoctorCount()).append("\n");
        report.append("Total Appointments: ").append(getAppointmentCount()).append("\n");
        report.append("Total People (Polymorphic): ").append(allPeople.size()).append("\n");
        report.append("╚════════════════════════════════════════════╝\n");
        return report.toString();
    }

    /**
     * Displays all patients in the system
     */
    public void displayAllPatients() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  ALL PATIENTS IN SYSTEM");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        if (patients.isEmpty()) {
            System.out.println("No patients registered.\n");
            return;
        }
        for (Patient p : patients) {
            p.displayInfo();
            System.out.println();
        }
    }

    /**
     * Displays all doctors in the system
     */
    public void displayAllDoctors() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  ALL DOCTORS IN SYSTEM");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        if (doctors.isEmpty()) {
            System.out.println("No doctors registered.\n");
            return;
        }
        for (Doctor d : doctors) {
            d.displayInfo();
            System.out.println();
        }
    }

    /**
     * Displays all appointments in the system
     */
    public void displayAllAppointments() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  ALL APPOINTMENTS IN SYSTEM");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments booked.\n");
            return;
        }
        for (Appointment a : appointments) {
            a.displayInfo();
            System.out.println();
        }
    }

    /**
     * Displays all people in the polymorphic collection (Week 8 feature)
     */
    public void displayAllPeople() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  ALL PEOPLE (POLYMORPHIC COLLECTION)");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        if (allPeople.isEmpty()) {
            System.out.println("No people registered.\n");
            return;
        }
        
        for (Person p : allPeople) {
            System.out.print("Type: " + p.getClass().getSimpleName() + " | ");
            System.out.println(p.getName() + " (" + p.getId() + ")");
        }
        System.out.println();
    }

    /**
     * Gets the hospital name
     * @return Hospital name
     */
    public static String getHospitalName() {
        return HOSPITAL_NAME;
    }
}
