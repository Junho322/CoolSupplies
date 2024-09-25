/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.34.0.7242.6b8819789 modeling language!*/


import java.sql.Date;
import java.util.*;

// line 43 "interation1.ump"
public class SchoolYear
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //SchoolYear Attributes
  private int year;
  private Date startDate;
  private Date endDate;

  //SchoolYear Associations
  private List<Grade> grades;
  private School school;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public SchoolYear(int aYear, Date aStartDate, Date aEndDate, School aSchool)
  {
    year = aYear;
    startDate = aStartDate;
    endDate = aEndDate;
    grades = new ArrayList<Grade>();
    if (aSchool == null || aSchool.getYear() != null)
    {
      throw new RuntimeException("Unable to create SchoolYear due to aSchool. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    school = aSchool;
  }

  public SchoolYear(int aYear, Date aStartDate, Date aEndDate, String aSchoolNameForSchool, double aPriceIncreaseRateForSchool)
  {
    year = aYear;
    startDate = aStartDate;
    endDate = aEndDate;
    grades = new ArrayList<Grade>();
    school = new School(aSchoolNameForSchool, aPriceIncreaseRateForSchool, this);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setYear(int aYear)
  {
    boolean wasSet = false;
    year = aYear;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartDate(Date aStartDate)
  {
    boolean wasSet = false;
    startDate = aStartDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndDate(Date aEndDate)
  {
    boolean wasSet = false;
    endDate = aEndDate;
    wasSet = true;
    return wasSet;
  }

  public int getYear()
  {
    return year;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }
  /* Code from template association_GetMany */
  public Grade getGrade(int index)
  {
    Grade aGrade = grades.get(index);
    return aGrade;
  }

  public List<Grade> getGrades()
  {
    List<Grade> newGrades = Collections.unmodifiableList(grades);
    return newGrades;
  }

  public int numberOfGrades()
  {
    int number = grades.size();
    return number;
  }

  public boolean hasGrades()
  {
    boolean has = grades.size() > 0;
    return has;
  }

  public int indexOfGrade(Grade aGrade)
  {
    int index = grades.indexOf(aGrade);
    return index;
  }
  /* Code from template association_GetOne */
  public School getSchool()
  {
    return school;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfGrades()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Grade addGrade(String aGradeLevel)
  {
    return new Grade(aGradeLevel, this);
  }

  public boolean addGrade(Grade aGrade)
  {
    boolean wasAdded = false;
    if (grades.contains(aGrade)) { return false; }
    SchoolYear existingSchoolYear = aGrade.getSchoolYear();
    boolean isNewSchoolYear = existingSchoolYear != null && !this.equals(existingSchoolYear);
    if (isNewSchoolYear)
    {
      aGrade.setSchoolYear(this);
    }
    else
    {
      grades.add(aGrade);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeGrade(Grade aGrade)
  {
    boolean wasRemoved = false;
    //Unable to remove aGrade, as it must always have a schoolYear
    if (!this.equals(aGrade.getSchoolYear()))
    {
      grades.remove(aGrade);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addGradeAt(Grade aGrade, int index)
  {  
    boolean wasAdded = false;
    if(addGrade(aGrade))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGrades()) { index = numberOfGrades() - 1; }
      grades.remove(aGrade);
      grades.add(index, aGrade);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveGradeAt(Grade aGrade, int index)
  {
    boolean wasAdded = false;
    if(grades.contains(aGrade))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfGrades()) { index = numberOfGrades() - 1; }
      grades.remove(aGrade);
      grades.add(index, aGrade);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addGradeAt(aGrade, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    for(int i=grades.size(); i > 0; i--)
    {
      Grade aGrade = grades.get(i - 1);
      aGrade.delete();
    }
    School existingSchool = school;
    school = null;
    if (existingSchool != null)
    {
      existingSchool.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "year" + ":" + getYear()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "startDate" + "=" + (getStartDate() != null ? !getStartDate().equals(this)  ? getStartDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "endDate" + "=" + (getEndDate() != null ? !getEndDate().equals(this)  ? getEndDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "school = "+(getSchool()!=null?Integer.toHexString(System.identityHashCode(getSchool())):"null");
  }
}