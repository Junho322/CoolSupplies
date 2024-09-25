/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 74 "model.ump"
// line 171 "model.ump"
public class BundleItem
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum Requirement { Mandatory, Recommended, Optional }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //BundleItem Attributes
  private int recommenedNumber;

  //BundleItem Associations
  private Item catalogueItem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public BundleItem(int aRecommenedNumber, Item aCatalogueItem)
  {
    recommenedNumber = aRecommenedNumber;
    if (!setCatalogueItem(aCatalogueItem))
    {
      throw new RuntimeException("Unable to create BundleItem due to aCatalogueItem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRecommenedNumber(int aRecommenedNumber)
  {
    boolean wasSet = false;
    recommenedNumber = aRecommenedNumber;
    wasSet = true;
    return wasSet;
  }

  public int getRecommenedNumber()
  {
    return recommenedNumber;
  }
  /* Code from template association_GetOne */
  public Item getCatalogueItem()
  {
    return catalogueItem;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setCatalogueItem(Item aNewCatalogueItem)
  {
    boolean wasSet = false;
    if (aNewCatalogueItem != null)
    {
      catalogueItem = aNewCatalogueItem;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    catalogueItem = null;
  }


  public String toString()
  {
    return super.toString() + "["+
            "recommenedNumber" + ":" + getRecommenedNumber()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "catalogueItem = "+(getCatalogueItem()!=null?Integer.toHexString(System.identityHashCode(getCatalogueItem())):"null");
  }
}