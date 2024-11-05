package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.model.*;
import java.util.ArrayList;
import java.util.List;

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


  /**
   * Processes the payment for an order.
   *
   * @param orderNumber The number of the order to be paid.
   * @param authCode The authorization code for the payment.
   * @return A message indicating the result of the payment process:
   *         - "Order orderNumber does not exist" if the order does not exist.
   *         - "Order orderNumber has no items" if the order has no items.
   *         - "Cannot pay for a penalized order" if the order is penalized.
   *         - "Cannot pay for a prepared order" if the order is prepared.
   *         - "Cannot pay for a picked up order" if the order is picked up.
   *         - "The order is already paid" if the order is already paid.
   *         - "Authorization code is invalid" if the authorization code is invalid.
   *         - "Payment processed" if the payment is successfully processed.
   */

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



  /**
   * Starts the school year for an order.
   *
   * @param orderNumber The number of the order for which the school year is to be started.
   * @return A message indicating the result of the operation:
   *         - "Order orderNumber does not exist" if the order does not exist.
   *         - "The school year has already been started" if the school year has already been started.
   *         - An empty string if the school year is successfully started.
   */

  public static String startSchoolYear(int orderNumber ) {

    boolean orderExists = Order.hasWithNumber(orderNumber);
    if(!orderExists) return "Order " + orderNumber + " does not exist";
    Order order = Order.getWithNumber(orderNumber);

    boolean yearStarted = order.startSchoolYear();

    if(!yearStarted) return "The school year has already been started";

    return "";

  }
}
