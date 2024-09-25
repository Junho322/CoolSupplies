/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;

// line 7 "interation1.ump"
public class Admin extends Account
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Admin Associations
  private School school;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Admin(String aAccountName, String aPassword, School aSchool)
  {
    super(aAccountName, aPassword);
    if (!setSchool(aSchool))
    {
      throw new RuntimeException("Unable to create Admin due to aSchool. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public School getSchool()
  {
    return school;
  }
  /* Code from template association_SetUnidirectionalOne */
  public boolean setSchool(School aNewSchool)
  {
    boolean wasSet = false;
    if (aNewSchool != null)
    {
      school = aNewSchool;
      wasSet = true;
    }
    return wasSet;
  }

  public void delete()
  {
    school = null;
    super.delete();
  }

}