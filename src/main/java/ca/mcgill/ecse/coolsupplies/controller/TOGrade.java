/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.coolsupplies.controller;

// line 38 "../../../../../CoolSuppliesTransferObjects.ump"
public class TOGrade
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOGrade Attributes
  private String level;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOGrade(String aLevel)
  {
    level = aLevel;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getLevel()
  {
    return level;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "level" + ":" + getLevel()+ "]";
  }
}