/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;
import java.sql.Date;

// line 40 "model.ump"
// line 134 "model.ump"
public class Transaction
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Transaction> transactionsByAuthorizationCode = new HashMap<String, Transaction>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Transaction Attributes
  private String authorizationCode;

  //Transaction Associations
  private Order order;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Transaction(String aAuthorizationCode, Order aOrder)
  {
    if (!setAuthorizationCode(aAuthorizationCode))
    {
      throw new RuntimeException("Cannot create due to duplicate authorizationCode. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    if (aOrder == null || aOrder.getTransaction() != null)
    {
      throw new RuntimeException("Unable to create Transaction due to aOrder. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    order = aOrder;
  }

  public Transaction(String aAuthorizationCode, Date aDateForOrder, String aIdForOrder, boolean aIsPickedUpForOrder, boolean aOrderCompletedForOrder, School aSchoolForOrder)
  {
    if (!setAuthorizationCode(aAuthorizationCode))
    {
      throw new RuntimeException("Cannot create due to duplicate authorizationCode. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    order = new Order(aDateForOrder, aIdForOrder, aIsPickedUpForOrder, aOrderCompletedForOrder, this, aSchoolForOrder);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setAuthorizationCode(String aAuthorizationCode)
  {
    boolean wasSet = false;
    String anOldAuthorizationCode = getAuthorizationCode();
    if (anOldAuthorizationCode != null && anOldAuthorizationCode.equals(aAuthorizationCode)) {
      return true;
    }
    if (hasWithAuthorizationCode(aAuthorizationCode)) {
      return wasSet;
    }
    authorizationCode = aAuthorizationCode;
    wasSet = true;
    if (anOldAuthorizationCode != null) {
      transactionsByAuthorizationCode.remove(anOldAuthorizationCode);
    }
    transactionsByAuthorizationCode.put(aAuthorizationCode, this);
    return wasSet;
  }

  public String getAuthorizationCode()
  {
    return authorizationCode;
  }
  /* Code from template attribute_GetUnique */
  public static Transaction getWithAuthorizationCode(String aAuthorizationCode)
  {
    return transactionsByAuthorizationCode.get(aAuthorizationCode);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithAuthorizationCode(String aAuthorizationCode)
  {
    return getWithAuthorizationCode(aAuthorizationCode) != null;
  }
  /* Code from template association_GetOne */
  public Order getOrder()
  {
    return order;
  }

  public void delete()
  {
    transactionsByAuthorizationCode.remove(getAuthorizationCode());
    Order existingOrder = order;
    order = null;
    if (existingOrder != null)
    {
      existingOrder.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "authorizationCode" + ":" + getAuthorizationCode()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "order = "+(getOrder()!=null?Integer.toHexString(System.identityHashCode(getOrder())):"null");
  }
}