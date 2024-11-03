package ca.mcgill.ecse.coolsupplies.controller;

public class TOOrderItem {

  private int quantity;
  private String itemName;
  private String gradeBundleName;
  private int price;
  private String discount;

  public TOOrderItem(int quantity, String itemName, String gradeBundleName, double price, String discount) {
    this.quantity = quantity;
    this.itemName = itemName;
    this.gradeBundleName = gradeBundleName.isEmpty() ? null : gradeBundleName;
    this.price = (int) price;
    this.discount = (discount != null && !"0".equals(discount)) ? discount : null;
  }

  // Getters for all fields
  public int getQuantity() { return quantity; }
  public String getItemName() { return itemName; }
  public String getGradeBundleName() { return gradeBundleName; }
  public int getPrice() { return price; }
  public String getDiscount() { return discount; }

  // Setter for discount as a String
  public void setDiscount(String discount) {
    this.discount = (discount != null && !"0".equals(discount)) ? discount : null;
  }
}