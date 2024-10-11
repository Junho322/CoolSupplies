/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/

package ca.mcgill.ecse.coolsupplies.controller;

// line 23 "../../../../../CoolSuppliesTransferObjects.ump"
public class TOGradeBundle
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOGradeBundle Attributes
  private String name;
  private int discount;
  private String gradeLevel;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOGradeBundle(String aName, int aDiscount, String aGradeLevel)
  {
    name = aName;
    discount = aDiscount;
    gradeLevel = aGradeLevel;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public String getName()
  {
    return name;
  }

  public int getDiscount()
  {
    return discount;
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
            "discount" + ":" + getDiscount()+ "," +
            "gradeLevel" + ":" + getGradeLevel()+ "]";
  }
}