/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;

// line 84 "interation1.ump"
public class System
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static System theInstance = null;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //System Associations
  private List<School> schools;
  private List<Account> accounts;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  private System()
  {
    schools = new ArrayList<School>();
    accounts = new ArrayList<Account>();
  }

  public static System getInstance()
  {
    if(theInstance == null)
    {
      theInstance = new System();
    }
    return theInstance;
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public School getSchool(int index)
  {
    School aSchool = schools.get(index);
    return aSchool;
  }

  public List<School> getSchools()
  {
    List<School> newSchools = Collections.unmodifiableList(schools);
    return newSchools;
  }

  public int numberOfSchools()
  {
    int number = schools.size();
    return number;
  }

  public boolean hasSchools()
  {
    boolean has = schools.size() > 0;
    return has;
  }

  public int indexOfSchool(School aSchool)
  {
    int index = schools.indexOf(aSchool);
    return index;
  }
  /* Code from template association_GetMany */
  public Account getAccount(int index)
  {
    Account aAccount = accounts.get(index);
    return aAccount;
  }

  public List<Account> getAccounts()
  {
    List<Account> newAccounts = Collections.unmodifiableList(accounts);
    return newAccounts;
  }

  public int numberOfAccounts()
  {
    int number = accounts.size();
    return number;
  }

  public boolean hasAccounts()
  {
    boolean has = accounts.size() > 0;
    return has;
  }

  public int indexOfAccount(Account aAccount)
  {
    int index = accounts.indexOf(aAccount);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSchools()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addSchool(School aSchool)
  {
    boolean wasAdded = false;
    if (schools.contains(aSchool)) { return false; }
    schools.add(aSchool);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSchool(School aSchool)
  {
    boolean wasRemoved = false;
    if (schools.contains(aSchool))
    {
      schools.remove(aSchool);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addSchoolAt(School aSchool, int index)
  {  
    boolean wasAdded = false;
    if(addSchool(aSchool))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSchools()) { index = numberOfSchools() - 1; }
      schools.remove(aSchool);
      schools.add(index, aSchool);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveSchoolAt(School aSchool, int index)
  {
    boolean wasAdded = false;
    if(schools.contains(aSchool))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfSchools()) { index = numberOfSchools() - 1; }
      schools.remove(aSchool);
      schools.add(index, aSchool);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addSchoolAt(aSchool, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfAccounts()
  {
    return 0;
  }
  /* Code from template association_AddUnidirectionalMany */
  public boolean addAccount(Account aAccount)
  {
    boolean wasAdded = false;
    if (accounts.contains(aAccount)) { return false; }
    accounts.add(aAccount);
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeAccount(Account aAccount)
  {
    boolean wasRemoved = false;
    if (accounts.contains(aAccount))
    {
      accounts.remove(aAccount);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addAccountAt(Account aAccount, int index)
  {  
    boolean wasAdded = false;
    if(addAccount(aAccount))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAccounts()) { index = numberOfAccounts() - 1; }
      accounts.remove(aAccount);
      accounts.add(index, aAccount);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveAccountAt(Account aAccount, int index)
  {
    boolean wasAdded = false;
    if(accounts.contains(aAccount))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfAccounts()) { index = numberOfAccounts() - 1; }
      accounts.remove(aAccount);
      accounts.add(index, aAccount);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addAccountAt(aAccount, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    schools.clear();
    accounts.clear();
  }

}