package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.model.*;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Controller class for handling operations related to parents and students in CoolSupplies.
 * Methods include adding/removing students from parents, retrieving students, and creating orders.
 *
 * @author David Zhou
 */
public class CoolSuppliesFeatureSet6Controller {

  /**
   * Adds a student to a parent's list of students.
   *
   * @param studentName The name of the student to be added.
   * @param parentEmail The email of the parent.
   * @return A success message if the student was added, or an appropriate error message.
   * @author David Zhou
   */
  public static String addStudentToParent(String studentName, String parentEmail) {
    Student student = findStudentByName(studentName);
    if (student == null) {
      return "The student does not exist.";
    }

    Parent parent = findParentByEmail(parentEmail);
    if (parent == null) {
      return "The parent does not exist.";
    }

    List<TOStudent> studentsOfParent = getStudentsOfParent(parentEmail);
    for (TOStudent existingStudent : studentsOfParent) {
      if (existingStudent.getName().equals(studentName)) {
        return "The student is already assigned to parent.";
      }
    }

    if (student.getParent() != null) {
      return "The student is already assigned to another parent.";
    }

      try {
          parent.addStudent(student);
          CoolSuppliesPersistence.save();
      } catch (Exception e) {
          return e.getMessage();
      }
      return "Student added to parent.";
  }

  /**
   * Removes a student from a parent's list of students.
   *
   * @param studentName The name of the student to be removed.
   * @param parentEmail The email of the parent.
   * @return A success message if the student was removed, or an appropriate error message.
   * @author David Zhou
   */
  public static String deleteStudentFromParent(String studentName, String parentEmail) {
    Student student = findStudentByName(studentName);
    if (student == null) {
      return "The student does not exist.";
    }

    Parent parent = findParentByEmail(parentEmail);
    if (parent == null) {
      return "The parent does not exist.";
    }

    if (parent.getStudents() == null || !parent.getStudents().contains(student)) {
      return "The student is not associated with this parent.";
    }

      try {
          parent.removeStudent(student);
          CoolSuppliesPersistence.save();
      } catch (Exception e) {
          return e.getMessage();
      }
      return "Student removed from parent.";
  }

  /**
   * Retrieves a specific student of a parent by student name.
   *
   * @param studentName The name of the student to retrieve.
   * @param parentEmail The email of the parent.
   * @return A TOStudent object representing the student, or null if not found.
   * @author David Zhou
   */
  public static TOStudent getStudentOfParent(String studentName, String parentEmail) {
    Parent parent = findParentByEmail(parentEmail);
    if (parent == null) {
      return null;
    }

    for (Student s : parent.getStudents()) {
      if (s.getName().equals(studentName)) {
        return new TOStudent(s.getName(), s.getGrade().getLevel());
      }
    }

    return null;
  }

  /**
   * Retrieves all students associated with a parent by the parent's email.
   *
   * @param parentEmail The email of the parent.
   * @return A list of TOStudent objects representing the parent's students, or an empty list if none found.
   * @author David Zhou
   */
  public static List<TOStudent> getStudentsOfParent(String parentEmail) {
    Parent parent = findParentByEmail(parentEmail);
    List<TOStudent> students = new ArrayList<>();

    if (parent != null) {
      for (Student s : parent.getStudents()) {
        students.add(new TOStudent(s.getName(), s.getGrade().getLevel()));
      }
    }

    return students;
  }

  /**
   * Starts an order for a student, specifying the purchase level and order details.
   *
   * @param number The order number.
   * @param date The date of the order.
   * @param level The purchase level (mandatory, recommended, optional).
   * @param parentEmail The email of the parent placing the order.
   * @param studentName The name of the student for whom the order is placed.
   * @return A success message if the order is created, or an error message if validation fails.
   * @author David Zhou
   */
  public static String startOrder(int number, Date date, String level, String parentEmail,
                                  String studentName) {
    CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

    if (number <= 0) {
      return "The number must be greater than 0.";
    }

    if (Order.hasWithNumber(number)) {
      return "The number must be unique.";
    }

    Parent parent = findParentByEmail(parentEmail);
    if (parent == null) {
      return "The parent does not exist.";
    }

    Student student = findStudentByName(studentName);
    if (student == null) {
      return "The student does not exist.";
    }

    if (level == null) {
      return "Purchase level cannot be null.";
    }

    if (student.getParent() != parent) {
      return "The student is not associated with this parent.";
    }

    BundleItem.PurchaseLevel purchaseLevel;
    switch (level.toLowerCase()) {
      case "mandatory":
        purchaseLevel = BundleItem.PurchaseLevel.Mandatory;
        break;
      case "recommended":
        purchaseLevel = BundleItem.PurchaseLevel.Recommended;
        break;
      case "optional":
        purchaseLevel = BundleItem.PurchaseLevel.Optional;
        break;
      default:
        return "The level must be Mandatory, Recommended, or Optional.";
    }

      try {
          coolSupplies.addOrder(number, date, purchaseLevel, parent, student);
          CoolSuppliesPersistence.save();
      } catch (Exception e) {
          return e.getMessage();
      }
      return "Order created successfully.";
  }

  /**
   * Finds a Parent by their email address.
   *
   * @param parentEmail The email of the parent.
   * @return The Parent object associated with the provided email, or null if not found.
   * @author David Zhou
   */
  private static Parent findParentByEmail(String parentEmail) {
    CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
    for (Parent parent : coolSupplies.getParents()) {
      if (parent.getEmail().equals(parentEmail)) {
        return parent;
      }
    }
    return null;
  }

  /**
   * Finds a Student by their name.
   *
   * @param studentName The name of the student.
   * @return The Student object associated with the provided name, or null if not found.
   * @author David Zhou
   */
  private static Student findStudentByName(String studentName) {
    CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
    for (Student student : coolSupplies.getStudents()) {
      if (student.getName().equals(studentName)) {
        return student;
      }
    }
    return null;
  }
}
