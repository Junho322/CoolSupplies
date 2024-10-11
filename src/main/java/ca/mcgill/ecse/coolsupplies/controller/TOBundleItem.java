/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.coolsupplies.controller;

// line 30 "../../../../../CoolSuppliesTransferObjects.ump"
public class TOBundleItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOBundleItem Attributes
  private int quantity;
  private String level;
  private String itemName;
  private String gradeBundleName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOBundleItem(int aQuantity, String aLevel, String aItemName, String aGradeBundleName)
  {
    quantity = aQuantity;
    level = aLevel;
    itemName = aItemName;
    gradeBundleName = aGradeBundleName;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getQuantity()
  {
    return quantity;
  }

  public String getLevel()
  {
    return level;
  }

  public String getItemName()
  {
    return itemName;
  }

  public String getGradeBundleName()
  {
    return gradeBundleName;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "quantity" + ":" + getQuantity()+ "," +
            "level" + ":" + getLevel()+ "," +
            "itemName" + ":" + getItemName()+ "," +
            "gradeBundleName" + ":" + getGradeBundleName()+ "]";
  }
}