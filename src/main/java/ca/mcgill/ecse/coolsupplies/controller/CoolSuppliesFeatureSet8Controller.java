package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.model.*;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;

public class CoolSuppliesFeatureSet8Controller {
    /**
     * Cancels an order. Checks for the order's existence and status to determine if cancellation is permitted.
     *
     *   "Order <orderNumber> does not exist" - if no order matches the given number.
     *   "Cannot cancel a penalized order" - if the order is in a "Penalized" state.
     *   "Cannot cancel a prepared order" - if the order is in a "Prepared" state.
     *   "Cannot cancel a picked up order" - if the order is in a "PickedUp" state.
     *   "Cannot cancel a finalized order" - if the order is in a "Final" state.
     *   "Order canceled successfully" - if the order is in a "Started" or "Paid" state and is removed.
     *   "Could not cancel the order" - for any unexpected cases.
     *
     * @param orderNumber the unique identifier of the order to cancel.
     * @return a message indicating the result of the cancellation attempt.
     * 
     * Author: David Vo
     */
    public String cancelOrder(int orderNumber) {
        Order order = Order.getWithNumber(orderNumber);
        
        // Check if order exists
        if (order == null) {
            return "Order " + orderNumber + " does not exist";
        }

        // Process order based on its status
        return switch (order.getStatusFullName()) {
            case "Penalized" -> "Cannot cancel a penalized order";
            case "Prepared" -> "Cannot cancel a prepared order";
            case "PickedUp" -> "Cannot cancel a picked up order";
            case "Final" -> "Cannot cancel a finalized order";
            case "Started", "Paid" -> {
                order.cancelOrder();
                yield "Order canceled successfully";
            }
            default -> "Could not cancel the order";
        };
    }

    /**
     * Picks up an order. Checks for the order's existence and status to determine if pickup is permitted.
     *
     *   "Order <orderNumber> does not exist" - if no order matches the given number.
     *   "Cannot pickup a started order" - if the order is in a "Started" state.
     *   "Cannot pickup a paid order" - if the order is in a "Paid" state.
     *   "Cannot pickup a penalized order" - if the order is in a "Penalized" state.
     *   "The order is already picked up" - if the order is in a "PickedUp" state.
     *   "Order picked up successfully" - if the order is in a "Prepared" state and is picked up.
     *   "Could not pick up the order" - for any unexpected cases.
     *
     * @param orderNumber the unique identifier of the order to pick up.
     * @return a message indicating the result of the pickup attempt.
     * 
     * Author: David Vo
     */

    public String pickUpOrder(int orderNumber) {
      Order order = Order.getWithNumber(orderNumber);
      
      // Check if order exists
      if (order == null) {
          return "Order " + orderNumber + " does not exist";
      }

      // Process order based on its status
      return switch (order.getStatusFullName()) {
          case "Started" -> "Cannot pickup a started order";
          case "Paid" -> "Cannot pickup a paid order";
          case "Penalized" -> "Cannot pickup a penalized order";
          case "PickedUp" -> "The order is already picked up";
          case "Prepared" -> {
              order.pickUp();
              yield "Order picked up successfully";
          }
          default -> "Could not pick up the order";
      };
  }

  /**
   * Pays the penalty for a penalized order.
   *
   * @param orderNumber           the number of the order to pay the penalty for
   * @param authorizationCode     the main payment authorization code
   * @param penaltyAuthorizationCode the authorization code specifically for the penalty
   * @return a string message indicating success or an error message if any condition fails
   * @author David Zhou
   */
  public String payPenaltyForOrder(int orderNumber, String authorizationCode, String penaltyAuthorizationCode) {
    // Retrieve the order using the order number
    Order order = Order.getWithNumber(orderNumber);

    // Check if order exists
    if (order == null) {
      return "Order " + orderNumber + " does not exist";
    }

    // Check if authorization codes are provided
    if (penaltyAuthorizationCode == null || penaltyAuthorizationCode.isEmpty()) {
      return "Penalty authorization code is invalid";
    }
    if (authorizationCode == null || authorizationCode.isEmpty()) {
      return "Authorization code is invalid";
    }

    try {
      boolean success = order.payPenalty(authorizationCode, penaltyAuthorizationCode);
      if (success) {
        return "Penalty payment successful. The order is now prepared.";
      } else {
        // Check specific statuses for message details
        if (order.getStatus() == Order.Status.PickedUp) {
          return "Cannot pay penalty for a picked up order";
        }
        return "Cannot pay penalty for a " + order.getStatus().toString().toLowerCase() + " order";
      }
    } catch (RuntimeException e) {
      return e.getMessage();
    }
  }


