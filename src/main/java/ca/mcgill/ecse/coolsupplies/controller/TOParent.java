/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.coolsupplies.controller;

// line 3 "../../../../../CoolSuppliesTransferObjects.ump"
public class TOParent
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOParent Attributes
  private String email;
  private String password;
  private String name;
  private int phoneNumber;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOParent(String aEmail, String aPassword, String aName, int aPhoneNumber)
  {
    email = aEmail;
    password = aPassword;
    name = aName;
    phoneNumber = aPhoneNumber;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
  }

  public String getName()
  {
    return name;
  }

  public int getPhoneNumber()
  {
    return phoneNumber;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "password" + ":" + getPassword()+ "," +
            "name" + ":" + getName()+ "," +
            "phoneNumber" + ":" + getPhoneNumber()+ "]";
  }
}