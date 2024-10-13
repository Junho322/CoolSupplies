package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.Grade;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class responsible for Grade related operations in CoolSupplies.
 * This includes adding, updating, deleting, and retrieving grade information.
 * 
 * @author David Vo
 */

public class CoolSuppliesFeatureSet7Controller {

    /**
     * Adds a new grade to the CoolSupplies system. 
     * 
     * @param gradeLevel The grade level to be added.
     * @return A message indicating success or that the grade already exists.
     * @throws RuntimeException if there is an issue adding the grade.
     * @author David Vo
     */

    public static String addGrade(String gradeLevel) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        
        try {
            // Uses addGrade() from CoolSupplies.java, which calls Grade.java constructor. Note that Exception handling is already done in Grade.java constructor.
            Grade newGrade = coolSupplies.addGrade(gradeLevel);
            return "Grade " + newGrade.getLevel() + " added successfully.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    /**
     * Updates an existing grade with a new level.  
     * 
     * @param currentGradeLevel The current level of the grade.
     * @param newGradeLevel The new level to update the grade to.
     * @return A message indicating the success of the update, or if the current grade does not exist.
     * @throws IllegalArgumentException if the new grade level already exists or the current grade level is not found.
     * @author David Vo
     */

    public static String updateGrade(String currentGradeLevel, String newGradeLevel) {
        // Finds the corresponding grade using getWithLevel() from Grade. This searches the gradeByLevel HashMap.
        Grade grade = Grade.getWithLevel(currentGradeLevel);
        
        if (grade == null) {
            return "Grade " + currentGradeLevel + " not found.";
        }

        if (Grade.hasWithLevel(newGradeLevel)) {
            return "Grade with level " + newGradeLevel + " already exists.";
        }
    
        try {
            // Attempt to set the new level; it will return false if the level already exists
            grade.setLevel(newGradeLevel);
            return "Grade " + currentGradeLevel + " updated successfully to Grade " + newGradeLevel + ".";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    /**
     * Deletes an existing grade with the corresponding level.  
     * 
     * @param gradeLevel The level of the grade to be deleted.
     * @return A message indicating the success of the deletion, or if the grade was not found.
     * @throws IllegalArgumentException if the grade level is not found.
     * @author David Vo
     */

    public static String deleteGrade(String gradeLevel) {
        Grade grade = Grade.getWithLevel(gradeLevel);
            
        if (grade == null) {
            return "The grade does not exist.";
        }
        
        try {
            //Destructor for Grade.java will remove the grade from the gradesByLevel HashMap, remove bundles, remove students, and remove the grade from the CoolSupplies.java association.
            grade.delete();
            return "Grade removed successfully.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
 
    /**
     * Retrieves a grade from the CoolSupplies system based on the specified level. Returns a Transfer Object (TOGrade) representing the grade.
     * 
     * @param level The level of the grade to retrieve.
     * @return A TOGrade object representing the retrieved grade.
     * @throws IllegalArgumentException if no grade with the specified level exists.
     * @author David Vo
     */

    public static TOGrade getGrade(String level) {
    
        Grade grade = Grade.getWithLevel(level);
        if (grade != null) {
            return new TOGrade(grade.getLevel());
        }
    
        throw new IllegalArgumentException("Grade with level " + level + " not found.");
    }

    /**
     * Retrieves a list of all grades in the CoolSupplies system as a list of TOGrade objects.
     * 
     * @return A list of TOGrade objects representing all grades.
     * @author David Vo
     */

    public static List<TOGrade> getGrades() {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
    
        List<TOGrade> gradeList = new ArrayList<TOGrade>();
    
        for (Grade g : coolSupplies.getGrades()) {
            gradeList.add(new TOGrade(g.getLevel()));
        }
    
        return gradeList;
    }
}
