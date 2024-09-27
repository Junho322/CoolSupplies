/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.coolsupplies.controller;

// line 11 "../../../../../CoolSuppliesTransferObjects.ump"
public class TOStudent
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOStudent Attributes
  private String name;
  private String gradeLevel;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOStudent(String aName, String aGradeLevel)
  {
    name = aName;
    gradeLevel = aGradeLevel;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getName()
  {
    return name;
  }

  public String getGradeLevel()
  {
    return gradeLevel;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "name" + ":" + getName()+ "," +
            "gradeLevel" + ":" + getGradeLevel()+ "]";
  }
}