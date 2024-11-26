package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.*;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CoolSuppliesFeatureSet1Controller {

    private static final String EMAIL_PATTERN = //regex pattern for email validation
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    /**
     * Updates the admin's password to the provided password.
     *
     * @param password The new password for the admin.
     * @return A success message if the admin's password was updated or an appropriate error message.
     * @author Jack McDonald
     */
    public static String updateAdmin(String password) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        SchoolAdmin admin = coolSupplies.getAdmin();

        if (password.length() < 4) // password length
            return "Password must be at least four characters long.";

        if (!password.matches(".*[!#$].*") || // special character
                !password.matches(".*[A-Z].*") || // upper case
                !password.matches(".*[a-z].*")) // lower case
            return "Password must contain a special character out of !#$, an upper case character, " +
                    "and a lower case character.";

        try {
            admin.setPassword(password);
            CoolSuppliesPersistence.save();
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Admin password updated successfully";
    }

    /**
     * Checks if the provided password matches the current admin password.
     *
     * @param password The password to check.
     * @return True if the provided password matches the current admin password, false otherwise.
     */
    public static Boolean matchesCurrentPassword(String password) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        SchoolAdmin admin = coolSupplies.getAdmin();
        return admin.getPassword().equals(password);
    }

    /**
     * Adds a parent to the system with the provided information.
     *
     * @param email       The email of the parent.
     * @param password    The password of the parent.
     * @param name        The name of the parent.
     * @param phoneNumber The phone number of the parent.
     * @return A success message if the parent was added or an appropriate error message.
     * @author Jack McDonald
     */
    public static String addParent(String email, String password, String name, int phoneNumber) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

        if (email == null || email.isEmpty())
            return "The email must not be empty.";

        if (email.equals("admin@cool.ca"))
            return "The email must not be admin@cool.ca.";

        if (email.contains(" "))
            return "The email must not contain spaces.";

        // email validation (regex) (follows [substring]@[substring].[substring] format)
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            return "The email must be well-formed.";
        }

        for (Parent parent : coolSupplies.getParents())
            if (parent.getEmail().equals(email))
                return "The email must be unique.";

        if (password == null || password.isEmpty())
            return "The password must not be empty.";

        if (name == null || name.isEmpty())
            return "The name must not be empty.";

        if (Integer.toString(phoneNumber).length() != 7) // phone number length check
            return "The phone number must be seven digits.";

        try {
            coolSupplies.addParent(email, password, name, phoneNumber);
            CoolSuppliesPersistence.save();
        } catch (Exception e) {
            return e.getMessage();
        }
        return "Parent added successfully";
    }

    /**
     * Updates a parent's information with the provided information.
     *
     * @param email          The email of the parent.
     * @param newPassword    The new password for the parent.
     * @param newName        The new name for the parent.
     * @param newPhoneNumber The new phone number for the parent.
     * @return A success message if the parent was updated or an appropriate error message.
     * @author Jack McDonald
     */
    public static String updateParent(String email, String newPassword, String newName,
                                      int newPhoneNumber) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

        if (newPassword == null || newPassword.isEmpty())
            return "The password must not be empty.";

        if (newName == null || newName.isEmpty())
            return "The name must not be empty.";

        if (Integer.toString(newPhoneNumber).length() != 7) // phone number length check
            return "The phone number must be seven digits.";

        for (Parent parent : coolSupplies.getParents()) {
            if (parent.getEmail().equals(email)) { // email is unique
                try {
                    parent.setPassword(newPassword);
                    parent.setName(newName);
                    parent.setPhoneNumber(newPhoneNumber);
                    CoolSuppliesPersistence.save();
                } catch (Exception e) {
                    return e.getMessage();
                }
                return "Parent updated successfully";
            }
        }
        return "The parent does not exist.";
    }

    /**
     * Deletes a parent from the system with the provided email.
     *
     * @param email The email of the parent to be deleted.
     * @return A success message if the parent was deleted or an appropriate error message.
     * @author Jack McDonald
     */
    public static String deleteParent(String email) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

        for (Parent parent : coolSupplies.getParents()) {
            if (parent.getEmail().equals(email)) { // email is unique
                try {
                    parent.delete();
                    CoolSuppliesPersistence.save();
                } catch (Exception e) {
                    return e.getMessage();
                }
                return "Parent deleted successfully";
            }
        }
        return "The parent does not exist.";
    }
    
    /**
     * Retrieves a parent by email.
     *
     * @param email The email of the parent to retrieve.
     * @return A TOParent object representing the parent with the provided email or null if no parent is found.
     * @author Jack McDonald
     */
    public static TOParent getParent(String email) {
        CoolSupplies coolsupplies = CoolSuppliesApplication.getCoolSupplies();

        for (Parent parent : coolsupplies.getParents()) {
            if (parent.getEmail().equals(email)) { // email is unique
                return new TOParent(email, parent.getPassword(), parent.getName(), parent.getPhoneNumber());
            }
        }
        return null;
    }

    /**
     * Retrieves all parents in the system.
     *
     * @return A list of TOParent objects representing all parents in the system.
     * @author Jack McDonald
     */
    public static List<TOParent> getParents() {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        List<TOParent> parents = new ArrayList<>();

        for (Parent parent : coolSupplies.getParents()) {
            parents.add(new TOParent(parent.getEmail(), parent.getPassword(),
                    parent.getName(), parent.getPhoneNumber()));
        }
        return parents;
    }

    public static TOParent getParentFromStudentName(String name) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        for (Parent parent : coolSupplies.getParents()) {
            for (Student student : parent.getStudents()) {
                if (student.getName().equals(name)) {
                    return new TOParent(parent.getEmail(), parent.getPassword(), parent.getName(), parent.getPhoneNumber());
                }
            }
        }
        return null;
    }
}
