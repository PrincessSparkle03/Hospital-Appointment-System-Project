package Model;

public class Doctor extends Person {

    private String specialization;

    public Doctor(String id, String name, String phone, String specialization) {
        super(id, name, phone);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String getCreatedBy() {
        return "Hospital System - Doctor Registry";
    }

    @Override
    public void displayInfo() {
        System.out.println("Doctor ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phone);
        System.out.println("Specialization: " + specialization);
        System.out.println("Created By: " + getCreatedBy());
    }
}