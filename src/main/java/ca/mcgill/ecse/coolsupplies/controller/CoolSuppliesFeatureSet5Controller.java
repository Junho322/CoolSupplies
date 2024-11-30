package ca.mcgill.ecse.coolsupplies.controller;

import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.BundleItem;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.GradeBundle;
import ca.mcgill.ecse.coolsupplies.model.Grade;
import ca.mcgill.ecse.coolsupplies.model.Item;
import ca.mcgill.ecse.coolsupplies.model.BundleItem.PurchaseLevel;
import ca.mcgill.ecse.coolsupplies.model.InventoryItem;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;

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
   * @param quantity    number of items to be added
   * @param level       requirement level of the item to be added
   * @param itemName    name of the item to be added
   * @param bundleName  name of the bundle to which the item will be added to
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
      System.out.println(bundleItems);
        for (BundleItem bundleItem : bundleItems) {
          if (bundleItem.getItem().equals(aItem)) {
            return "The item already exists for the bundle.";
          }
        }

      try {
          coolSupplies.addBundleItem(quantity, aLevel, aBundle, aItem);
          CoolSuppliesPersistence.save();
      } catch (Exception e) {
          return e.getMessage();
      }
        return "BundleItem added succefully.";
      }

    /**
     * Update the quantity and the requirement level of an item in the bundle
     *
     * @param itemName      name of the item that is to be updated
     * @param bundleName    name of the bundle in which the item is in
     * @param newQuantity   new quantity for the item
     * @param newLevel      new requirement item for the item
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
              try {
                  bundleItem.setQuantity(newQuantity);
                  bundleItem.setLevel(aNewLevel);
                  CoolSuppliesPersistence.save();
              } catch (Exception e) {
                  return e.getMessage();
              }
              return "Successfullly updated the bundle item.";
          }
        }

        return "The bundle item does not exist for the grade bundle.";

  }

  /**
   * removes an item form the bundle
   *
   * @param itemName    name of the item to be removed
   * @param bundleName  name of the bundle in which the item that is to be removed was in
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
          try {
              bundleItem.delete();
              CoolSuppliesPersistence.save();
          } catch (Exception e) {
              return e.getMessage();
          }
          if (aBundle.getBundleItems().size() < 2) {
              aBundle.setDiscount(0);
              CoolSuppliesPersistence.save();
          }
          return "Bundle Item successfully deleted.";
      }
    }

    return "The bundle item does not exist.";
  }

  /**
   * Gets a bundle item by its name and bundle name
   *
   * @param itemName    name of the item
   * @param bundleName  name of the bundle
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
   * Gets all bundle items of a bundle
   *
   * @param bundleName  name if the bundle
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
