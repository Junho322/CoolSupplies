package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.Parent;
import ca.mcgill.ecse.coolsupplies.model.SchoolAdmin;

import java.util.ArrayList;
import java.util.List;

public class CoolSuppliesFeatureSet1Controller {

    /**
     * Updates the admin's password to the provided password.
     *
     * @param password The new password for the admin.
     * @return A success message if the admin's password was updated.
     * @throws IllegalArgumentException if the admin is not found.
     * @author Jack McDonald
     */
    public static String updateAdmin(String password) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        if (coolSupplies.hasAdmin()) {
            SchoolAdmin admin = coolSupplies.getAdmin();
            admin.setPassword(password);
            return "Admin password updated successfully";
        }
        throw new IllegalArgumentException("Admin not found");
    }

    /**
     * Adds a parent to the system with the provided information.
     *
     * @param email The email of the parent.
     * @param password The password of the parent.
     * @param name The name of the parent.
     * @param phoneNumber The phone number of the parent.
     * @return A success message if the parent was added.
     * @throws IllegalArgumentException if the parent already exists.
     * @author Jack McDonald
     */
    public static String addParent(String email, String password, String name, int phoneNumber) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        try {
            coolSupplies.addParent(email, password, name, phoneNumber);
            return "Parent added successfully";
        } catch (Exception e) {
            throw new IllegalArgumentException("Parent already exists");
        }
    }

    /**
     * Updates a parent's information with the provided information.
     *
     * @param email The email of the parent.
     * @param newPassword The new password for the parent.
     * @param newName The new name for the parent.
     * @param newPhoneNumber The new phone number for the parent.
     * @return A success message if the parent was updated.
     * @throws IllegalArgumentException if the parent is not found.
     * @author Jack McDonald
     */
    public static String updateParent(String email, String newPassword, String newName,
                                      int newPhoneNumber) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        for (Parent parent : coolSupplies.getParents()) {
            if (parent.getEmail().equals(email)) {
                parent.setPassword(newPassword);
                parent.setName(newName);
                parent.setPhoneNumber(newPhoneNumber);
                return "Parent updated successfully";
            }
        }
        throw new IllegalArgumentException("Parent not found");
    }

    /**
     * Deletes a parent from the system with the provided email.
     *
     * @param email The email of the parent to be deleted.
     * @return A success message if the parent was deleted.
     * @throws IllegalArgumentException if the parent is not found.
     * @author Jack McDonald
     */
    public static String deleteParent(String email) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        for (Parent parent : coolSupplies.getParents()) {
            if (parent.getEmail().equals(email)) {
                coolSupplies.removeParent(parent);
                return "Parent deleted successfully";
            }
        }
        throw new IllegalArgumentException("Parent not found");
    }

    /**
     * Retrieves a parent by email.
     *
     * @param email The email of the parent to retrieve.
     * @return A TOParent object representing the parent.
     * @throws IllegalArgumentException if the parent cannot be found by email.
     * @author Jack McDonald
     */
    public static TOParent getParent(String email) {
        CoolSupplies coolsupplies = CoolSuppliesApplication.getCoolSupplies();
        for (Parent parent : coolsupplies.getParents()) {
            if (parent.getEmail().equals(email)) {
                return new TOParent(email, parent.getPassword(), parent.getName(), parent.getPhoneNumber());
            }
        }
        throw new IllegalArgumentException("Parent not found");
    }

    /**
     * Retrieves all parents in the system.
     *
     * @return A list of TOParent objects representing all parents in the system.
     * @throws IllegalArgumentException if there are no parents in the system.
     * @author Jack McDonald
     */
    public static List<TOParent> getParents() {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        List<TOParent> parents = new ArrayList<>();
        for (Parent parent : coolSupplies.getParents()) {
            parents.add(new TOParent(parent.getEmail(), parent.getPassword(), parent.getName(), parent.getPhoneNumber()));
        }
        if (parents.isEmpty()) {
            throw new IllegalArgumentException("No parents found in system");
        }
        return parents;
    }

}
