
package Model;

/**
 * Person Class - Superclass for Patient and Doctor
 * 
 * Represents a person in the hospital system.
 * Stores common attributes shared by all people (patients and doctors).
 * 
 * This is a foundational class that demonstrates inheritance:
 * - Patient extends Person (A patient IS a person)
 * - Doctor extends Person (A doctor IS a person)
 * 
 * Access Modifiers:
 * - Protected: name, phone (shared with subclasses)
 * - Private: None (all data is protected for subclass access)
 * - Public: Getters, setters, and accessor methods (interface)
 */
public class Person implements Displayable {
    // Protected fields - accessible to subclasses
    protected String id;
    protected String name;
    protected String phone;

    /**
     * Constructor: Creates a new person with basic information
     * @param id Person's unique identifier
     * @param name Person's full name
     * @param phone Person's contact phone number
     */
    public Person(String id, String name, String phone) {
        // Call setters to trigger validation logic
        setId(id);
        setName(name);
        setPhone(phone);
    }

    // --- SETTERS (Mutators) with Business Logic ---

    /**
     * Sets the person ID
     * Logic: A person ID cannot be empty
     * @param id The person's unique identifier
     */
    public void setId(String id) {
        // Logic: An ID cannot be empty
        if (id == null || id.trim().isEmpty()) {
            this.id = "UNKNOWN";
        } else {
            this.id = id;
        }
    }

    /**
     * Sets the person name
     * Logic: A person name cannot be empty or too short
     * @param name The person's full name
     */
    public void setName(String name) {
        // Logic: A name cannot be empty or too short
        if (name == null || name.trim().length() < 2) {
            this.name = "Invalid Name";
        } else {
            this.name = name;
        }
    }

    /**
     * Sets the person phone number
     * Logic: Validates phone number length
     * @param phone The person's phone number
     */
    public void setPhone(String phone) {
        // Logic: A phone number should usually have at least 10 digits
        if (phone != null && phone.replaceAll("[^0-9]", "").length() >= 10) {
            this.phone = phone;
        } else {
            this.phone = "Invalid Phone Number";
        }
    }

    // --- GETTERS (Accessors) with Business Logic ---

    /**
     * Gets the person ID
     * @return The person's unique identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the person name
     * @return The person's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the person phone number (masked for privacy)
     * @return Phone number with first digits masked
     */
    public String getPhone() {
        // Logic: Mask the phone number for privacy (Security logic)
        // Show only the last 4 digits: *******1234
        if (phone != null && phone.length() > 4) {
            return "*******" + phone.substring(phone.length() - 4);
        }
        return phone;
    }

    /**
     * Gets the raw phone number (unmasked)
     * Used for internal hospital operations
     * @return Phone number as stored
     */
    public String getPhoneRaw() {
        return phone;
    }

    /**
     * Displays common person information.
     * Overridden by subclasses to print additional attributes.
     */
    @Override
    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Phone: " + getPhone());
    }
}
