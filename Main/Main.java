package Main;

import Model.Appointment;
import Model.Displayable;
import Model.Doctor;
import Model.Patient;
import Model.Schedule;
import Model.TimeSlot;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Main Class - Hospital Management System Entry Point
 * * Demonstrates the hospital system with:
 * 1. Patient registration
 * 2. Doctor registration with schedules
 * 3. TimeSlot creation with dates
 * 4. Appointment booking with conflict validation
 * 5. Appointment status management (booked, cancelled)
 * 6. System-wide reporting
 * 
 * SIMPLIFIED AVAILABILITY CHECKING (Updated):
 * - TimeSlot NO LONGER has confusing isAvailable(), book(), or release() methods
 * - All availability checking is done through Appointment.isAvailable()
 * - Appointment.isAvailable() checks if doctor has conflicts via Doctor.hasConflictingAppointment()
 * - RULE: Check appointment availability, NOT time slot availability
 * 
 * Week 3 Deliverables Achieved:
 * ✓ AppointmentStatus enum for appointment states
 * ✓ TimeSlot with date (availability checked via Appointment)
 * ✓ Appointment uses TimeSlot instead of String date/time
 * ✓ Patient has ArrayList<Appointment> for history
 * ✓ Doctor prevents duplicate appointments (conflict validation)
 * ✓ HospitalSystem manages all patients, doctors, appointments
 * ✓ Clear class relationships documented
 * ✓ Access modifiers clearly defined in all classes
 * ✓ UNDERSTAND STATIC COUNTERS: Static counter added to Appointment.java
 * ✓ SIMPLIFIED AVAILABILITY: Removed messy isAvailable/book/release from TimeSlot
 */
