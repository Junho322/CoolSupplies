/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;
import java.util.*;

// line 21 "interation1.ump"
public class Order
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private Date date;
  private String id;
  private boolean isPickedUp;
  private boolean orderCompleted;

  //Order Associations
  private Transaction transaction;
  private School school;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(Date aDate, String aId, boolean aIsPickedUp, boolean aOrderCompleted, Transaction aTransaction, School aSchool)
  {
    date = aDate;
    id = aId;
    isPickedUp = aIsPickedUp;
    orderCompleted = aOrderCompleted;
    if (aTransaction == null || aTransaction.getOrder() != null)
    {
      throw new RuntimeException("Unable to create Order due to aTransaction. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    transaction = aTransaction;
    boolean didAddSchool = setSchool(aSchool);
    if (!didAddSchool)
    {
      throw new RuntimeException("Unable to create order due to school. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Order(Date aDate, String aId, boolean aIsPickedUp, boolean aOrderCompleted, String aAuthorizationCodeForTransaction, School aSchool)
  {
    date = aDate;
    id = aId;
    isPickedUp = aIsPickedUp;
    orderCompleted = aOrderCompleted;
    transaction = new Transaction(aAuthorizationCodeForTransaction, this);
    boolean didAddSchool = setSchool(aSchool);
    if (!didAddSchool)
    {
      throw new RuntimeException("Unable to create order due to school. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setId(String aId)
  {
    boolean wasSet = false;
    id = aId;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsPickedUp(boolean aIsPickedUp)
  {
    boolean wasSet = false;
    isPickedUp = aIsPickedUp;
    wasSet = true;
    return wasSet;
  }

  public boolean setOrderCompleted(boolean aOrderCompleted)
  {
    boolean wasSet = false;
    orderCompleted = aOrderCompleted;
    wasSet = true;
    return wasSet;
  }

  public Date getDate()
  {
    return date;
  }

  public String getId()
  {
    return id;
  }

  public boolean getIsPickedUp()
  {
    return isPickedUp;
  }

  public boolean getOrderCompleted()
  {
    return orderCompleted;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isIsPickedUp()
  {
    return isPickedUp;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isOrderCompleted()
  {
    return orderCompleted;
  }
  /* Code from template association_GetOne */
  public Transaction getTransaction()
  {
    return transaction;
  }
  /* Code from template association_GetOne */
  public School getSchool()
  {
    return school;
  }
  /* Code from template association_SetOneToMany */
  public boolean setSchool(School aSchool)
  {
    boolean wasSet = false;
    if (aSchool == null)
    {
      return wasSet;
    }

    School existingSchool = school;
    school = aSchool;
    if (existingSchool != null && !existingSchool.equals(aSchool))
    {
      existingSchool.removeOrder(this);
    }
    school.addOrder(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Transaction existingTransaction = transaction;
    transaction = null;
    if (existingTransaction != null)
    {
      existingTransaction.delete();
    }
    School placeholderSchool = school;
    this.school = null;
    if(placeholderSchool != null)
    {
      placeholderSchool.removeOrder(this);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "isPickedUp" + ":" + getIsPickedUp()+ "," +
            "orderCompleted" + ":" + getOrderCompleted()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "transaction = "+(getTransaction()!=null?Integer.toHexString(System.identityHashCode(getTransaction())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "school = "+(getSchool()!=null?Integer.toHexString(System.identityHashCode(getSchool())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 28 "interation1.ump"
  orders -- 1 Parent ;
// line 29 "interation1.ump"
  -> 1 Student ;

  
}