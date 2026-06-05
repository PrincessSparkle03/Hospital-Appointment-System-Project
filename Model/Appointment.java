package Model;

public class Appointment implements Displayable {

    private String id;
    private Patient patient;
    private Doctor doctor;
    private String status;

    public Appointment(String id, Patient patient, Doctor doctor, String status) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public String getStatusDisplay() {
        return status;
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
                "\nStatus: " + status;
    }
}