  /**
   * Retrieves the details of an individual order, including all related information.
   *
   * @param orderNumber the number of the order to view
   * @return a TOOrder object containing detailed information about the order
   * @author David Zhou
   */
  public TOOrder viewIndividualOrder(int orderNumber) {
    Order order = Order.getWithNumber(orderNumber);
    if (order == null) {
      throw new RuntimeException("Order not found.");
    }

    // Initialize total price and list for order items
    double totalPrice = 0;
    List<TOOrderItem> items = new ArrayList<>();

    for (OrderItem orderItem : order.getOrderItems()) {
      InventoryItem item = orderItem.getItem();

      if (item instanceof Item) {
        // Handle standalone items (like erasers)
        int itemPrice = (int) ((Item) item).getPrice();
        items.add(new TOOrderItem(
            orderItem.getQuantity(),
            item.getName(),
            "",
            itemPrice,
            null  // No discount for standalone items
        ));
        totalPrice += itemPrice * orderItem.getQuantity();
      } else if (item instanceof GradeBundle) {

        GradeBundle bundle = (GradeBundle) item;
        double discountRate = bundle.getDiscount() / 100.0;

        // Collect items within the bundle and initialize bundle total
        List<TOOrderItem> bundleItems = new ArrayList<>();
        int distinctItemCount = 0;

        for (BundleItem bundleItem : bundle.getBundleItems()) {
          if (bundleItem.getLevel() == order.getLevel() || (order.getLevel() == BundleItem.PurchaseLevel.Recommended && bundleItem.getLevel() == BundleItem.PurchaseLevel.Mandatory) || order.getLevel() == BundleItem.PurchaseLevel.Optional) {
            int itemPrice = bundleItem.getItem().getPrice();
            int totalItemQuantity = bundleItem.getQuantity() * orderItem.getQuantity();

            // Check if this item is distinct within the bundle for discount
            if (bundleItems.stream().noneMatch(i -> i.getItemName().equals(bundleItem.getItem().getName()))) {
              distinctItemCount++;
            }

            TOOrderItem toOrderItem = new TOOrderItem(
                totalItemQuantity,
                bundleItem.getItem().getName(),
                bundle.getName(),
                itemPrice,
                null
            );
            bundleItems.add(toOrderItem);

            // Add this item’s cost to the total price for now (discount will adjust it if applicable)
            totalPrice += itemPrice * totalItemQuantity;
          }
        }

        // Apply discount to bundle items if eligible
        if (distinctItemCount >= 2) {
          for (TOOrderItem bundleItem : bundleItems) {
            double discountPerItem = bundleItem.getPrice() * discountRate;

            String discountString;
            if (discountPerItem == Math.floor(discountPerItem)) {
              discountString = String.valueOf((int) -discountPerItem);
            } else {
              discountString = String.valueOf(-discountPerItem);
            }

            bundleItem.setDiscount(discountString);
            totalPrice -= discountPerItem * bundleItem.getQuantity();
          }
        }

        items.addAll(bundleItems);
      }
    }

    return new TOOrder(
        order.getParent().getEmail(),
        order.getStudent().getName(),
        order.getStatusFullName(),
        order.getNumber(),
        order.getDate(),
        order.getLevel().toString(),
        order.getAuthorizationCode(),
        order.getPenaltyAuthorizationCode(),
        totalPrice,
        items
    );
  }

