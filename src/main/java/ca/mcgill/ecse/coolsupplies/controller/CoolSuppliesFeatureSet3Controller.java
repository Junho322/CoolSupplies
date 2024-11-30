package ca.mcgill.ecse.coolsupplies.controller;

import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.InventoryItem;
import ca.mcgill.ecse.coolsupplies.model.Item;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;


/**
 * This controller feature class handles all operations concerning Items
 * Methods include adding, updating,and deleting items (from list of items)
 * 
 * @author Jun Ho Oh
 */
public class CoolSuppliesFeatureSet3Controller {

  /**
   * Adds an Item to the application CoolSupplies
   * 
   * @param name name of the item to be added
   * @param price price of the item to be added
   * @return a string message that indicates whether the item was added or not, name has to be unique, not empty and requires a positive price
   * @author Jun Ho Oh
   */
  public static String addItem(String name, int price) {
    CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
    //checks if name exists already
    if (Item.hasWithName(name)){
      return "The name must be unique.";
    }  
    //checks if name is imputed

    if (name == null || name.isEmpty()){
      return "The name must not be empty.";
    }
    //checks if price is positive
    if (price < 0){
      return "The price must be greater than or equal to 0.";
    }

    //we can now add the Item to the coolSupplies application using .addItem() from CoolSupplies.java
    //Exception handling is already done since Item.java constructor is called, which handles exceptions
    try {
      coolSupplies.addItem(name, price);
      CoolSuppliesPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }
      return "Item added successfully.";
  }

  /**
   * Updates the desired item's name and price with a new name and a new price
   * 
   * @param name the name of the item we desire to update
   * @param newName the new name that we will update to the desired item
   * @param newPrice the new price that we will update to the desired item
   * @return a string message that indicates whether the item was updated or not, the desired item's new name must not be empty, desired item must exist and must be unique, and the new price must be positive integer 
   * @author Jun Ho Oh
   */
  public static String updateItem(String name, String newName, int newPrice) {
    //check the values inputed for new name and new price
    if (newName == null || newName.isEmpty()){
      return "The name must not be empty.";
    }
    if (newPrice < 0) {
      return "The price must be greater than or equal to 0.";
    }
    
    Item targetItem = (Item) InventoryItem.getWithName(name);
    
    //check if the item that we wish to update exists
    if (targetItem == null){
      return "The item does not exist.";
    }

    //check if the item with the new name already exists
    if (Item.hasWithName(newName)){
      return "The name must be unique.";
    }

    //update name and price to new ones
    try {
      targetItem.setName(newName);
      targetItem.setPrice(newPrice);
      CoolSuppliesPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }

      return "Item was succesfully updated";
  }
  
  /**
   * Removes the desired item from the application 
   * 
   * @param name the name of the item to be removed
   * @return a string message indicating whether item was removed or not; item must exist to be removed
   * @author Jun Ho Oh
   */
  public static String deleteItem(String name) {
    Item targetItem = (Item) InventoryItem.getWithName(name);

    if (targetItem == null){
      return "The item does not exist.";
    }

    //after checking, now delete item
    try {
      targetItem.delete();
      CoolSuppliesPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }

      return "the item has successfully been deleted";
    
  }

  /**
   * Gets an item by the name inputed 
   * 
   * @param name the name of the item we wish to get
   * @return a TOItem object that represents the item we wish to get. Returns null if item does not exist
   * @author Jun Ho Oh
   */
  public static TOItem getItem(String name) {

    //if the item does not exist
    if (!Item.hasWithName(name)){
      return null;
    }
    //then we can get the desired item in a variable
    Item targetItem = (Item) InventoryItem.getWithName(name);

    //if TOItem does not exist?
    if (targetItem != null){
      return new TOItem(targetItem.getName(), targetItem.getPrice());
    }

    return null;
  }

  /**
   * Gets all items into a list of items
   * 
   * @return a List<TOItem> list that contains all items in the application
   * @author Jun Ho Oh
   */
  public static List<TOItem> getItems() {
    CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
    List<TOItem> listTOItems = new ArrayList<TOItem>();

    //check for non existing items??

    //iterate through the items in coolsupplies and add it into the new list
    for (Item i : coolSupplies.getItems()){
      listTOItems.add(new TOItem(i.getName(), i.getPrice()));
    }
    return listTOItems;
  }
}
