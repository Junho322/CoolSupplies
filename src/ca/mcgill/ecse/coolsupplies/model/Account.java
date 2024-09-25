/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;

// line 1 "interation1.ump"
public abstract class Account
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<String, Account> accountsByAccountName = new HashMap<String, Account>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Account Attributes
  private String accountName;
  private String password;

  //Helper Variables
  private boolean canSetAccountName;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Account(String aAccountName, String aPassword)
  {
    canSetAccountName = true;
    password = aPassword;
    if (!setAccountName(aAccountName))
    {
      throw new RuntimeException("Cannot create due to duplicate accountName. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template attribute_SetImmutable */
  public boolean setAccountName(String aAccountName)
  {
    boolean wasSet = false;
    if (!canSetAccountName) { return false; }
    String anOldAccountName = getAccountName();
    if (anOldAccountName != null && anOldAccountName.equals(aAccountName)) {
      return true;
    }
    if (hasWithAccountName(aAccountName)) {
      return wasSet;
    }
    canSetAccountName = false;
    accountName = aAccountName;
    wasSet = true;
    if (anOldAccountName != null) {
      accountsByAccountName.remove(anOldAccountName);
    }
    accountsByAccountName.put(aAccountName, this);
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public String getAccountName()
  {
    return accountName;
  }
  /* Code from template attribute_GetUnique */
  public static Account getWithAccountName(String aAccountName)
  {
    return accountsByAccountName.get(aAccountName);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithAccountName(String aAccountName)
  {
    return getWithAccountName(aAccountName) != null;
  }

  public String getPassword()
  {
    return password;
  }

  public void delete()
  {
    accountsByAccountName.remove(getAccountName());
  }


  public String toString()
  {
    return super.toString() + "["+
            "accountName" + ":" + getAccountName()+ "," +
            "password" + ":" + getPassword()+ "]";
  }
}