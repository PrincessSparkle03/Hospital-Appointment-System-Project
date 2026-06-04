package Model;

import java.util.List;

/**
 * Searchable Interface
 * 
 * Defines search and lookup behavior for system classes that maintain collections
 * and need to provide search capabilities.
 * 
 * Purpose:
 * - Standardize search/query behavior across system management classes
 * - Separate search logic into a dedicated interface
 * - Allow multiple classes to implement consistent search patterns
 * 
 * Implemented by: HospitalSystem
 */
public interface Searchable {
    /**
     * Searches by name (flexible string matching)
     * @param name The name to search for
     * @return An object matching the name, or null if not found
     */
    Object searchByName(String name);
    
    /**
     * Retrieves all records/objects managed by this searchable entity
     * @return A list of all managed objects
     */
    List<?> getAllRecords();
    
    /**
     * Gets the total count of records managed
     * @return Number of records
     */
    int getRecordCount();
}
