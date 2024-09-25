/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 82 "model.ump"
// line 178 "model.ump"
public class Item
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Item Attributes
  private String name;
  private double basePrice;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Item(String aName, double aBasePrice)
  {
    name = aName;
    basePrice = aBasePrice;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setBasePrice(double aBasePrice)
  {
    boolean wasSet = false;
    basePrice = aBasePrice;
    wasSet = true;
    return wasSet;
  }

  public String getName()
  {
    return name;
  }

  public double getBasePrice()
  {
    return basePrice;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "basePrice" + ":" + getBasePrice()+ "]";
  }
}