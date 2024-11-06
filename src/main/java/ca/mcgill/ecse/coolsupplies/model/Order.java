/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.35.0.7523.c616a4dce modeling language!*/

package ca.mcgill.ecse.coolsupplies.model;
import ca.mcgill.ecse.coolsupplies.model.BundleItem.PurchaseLevel;
import java.util.*;
import java.sql.Date;

// line 3 "../../../../../../CoolSuppliesStates.ump"
// line 132 "../../../../../../CoolSuppliesStates.ump"
// line 39 "../../../../../../CoolSuppliesPersistence.ump"
// line 83 "../../../../../../CoolSuppliesPersistence.ump"
// line 43 "../../../../../../model.ump"
// line 116 "../../../../../../model.ump"
public class Order
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static Map<Integer, Order> ordersByNumber = new HashMap<Integer, Order>();

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Order Attributes
  private int number;
  private Date date;
  private PurchaseLevel level;
  private String authorizationCode;
  private String penaltyAuthorizationCode;

  //Order State Machines
  public enum Status { Started, Final, Paid, Penalized, Prepared, PickedUp }
  private Status status;

  //Order Associations
  private Parent parent;
  private Student student;
  private CoolSupplies coolSupplies;
  private List<OrderItem> orderItems;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Order(int aNumber, Date aDate, PurchaseLevel aLevel, Parent aParent, Student aStudent, CoolSupplies aCoolSupplies)
  {
    date = aDate;
    level = aLevel;
    authorizationCode = null;
    penaltyAuthorizationCode = null;
    if (!setNumber(aNumber))
    {
      throw new RuntimeException("Cannot create due to duplicate number. See https://manual.umple.org?RE003ViolationofUniqueness.html");
    }
    boolean didAddParent = setParent(aParent);
    if (!didAddParent)
    {
      throw new RuntimeException("Unable to create order due to parent. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddStudent = setStudent(aStudent);
    if (!didAddStudent)
    {
      throw new RuntimeException("Unable to create order due to student. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddCoolSupplies = setCoolSupplies(aCoolSupplies);
    if (!didAddCoolSupplies)
    {
      throw new RuntimeException("Unable to create order due to coolSupplies. See https://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    orderItems = new ArrayList<OrderItem>();
    setStatus(Status.Started);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setNumber(int aNumber)
  {
    boolean wasSet = false;
    Integer anOldNumber = getNumber();
    if (anOldNumber != null && anOldNumber.equals(aNumber)) {
      return true;
    }
    if (hasWithNumber(aNumber)) {
      return wasSet;
    }
    number = aNumber;
    wasSet = true;
    if (anOldNumber != null) {
      ordersByNumber.remove(anOldNumber);
    }
    ordersByNumber.put(aNumber, this);
    return wasSet;
  }

  public boolean setDate(Date aDate)
  {
    boolean wasSet = false;
    date = aDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setLevel(PurchaseLevel aLevel)
  {
    boolean wasSet = false;
    level = aLevel;
    wasSet = true;
    return wasSet;
  }

  public boolean setAuthorizationCode(String aAuthorizationCode)
  {
    boolean wasSet = false;
    authorizationCode = aAuthorizationCode;
    wasSet = true;
    return wasSet;
  }

  public boolean setPenaltyAuthorizationCode(String aPenaltyAuthorizationCode)
  {
    boolean wasSet = false;
    penaltyAuthorizationCode = aPenaltyAuthorizationCode;
    wasSet = true;
    return wasSet;
  }

  public int getNumber()
  {
    return number;
  }
  /* Code from template attribute_GetUnique */
  public static Order getWithNumber(int aNumber)
  {
    return ordersByNumber.get(aNumber);
  }
  /* Code from template attribute_HasUnique */
  public static boolean hasWithNumber(int aNumber)
  {
    return getWithNumber(aNumber) != null;
  }

  public Date getDate()
  {
    return date;
  }

  public PurchaseLevel getLevel()
  {
    return level;
  }

  public String getAuthorizationCode()
  {
    return authorizationCode;
  }

  public String getPenaltyAuthorizationCode()
  {
    return penaltyAuthorizationCode;
  }

  public String getStatusFullName()
  {
    String answer = status.toString();
    return answer;
  }

  public Status getStatus()
  {
    return status;
  }

  public boolean updateOrder(PurchaseLevel level,Student student)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Started:
        if (isChildOfParent(student))
        {
        // line 8 "../../../../../../CoolSuppliesStates.ump"
          setLevel(level);
        setStudent(student);
          setStatus(Status.Started);
          wasEventProcessed = true;
          break;
        }
        if (!(isChildOfParent(student)))
        {
        // line 13 "../../../../../../CoolSuppliesStates.ump"
          rejectUpdateOrder(level, student);
          setStatus(Status.Started);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean addItem(InventoryItem item,int quantity)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Started:
        if (!(inventoryItemIsInOrder(item))&&quantity>0)
        {
        // line 17 "../../../../../../CoolSuppliesStates.ump"
          addOrderItem(quantity, coolSupplies, item);
          setStatus(Status.Started);
          wasEventProcessed = true;
          break;
        }
        if (quantity<=0||inventoryItemIsInOrder(item))
        {
        // line 21 "../../../../../../CoolSuppliesStates.ump"
          rejectAddItem(item, quantity);
          setStatus(Status.Started);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean updateItem(OrderItem item,int quantity)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Started:
        if (quantity>0)
        {
        // line 25 "../../../../../../CoolSuppliesStates.ump"
          item.setQuantity(quantity);
          setStatus(Status.Started);
          wasEventProcessed = true;
          break;
        }
        if (quantity<=0)
        {
        // line 29 "../../../../../../CoolSuppliesStates.ump"
          rejectUpdateItem(item, quantity);
          setStatus(Status.Started);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean deleteItem(OrderItem item)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Started:
        if (inventoryItemIsInOrder(item.getItem()))
        {
        // line 33 "../../../../../../CoolSuppliesStates.ump"
          item.delete();
          setStatus(Status.Started);
          wasEventProcessed = true;
          break;
        }
        if (!(inventoryItemIsInOrder(item.getItem())))
        {
        // line 37 "../../../../../../CoolSuppliesStates.ump"
          rejectDeleteItem(item);
          setStatus(Status.Started);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean pay(String authorizationCode)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Started:
        if (codeIsValid(authorizationCode))
        {
        // line 41 "../../../../../../CoolSuppliesStates.ump"
          setAuthorizationCode(authorizationCode);
          setStatus(Status.Paid);
          wasEventProcessed = true;
          break;
        }
        if (!(codeIsValid(authorizationCode)))
        {
        // line 45 "../../../../../../CoolSuppliesStates.ump"
          rejectPay(authorizationCode);
          setStatus(Status.Started);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean startSchoolYear()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Started:
        setStatus(Status.Penalized);
        wasEventProcessed = true;
        break;
      case Paid:
        setStatus(Status.Prepared);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean cancelOrder()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Started:
        setStatus(Status.Final);
        wasEventProcessed = true;
        break;
      case Paid:
        setStatus(Status.Final);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean payPenalty(String authorizationCode,String penaltyAuthCode)
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Penalized:
        if (codeIsValid(authorizationCode)&&codeIsValid(penaltyAuthCode))
        {
        // line 57 "../../../../../../CoolSuppliesStates.ump"
          setAuthorizationCode(authorizationCode);
        setPenaltyAuthorizationCode(penaltyAuthCode);
          setStatus(Status.Prepared);
          wasEventProcessed = true;
          break;
        }
        if (!(codeIsValid(authorizationCode))||!(codeIsValid(penaltyAuthCode)))
        {
        // line 62 "../../../../../../CoolSuppliesStates.ump"
          rejectPayPenalty(authorizationCode, penaltyAuthCode);
          setStatus(Status.Penalized);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean pickUp()
  {
    boolean wasEventProcessed = false;
    
    Status aStatus = status;
    switch (aStatus)
    {
      case Prepared:
        setStatus(Status.PickedUp);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setStatus(Status aStatus)
  {
    status = aStatus;

    // entry actions and do activities
    switch(status)
    {
      case Final:
        delete();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Parent getParent()
  {
    return parent;
  }
  /* Code from template association_GetOne */
  public Student getStudent()
  {
    return student;
  }
  /* Code from template association_GetOne */
  public CoolSupplies getCoolSupplies()
  {
    return coolSupplies;
  }
  /* Code from template association_GetMany */
  public OrderItem getOrderItem(int index)
  {
    OrderItem aOrderItem = orderItems.get(index);
    return aOrderItem;
  }

  public List<OrderItem> getOrderItems()
  {
    List<OrderItem> newOrderItems = Collections.unmodifiableList(orderItems);
    return newOrderItems;
  }

  public int numberOfOrderItems()
  {
    int number = orderItems.size();
    return number;
  }

  public boolean hasOrderItems()
  {
    boolean has = orderItems.size() > 0;
    return has;
  }

  public int indexOfOrderItem(OrderItem aOrderItem)
  {
    int index = orderItems.indexOf(aOrderItem);
    return index;
  }
  /* Code from template association_SetOneToMany */
  public boolean setParent(Parent aParent)
  {
    boolean wasSet = false;
    if (aParent == null)
    {
      return wasSet;
    }

    Parent existingParent = parent;
    parent = aParent;
    if (existingParent != null && !existingParent.equals(aParent))
    {
      existingParent.removeOrder(this);
    }
    parent.addOrder(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setStudent(Student aStudent)
  {
    boolean wasSet = false;
    if (aStudent == null)
    {
      return wasSet;
    }

    Student existingStudent = student;
    student = aStudent;
    if (existingStudent != null && !existingStudent.equals(aStudent))
    {
      existingStudent.removeOrder(this);
    }
    student.addOrder(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setCoolSupplies(CoolSupplies aCoolSupplies)
  {
    boolean wasSet = false;
    if (aCoolSupplies == null)
    {
      return wasSet;
    }

    CoolSupplies existingCoolSupplies = coolSupplies;
    coolSupplies = aCoolSupplies;
    if (existingCoolSupplies != null && !existingCoolSupplies.equals(aCoolSupplies))
    {
      existingCoolSupplies.removeOrder(this);
    }
    coolSupplies.addOrder(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOrderItems()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public OrderItem addOrderItem(int aQuantity, CoolSupplies aCoolSupplies, InventoryItem aItem)
  {
    return new OrderItem(aQuantity, aCoolSupplies, this, aItem);
  }

  public boolean addOrderItem(OrderItem aOrderItem)
  {
    boolean wasAdded = false;
    if (orderItems.contains(aOrderItem)) { return false; }
    Order existingOrder = aOrderItem.getOrder();
    boolean isNewOrder = existingOrder != null && !this.equals(existingOrder);
    if (isNewOrder)
    {
      aOrderItem.setOrder(this);
    }
    else
    {
      orderItems.add(aOrderItem);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOrderItem(OrderItem aOrderItem)
  {
    boolean wasRemoved = false;
    //Unable to remove aOrderItem, as it must always have a order
    if (!this.equals(aOrderItem.getOrder()))
    {
      orderItems.remove(aOrderItem);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOrderItemAt(OrderItem aOrderItem, int index)
  {  
    boolean wasAdded = false;
    if(addOrderItem(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOrderItemAt(OrderItem aOrderItem, int index)
  {
    boolean wasAdded = false;
    if(orderItems.contains(aOrderItem))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOrderItems()) { index = numberOfOrderItems() - 1; }
      orderItems.remove(aOrderItem);
      orderItems.add(index, aOrderItem);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOrderItemAt(aOrderItem, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    ordersByNumber.remove(getNumber());
    Parent placeholderParent = parent;
    this.parent = null;
    if(placeholderParent != null)
    {
      placeholderParent.removeOrder(this);
    }
    Student placeholderStudent = student;
    this.student = null;
    if(placeholderStudent != null)
    {
      placeholderStudent.removeOrder(this);
    }
    CoolSupplies placeholderCoolSupplies = coolSupplies;
    this.coolSupplies = null;
    if(placeholderCoolSupplies != null)
    {
      placeholderCoolSupplies.removeOrder(this);
    }
    for(int i=orderItems.size(); i > 0; i--)
    {
      OrderItem aOrderItem = orderItems.get(i - 1);
      aOrderItem.delete();
    }
  }

  // line 75 "../../../../../../CoolSuppliesStates.ump"
   private Boolean isChildOfParent(Student student){
    return student.getParent() == getParent();
  }

  // line 79 "../../../../../../CoolSuppliesStates.ump"
   private Boolean inventoryItemIsInOrder(InventoryItem item){
    for (OrderItem orderItem : getOrderItems()) {
      if (orderItem.getItem() == item) {
        return true;
      }
    }
    return false;
  }

  // line 88 "../../../../../../CoolSuppliesStates.ump"
   private Boolean codeIsValid(String code){
    return code != null && !code.contains(" ") && !code.isEmpty();
  }

  // line 92 "../../../../../../CoolSuppliesStates.ump"
   private void rejectUpdateOrder(PurchaseLevel level, Student student){
    throw new RuntimeException("Student " + student.getName() + " is not a child of the parent " + getParent().getEmail() + ".");
  }

  // line 96 "../../../../../../CoolSuppliesStates.ump"
   private void rejectAddItem(InventoryItem item, int quantity){
    if (quantity <=0) {
      throw new RuntimeException("Quantity must be greater than 0.");
    }
    if (inventoryItemIsInOrder(item)) {
      throw new RuntimeException("Item " + item.getName() + " already exists in the order " + getNumber() + ".");
    }
    throw new RuntimeException("Could not add Item.");
  }

  // line 106 "../../../../../../CoolSuppliesStates.ump"
   private void rejectUpdateItem(OrderItem item, int quantity){
    throw new RuntimeException("Quantity must be greater than 0.");
  }

  // line 110 "../../../../../../CoolSuppliesStates.ump"
   private void rejectDeleteItem(OrderItem item){
    throw new RuntimeException("Item " + item.getItem().getName() + " does not exist in the order " + getNumber() + ".");
  }

  // line 114 "../../../../../../CoolSuppliesStates.ump"
   private void rejectPay(String authorizationCode){
    throw new RuntimeException("Authorization code is invalid");
  }

  // line 118 "../../../../../../CoolSuppliesStates.ump"
   private void rejectPayPenalty(String authorizationCode, String penaltyAuthCode){
    if (!codeIsValid(authorizationCode)) {
      throw new RuntimeException("Authorization code is invalid");
    }
    if (!codeIsValid(penaltyAuthCode)) {
      throw new RuntimeException("Penalty authorization code is invalid");
    }
    throw new RuntimeException("Could not Pay Penalty.");
  }

  // line 41 "../../../../../../CoolSuppliesPersistence.ump"
   public static  void reinitializeUniqueNumber(List<Order> orders){
    ordersByNumber.clear();
    for (var order : orders) {
      ordersByNumber.put(order.getNumber(), order);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "number" + ":" + getNumber()+ "," +
            "authorizationCode" + ":" + getAuthorizationCode()+ "," +
            "penaltyAuthorizationCode" + ":" + getPenaltyAuthorizationCode()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "date" + "=" + (getDate() != null ? !getDate().equals(this)  ? getDate().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "level" + "=" + (getLevel() != null ? !getLevel().equals(this)  ? getLevel().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "parent = "+(getParent()!=null?Integer.toHexString(System.identityHashCode(getParent())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "student = "+(getStudent()!=null?Integer.toHexString(System.identityHashCode(getStudent())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "coolSupplies = "+(getCoolSupplies()!=null?Integer.toHexString(System.identityHashCode(getCoolSupplies())):"null");
  }
}
