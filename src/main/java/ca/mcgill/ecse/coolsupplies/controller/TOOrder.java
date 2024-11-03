package ca.mcgill.ecse.coolsupplies.controller;

import java.util.Date;
import java.util.List;

public class TOOrder {

  private String parentEmail;
  private String studentName;
  private String status;
  private int number;
  private Date date;
  private String level;
  private String authorizationCode;
  private String penaltyAuthorizationCode;
  private double totalPrice;
  private List<TOOrderItem> items;

  public TOOrder(String parentEmail, String studentName, String status, int number, Date date,
                 String level, String authorizationCode, String penaltyAuthorizationCode, double totalPrice,
                 List<TOOrderItem> items) {
    this.parentEmail = parentEmail;
    this.studentName = studentName;
    this.status = status;
    this.number = number;
    this.date = date;
    this.level = level;
    this.authorizationCode = authorizationCode;
    this.penaltyAuthorizationCode = penaltyAuthorizationCode;
    this.totalPrice = totalPrice;
    this.items = items;
  }

  // Getters for all fields
  public String getParentEmail() { return parentEmail; }
  public String getStudentName() { return studentName; }
  public String getStatus() { return status; }
  public int getNumber() { return number; }
  public Date getDate() { return date; }
  public String getLevel() { return level; }
  public String getAuthorizationCode() { return authorizationCode; }
  public String getPenaltyAuthorizationCode() { return penaltyAuthorizationCode; }
  public double getTotalPrice() { return totalPrice; }
  public List<TOOrderItem> getItems() { return items; }

}