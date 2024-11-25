package ca.mcgill.ecse.coolsupplies.application;

import ca.mcgill.ecse.coolsupplies.controller.*;
import ca.mcgill.ecse.coolsupplies.javafx.CoolSuppliesFxmlView;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.SchoolAdmin;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;
import javafx.application.Application;

public class CoolSuppliesApplication {

  private static CoolSupplies coolSupplies;

  public static final boolean DARK_MODE = false;

  public static void main(String[] args) {

    //Test
    CoolSuppliesFeatureSet1Controller.addParent("abc@abc.ca", "a", "John Doe", 6062535);
    CoolSuppliesFeatureSet1Controller.addParent("abcd@abc.ca", "a", "Jane Doe", 1111111);
    CoolSuppliesFeatureSet1Controller.addParent("abcde@abc.ca", "a", "abc", 6062535);
    CoolSuppliesFeatureSet1Controller.addParent("abcdef@abc.ca", "a", "abcd", 6062535);
    CoolSuppliesFeatureSet1Controller.addParent("abcdefg@abc.ca", "a", "abcde", 6062535);
    CoolSuppliesFeatureSet1Controller.addParent("abcdefgh@abc.ca", "a", "abcdef", 6062535);

    CoolSuppliesFeatureSet7Controller.addGrade("3");
    CoolSuppliesFeatureSet7Controller.addGrade("4");
    CoolSuppliesFeatureSet7Controller.addGrade("5th");
    CoolSuppliesFeatureSet7Controller.addGrade("6th");

    CoolSuppliesFeatureSet2Controller.addStudent("s1", "3");
    CoolSuppliesFeatureSet2Controller.addStudent("s2", "4");
    CoolSuppliesFeatureSet2Controller.addStudent("s3", "3");

    CoolSuppliesFeatureSet6Controller.addStudentToParent("s1", "abcd@abc.ca");
    CoolSuppliesFeatureSet6Controller.addStudentToParent("s2", "abcd@abc.ca");
    CoolSuppliesFeatureSet6Controller.addStudentToParent("s3", "abc@abc.ca");

    CoolSuppliesFeatureSet4Controller.addBundle("Pencil Bundle", 1, "3");
    CoolSuppliesFeatureSet4Controller.addBundle("Notebook Bundle", 1, "5th");

    try {
        getCoolSupplies().setAdmin(new SchoolAdmin("admin@cool.ca", "admin", getCoolSupplies()));
        CoolSuppliesPersistence.save();
    } catch (Exception e) {
        e.printStackTrace();
    }

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
