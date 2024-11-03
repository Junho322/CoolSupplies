package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the controller methods for the features in the eighth feature set.
 * The methods in this class are used to manage orders in the CoolSupplies system.
 *
 * @author Group p2
 */
public class CoolSuppliesFeatureSet8Controller {

    /**
     * Adds an item to an order.
     *
     * @param itemName The name of the item to add.
     * @param quantity The quantity of the item to add.
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

            InventoryItem targetItem;

            List<InventoryItem> items = new ArrayList<>(coolSupplies.getItems());
            items.addAll(new ArrayList<InventoryItem>(coolSupplies.getBundles()));

            targetItem = items.stream().filter(item -> item.getName().equals(itemName)).findFirst().orElse(null);

            if (targetItem == null) {
                return "Item " + itemName + " does not exist";
            }

            boolean wasAdded = order.addItem(targetItem, quantity);

            if (!wasAdded) {
                return switch (order.getStatusFullName()) {
                    case "Paid" -> "Cannot add items to a paid order";
                    case "Penalized" -> "Cannot add items to a penalized order";
                    case "Prepared" -> "Cannot add items to a prepared order";
                    case "PickedUp" -> "Cannot add items to a picked up order";
                    default -> "Could not add item to order";
                };
            }
        } catch (RuntimeException e) {
            return e.getMessage();
        }
        return "Could not add item to order";
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
            double total = 0;

            for (OrderItem orderItem : order.getOrderItems()) {
                InventoryItem inventoryItem = orderItem.getItem();

                if (inventoryItem instanceof Item) {
                    Item item = (Item) inventoryItem;
                    total += orderItem.getQuantity() * item.getPrice();
                }
                if (inventoryItem instanceof GradeBundle) {
                    int uniqueItemCount = 0;
                    for (BundleItem bundleItem : ((GradeBundle) inventoryItem).getBundleItems()) {
                        if (bundleItem.getLevel() == BundleItem.PurchaseLevel.Mandatory ||
                                (order.getLevel() == BundleItem.PurchaseLevel.Optional ||
                                        order.getLevel() == bundleItem.getLevel())) {
                            total += bundleItem.getQuantity() * bundleItem.getItem().getPrice();
                            uniqueItemCount++;
                        }
                    }
                    GradeBundle bundle = (GradeBundle) inventoryItem;
                    if (uniqueItemCount >= 2) total -= bundle.getDiscount();
                }
            }

            orders.add(new TOOrder(order.getParent().getEmail(), order.getStudent().getName(),
                    order.getStatusFullName(), order.getNumber(), order.getDate(), order.getLevel().name(),
                    order.getAuthorizationCode(), order.getPenaltyAuthorizationCode(), total, null));
        }
        return orders;
    }
}