 public static String payForOrder(int orderNumber, String authCode) {
    // 1. Check if auth code is valid, if not say "Authorization code is invalid".
    // 2. Check if order exists, if not return "Order orderNumber does not exist".
    // 3. Check if order is in correct state; if state == Paid, Penalized, Prepared OR PickedUp,
    //    we return "Cannot pay for a <state> order".
    // 4. If successfully paid, do nothing, but change the state.

    if (!Order.hasWithNumber(orderNumber)) {
      return "Order " + orderNumber + " does not exist";
    }

    Order order = Order.getWithNumber(orderNumber);


    if(order.getOrderItems().isEmpty()) {
        return "Order " + orderNumber + " has no items";
    }


    try {
      boolean paymentProcessed = order.pay(authCode);
      if (!paymentProcessed) {
        switch (order.getStatusFullName()) {
          case "Penalized":
            return "Cannot pay for a penalized order";
          case "Prepared":
            return "Cannot pay for a prepared order";
          case "PickedUp":
            return "Cannot pay for a picked up order";
          case "Paid":
            return "The order is already paid";
        }
      }
    } catch (Exception e) {
      return "Authorization code is invalid";
    }

    return "Payment processed";
  }


  public static String startSchoolYear(int orderNumber ) {

    boolean orderExists = Order.hasWithNumber(orderNumber);
    if(!orderExists) return "Order " + orderNumber + " does not exist";
    Order order = Order.getWithNumber(orderNumber);

    boolean yearStarted = order.startSchoolYear();

    if(!yearStarted) return "The school year has already been started";

    return "";

  }
    /**
     * 
     * @param orderNumber
     * @param newLevel
     * @param StudentName
     * @return a string message indicating if the update of an order was successful or not
     * @author Shayan Yamnanidouzi Sorkhabi
     */
    public static String updateOrder(int orderNumber, String newLevel, String StudentName) {
        //CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        try {
            Order order = Order.getWithNumber(orderNumber);
            if (order == null) {
                return "Order " + orderNumber + " does not exist";
            }

            if (Student.hasWithName(StudentName) == false) {
                return "Student " + StudentName + " does not exist.";
            }
            Student aStudent = Student.getWithName(StudentName);

            if (!newLevel.equalsIgnoreCase("mandatory") && !newLevel.equalsIgnoreCase("optional") && !newLevel.equalsIgnoreCase("recommended")) {
                return "Purchase level " + newLevel + " does not exist.";
            }
             BundleItem.PurchaseLevel aLevel= BundleItem.PurchaseLevel.valueOf(newLevel);
            
             if (!order.getStatusFullName().equalsIgnoreCase("Started")) {
                return switch (order.getStatusFullName()) {
                    case "Paid" -> "Cannot update a paid order";
                    case "Penalized" -> "Cannot update a penalized order";
                    case "Prepared" -> "Cannot update a prepared order";
                    case "PickedUp" -> "Cannot update a picked up order";
                    case "Final" -> "Cannot update a finalized order";
                    default -> "Could not update the order";
                }; 
             }
            order.updateOrder(aLevel, aStudent); //this checks in itself if the student belond to the parent

        } catch (RuntimeException e) {
            return e.getMessage();
        }
        return "Could not update the order";
    }

    /**
     * Deletes an item from an order, should either exist simultaneously and if the item exists in the order.
     *
     * @param itemName String: item name to delete
     * @param orderNumber String: order number from which the item must be deleted
     * @return String to show the result of the operation
     * @author David Wang
     *
     * */
    public static String deleteOrderItem(String itemName, String orderNumber) {
      int orderNumberInt;
      try {
          orderNumberInt = Integer.parseInt(orderNumber);
      } catch (Exception e) {
          return "Order " + orderNumber + " does not exist";
      }

      Order order = Order.getWithNumber(orderNumberInt);

      if (order == null) {
          return "Order " + orderNumber + " does not exist";
      }

      String status = order.getStatusFullName();
      switch (status) {
          case "Penalized":
          case "Paid":
          case "Prepared":
              return "Cannot delete items from a " + status.toLowerCase() + " order";
          case "PickedUp":
              return "Cannot delete items from a picked up order";
          default:
              break;
      }

      InventoryItem item = InventoryItem.getWithName(itemName);

      if (item == null) {
          return "Item " + itemName + " does not exist.";
      }

      List<OrderItem> itemsInOrder = order.getOrderItems();
      for (int i = 0; i < order.getOrderItems().size(); i++) {
          if (itemsInOrder.get(i).getItem() == item) {
              // == equality should work as it should be the same memory address
              itemsInOrder.get(i).delete();
              return "Item " + itemName + " deleted successfully from order " + orderNumber;
          }
      }
      return "Item " + itemName + " does not exist in the order " + orderNumber + ".";

  }

    public static List<TOOrder> getOrders() {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        List<TOOrder> orders = new ArrayList<>();

        for (Order order : coolSupplies.getOrders()) {
            // Create a list of item transfer objects for each order
            List<TOOrderItem> orderItems = new ArrayList<>();
            // Initialize the total price of the order for each order
            double total = 0;

            //iterate through each OrderItem in the order
            for (OrderItem orderItem : order.getOrderItems()) {
                InventoryItem inventoryItem = orderItem.getItem();

                //if the item is an instance of Item, add the item to the total; no discount
                if (inventoryItem instanceof Item) {
                    Item item = (Item) inventoryItem;
                    total += orderItem.getQuantity() * item.getPrice();
                    orderItems.add(new TOOrderItem(orderItem.getQuantity(), item.getName(), "",
                            item.getPrice(), ""));
                }

                //if the item is in a bundle, we need to do a few checks...
                else if (inventoryItem instanceof GradeBundle) {
                    //we don't know if a discount is applicable yet, so we'll add the items each temporary list
                    List<TOOrderItem> bundleItemsDiscounted = new ArrayList<>();
                    List<TOOrderItem> bundleItemsNondiscounted = new ArrayList<>();

                    GradeBundle bundle = (GradeBundle) inventoryItem;
                    int uniqueItemCount = 0;

                    //iterate through each BundleItem in the bundle
                    for (BundleItem bundleItem : bundle.getBundleItems()) {
                        //check if BundleItem is eligible for purchase based on given purchase level
                        if (bundleItem.getLevel() == BundleItem.PurchaseLevel.Mandatory ||
                                (order.getLevel() == BundleItem.PurchaseLevel.Optional ||
                                        order.getLevel() == bundleItem.getLevel())) {
                            //bundle subtotal before a discount is applied
                            total += bundleItem.getQuantity() * bundleItem.getItem().getPrice();
                            //recall: two unique items are required for a discount
                            uniqueItemCount++;

                            //add BundleItem with and without a discount to temporary lists
                            bundleItemsDiscounted.add(new TOOrderItem(bundleItem.getQuantity() *
                                    orderItem.getQuantity(), bundleItem.getItem().getName(), bundle.getName(),
                                    bundleItem.getItem().getPrice(), String.valueOf(bundleItem.getItem().getPrice()
                                    * ((double) bundle.getDiscount() / -100)).replace(".0", "")));
                            bundleItemsNondiscounted.add(new TOOrderItem(bundleItem.getQuantity() *
                                    orderItem.getQuantity(), bundleItem.getItem().getName(), bundle.getName(),
                                    bundleItem.getItem().getPrice(), ""));
                        }
                    }

                    //apply discount if two unique items are present; otherwise, add non-discounted items to the order
                    if (uniqueItemCount >= 2) {
                        total -= bundle.getDiscount();
                        orderItems.addAll(bundleItemsDiscounted);
                    } else {
                        orderItems.addAll(bundleItemsNondiscounted);
                    }

                }
                //item is neither an instance of Item nor GradeBundle; throw an exception
                else throw new RuntimeException("Item is not an instance of Item or GradeBundle");
            }

            //add the order to the list of orders
            orders.add(new TOOrder(order.getParent().getEmail(), order.getStudent().getName(),
                    order.getStatusFullName(), order.getNumber(), order.getDate(), order.getLevel().name(),
                    order.getAuthorizationCode(), order.getPenaltyAuthorizationCode(), total, orderItems));
        }
        return orders;
    }
  public static String updateQuanitityOfAnExistingItemOfOrder(int orderNumber, String itemName, int quantity) {
    //1. check if the order exists, if not "Order" + order number + "does not exist"
    //2. check items
        //verify item name exists, "Item" + item name + "does not exist in order" + order number
        //verify the quanitites, "Quantity must be greater than 0"
    //3. check states using switch and case
        //paid, penalized, prepared, pickedup, final

    //check order existence
    try {
        Order order = Order.getWithNumber(orderNumber);
        if (order == null) {
            return "Order " + orderNumber + " does not exist";
        }

        //check items for the entire application then specific to the order number
        //get item from application to a variable
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
        List<OrderItem> orderItems = new ArrayList<>(coolSupplies.getOrderItems());

        //if item does not exist then print appropriate message
        InventoryItem targetItem = (InventoryItem) InventoryItem.getWithName(itemName);
        if (targetItem == null){
            return "Item " + itemName + " does not exist.";
        }

        //check if item is in the right order number
        OrderItem targetOrderItem = null;

        for (int i = 0; i < orderItems.size(); i++){
            OrderItem tempOrderItem = orderItems.get(i);
            if (tempOrderItem.getItem() == targetItem){
                targetOrderItem = tempOrderItem;
            }
        }
        if (targetOrderItem == null){
            return "Item " + itemName + " does not exist in the order " + orderNumber + ".";
        }
        //check if quantity indicated is positive (greater than 0)
        if (quantity <= 0) {
            return "Quantity must be greater than 0.";
        }
        //use switch case to check all states
        if (!order.getStatusFullName().equalsIgnoreCase("Started")){
            return switch (order.getStatusFullName()){
                case "Paid" -> "Cannot update items in a paid order";
                case "Penalized" -> "Cannot update items in a penalized order";
                case "Prepared" -> "Cannot update items in a prepared order";
                case "PickedUp" -> "Cannot update items in a picked up order";
                default -> "Could not update the quantity of existing item of order";
            };
        }
        //then update the item's quantity
        order.updateItem(targetOrderItem, quantity);
    }catch (RuntimeException e) {
        return e.getMessage();
    }
    return "Could not update the quantity of existing item of order";
}

    /**
     * Adds an item to an order.
     *
     * @param itemName    The name of the item to add.
     * @param quantity    The quantity of the item to add.
     * @param orderNumber The number of the order to add the item to.
     * @return A success message if the item was added to the order or an appropriate error message.
     * @author Jack McDonald
     */
    public static String addItemToOrder(String itemName, int quantity, int orderNumber) {
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

        try {
            Order order = Order.getWithNumber(orderNumber);
            if (order == null) {
                return "Order " + orderNumber + " does not exist";
            }

            InventoryItem targetItem = Item.getWithName(itemName);
            if (targetItem == null) {
                return "Item " + itemName + " does not exist.";
            }

            //attempt to add the item to the order and store the result
            boolean wasAdded = order.addItem(targetItem, quantity);

            //most likely reason for failure is that the order is in a state where items cannot be added
            if (!wasAdded) {
                switch (order.getStatusFullName()) {
                    case "Paid":
                        return "Cannot add items to a paid order";
                    case "Penalized":
                        return "Cannot add items to a penalized order";
                    case "Prepared":
                        return "Cannot add items to a prepared order";
                    case "PickedUp":
                        return "Cannot add items to a picked up order";
                    default:
                        return "Could not add item to order";
                }
            }
        } catch (RuntimeException e) {
            return e.getMessage();
        }
        return "Could not add item to order";
    }
}
