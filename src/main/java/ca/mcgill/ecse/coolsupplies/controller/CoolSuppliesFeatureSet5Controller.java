package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.BundleItem;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.GradeBundle;
import ca.mcgill.ecse.coolsupplies.model.Grade;
import ca.mcgill.ecse.coolsupplies.model.Item;
import ca.mcgill.ecse.coolsupplies.model.BundleItem.PurchaseLevel;
import ca.mcgill.ecse.coolsupplies.model.InventoryItem;

import java.util.List;
import java.util.ArrayList;

/**
 * Controller class for handling operations related to Items in a Bundle
 * Methods include Adding an item to bundle, updating it, and removing item from bundle
 *
 * @author Shayan Yamanidouzi Sorkhabi
 */

public class CoolSuppliesFeatureSet5Controller {


  /**
   * Adds an item to the current bundle
   * 
   * @param quantity
   * @param level
   * @param itemName
   * @param bundleName
   * @return A string message indicating if the item is added to the bundle or not
   * @author Shayan Yamanidouzi Sorkhabi
   */

  public static String addBundleItem(int quantity, String level, String itemName,
      String bundleName) {
        if (quantity <= 0) {
          return "The quantity must be greater than 0.";
        }
        if (!level.equalsIgnoreCase("mandatory") && !level.equalsIgnoreCase("optional") && !level.equalsIgnoreCase("recommended")) {
          return "The level must be Mandatory, Recommended, or Optional.";
        }
        if (!GradeBundle.hasWithName(bundleName)) {
          return "The grade bundle does not exist.";
        }
        if (!Item.hasWithName(itemName)) {
          return "The item does not exist.";
        }
        
        CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

        BundleItem.PurchaseLevel aLevel= BundleItem.PurchaseLevel.valueOf(level);
        
        Item aItem = (Item) Item.getWithName(itemName);
        GradeBundle aBundle = (GradeBundle) GradeBundle.getWithName(bundleName);
        List<BundleItem> bundleItems = aBundle.getBundleItems();
        for (BundleItem bundleItem : bundleItems) {
          if (bundleItem.getItem().equals(aItem)) {
            return "The item already exists for the bundle.";
          }
        }

        try {
          //BundleItem newBundleItem = coolSupplies.addBundleItem(quantity, aLevel, aBundle, aItem);
          coolSupplies.addBundleItem(quantity, aLevel, aBundle, aItem);
          return "BundleItem added succefully.";
        } catch (Exception e) {
          return e.getMessage();
        }
      }
    /**
     * 
     * @param itemName
     * @param bundleName
     * @param newQuantity
     * @param newLevel
     * @return A string message indicating whether the desired item in the bundle was updated or not
     * @author Shayan Yamanidouzi Sorkhabi
     */
  public static String updateBundleItem(String itemName, String bundleName, int newQuantity,
      String newLevel) {

        if (newQuantity <= 0) {
          return "The quantity must be greater than 0.";
        }
        if (!newLevel.equalsIgnoreCase("mandatory") && !newLevel.equalsIgnoreCase("optional") && !newLevel.equalsIgnoreCase("recommended")) {
          return "The level must be Mandatory, Recommended, or Optional.";
        }
        if (!GradeBundle.hasWithName(bundleName)) {
          return "The grade bundle does not exist.";
        }

        BundleItem.PurchaseLevel aNewLevel= BundleItem.PurchaseLevel.valueOf(newLevel);

        Item aItem = (Item) Item.getWithName(itemName);
        GradeBundle aBundle = (GradeBundle) GradeBundle.getWithName(bundleName);
        List<BundleItem> bundleItems = aBundle.getBundleItems();
        for (BundleItem bundleItem : bundleItems) {
          if (bundleItem.getItem().equals(aItem)) {
            bundleItem.setQuantity(newQuantity);
            bundleItem.setLevel(aNewLevel);
            return "Successfullly updated the bundle item.";
          }
        }

        return "The bundle item does not exist for the grade bundle.";

  }

  /**
   * 
   * @param itemName
   * @param bundleName
   * @return a string message indicating whether a desired item was removed or not in case of an error
   * @author Shayan Yamanidouzi Sorkhabi
   */
  public static String deleteBundleItem(String itemName, String bundleName) {

    if (!GradeBundle.hasWithName(bundleName)) {
      return "The grade bundle does not exist.";
    }
    
    Item aTargetItem = (Item) Item.getWithName(itemName);
    GradeBundle aBundle = (GradeBundle) GradeBundle.getWithName(bundleName);

    List<BundleItem> bundleItems = aBundle.getBundleItems();
    for (BundleItem bundleItem : bundleItems) {
      if (bundleItem.getItem().equals(aTargetItem)) {
        aTargetItem.delete();
        return "Bundle Item successfully deleted.";
      }
    }

    return "The bundle item does not exist.";
  }

  /**
   * 
   * @param itemName
   * @param bundleName
   * @return a TOBundleItem object representing the bundle item found if not then it returns null
   * @author Shayan Yamanidouzi Sorkhabi
   */
  public static TOBundleItem getBundleItem(String itemName, String bundleName) {

    if (!GradeBundle.hasWithName(bundleName)) {
      return null;
    }

    Item aTargetItem = (Item) Item.getWithName(itemName);
    GradeBundle aBundle = (GradeBundle) GradeBundle.getWithName(bundleName);

    List<BundleItem> bundleItems = aBundle.getBundleItems();

    for (BundleItem bundleItem : bundleItems) {
      if (bundleItem.getItem().equals(aTargetItem)) {
        return new TOBundleItem(bundleItem.getQuantity(), bundleItem.getLevel().toString(), itemName, bundleName);
      }
    }

    return null;

  }

  /**
   * 
   * @param bundleName
   * @return all bundle items of a bundle
   * @author Shayan Yamanidouzi Sorkhabi
   */
 
  public static List<TOBundleItem> getBundleItems(String bundleName) {
    
    List<TOBundleItem> TObundleItems = new ArrayList<>();

    if (!GradeBundle.hasWithName(bundleName)) {
      return TObundleItems;
    }

    GradeBundle aBundle = (GradeBundle) GradeBundle.getWithName(bundleName);
    
    List<BundleItem> bundleItems = aBundle.getBundleItems();
    for (BundleItem bundleItem : bundleItems) {
      TOBundleItem toBundleItem = new TOBundleItem(bundleItem.getQuantity(), bundleItem.getLevel().toString(),bundleItem.getItem().getName(), bundleName);
      TObundleItems.add(toBundleItem);
    }

    return TObundleItems;

  }

}
