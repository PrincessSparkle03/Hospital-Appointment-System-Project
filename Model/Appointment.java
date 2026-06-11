package Model;

public class Appointment implements Displayable, StatusManageable {

    private String id;
    private Patient patient;
    private Doctor doctor;
    private TimeSlot timeSlot;
    private AppointmentStatus status;
    private static int totalAppointmentsCreated = 0;

    public Appointment(String id, Patient patient, Doctor doctor, String status) {
        this(id, patient, doctor, null, parseStatus(status));
    }

    public Appointment(String id, Patient patient, Doctor doctor, TimeSlot timeSlot) {
        this(id, patient, doctor, timeSlot, AppointmentStatus.BOOKED);
    }

    public Appointment(String id, Patient patient, Doctor doctor, TimeSlot timeSlot, AppointmentStatus status) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.timeSlot = timeSlot;
        setStatus(status);
        totalAppointmentsCreated++;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    @Override
    public AppointmentStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(AppointmentStatus newStatus) {
        this.status = (newStatus == null) ? AppointmentStatus.BOOKED : newStatus;
    }

    @Override
    public boolean hasStatus(AppointmentStatus status) {
        return this.status == status;
    }

    public boolean isActive() {
        return status != AppointmentStatus.CANCELLED;
    }

    public boolean cancel() {
        if (status == AppointmentStatus.CANCELLED) {
            return false;
        }
        status = AppointmentStatus.CANCELLED;
        return true;
    }

    public String getStatusDisplay() {
        return status.getDisplayValue();
    }

    public static int getTotalAppointmentsCreated() {
        return totalAppointmentsCreated;
    }

    @Override
    public void displayInfo() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Receipt " + id +
                "\nPatient: " + patient.getName() +
                "\nDoctor: " + doctor.getName() +
                "\nCreated By: Hospital System" +
                (timeSlot == null ? "" : "\nTime Slot: " + timeSlot.getSlotDetails()) +
                "\nStatus: " + getStatusDisplay();
    }

    private static AppointmentStatus parseStatus(String status) {
        if (status == null) {
            return AppointmentStatus.BOOKED;
        }
        if (status.equalsIgnoreCase("cancelled") || status.equalsIgnoreCase("canceled")) {
            return AppointmentStatus.CANCELLED;
        }
        return AppointmentStatus.BOOKED;
    }
}
