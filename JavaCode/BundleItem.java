/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/



// line 73 "interation1.ump"
public class BundleItem
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //BundleItem Attributes
  private int recommendedNumber;

  //BundleItem Associations
  private Item catalogueItem;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public BundleItem(int aRecommendedNumber, Item aCatalogueItem)
  {
    recommendedNumber = aRecommendedNumber;
    if (!setCatalogueItem(aCatalogueItem))
    {
      throw new RuntimeException("Unable to create BundleItem due to aCatalogueItem. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setRecommendedNumber(int aRecommendedNumber)
  {
    boolean wasSet = false;
    recommendedNumber = aRecommendedNumber;
    wasSet = true;
    return wasSet;
  }

  public int getRecommendedNumber()
  {
    return recommendedNumber;
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
            "recommendedNumber" + ":" + getRecommendedNumber()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "catalogueItem = "+(getCatalogueItem()!=null?Integer.toHexString(System.identityHashCode(getCatalogueItem())):"null");
  }
}