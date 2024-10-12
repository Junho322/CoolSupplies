package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.Grade;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;

import java.util.ArrayList;
import java.util.List;

public class CoolSuppliesFeatureSet7Controller {

    public static String addGrade(String gradeLevel) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

        if (Grade.hasWithLevel(gradeLevel)) {
            return "Grade already exists.";
        }

        try {
            new Grade(gradeLevel, coolSupplies);
            return "Grade added successfully.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public static String updateGrade(String currentGradeLevel, String newGradeLevel) {

        Grade grade = Grade.getWithLevel(currentGradeLevel);

        if (grade == null) {
            return "The grade does not exist.";
        }

        if (Grade.hasWithLevel(newGradeLevel)) {
            return "Grade with level " + newGradeLevel + " already exists.";
        }

        grade.setLevel(newGradeLevel);
        return "Grade updated successfully.";
    }

    public static String deleteGrade(String gradeLevel) {

        Grade grade = Grade.getWithLevel(gradeLevel);

        if (grade == null) {
            return "The grade does not exist.";
        }

        grade.delete();
        return "Grade removed successfully.";
    }

    public static TOGrade getGrade(String level) {

        Grade grade = Grade.getWithLevel(level);
        if (grade != null) {
            return new TOGrade(grade.getLevel());
        }

        throw new IllegalArgumentException("The grade does not exist.");
    }

    public static List<TOGrade> getGrades() {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

        List<TOGrade> gradeList = new ArrayList<TOGrade>();

        for (Grade g : coolSupplies.getGrades()) {
            gradeList.add(new TOGrade(g.getLevel()));
        }

        return gradeList;
    }
}