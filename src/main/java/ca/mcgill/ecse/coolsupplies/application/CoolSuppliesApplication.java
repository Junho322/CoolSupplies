package ca.mcgill.ecse.coolsupplies.application;

import ca.mcgill.ecse.coolsupplies.controller.*;
import ca.mcgill.ecse.coolsupplies.javafx.CoolSuppliesFxmlView;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;
import javafx.application.Application;

public class CoolSuppliesApplication {

  private static CoolSupplies coolSupplies;

  public static final boolean DARK_MODE = false;

  public static void main(String[] args) {

    // Test
    // CoolSuppliesFeatureSet7Controller.addGrade("1");
    // CoolSuppliesFeatureSet7Controller.addGrade("2");
    // System.out.println("Number of grades added: " + CoolSuppliesFeatureSet7Controller.getGrades().size());
    // System.out.println("Grades: " + CoolSuppliesFeatureSet7Controller.getGrades());
    
    // CoolSuppliesFeatureSet2Controller.addStudent("Hamza", "1");
    // CoolSuppliesFeatureSet2Controller.addStudent("David", "2");
    // System.out.println("Number of students added: " + CoolSuppliesFeatureSet2Controller.getStudents().size());
    // System.out.println("Students: " + CoolSuppliesFeatureSet2Controller.getStudents());
    // System.out.println("Number of students in grade 1: " + CoolSuppliesFeatureSet7Controller.getStudentsOfGrade("1").size());
    // System.out.println("Number of students in grade 2: " + CoolSuppliesFeatureSet7Controller.getStudentsOfGrade("2").size());
    // CoolSuppliesFeatureSet2Controller.addStudent("Diddy", "1");
    // System.out.println("Number of students in grade 1: " + CoolSuppliesFeatureSet7Controller.getStudentsOfGrade("1").size());
    // System.out.println("Students: " + CoolSuppliesFeatureSet2Controller.getStudents());
    // System.out.println("Number of students in grade 1: " + CoolSuppliesFeatureSet7Controller.getStudentsOfGrade("1").size());
    // System.out.println("Finding Diddy in grade 1: " + CoolSuppliesFeatureSet7Controller.getStudentOfGrade("Diddy","1"));

    CoolSuppliesFeatureSet1Controller.addParent("abc@abc.ca", "a", "John Doe", 6062535);
    CoolSuppliesFeatureSet1Controller.addParent("abcd@abc.ca", "a", "Jane Doe", 1111111);
    CoolSuppliesFeatureSet1Controller.addParent("abcde@abc.ca", "a", "abc", 6062535);
    CoolSuppliesFeatureSet1Controller.addParent("abcdef@abc.ca", "a", "abcd", 6062535);
    CoolSuppliesFeatureSet1Controller.addParent("abcdefg@abc.ca", "a", "abcde", 6062535);
    CoolSuppliesFeatureSet1Controller.addParent("abcdefgh@abc.ca", "a", "abcdef", 6062535);

    CoolSuppliesFeatureSet7Controller.addGrade("3");
    CoolSuppliesFeatureSet7Controller.addGrade("4");

    CoolSuppliesFeatureSet2Controller.addStudent("s1", "3");
    CoolSuppliesFeatureSet2Controller.addStudent("s2", "4");
    CoolSuppliesFeatureSet2Controller.addStudent("s3", "3");

    CoolSuppliesFeatureSet6Controller.addStudentToParent("s1", "abcd@abc.ca");
    CoolSuppliesFeatureSet6Controller.addStudentToParent("s2", "abcd@abc.ca");
    CoolSuppliesFeatureSet6Controller.addStudentToParent("s3", "abc@abc.ca");

    CoolSuppliesFeatureSet4Controller.addBundle("BIG BUNDLE", 1, "3");

    Application.launch(CoolSuppliesFxmlView.class, args);
  }

  public static CoolSupplies getCoolSupplies() {
    if (coolSupplies == null) {
      // these attributes are default, you should set them later with the setters
      coolSupplies = CoolSuppliesPersistence.load();
    }
    return coolSupplies;
  }

}
