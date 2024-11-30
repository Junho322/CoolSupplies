package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.model.*;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;

/**
 * CoolSuppliesFeatureSet8Controller class provides methods to manage orders, items, and payments
 * within the CoolSupplies application. This includes updating orders, adding items to orders, updating item quantities,
 * deleting items from orders, processing payments, paying penalties, picking up orders, canceling orders, viewing individual orders,
 * retrieving all orders, and starting the school year for an order.
 *
 * @author Jun Ho
 * @author Hamza Khalfi
 * @author Jack McDonald
 * @author David Vo
 * @author David Wang
 * @author Shayan Yamnanidouzi Sorkhabi
 * @author David Zhou
 *
 * @see #updateOrder(int, String, String) Updates an order to a new level and assigns it to a student.
 * @see #addItemToOrder(int, String, int) Adds a specific item with a quantity to an order.
 * @see #updateQuantityOfAnExistingItemOfOrder(int, String, int) Updates the quantity of an existing item in an order.
 * @see #deleteOrderItem(int, String) Deletes an item from an order if it exists.
 * @see #payForOrder(int) Completes the payment process for an order.
 * @see #payPenaltyForOrder(int) Pays a penalty associated with a penalized order.
 * @see #pickUpOrder(int) Marks an order as picked up if conditions allow.
 * @see #cancelOrder(int) Cancels an order if it meets cancellation criteria.
 * @see #viewIndividualOrder(int) Retrieves detailed information about a specific order.
 * @see #getOrders() Retrieves all orders within the system.
 * @see #startSchoolYear() Resets attributes in preparation for a new school year.
 */

public class CoolSuppliesFeatureSet8Controller {

    /**
     * Updates the order with the given order number to a new purchase level and assigns it to a student.
     *
     * @param orderNumber
     * @param newLevel
     * @param StudentName
     * @return a string message indicating if the update of an order was successful or not
     * @author Shayan Yamnanidouzi Sorkhabi
     */
    public static String updateOrder(int orderNumber, String newLevel, String StudentName) {
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
            newLevel = newLevel.substring(0, 1).toUpperCase() + newLevel.substring(1);
            BundleItem.PurchaseLevel aLevel= BundleItem.PurchaseLevel.valueOf(newLevel);

            if (!order.getStatusFullName().equalsIgnoreCase("Started")) {
                switch (order.getStatusFullName()) {
                    case "Paid":
                        return "Cannot update a paid order";
                    case "Penalized":
                        return "Cannot update a penalized order";
                    case "Prepared":
                        return "Cannot update a prepared order";
                    case "PickedUp":
                        return "Cannot update a picked up order";
                    case "Final":
                        return "Cannot update a finalized order";
                    default:
                        return "Could not update the order";
                }
            }
            order.updateOrder(aLevel, aStudent); //this checks in itself if the student belond to the parent
            CoolSuppliesPersistence.save();
            return "Order updated successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    /**
     * Adds an item to an order with the given order number and quantity.
     *
     * @param itemName    The name of the item to add.
     * @param quantity    The quantity of the item to add.
     * @param orderNumber The number of the order to add the item to.a
     * @return A success message if the item was added to the order or an appropriate error message.
     * @author Jack McDonald
     */
    public static String addItemToOrder(String itemName, int quantity, int orderNumber) {
        try {
            Order order = Order.getWithNumber(orderNumber);
            if (order == null) {
                return "Order " + orderNumber + " does not exist";
            }

            InventoryItem targetItem = Item.getWithName(itemName);
            if (targetItem == null) {
                return "Item " + itemName + " does not exist.";
            }

          try {
            List<TOBundleItem> bundleItems = CoolSuppliesFeatureSet5Controller.getBundleItems(itemName);
            if (bundleItems.isEmpty()) {
              return "Bundle is empty. Add items to the bundle";
            }
          } catch (Exception e) {
          }

            //attempt to add the item to the order and store the result
            boolean wasAdded = order.addItem(targetItem, quantity);
            CoolSuppliesPersistence.save();

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
        return "Successfully added item to order";
    }

    /**
     * Updates the quantity of an existing item in an order. The item is an InventoryItem meaning that it can be a Bundle
     *
     * @param orderNumber the number of the order to update. To be more precise, it is the order which contains the item to be updated
     * @param itemName the name of the item to update its quantity
     * @param quantity the new quantity for the item to be updated to
     * @return a message indicating the result of the update operation, an appropriate error message depending on the scenario (gherkin)
     * @author Jun Ho Oh
     */
    public static String updateQuantityOfAnExistingItemOfOrder(int orderNumber, String itemName, int quantity) {
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
                switch (order.getStatusFullName()) {
                    case "Paid":
                        return "Cannot update items in a paid order";
                    case "Penalized":
                        return "Cannot update items in a penalized order";
                    case "Prepared":
                        return "Cannot update items in a prepared order";
                    case "PickedUp":
                        return "Cannot update items in a picked up order";
                    default:
                        return "Could not update the quantity of existing item of order";
                }
            }
            //then update the item's quantity
            order.updateItem(targetOrderItem, quantity);
            CoolSuppliesPersistence.save();
        }catch (RuntimeException e) {
            return e.getMessage();
        }
        return "Item updated successfully";
    }