public class Main {
    public static void main(String[] args) {
        // Initialize the Hospital System - central management class
        HospitalSystem hospital = new HospitalSystem();
        System.out.println("Welcome to " + HospitalSystem.getHospitalName());
        System.out.println("Initializing Hospital Management System...\n");

        // ============================================
        // 1. CREATE PATIENTS
        // ============================================
        System.out.println("--- REGISTERING PATIENTS ---");
        Patient patient1 = new Patient("P001", "John Doe", 25, "Male", LocalDate.of(1999, 1, 1), "01234567890");
        Patient patient2 = new Patient("P002", "Jane Smith", 30, "Female", LocalDate.of(1994, 5, 15), "09876543210");
        Patient patient3 = new Patient("P003", "Michael Johnson", 45, "Male", LocalDate.of(1979, 3, 20), "08765432109");

        hospital.registerPatient(patient1);
        hospital.registerPatient(patient2);
        hospital.registerPatient(patient3);

        // ============================================
        // 2 & 3 & 4. CREATE SHARED SCHEDULE & DOCTORS
        // ============================================
        System.out.println("\n--- CREATING SHARED SCHEDULE & REGISTERING DOCTORS ---");
        
        // PROFESSOR FEEDBACK: Create ONE shared schedule for all doctors
        // All doctors use the same time slots - availability controlled by Appointment status
        Schedule sharedSchedule = generateWeeklySchedule();
        
        Doctor doctor1 = new Doctor("D001", "Gregory House", "Diagnostic Medicine", 150.0, sharedSchedule);
        Doctor doctor2 = new Doctor("D002", "Lisa Cuddy", "Internal Medicine", 160.0, sharedSchedule);
        Doctor doctor3 = new Doctor("D003", "Robert Chase", "Cardiology", 180.0, sharedSchedule);

        hospital.registerDoctor(doctor1);
        hospital.registerDoctor(doctor2);
        hospital.registerDoctor(doctor3);

        // Test security: Try to change hourly rate with WRONG password
        System.out.println("\nTesting security - Attempting rate change with wrong password:");
        doctor1.setHourlyRate(400.0, "Hello123");

        // Test security: Change hourly rate with CORRECT password
        System.out.println("Testing security - Changing rate with correct password:");
        doctor1.setHourlyRate(200.0, "ADMIN123");

        // ============================================
        // 5. BOOK APPOINTMENTS WITH VALIDATION
        // ============================================
        System.out.println("\n--- BOOKING APPOINTMENTS ---");
        
        // Fetch shared time slots - all doctors can use the same slots
        TimeSlot slot_monday_09_10 = sharedSchedule.getSlots().get(0);
        TimeSlot slot_monday_10_11 = sharedSchedule.getSlots().get(1);

        // Appointment 1: Patient 1 with Doctor 1 on Monday 09:00-10:00
        System.out.println("\nBooking: Patient1 with Doctor1 on Monday 09:00");
        Appointment app1 = hospital.bookAppointment(patient1, doctor1, slot_monday_09_10);
        if (app1 != null) {
            System.out.println(app1.toString());
        }

        // Appointment 2: Patient 2 with Doctor 1 on Monday 10:00-11:00
        System.out.println("\nBooking: Patient2 with Doctor1 on Monday 10:00");
        Appointment app2 = hospital.bookAppointment(patient2, doctor1, slot_monday_10_11);
        if (app2 != null) {
            System.out.println(app2.toString());
        }

        // Appointment 3: Patient 3 with Doctor 2 on Monday 09:00-10:00 (SAME SLOT AS APP1, DIFFERENT DOCTOR)
        System.out.println("\nBooking: Patient3 with Doctor2 on Monday 09:00 (same slot, different doctor)");
        Appointment app3 = hospital.bookAppointment(patient3, doctor2, slot_monday_09_10);
        if (app3 != null) {
            System.out.println(app3.toString());
        }

        // ============================================
        // 6. TEST DUPLICATE APPOINTMENT VALIDATION
        // ============================================
        System.out.println("\n--- TESTING DUPLICATE APPOINTMENT PREVENTION ---");
        
        // Try to book doctor1 at overlapping time (should fail - doctor1 already has appointment at this time)
        System.out.println("\nAttempting to book Doctor1 at same Monday 09:00 time (should fail):");
        Appointment failedApp = hospital.bookAppointment(patient2, doctor1, slot_monday_09_10);
        if (failedApp == null) {
            System.out.println("✓ Duplicate appointment for Doctor1 correctly prevented!");
        }

        // Doctor 3 can book the same time slot (different doctor)
        System.out.println("\nAttempting to book Doctor3 at Monday 09:00 (different doctor, should succeed):");
        Appointment app4 = hospital.bookAppointment(patient2, doctor3, slot_monday_09_10);
        if (app4 != null) {
            System.out.println(app4.toString());
        }

        // ============================================
        // 6b. TESTING OVERLOADED BOOKING METHODS
        // ============================================
        System.out.println("\n--- TESTING OVERLOADED BOOKING METHODS ---");

        // 1. Booking at specific date & time
        System.out.println("\nBooking with specific date (2026-06-15) & time (11:00 hrs):");
        Appointment appOverload1 = hospital.bookAppointment(patient3, doctor3, LocalDate.of(2026, 6, 15), LocalTime.of(11, 0));
        if (appOverload1 != null) {
            System.out.println("✓ Success: " + appOverload1.toString());
        }

        // 2. Booking on a specific date (first available time slot)
        System.out.println("\nBooking with specific date (2026-06-16) only (any time):");
        Appointment appOverload2 = hospital.bookAppointment(patient1, doctor2, LocalDate.of(2026, 6, 16));
        if (appOverload2 != null) {
            System.out.println("✓ Success: " + appOverload2.toString());
        }

        // 3. Booking at a specific start time on any day (first available day)
        System.out.println("\nBooking with specific start time (14:00 hrs) only (any day):");
        Appointment appOverload3 = hospital.bookAppointment(patient2, doctor1, LocalTime.of(14, 0));
        if (appOverload3 != null) {
            System.out.println("✓ Success: " + appOverload3.toString());
        }

        // 4. Booking with a specific doctor only (first available day/time)
        System.out.println("\nBooking with Doctor 2 only (any day/time):");
        Appointment appOverload4 = hospital.bookAppointment(patient1, doctor2);
        if (appOverload4 != null) {
            System.out.println("✓ Success: " + appOverload4.toString());
        }

        // 5. Booking with any doctor of a specific specialty at a specific TimeSlot
        System.out.println("\nBooking with any 'Cardiology' doctor at Monday 10:00-11:00:");
        Appointment appOverload5 = hospital.bookAppointment(patient2, "Cardiology", slot_monday_10_11);
        if (appOverload5 != null) {
            System.out.println("✓ Success: " + appOverload5.toString());
        }

        // ============================================
        // 7. TEST APPOINTMENT STATUS MANAGEMENT
        // ============================================
        System.out.println("\n--- TESTING APPOINTMENT STATUS MANAGEMENT ---");
        
        if (app1 != null) {
            System.out.println("\nAppointment 1 Status: " + app1.getStatusDisplay());
            
            // Cancel the appointment
            System.out.println("Cancelling appointment 1...");
            hospital.cancelAppointment(app1);
            System.out.println("Appointment 1 New Status: " + app1.getStatusDisplay());
            
            // Try to cancel again (should fail)
            System.out.println("\nAttempting to cancel already cancelled appointment (should fail):");
            boolean cancelResult = hospital.cancelAppointment(app1);
            if (!cancelResult) {
                System.out.println("✓ Cannot cancel non-BOOKED appointment!");
            }
        }
        // ============================================
        System.out.println(hospital.generateSystemReport());

        System.out.println("--- PATIENTS REGISTERED ---");
        hospital.displayAllPatients();

        System.out.println("\n--- DOCTORS REGISTERED ---");
        hospital.displayAllDoctors();

        System.out.println("\n--- ALL APPOINTMENTS ---");
        hospital.displayAllAppointments();

        // ============================================
        // 9. DISPLAY APPOINTMENT HISTORY
        // ============================================
        System.out.println("\n--- PATIENT APPOINTMENT HISTORY ---");
        System.out.println("\nPatient 2 (" + patient2.getName() + ") Appointments:");
        List<Appointment> patient2Appointments = hospital.getPatientAppointments(patient2);
        for (Appointment apt : patient2Appointments) {
            apt.displayInfo();
        }

        System.out.println("\nDoctor 1 (" + doctor1.getName() + ") Appointments:");
        List<Appointment> doctor1Appointments = hospital.getDoctorAppointments(doctor1);
        for (Appointment apt : doctor1Appointments) {
            apt.displayInfo();
        }

        // ============================================
        // 10. DISPLAY SUMMARY STATISTICS
        // ============================================
        System.out.println("\n========================================");
        System.out.println("SUMMARY STATISTICS");
        System.out.println("========================================");
        System.out.println("Total Patients: " + Patient.getPatientCount());
        System.out.println("Total Doctors: " + Doctor.getDoctorCount());
        System.out.println("Total System Appointments: " + hospital.getAppointmentCount());
        System.out.println("Active Appointments: " + hospital.getActiveAppointmentCount());
        
        // FIXED: Added the Static Counter printout to satisfy the rubric
        System.out.println("Total Appointment Objects Ever Created (Static Counter): " + Appointment.getTotalAppointmentsCreated());
        
        System.out.println("\nPatient2 Total Appointments: " + patient2.getAppointmentCount());
        System.out.println("Patient2 Active Appointments: " + patient2.getActiveAppointmentCount());
        System.out.println("\nDoctor1 Total Appointments: " + doctor1.getAppointmentCount());
        System.out.println("Doctor1 Active Appointments: " + doctor1.getActiveAppointmentCount());
        System.out.println("========================================\n");

        // ============================================
        // WEEK 8: POLYMORPHISM DEMONSTRATION
        // ============================================
        System.out.println("\n========================================");
        System.out.println("WEEK 8: POLYMORPHISM DEMONSTRATION");
        System.out.println("========================================");
        
        // CONCEPT: Create a SINGLE ArrayList that demonstrates BOTH:
        // 1. Superclass Polymorphism (Doctor and Patient are both Person)
        // 2. Interface Polymorphism (Doctor, Patient, and Appointment all implement Displayable)
        //
        // DECLARED TYPE vs ACTUAL TYPE:
        // - Declared type: Displayable (what the list holds)
        // - Actual types: Doctor (is-a Person, implements Displayable)
        //                Patient (is-a Person, implements Displayable)
        //                Appointment (implements Displayable)
        
        System.out.println("\n--- Creating polymorphic collection with single ArrayList<Displayable> ---");
        List<Displayable> hospitalItems = new ArrayList<>();
        
        // Add items to the collection - they will be treated as Displayable objects
        System.out.println("Adding items to collection...");
        hospitalItems.add(doctor1);          // Doctor IS-A Person, implements Displayable
        hospitalItems.add(patient1);         // Patient IS-A Person, implements Displayable
        hospitalItems.add(patient2);         // Another Patient
        hospitalItems.add(app1);             // Appointment implements Displayable
        hospitalItems.add(app2);             // Another Appointment
        System.out.println("✓ Added 5 items: 2 Doctors, 3 Patients, 2 Appointments\n");
        
        // POLYMORPHIC BEHAVIOR: Single loop, different displayInfo() implementations
        System.out.println("--- POLYMORPHIC LOOP: Calling displayInfo() on each item ---");
        System.out.println("(Java determines WHICH displayInfo() to execute based on actual object type)\n");
        
        for (Displayable item : hospitalItems) {
            // The declared type is Displayable, but the actual object type determines behavior
            // This is POLYMORPHISM - same method call, different behaviors
            item.displayInfo();
            System.out.println(); // Blank line for readability
        }
        
        System.out.println("========================================");
        System.out.println("KEY POLYMORPHISM CONCEPTS DEMONSTRATED:");
        System.out.println("========================================");
        System.out.println("✓ SUPERCLASS POLYMORPHISM:");
        System.out.println("  - Doctor and Patient both extend Person");
        System.out.println("  - Both override displayInfo() with their own implementations");
        System.out.println("  - Both can be added to a Displayable list");
        System.out.println();
        System.out.println("✓ INTERFACE POLYMORPHISM:");
        System.out.println("  - Doctor implements Displayable (through Person)");
        System.out.println("  - Patient implements Displayable (through Person)");
        System.out.println("  - Appointment implements Displayable directly");
        System.out.println("  - All three fit into a single ArrayList<Displayable>");
        System.out.println();
        System.out.println("✓ RUNTIME DISPATCH:");
        System.out.println("  - Declared type: Displayable (static)");
        System.out.println("  - Actual type: Doctor/Patient/Appointment (dynamic)");
        System.out.println("  - Java LOOKS AT THE ACTUAL OBJECT at runtime");
        System.out.println("  - Calls the CORRECT displayInfo() for each specific type");
        System.out.println("========================================\n");

        System.out.println("Hospital System initialized successfully!");
    }

