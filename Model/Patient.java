package Model;

public class Patient extends Person {

    public Patient(String id, String name, String phone) {
        super(id, name, phone);
    }

    @Override
    public String getCreatedBy() {
        return "Hospital System - Patient Registration";
    }

    @Override
    public void displayInfo() {
        System.out.println("Patient ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phone);
        System.out.println("Created By: " + getCreatedBy());
    }
}