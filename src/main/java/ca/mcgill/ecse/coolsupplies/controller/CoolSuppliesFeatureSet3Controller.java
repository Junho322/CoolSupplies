package ca.mcgill.ecse.coolsupplies.controller;

import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.InventoryItem;
import ca.mcgill.ecse.coolsupplies.model.Item;

public class CoolSuppliesFeatureSet3Controller {

  public static String addItem(String name, int price) {
    CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
    //checks if name exists already
    if (Item.hasWithName(name)){
      return "The name must be unique.";
    }  
    //checks if name is imputed
    if (name == null || name == ""){
      return "The name must not be empty.";
    }
    //checks if price is positive
    if (price < 0){
      return "The price must be greater than or equal to 0.";
    }

    //idk if try catch is necessary here
    try {
      //since we already checked if the item already exists,
      //we can now create a new item
      new Item(name, price, coolSupplies);
      return "Item added successfully.";
    } catch (RuntimeException e) {
        return e.getMessage();
    }
    
  }

  public static String updateItem(String name, String newName, int newPrice) {
    Item targetItem = (Item) InventoryItem.getWithName(name);
    
    //check if the item that we wish to update exists
    if (targetItem == null){
      return "The item does not exist.";
    }

    //check the values inputed for new name and new price
    if (newName == null || newName == ""){
      return "The name must not be empty.";
    }
    if (newPrice < 0) {
      return "The price must be greater than or equal to 0.";
    }

    //check if the item with the new name already exists
    if (Item.hasWithName(newName)){
      return "the name must be unique.";
    }

    //update name and price to new ones
    targetItem.setName(newName);
    targetItem.setPrice(newPrice);

    return "Item was succesfully updated";
  }

  public static String deleteItem(String name) {
    Item targetItem = (Item) InventoryItem.getWithName(name);

    if (targetItem == null){
      return "The item does not exist.";
    }

    //delete item
    targetItem.delete();

    return "the item has successfully been deleted";
    
  }

  public static TOItem getItem(String name) {

    Item targetItem = (Item) InventoryItem.getWithName(name);

    //if TOItem does not exist?
    if (targetItem != null){
      return new TOItem(targetItem.getName(), targetItem.getPrice());
    }

    //if the item does not exist
    if (!Item.hasWithName(name)){
      return null;
    }

    return null;
  }

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