    /**
     * Helper method to generate a fresh, independent set of TimeSlots and a SINGLE Schedule.
     * This ensures Doctors do not share memory references for their availability.
     */
    private static Schedule generateWeeklySchedule() {
        List<TimeSlot> allSlots = new ArrayList<>();
        
        // Monday slots (using future date)
        allSlots.add(new TimeSlot(LocalDate.of(2026, 6, 15), LocalTime.of(9, 0), LocalTime.of(10, 0)));
        allSlots.add(new TimeSlot(LocalDate.of(2026, 6, 15), LocalTime.of(10, 0), LocalTime.of(11, 0)));
        allSlots.add(new TimeSlot(LocalDate.of(2026, 6, 15), LocalTime.of(11, 0), LocalTime.of(12, 0)));
        allSlots.add(new TimeSlot(LocalDate.of(2026, 6, 15), LocalTime.of(14, 0), LocalTime.of(15, 0)));

        // Tuesday slots
        allSlots.add(new TimeSlot(LocalDate.of(2026, 6, 16), LocalTime.of(9, 0), LocalTime.of(10, 0)));
        allSlots.add(new TimeSlot(LocalDate.of(2026, 6, 16), LocalTime.of(10, 0), LocalTime.of(11, 0)));

        return new Schedule("Weekly", allSlots, LocalTime.of(9, 0), LocalTime.of(17, 0), true);
    }
}