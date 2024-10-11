/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.coolsupplies.controller;

// line 17 "../../../../../CoolSuppliesTransferObjects.ump"
public class TOItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOItem Attributes
  private String name;
  private int price;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOItem(String aName, int aPrice)
  {
    name = aName;
    price = aPrice;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getName()
  {
    return name;
  }

  public int getPrice()
  {
    return price;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "price" + ":" + getPrice()+ "]";
  }
}