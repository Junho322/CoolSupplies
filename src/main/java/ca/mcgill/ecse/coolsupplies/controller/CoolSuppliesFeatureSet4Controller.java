package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.Grade;
import ca.mcgill.ecse.coolsupplies.model.GradeBundle;
import ca.mcgill.ecse.coolsupplies.model.InventoryItem;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Wang
 * Feature Set 4 Controller Methods responsible for GradeBundles.
 * Implemented operations:
 *  Create
 *  Update
 *  Delete
 *  Get
 */
public class CoolSuppliesFeatureSet4Controller {

  /**
   * GradeBundle constructor. Returns String to indicate outcome of construction.
   *
   * @param name name of target GradeBundle to instantiate
   * @param discount discount to apply
   * @param gradeLevel Grade's level to which this GradeBundle will belong to
   * @return A String indicating success or failure of construction
   * @author David Wang
   */
  public static String addBundle(String name, int discount, String gradeLevel) {

    CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

    if (InventoryItem.hasWithName(name)) {
      return "The name must be unique.";
    }
    if (name == null || name.isEmpty()) {
      return "The name must not be empty.";
    }
    if (discount < 0 || discount > 100) {
      return "The discount must be greater than or equal to 0 and less than or equal to 100.";
    }
    if (!Grade.hasWithLevel(gradeLevel)) { // validate Grade input, does it exist?
      return "The grade does not exist.";
    }
    if (Grade.getWithLevel(gradeLevel).hasBundle()) {
      return "A bundle already exists for the grade.";
    }

    Grade grade = Grade.getWithLevel(gradeLevel);
    try { // catch any GradeBundle instantiation issues.
      new GradeBundle(name, discount, coolSupplies, grade);
      CoolSuppliesPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }
    return "GradeBundle added successfully.";

  }

  /**
   * Updates a given GradeBundle, should it exist, with a new name, new discount and new grade.
   * Returns String to indicate outcome of update.
   *
   * @param name name of target GradeBundle to update
   * @param newName new name to apply
   * @param newDiscount new discount to apply
   * @param newGradeLevel new Grade's level attribute to which this GradeBundle will belong to
   * @return A String indicating outcome of the update.
   * @author David Wang
   */
  public static String updateBundle(String name, String newName, int newDiscount,
                                    String newGradeLevel) {

    GradeBundle targetBundle = (GradeBundle) GradeBundle.getWithName(name);


    if (newName == null || newName.isEmpty()) {
      return "The name must not be empty.";
    }
    if (newDiscount < 0 || newDiscount > 100) {
      return "The discount must be greater than or equal to 0 and less than or equal to 100.";
    }
    if (targetBundle == null) {
      return "The bundle does not exist.";
    }
    if (!Grade.hasWithLevel(newGradeLevel)) {
      return "The grade does not exist.";
    }
    if (Grade.getWithLevel(newGradeLevel).hasBundle() && !(newGradeLevel.equals(targetBundle.getGrade().getLevel()))) {
      return "A bundle already exists for the grade.";
    }
    if (GradeBundle.hasWithName(newName) && !(newName.equals(targetBundle.getName()))) {
      return "The name must be unique.";
    }
    try {
      targetBundle.setName(newName);
      targetBundle.setGrade(Grade.getWithLevel(newGradeLevel));
      targetBundle.setDiscount(newDiscount);
      CoolSuppliesPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }
      return "Successfully updated Bundle: " + name;
  }

  /**
   * Deletes a given GradeBundle, should it exist.
   * Returns String to indicate outcome of deletion.
   *
   * @param name name of target GradeBundle to delete
   * @return A String indicating outcome of deletion
   * @author David Wang
   */
  public static String deleteBundle(String name) {

    GradeBundle targetBundle = (GradeBundle) GradeBundle.getWithName(name);
    if (targetBundle == null) {
      return "The grade bundle does not exist.";
    } else {
      try {
        targetBundle.delete();
        CoolSuppliesPersistence.save();
      } catch (Exception e) {
        return e.getMessage();
      }
      return "Bundle " + name + " successfully deleted.";
    }
  }

  /**
   * Returns a Transfer Object representation of a given GradeBundle with a TOGradeBundle object.
   *
   * @param name name of target GradeBundle to get as transfer object.
   * @throws IllegalArgumentException if the target GradeBundle does not exist.
   * @return A TOGradeBundle of the given GradeBundle
   * @author David Wang
   */
  public static TOGradeBundle getBundle(String name) {
    GradeBundle targetBundle = (GradeBundle) GradeBundle.getWithName(name);
    if (targetBundle == null) {
      return null;
    }
    return new TOGradeBundle(targetBundle.getName(), targetBundle.getDiscount(), targetBundle.getGrade().getLevel());
  }

  /**
   * Returns all GradeBundles within CoolSupplies as a List<TOGradeBundle>
   *
   * @return A List<TOGradeBundle> of all GradeBundles within CoolSupplies.
   * @author David Wang
   */
  public static List<TOGradeBundle> getBundles() {
    CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
    List<GradeBundle> allGradeBundles = coolSupplies.getBundles();
    List<TOGradeBundle> TOgradeBundles = new ArrayList<>();

    for (GradeBundle gradeBundle : allGradeBundles) {
      String name = gradeBundle.getName();
      TOgradeBundles.add(getBundle(name));
    }
    return TOgradeBundles;
  }
}

