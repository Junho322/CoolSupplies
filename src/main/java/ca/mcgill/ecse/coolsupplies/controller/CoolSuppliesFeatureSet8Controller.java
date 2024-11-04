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
}
