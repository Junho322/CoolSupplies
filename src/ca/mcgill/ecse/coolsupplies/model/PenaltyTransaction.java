/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.util.*;

// line 63 "model.ump"
// line 159 "model.ump"
public class PenaltyTransaction extends Transaction
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PenaltyTransaction Attributes
  private double penaltyAmount;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PenaltyTransaction(String aAuthorizationCode, Order aOrder, double aPenaltyAmount)
  {
    super(aAuthorizationCode, aOrder);
    penaltyAmount = aPenaltyAmount;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setPenaltyAmount(double aPenaltyAmount)
  {
    boolean wasSet = false;
    penaltyAmount = aPenaltyAmount;
    wasSet = true;
    return wasSet;
  }

  public double getPenaltyAmount()
  {
    return penaltyAmount;
  }

  public void delete()
  {
    super.delete();
  }


  public String toString()
  {
    return super.toString() + "["+
            "penaltyAmount" + ":" + getPenaltyAmount()+ "]";
  }
}