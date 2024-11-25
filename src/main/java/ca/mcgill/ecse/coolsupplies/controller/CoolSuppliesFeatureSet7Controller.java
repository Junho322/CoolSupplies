package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.Grade;
import ca.mcgill.ecse.coolsupplies.model.GradeBundle;
import ca.mcgill.ecse.coolsupplies.model.Student;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;

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
     * @return A message indicating success.
     * @return An error message if: 1. The grade level already exists. 2. The grade level is empty.
     * @author David Vo
     */
    public static String addGrade(String gradeLevel) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

        // Checks if the grade level already exists.
        if (Grade.hasWithLevel(gradeLevel)) {
            return "The level must be unique.";
        }

        // Checks if the grade level is empty.
        if (gradeLevel == null || gradeLevel.trim().length() == 0) {
            return "The level must not be empty.";
        }
        
        try {
            // Uses addGrade() from CoolSupplies.java, which calls Grade.java constructor. Note that Exception handling is already done in Grade.java constructor.
            Grade newGrade = coolSupplies.addGrade(gradeLevel);
            CoolSuppliesPersistence.save();
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
     * @return A message indicating the success of the update.
     * @return An error message if: 1. The current grade level does not exist. 2. The new grade level already exists. 3. The new grade level is empty.
     * @author David Vo
     */
    public static String updateGrade(String currentGradeLevel, String newGradeLevel) {
        // Finds the corresponding grade using getWithLevel() from Grade. This searches the gradeByLevel HashMap.
        Grade grade = Grade.getWithLevel(currentGradeLevel);

        // Checks if the current grade level does not exist.
        if (grade == null) {
            return "The grade does not exist.";
        }

        // Checks if the new grade level already exists.
        if (Grade.hasWithLevel(newGradeLevel)) {
            return "The level must be unique.";
        }
    
        // Checks if the new grade level is empty.
        if (newGradeLevel == null || newGradeLevel.trim().length() == 0) {
            return "The level must not be empty.";
        }

        try {
            // Attempt to set the new level; it will return false if the level already exists
            grade.setLevel(newGradeLevel);
            CoolSuppliesPersistence.save();
            return "Grade " + currentGradeLevel + " updated successfully to Grade " + newGradeLevel + ".";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    /**
     * Deletes an existing grade with the corresponding level.  
     * 
     * @param gradeLevel The level of the grade to be deleted.
     * @return A message indicating the success of the deletion.
     * @return An error message if the grade level does not exist.
     * @author David Vo
     */
    public static String deleteGrade(String gradeLevel) {
        Grade grade = Grade.getWithLevel(gradeLevel);
            
        // Checks if the grade level does not exist.
        if (grade == null) {
            return "The grade does not exist.";
        }
        
        try {
            //Destructor for Grade.java will remove the grade from the gradesByLevel HashMap, remove bundles, remove students, and remove the grade from the CoolSupplies.java association.
            grade.delete();
            CoolSuppliesPersistence.save();
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
     * @return null if no grade with the specified level exists.
     * @author David Vo
     */
    public static TOGrade getGrade(String level) {
        Grade grade = Grade.getWithLevel(level);
        if (grade != null) {
            return new TOGrade(grade.getLevel());
        }
        return null;
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

   /**
   * Retrieves a specific student of a grade by student name.
   *
   * @param studentName The name of the student to retrieve.
   * @param gradeLevel The level of the grade
   * @return A TOStudent object representing the student, or null if not found.
   * @author David Vo
   */
    public static TOStudent getStudentOfGrade(String studentName, String gradeLevel) {
    Grade grade = Grade.getWithLevel(gradeLevel);
    if (grade == null) {
      return null;
    }

    for (Student s : grade.getStudents()) {
      if (s.getName().equals(studentName)) {
        return new TOStudent(s.getName(), s.getGrade().getLevel());
      }
    }

    return null;
  }

    /**
   * Retrieves all students associated with a grade by the grade level.
   *
   * @param gradeLevel The level of the grade
   * @return A list of TOStudent objects representing the grade's students, or an empty list if none found.
   * @author David Vo
   */
    public static List<TOStudent> getStudentsOfGrade(String gradeLevel) {
    Grade grade = Grade.getWithLevel(gradeLevel);
    List<TOStudent> students = new ArrayList<>();

    if (grade != null) {
      for (Student s : grade.getStudents()) {
        students.add(new TOStudent(s.getName(), s.getGrade().getLevel()));
      }
    }

    return students;
  }

   /**
   * Retrieves the bundle of a grade by student name.
   *
   * @param bundleName The name of the bundle to retrieve.
   * @param gradeLevel The level of the grade
   * @return A TOGradeBundle object representing the bundle, or null if not found.
   * @author David Vo
   */
    public static TOGradeBundle getBundleOfGrade(String gradeLevel) {
    Grade grade = Grade.getWithLevel(gradeLevel);
    if (grade == null) {
      return null;
    }

    if (grade.hasBundle()) {
        GradeBundle bundle = grade.getBundle();
        return new TOGradeBundle(bundle.getName(), bundle.getDiscount(), bundle.getGrade().getLevel());
    }
    else {
        return null;
    }
  }
}
