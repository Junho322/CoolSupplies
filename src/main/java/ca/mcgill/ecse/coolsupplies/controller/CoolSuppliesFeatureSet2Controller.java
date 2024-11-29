package ca.mcgill.ecse.coolsupplies.controller;

import java.util.List;
import java.util.ArrayList;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.Student;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.Grade;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;


/**
 * Controller class that handles operations related to Students in the system.
 * Methods implemented include adding/removing students from the system, retreiving all or a single student(s) from the system.
 *
 * @author Hamza Khalfi
 */

public class CoolSuppliesFeatureSet2Controller {

    private static CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();


    /**
     * Will add a Student by taking as input the student's name and grade level
     * Conditions for student to be added are:
     *                              1. Name must not already exist in the system and must not be blank
     *                              2. the requested gradeLevel must exist in the system
     * @param name: The name of the student to be added
     * @param gradeLevel : The grade level we wish to assign the student to
     * @return A string message indicating either an error while attempting to add the Student
     *          or null if student was successfuly added.
     **/

    public static String addStudent(String name, String gradeLevel) {

        Grade grade = Grade.getWithLevel(gradeLevel);
        boolean isUnique = !Student.hasWithName(name);

        if(name.isBlank() || grade == null || !isUnique){
            return name.isBlank() ? "The name must not be empty."
                    : grade == null ? "The grade does not exist."
                    : "The name must be unique.";
        }
        try {
            coolSupplies.addStudent(name,grade);
            CoolSuppliesPersistence.save();
        } catch (Exception e) {
            return e.getMessage();
        }

        return "Student successfully added.";
    }


    /**
     * Updates the specified student ( specified by "name" parameter )
     * with the given new name and given Grade Level
     *
     * @param name: The name of the student to be searched for ( should exist or else an error )
     * @param newName : The new name we want to assign to the specified student ( should be unique or else an error )
     * @param newGradeLevel : The new grade level we want to get the student to ( should exist or else an error )
     * @return A string message indicating either an error while attempting to Update the Student
     *          or null if student was successfuly edited.
     **/

    public static String updateStudent(String name, String newName, String newGradeLevel) {

        Student targetStudent = Student.getWithName(name);
        Grade grade = Grade.getWithLevel(newGradeLevel);
        boolean nameUniqueness;
        if(targetStudent!= null && targetStudent.getName().equals(newName)){
            nameUniqueness = true;
        }else{
            nameUniqueness = !Student.hasWithName(newName);
        }




        if(newName.isBlank() || !nameUniqueness || targetStudent == null || grade == null  ){
            return newName.isBlank() ? "The name must not be empty."
                    : !nameUniqueness ? "The name must be unique."
                    : targetStudent == null ? "The student does not exist."
                    : "The grade does not exist.";
        }
        else{
            try {
                targetStudent.setName(newName);
                targetStudent.setGrade(grade);
                CoolSuppliesPersistence.save();
            } catch (Exception e) {
                return e.getMessage();
            }

            return "Student successfully updated.";
        }
    }


    /**
     * Performs a verification to check if there exists a Student or not in the system with the given name.
     *
     * @param name: The name we aim to look for if it exists in the system
     * @return A boolean value representing if the given name was unique( we don't have a student with that name)
     * or not.
     **/

    public static String deleteStudent(String name) {

        Student student  = Student.getWithName(name);
        if(student == null){

            return("The student does not exist.");
        }
        try {
            student.delete();
            CoolSuppliesPersistence.save();
        } catch (Exception e) {
            return e.getMessage();
        }

        return "Success";
    }


    /**
     * Returns an object that holds information about the requested student if the Student with the given exists
     *
     * @param name: The name of the student we will look for and return
     * @return A Student transfer object that contains the information of the requested Student if he exists,
     * or "null" if the requested student does not exist.
     **/

    public static TOStudent getStudent(String name) {

        Student targetStudent = Student.getWithName(name);
        if(targetStudent != null){

            return new TOStudent(targetStudent.getName(), targetStudent.getGrade().getLevel());
        }

        return null;
    }


    /**
     * Returns a list of all the students that exist in the system.
     *
     * @return A List of Student transfer objects, that refer to each of the students that exists in the system
     **/

    public static List<TOStudent> getStudents() {

        List<TOStudent> studentList = new ArrayList<>();
        for(Student student: coolSupplies.getStudents()){
            studentList.add(new TOStudent(student.getName(),student.getGrade().getLevel()));
        }

        return studentList;
    }
}