package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.model.Parent;
import ca.mcgill.ecse.coolsupplies.model.Student;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.BundleItem;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.User;
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
   * Finds a Parent by their email address.
   * 
   * @param parentEmail The email of the parent.
   * @return The Parent object associated with the provided email.
   * @throws IllegalArgumentException if the parent is not found or the email does not belong to a parent.
   * @author David Zhou
   */
  private static Parent findParentByEmail(String parentEmail) {
    if (!User.hasWithEmail(parentEmail)) {
      throw new IllegalArgumentException("Parent with email " + parentEmail + " not found.");
    }

    User user = User.getWithEmail(parentEmail);
    if (user == null || !(user instanceof Parent)) {
      throw new IllegalArgumentException("User with email " + parentEmail + " is not a parent.");
    }

    return (Parent) user;
  }

  /**
   * Finds a Student by their name.
   * 
   * @param studentName The name of the student.
   * @return The Student object associated with the provided name.
   * @throws IllegalArgumentException if the student is not found by name.
   * @author David Zhou
   */
  private static Student findStudentByName(String studentName) {
    if (!Student.hasWithName(studentName)) {
      throw new IllegalArgumentException("Student with name " + studentName + " not found.");
    }
    return Student.getWithName(studentName);
  }

  /**
   * Adds a student to a parent's list of students.
   * 
   * @param studentName The name of the student to be added.
   * @param parentEmail The email of the parent.
   * @return A success message if the student was added.
   * @throws IllegalArgumentException if the student or parent cannot be found.
   * @author David Zhou
   */
  public static String addStudentToParent(String studentName, String parentEmail) {
    Student student = findStudentByName(studentName);
    Parent parent = findParentByEmail(parentEmail);

    parent.addStudent(student);
    return "Student added to parent.";
  }

  /**
   * Removes a student from a parent's list of students.
   * 
   * @param studentName The name of the student to be removed.
   * @param parentEmail The email of the parent.
   * @return A success message if the student was removed.
   * @throws IllegalArgumentException if the student or parent cannot be found, or if the student is not associated with the parent.
   * @author David Zhou
   */
  public static String deleteStudentFromParent(String studentName, String parentEmail) {
    Student student = findStudentByName(studentName);
    Parent parent = findParentByEmail(parentEmail);

    parent.removeStudent(student);
    return "Student removed from parent.";
  }

  /**
   * Retrieves a specific student of a parent by student name.
   * 
   * @param studentName The name of the student to retrieve.
   * @param parentEmail The email of the parent.
   * @return A TOStudent object representing the student.
   * @throws IllegalArgumentException if the student is not associated with the parent or cannot be found.
   * @author David Zhou
   */
  public static TOStudent getStudentOfParent(String studentName, String parentEmail) {
    Parent parent = findParentByEmail(parentEmail);

    for (Student s : parent.getStudents()) {
      if (s.getName().equals(studentName)) {
        return new TOStudent(s.getName(), s.getGrade().getLevel());
      }
    }

    throw new IllegalArgumentException("Student with name " + studentName + " not associated with this parent.");
  }

  /**
   * Retrieves all students associated with a parent by the parent's email.
   * 
   * @param parentEmail The email of the parent.
   * @return A list of TOStudent objects representing the parent's students.
   * @throws IllegalArgumentException if the parent cannot be found by email.
   * @author David Zhou
   */
  public static List<TOStudent> getStudentsOfParent(String parentEmail) {
    Parent parent = findParentByEmail(parentEmail);
    List<TOStudent> students = new ArrayList<>();

    for (Student s : parent.getStudents()) {
      students.add(new TOStudent(s.getName(), s.getGrade().getLevel()));
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
   * @return A success message if the order is created.
   * @throws IllegalArgumentException if any of the parameters are invalid or null, or if the parent/student cannot be found.
   * @author David Zhou
   */
  public static String startOrder(int number, Date date, String level, String parentEmail,
      String studentName) {
    CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
    Parent parent = findParentByEmail(parentEmail);
    Student student = findStudentByName(studentName);
    BundleItem.PurchaseLevel purchaseLevel;

    if (level == null) {
      throw new IllegalArgumentException("Purchase level cannot be null.");
    }

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
        throw new IllegalArgumentException("Invalid purchase level: " + level);
    }

    coolSupplies.addOrder(number, date, purchaseLevel, parent, student);
    return "Order created successfully.";
  }
}