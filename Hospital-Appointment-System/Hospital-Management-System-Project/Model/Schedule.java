package Model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Schedule implements Displayable, Bookable {
    private String day;
    private List<TimeSlot> slots;
    private LocalTime startTime;                // Using LocalTime for proper time handling
    private LocalTime endTime;                  // Using LocalTime for proper time handling
    private boolean isAvailable;                // If false, the doctor is on holiday/leave that day

    public Schedule(String day, List<TimeSlot> slots, LocalTime startTime, LocalTime endTime, boolean isAvailable) {
        setDay(day);
        setStartTime(startTime);
        setEndTime(endTime);
        this.isAvailable = isAvailable;
        setSlots(slots);
    }

    // --- SETTERS (Mutators) with Business Logic ---

    public void setDay(String day) {
        // Logic: Standardize day names to prevent "monday" vs "Monday" issues
        if (day == null || day.isEmpty()) {
            this.day = "To Be Determined";
        } else {
            this.day = day.substring(0, 1).toUpperCase() + day.substring(1).toLowerCase();
        }
    }

    public void setStartTime(LocalTime startTime) {
        // Logic: Default to standard opening hours if input is invalid
        if (startTime == null) {
            this.startTime = LocalTime.of(9, 0);
        } else {
            this.startTime = startTime;
        }
    }

    /**
     * Sets the start time from a String (alternative for parsing)
     * @param startTimeString The start time as a string (HH:MM format)
     */
    public void setStartTimeFromString(String startTimeString) {
        try {
            LocalTime parsedTime = LocalTime.parse(startTimeString);
            setStartTime(parsedTime);
        } catch (Exception e) {
            System.out.println("Warning: Invalid start time format. Expected HH:MM");
            this.startTime = LocalTime.of(9, 0);
        }
    }

    public void setEndTime(LocalTime endTime) {
        /* 
           Logic: CRITICAL BUSINESS RULE
           The End Time cannot be BEFORE the Start Time.
           If it is, we automatically set it to 1 hour after Start Time.
        */
        if (endTime == null || (this.startTime != null && endTime.isBefore(this.startTime))) {
            this.endTime = LocalTime.of(17, 0); // Fallback to standard closing
        } else {
            this.endTime = endTime;
        }
    }

    /**
     * Sets the end time from a String (alternative for parsing)
     * @param endTimeString The end time as a string (HH:MM format)
     */
    public void setEndTimeFromString(String endTimeString) {
        try {
            LocalTime parsedTime = LocalTime.parse(endTimeString);
            setEndTime(parsedTime);
        } catch (Exception e) {
            System.out.println("Warning: Invalid end time format. Expected HH:MM");
            this.endTime = LocalTime.of(17, 0);
        }
    }

    public void setSlots(List<TimeSlot> slots) {
        // Logic: Prevent NullPointerException
        if (slots == null) {
            this.slots = new ArrayList<>();
        } else {
            this.slots = slots;
        }
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    // --- GETTERS (Accessors) with Business Logic ---

    public String getDay() {
        return this.day;
    }

    /**
     * Gets the start time as LocalTime
     * @return The start time (LocalTime)
     */
    public LocalTime getStartTime() {
        return this.startTime;
    }

    /**
     * Gets the end time as LocalTime
     * @return The end time (LocalTime)
     */
    public LocalTime getEndTime() {
        return this.endTime;
    }

    public String getShiftDuration() {
        // Logic: A custom getter that creates a "View" of the full shift
        return this.startTime + " to " + this.endTime;
    }

    public List<TimeSlot> getSlots() {
        // Logic: If the doctor is NOT available today, return an empty list
        // so no one can book a slot.
        if (!this.isAvailable) {
            return new ArrayList<>();
        }
        return slots;
    }

    public boolean isAvailable() {
        // Logic: A doctor is only available if the flag is true AND they have slots
        return isAvailable && (slots != null && !slots.isEmpty());
    }

    // --- DISPLAYABLE INTERFACE METHODS ---

    @Override
    public void displayInfo() {
        System.out.println("==== SCHEDULE DETAILS ====");
        System.out.println("Day: " + day);
        System.out.println("Shift: " + getShiftDuration());
        System.out.println("Schedule Available: " + (isAvailable ? "Yes" : "No (On leave/Holiday)"));
        System.out.println("Total Time Slots: " + (slots != null ? slots.size() : 0));
        System.out.println("Note: Slot availability is checked per appointment via Doctor");
        System.out.println("===========================");
    }

    // --- BOOKABLE INTERFACE METHODS ---

    /**
     * Marks this schedule as booked/unavailable (doctor is fully booked)
     * Implements Bookable interface
     */
    @Override
    public void book() {
        this.isAvailable = false;
    }

    /**
     * Marks this schedule as released/available again (doctor has free slots)
     * Implements Bookable interface
     */
    @Override
    public void release() {
        this.isAvailable = true;
    }
}