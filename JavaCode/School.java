/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;
import java.sql.Date;

// line 12 "interation1.ump"
public class School
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //School Attributes
  private String schoolName;
  private double priceIncreaseRate;

  //School Associations
  private List<Parent> parents;
  private List<Order> orders;
  private SchoolYear year;
  private List<Item> catalogue;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public School(String aSchoolName, double aPriceIncreaseRate, SchoolYear aYear)
  {
    schoolName = aSchoolName;
    priceIncreaseRate = aPriceIncreaseRate;
    parents = new ArrayList<Parent>();
    orders = new ArrayList<Order>();
    if (aYear == null || aYear.getSchool() != null)
    {
      throw new RuntimeException("Unable to create School due to aYear. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    year = aYear;
    catalogue = new ArrayList<Item>();
  }

  public School(String aSchoolName, double aPriceIncreaseRate, int aYearForYear, Date aStartDateForYear, Date aEndDateForYear)
  {
    schoolName = aSchoolName;
    priceIncreaseRate = aPriceIncreaseRate;
    parents = new ArrayList<Parent>();
    orders = new ArrayList<Order>();
    year = new SchoolYear(aYearForYear, aStartDateForYear, aEndDateForYear, this);
    catalogue = new ArrayList<Item>();
    admin = new Admin(null);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSchoolName(String aSchoolName)
  {
    boolean wasSet = false;
    schoolName = aSchoolName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPriceIncreaseRate(double aPriceIncreaseRate)
  {
    boolean wasSet = false;
    priceIncreaseRate = aPriceIncreaseRate;
    wasSet = true;
    return wasSet;
  }

  public String getSchoolName()
  {
    return schoolName;
  }

  public double getPriceIncreaseRate()
  {
    return priceIncreaseRate;
  }
  /* Code from template association_GetMany */
  public Parent getParent(int index)
  {
    Parent aParent = parents.get(index);
    return aParent;
  }

  public List<Parent> getParents()
  {
    List<Parent> newParents = Collections.unmodifiableList(parents);
    return newParents;
  }

  public int numberOfParents()
  {
    int number = parents.size();
    return number;
  }

  public boolean hasParents()
  {
    boolean has = parents.size() > 0;
    return has;
  }

  public int indexOfParent(Parent aParent)
  {
    int index = parents.indexOf(aParent);
    return index;
  }
  /* Code from template association_GetMany */
  public Order getOrder(int index)
  {
    Order aOrder = orders.get(index);
    return aOrder;
  }

  public List<Order> getOrders()
  {
    List<Order> newOrders = Collections.unmodifiableList(orders);
    return newOrders;
  }

  public int numberOfOrders()
  {
    int number = orders.size();
    return number;
  }

  public boolean hasOrders()
  {
    boolean has = orders.size() > 0;
    return has;
  }

  public int indexOfOrder(Order aOrder)
  {
    int index = orders.indexOf(aOrder);
    return index;
  }
  /* Code from template association_GetOne */
  public SchoolYear getYear()
  {
    return year;
  }
  /* Code from template association_GetMany */
  public Item getCatalogue(int index)
  {
    Item aCatalogue = catalogue.get(index);
    return aCatalogue;
  }

  public List<Item> getCatalogue()
  {
    List<Item> newCatalogue = Collections.unmodifiableList(catalogue);
    return newCatalogue;
  }

  public int numberOfCatalogue()
  {
    int number = catalogue.size();
    return number;
  }

  public boolean hasCatalogue()
  {
    boolean has = catalogue.size() > 0;
    return has;
  }

  public int indexOfCatalogue(Item aCatalogue)
  {
    int index = catalogue.indexOf(aCatalogue);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfParents()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Parent addParent(String aAccountName, String aPassword)
  {
    return new Parent(aAccountName, aPassword, this);
  }

  public boolean addParent(Parent aParent)
  {
    boolean wasAdded = false;
    if (parents.contains(aParent)) { return false; }
    School existingSchool = aParent.getSchool();
    boolean isNewSchool = existingSchool != null && !this.equals(existingSchool);
    if (isNewSchool)
    {
      aParent.setSchool(this);
    }
    else
    {
      parents.add(aParent);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeParent(Parent aParent)
  {
    boolean wasRemoved = false;
    //Unable to remove aParent, as it must always have a school
    if (!this.equals(aParent.getSchool()))
    {
      parents.remove(aParent);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addParentAt(Parent aParent, int index)
  {  
    boolean wasAdded = false;
    if(addParent(aParent))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfParents()) { index = numberOfParents() - 1; }
      parents.remove(aParent);
      parents.add(index, aParent);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveParentAt(Parent aParent, int index)
  {
    boolean wasAdded = false;
    if(parents.contains(aParent))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfParents()) { index = numberOfParents() - 1; }
      parents.remove(aParent);
      parents.add(index, aParent);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addParentAt(aParent, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrders()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Order addOrder(Date aDate, String aId, boolean aIsPickedUp, boolean aOrderCompleted, Transaction aTransaction)
  {
    return new Order(aDate, aId, aIsPickedUp, aOrderCompleted, aTransaction, this);
  }

  public boolean addOrder(Order aOrder)
  {
    boolean wasAdded = false;
    if (orders.contains(aOrder)) { return false; }
    School existingSchool = aOrder.getSchool();
    boolean isNewSchool = existingSchool != null && !this.equals(existingSchool);
    if (isNewSchool)
    {
      aOrder.setSchool(this);
    }
    else
    {
      orders.add(aOrder);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrder(Order aOrder)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrder, as it must always have a school
    if (!this.equals(aOrder.getSchool()))
    {
      orders.remove(aOrder);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderAt(Order aOrder, int index)
  {  
    boolean wasAdded = false;
    if(addOrder(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderAt(Order aOrder, int index)
  {
    boolean wasAdded = false;
    if(orders.contains(aOrder))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrders()) { index = numberOfOrders() - 1; }
      orders.remove(aOrder);
      orders.add(index, aOrder);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderAt(aOrder, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfCatalogue()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addCatalogue(Item aCatalogue)
  {
    boolean wasAdded = false;
    if (catalogue.contains(aCatalogue)) { return false; }
    catalogue.add(aCatalogue);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeCatalogue(Item aCatalogue)
  {
    boolean wasRemoved = false;
    if (catalogue.contains(aCatalogue))
    {
      catalogue.remove(aCatalogue);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addCatalogueAt(Item aCatalogue, int index)
  {  
    boolean wasAdded = false;
    if(addCatalogue(aCatalogue))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCatalogue()) { index = numberOfCatalogue() - 1; }
      catalogue.remove(aCatalogue);
      catalogue.add(index, aCatalogue);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveCatalogueAt(Item aCatalogue, int index)
  {
    boolean wasAdded = false;
    if(catalogue.contains(aCatalogue))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfCatalogue()) { index = numberOfCatalogue() - 1; }
      catalogue.remove(aCatalogue);
      catalogue.add(index, aCatalogue);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addCatalogueAt(aCatalogue, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=parents.size(); i > 0; i--)
    {
      Parent aParent = parents.get(i - 1);
      aParent.delete();
    }
    for(int i=orders.size(); i > 0; i--)
    {
      Order aOrder = orders.get(i - 1);
      aOrder.delete();
    }
    SchoolYear existingYear = year;
    year = null;
    if (existingYear != null)
    {
      existingYear.delete();
    }
    catalogue.clear();
  }


  public String toString()
  {
    return super.toString() + "["+
            "schoolName" + ":" + getSchoolName()+ "," +
            "priceIncreaseRate" + ":" + getPriceIncreaseRate()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "year = "+(getYear()!=null?Integer.toHexString(System.identityHashCode(getYear())):"null");
  }
}