    /**
     * Deletes an item from an order, should either exist simultaneously and if the item exists in the order.
     *
     * @param itemName String: item name to delete
     * @param orderNumber String: order number from which the item must be deleted
     * @return String to show the result of the operation
     * @author David Wang
     *
     */

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
                try {
                    itemsInOrder.get(i).delete();
                    CoolSuppliesPersistence.save();
                } catch (Exception e) {
                    return e.getMessage();
                }
                return "Item " + itemName + " deleted successfully from order " + orderNumber;
            }
        }
        return "Item " + itemName + " does not exist in the order " + orderNumber + ".";
    }

    /**
     * Processes the payment for an order.
     *
     * @param orderNumber The number of the order to be paid.
     * @param authCode The authorization code for the payment.
     * @return A message indicating the result of the payment process.
     * @author Hamza Khalfi
     */
    public static String payForOrder(int orderNumber, String authCode) {

        if (!Order.hasWithNumber(orderNumber)) {
            return "Order " + orderNumber + " does not exist";
        }

        Order order = Order.getWithNumber(orderNumber);


        if(order.getOrderItems().isEmpty()) {
            return "Order " + orderNumber + " has no items";
        }

        try {
            boolean paymentProcessed = order.pay(authCode);
            CoolSuppliesPersistence.save();

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
            return e.getMessage();
        }

        return "Payment processed";
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
    public static String payPenaltyForOrder(int orderNumber, String authorizationCode, String penaltyAuthorizationCode) {
        // Retrieve the order using the order number
        Order order = Order.getWithNumber(orderNumber);

        // Check if order exists
        if (order == null) {
            return "Order " + orderNumber + " does not exist";
        }

        // Check if authorization codes are provided
        if (penaltyAuthorizationCode == null || penaltyAuthorizationCode.length() != 4) {
            return "Penalty authorization code is invalid";
        }

        if (authorizationCode == null || authorizationCode.length() != 4) {
            return "Authorization code is invalid";
        }

        try {
            boolean success = order.payPenalty(authorizationCode, penaltyAuthorizationCode);
            if (success) {
                CoolSuppliesPersistence.save();
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
     * Picks up an order. Checks for the order's existence and status to determine if pickup is permitted.
     *
     * @param orderNumber the unique identifier of the order to pick up.
     * @return a message indicating the result of the pickup attempt.
     * @author David Vo
     */
    public String pickUpOrder(int orderNumber) {
        try {
            Order order = Order.getWithNumber(orderNumber);

            // Check if order exists
            if (order == null) {
                return "Order " + orderNumber + " does not exist";
            }

            // Process order based on its status
            switch (order.getStatusFullName()) {
                case "Started":
                    return "Cannot pickup a started order";
                case "Paid":
                    return "Cannot pickup a paid order";
                case "Penalized":
                    return "Cannot pickup a penalized order";
                case "PickedUp":
                    return "The order is already picked up";
                case "Prepared":
                    order.pickUp();
                    CoolSuppliesPersistence.save();
                    return "Order picked up successfully";
                default:
                    return "Could not pick up the order";
            }
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    /**
     * Cancels an order. Checks for the order's existence and status to determine if cancellation is permitted.
     *
     * @param orderNumber the unique identifier of the order to cancel.
     * @return a message indicating the result of the cancellation attempt.
     * @author David Vo
     */
    public String cancelOrder(int orderNumber) {
        try {
            Order order = Order.getWithNumber(orderNumber);

            // Check if order exists
            if (order == null) {
                return "Order " + orderNumber + " does not exist";
            }

            // Process order based on its status
            switch (order.getStatusFullName()) {
                case "Penalized":
                    return "Cannot cancel a penalized order";
                case "Prepared":
                    return "Cannot cancel a prepared order";
                case "PickedUp":
                    return "Cannot cancel a picked up order";
                case "Final":
                    return "Cannot cancel a finalized order";
                case "Started", "Paid": {
                    order.cancelOrder();
                    CoolSuppliesPersistence.save();
                    return "Order canceled successfully";
                }
                default:
                    return "Could not cancel the order";
            }
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }


    public static TOOrder viewIndividualOrder(int orderNumber) {
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
     * Gets all orders in the system.
     *
     * @return A list of TOOrder objects representing all orders in the system.
     * @author Jack McDonald
     */
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

    /**
     * Starts the school year for an order.
     *
     * @param orderNumber The number of the order for which the school year is to be started.
     * @return A message indicating the result of the operation.
     * @author Hamza Khalfi
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