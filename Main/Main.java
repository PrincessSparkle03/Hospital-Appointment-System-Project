package Main;

import Model.Appointment;
import Model.Displayable;
import Model.Doctor;
import Model.Patient;
import Model.Person;
import Model.Schedule;
import Model.TimeSlot;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ========================================
 * WEEK 8: POLYMORPHISM DEMONSTRATION
 * ========================================
 * 
 * This program demonstrates the power of Polymorphism in Object-Oriented Programming:
 * - How a single parent reference (Person) can represent different child types
 * - How interfaces allow unrelated classes to be treated uniformly
 * - Runtime type determination (Declared type vs Actual type)
 * 
 * Uses both:
 * 1. HospitalSystem (for complete hospital management)
 * 2. Direct polymorphic demonstrations
 * 
 * Key Questions:
 * "Can my system treat different objects generally while still respecting their specific behaviors?"
 * Answer: YES! This is the magic of polymorphism.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          WEEK 8: POLYMORPHISM MASTERY DEMONSTRATION            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");

        // ============================================
        // INITIALIZE HOSPITAL SYSTEM
        // ============================================
        System.out.println("INITIALIZING HOSPITAL SYSTEM...\n");
        HospitalSystem hospital = new HospitalSystem();
        System.out.println("✓ Hospital System Ready: " + HospitalSystem.getHospitalName() + "\n");

        // ========================================
        // STEP 1: CREATE OBJECTS (Different Types)
        // ========================================
        System.out.println("STEP 1: Creating Hospital System Objects...\n");
        
        // Create Patients (subclass of Person)
        Patient p1 = new Patient("P001", "Alice Johnson", 32, "Female", LocalDate.of(1994, 3, 14), "1234567890");
        Patient p2 = new Patient("P002", "Bob Smith", 41, "Male", LocalDate.of(1985, 8, 22), "0987654321");
        
        // Create Doctors (subclass of Person)
        Doctor d1 = new Doctor("D001", "Dr. Emily Wilson", "Cardiology", 125.00, generateWeeklySchedule(), "5555555555");
        Doctor d2 = new Doctor("D002", "Dr. Michael Brown", "Neurology", 135.00, generateWeeklySchedule(), "9999999999");
        
        // Create Appointment (implements Displayable interface)
        Appointment a1 = new Appointment("A001", p1, d1, "Scheduled");
        Appointment a2 = new Appointment("A002", p2, d2, "Confirmed");
        
        System.out.println("✓ Created 2 Patients, 2 Doctors, 2 Appointments\n");

        // ========================================
        // REGISTER WITH HOSPITAL SYSTEM
        // ========================================
        System.out.println("REGISTERING WITH HOSPITAL SYSTEM...\n");
        hospital.registerPatient(p1);
        hospital.registerPatient(p2);
        hospital.registerDoctor(d1);
        hospital.registerDoctor(d2);
        System.out.println();

        // ========================================
        // DISPLAY HOSPITAL STATISTICS
        // ========================================
        System.out.println(hospital.generateSystemReport());

        // ========================================
        // SECTION 1: SUPERCLASS POLYMORPHISM
        // ========================================
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  1. SUPERCLASS POLYMORPHISM: Person ArrayList                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        
        System.out.println("CONCEPT: ArrayList<Person> holds BOTH Patients AND Doctors");
        System.out.println("         - Declared Type: Person (Superclass)\n");
        System.out.println("         - Actual Type: Patient or Doctor (Subclasses)\n");
        System.out.println("         - Java determines which displayInfo() to call at RUNTIME\n");
        
        // Get polymorphic collection from HospitalSystem
        List<Person> personList = hospital.getAllPeople();
        
        System.out.println("POLYMORPHIC LOOP: Same loop, DIFFERENT displayInfo() behaviors!\n");
        System.out.println("------- OUTPUT (from Hospital System) -------\n");
        
        // POLYMORPHISM IN ACTION: Same method call, different behaviors
        for (Person p : personList) {
            p.displayInfo();
            System.out.println();
        }
        
        System.out.println("------- ANALYSIS -------");
        System.out.println("✓ p1 called Patient's displayInfo() (actual type: Patient)");
        System.out.println("✓ p2 called Patient's displayInfo() (actual type: Patient)");
        System.out.println("✓ d1 called Doctor's displayInfo() (actual type: Doctor)");
        System.out.println("✓ d2 called Doctor's displayInfo() (actual type: Doctor)");
        System.out.println("✓ SAME method call → DIFFERENT outputs based on actual object type\n");

        // ========================================
        // SECTION 2: INTERFACE POLYMORPHISM
        // ========================================
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║  2. INTERFACE POLYMORPHISM: Displayable ArrayList             ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        
        System.out.println("CONCEPT: ArrayList<Displayable> holds UNRELATED classes:");
        System.out.println("         - Patient (implements Displayable via Person)");
        System.out.println("         - Doctor (implements Displayable via Person)");
        System.out.println("         - Appointment (implements Displayable directly)\n");
        System.out.println("         All classes fit into ONE collection because they ALL");
        System.out.println("         implement the Displayable interface!\n");
        
        // FIXED: Added the Static Counter printout to satisfy the rubric
        System.out.println("Total Appointment Objects Ever Created (Static Counter): " + Appointment.getTotalAppointmentsCreated());
        
        System.out.println("\nPatient2 Total Appointments: " + p2.getAppointmentCount());
        System.out.println("Patient2 Active Appointments: " + p2.getActiveAppointmentCount());
        System.out.println("\nDoctor1 Total Appointments: " + d1.getAppointmentCount());
        System.out.println("Doctor1 Active Appointments: " + d1.getActiveAppointmentCount());
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
        hospitalItems.add(d1);               // Doctor IS-A Person, implements Displayable
        hospitalItems.add(p1);               // Patient IS-A Person, implements Displayable
        hospitalItems.add(p2);               // Another Patient
        hospitalItems.add(a1);               // Appointment implements Displayable
        hospitalItems.add(a2);               // Another Appointment
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
