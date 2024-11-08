package ca.mcgill.ecse.coolsupplies.features;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.*;

import ca.mcgill.ecse.coolsupplies.controller.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class OrderStepDefinitions {
  private CoolSuppliesFeatureSet8Controller controller = new CoolSuppliesFeatureSet8Controller();
  private static CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
  public static String error = "";
  private TOOrder lastRetrievedOrder;
  private List<TOOrder> lastRetrievedOrders;

  /**
   * @author David Zhou
   * Sets up parent entities in the system from a provided data table.
   * Each row in the data table represents a parent with email, password, name, and phone number.
   * Creates new Parent objects and adds them to the CoolSupplies system.
   *
   * @param dataTable cucumber DataTable containing parent information with columns:
   *                 [email, password, name, phoneNumber]
   */
  @Given("the following parent entities exist in the system")
  public void the_following_parent_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {

    //convert datatable to list of lists
    List<List<String>> parentList = dataTable.asLists(String.class);

    for (int i = 1; i < parentList.size(); i++) {
      List<String> parent = parentList.get(i);
      //get parent details from datatable
      String email = parent.get(0);
      String password = parent.get(1);
      String name = parent.get(2);
      int phoneNumber = Integer.parseInt(parent.get(3));
      //add parents
      coolSupplies.addParent(new Parent(email, password, name, phoneNumber, coolSupplies));
    }
  }

  /**
   * @author David Zhou
   * Sets up grade entities in the system from a provided data table.
   * Each row in the data table represents a grade with a level.
   * Adds each grade to the CoolSupplies system.
   *
   * @param grades cucumber DataTable containing grade information with column: [level]
   */
  @Given("the following grade entities exist in the system")
  public void the_following_grade_entities_exist_in_the_system(

      io.cucumber.datatable.DataTable grades) {
    List<Map<String, String>> entities = grades.asMaps();

    for (var entity : entities) {
      String level = entity.get("level");
      coolSupplies.addGrade(level);
    }
  }

  /**
   * @author David Zhou
   * Sets up student entities in the system from a provided data table.
   * Each row represents a student with name and grade level.
   * Creates new Student objects and associates them with the corresponding Grade.
   *
   * @param dataTable cucumber DataTable containing student information with columns:
   *                 [name, gradeLevel]
   */
  @Given("the following student entities exist in the system")
  public void the_following_student_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

    for (Map<String, String> row : rows) {
      String studentName = row.get("name");
      String studentGradeLevel = row.get("gradeLevel");
      Grade grade = Grade.getWithLevel(studentGradeLevel);
      new Student(studentName, coolSupplies, grade);
    }
  }

  /**
   * @author David Zhou
   * Sets up student-parent relationships in the system from a provided data table.
   * Each row represents a student and their associated parent email.
   * Links existing Student objects with their corresponding Parent objects.
   *
   * @param dataTable cucumber DataTable containing student-parent relationship information
   *                 with columns: [name, parentEmail]
   */
  @Given("the following student entities exist for a parent in the system")
  public void the_following_student_entities_exist_for_a_parent_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> studentToParentEntities = dataTable.asMaps();

    for (Map<String, String> studentParent : studentToParentEntities) {

      Parent parentOfStudent = (Parent) User.getWithEmail(studentParent.get("parentEmail"));

      if (parentOfStudent == null) {
        fail("Parent with email " + studentParent.get("parentEmail") + " not found");
      }

      Student student = Student.getWithName(studentParent.get("name"));

      if (student != null) {
        student.setParent(parentOfStudent);
      } else {
        fail("Student with name " + studentParent.get("name") + " is null");
      }
    }
  }

  /**
   * @author David Zhou
   * Sets up item entities in the system from a provided data table.
   * Each row represents an item with name and price.
   * Creates new Item objects and adds them to the CoolSupplies system.
   *
   * @param dataTable cucumber DataTable containing item information with columns:
   *                 [name, price]
   */
  @Given("the following item entities exist in the system")
  public void the_following_item_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {

    List<List<String>> itemList = dataTable.asLists(String.class);
    for (List<String> row : itemList.subList(1, itemList.size())) {
      String name = row.get(0);
      int price = Integer.parseInt(row.get(1));

      Item item = new Item(name, price, coolSupplies);

      coolSupplies.addItem(item);
    }
  }

  /**
   * @author David Zhou
   * Sets up grade bundle entities in the system from a provided data table.
   * Each row represents a grade bundle with name, grade level, and discount.
   * Creates new GradeBundle objects and adds them to the CoolSupplies system.
   *
   * @param dataTable cucumber DataTable containing grade bundle information with columns:
   *                 [name, gradeLevel, discount]
   */
  @Given("the following grade bundle entities exist in the system")
  public void the_following_grade_bundle_entities_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> bundleList = dataTable.asMaps();


    for (Map<String, String> bundleEntity : bundleList) {
      String name = bundleEntity.get("name");
      String gradeLevel = bundleEntity.get("gradeLevel");
      Grade grade = Grade.getWithLevel(gradeLevel);
      int discount = Integer.parseInt(bundleEntity.get("discount"));

      GradeBundle bundle = new GradeBundle(name, discount, coolSupplies, grade);

      coolSupplies.addBundle(bundle);
    }
  }

  /**
   * @author David Zhou
   * Sets up bundle item entities in the system from a provided data table.
   * Each row represents a bundle item with its associated grade bundle, purchase level,
   * quantity, and item name. Creates new BundleItem objects and links them with
   * their corresponding GradeBundle and Item objects.
   *
   * @param dataTable cucumber DataTable containing bundle item information with columns:
   *                 [gradeBundleName, level, quantity, itemName]
   */
  @Given("the following bundle item entities exist in the system")
  public void the_following_bundle_item_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> bundleItems = dataTable.asMaps();

    for (Map<String, String> bundleItem : bundleItems) {

      String gradeBundleName = bundleItem.get("gradeBundleName");
      GradeBundle gradeBundle = null;
      for (GradeBundle bundle : coolSupplies.getBundles()) {
        if (bundle.getName().equals(gradeBundleName)) {
          gradeBundle = bundle;
          break;
        }
      }

      String level = bundleItem.get("level");
      BundleItem.PurchaseLevel pLevel = BundleItem.PurchaseLevel.valueOf(level);

      int quantity = Integer.parseInt(bundleItem.get("quantity"));

      String itemName = bundleItem.get("itemName");
      InventoryItem item = Item.getWithName(itemName);

      new BundleItem(quantity, pLevel, coolSupplies, gradeBundle, (Item) item);
    }
  }

  /**
   * @author David Zhou
   * Sets up order entities in the system from a provided data table.
   * Each row represents an order with its details including number, date, level,
   * parent email, student name, and optional authorization codes and status.
   * Creates new Order objects and sets their properties accordingly.
   *
   * @param dataTable cucumber DataTable containing order information with columns:
   *                 [number, date, level, parentEmail, studentName, authorizationCode,
   *                  penaltyAuthorizationCode, status]
   */
  @Given("the following order entities exist in the system")
  public void the_following_order_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> orders = dataTable.asMaps();

    for (Map<String, String> order : orders) {

      int number = Integer.parseInt(order.get("number"));

      String dateString = order.get("date");
      Date date = Date.valueOf(dateString);

      String level = order.get("level");
      BundleItem.PurchaseLevel pLevel = BundleItem.PurchaseLevel.valueOf(level);

      String parentEmail = order.get("parentEmail");
      Parent parent = (Parent) User.getWithEmail(parentEmail);

      String studentName = order.get("studentName");
      Student student = Student.getWithName(studentName);

      Order newOrder = new Order(number, date, pLevel, parent, student, coolSupplies);
      coolSupplies.addOrder(newOrder);

      // Check if the "authorizationCode" column is present, then set it
      if (order.containsKey("authorizationCode")) {
        String authorizationCode = order.get("authorizationCode");
        if (authorizationCode != null) {
          newOrder.setAuthorizationCode(authorizationCode);
        }
      }

      // Check if the "penaltyAuthorizationCode" column is present, then set it
      if (order.containsKey("penaltyAuthorizationCode")) {
        String penaltyAuthorizationCode = order.get("penaltyAuthorizationCode");
        if (penaltyAuthorizationCode != null) {
          newOrder.setPenaltyAuthorizationCode(penaltyAuthorizationCode);
        }
      }

      // Check if the "status" column is present, then handle order status
      if (order.containsKey("status")) {
        String status = order.get("status");
        switch (status) {
          case "Started":
            break;
          case "Paid":
            newOrder.pay(newOrder.getAuthorizationCode());
            break;
          case "Penalized":
            newOrder.startSchoolYear();
            break;
          case "Prepared":
            newOrder.pay(newOrder.getAuthorizationCode());
            newOrder.startSchoolYear();
            break;
          case "PickedUp":
            newOrder.pay(newOrder.getAuthorizationCode());
            newOrder.startSchoolYear();
            newOrder.pickUp();
            break;
          default:
            throw new IllegalArgumentException("Unknown order status: " + status);
        }
      }
    }
  }

  /**
   * @author David Zhou
   * Sets up order item entities in the system from a provided data table.
   * Each row in the data table represents an order item with quantity, order number, and item name.
   * Creates new OrderItem objects and links them with their corresponding Order and InventoryItem objects.
   *
   * @param dataTable cucumber DataTable containing order item information with columns:
   *                 [quantity, orderNumber, itemName]
   * @throws IllegalArgumentException if:
   *         - quantity or orderNumber are not valid numbers
   *         - referenced order number does not exist in the system
   *         - referenced item name does not exist in the system
   * @throws RuntimeException if any other error occurs while creating the order item
   */
  @Given("the following order item entities exist in the system")
  public void the_following_order_item_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> orderItems = dataTable.asMaps();
    for (Map<String, String> orderItem : orderItems) {
      try {
        int quantity = Integer.parseInt(orderItem.get("quantity"));

        int orderNumber = Integer.parseInt(orderItem.get("orderNumber"));
        Order order = Order.getWithNumber(orderNumber);
        if (order == null) {
          throw new IllegalArgumentException("Order with number " + orderNumber + " not found.");
        }

        String itemName = orderItem.get("itemName");
        InventoryItem item = InventoryItem.getWithName(itemName);
        if (item == null) {
          throw new IllegalArgumentException("Inventory item with name " + itemName + " not found.");
        }

        // Assuming coolSupplies is defined and initialized appropriately
        new OrderItem(quantity, coolSupplies, order, item);

      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid number format in order item: " + orderItem, e);
      } catch (Exception e) {
        // Handle other exceptions appropriately
        throw new RuntimeException("Error creating order item from: " + orderItem, e);
      }
    }
  }

  /**
   * @author David Zhou
   * @author David Wang
   * @author David Vo
   * @author Hamza Khalfi
   *
   * Updates the status of a given order based on the provided status string.
   * This method simulates different stages in an order's lifecycle including
   * payment processing, school year initiation, and pickup completion.
   *
   * @param orderNumString The order number as a string, to be parsed as an integer
   * @param statusString The desired status to set for the order. Valid values are:
   *                    "Started" - Initial state, no action required
   *                    "Paid" - Processes payment for the order
   *                    "Prepared" - Processes payment and starts school year
   *                    "Penalized" - Only starts school year
   *                    "PickedUp" - Processes payment, starts school year, and marks as picked up
   *
   * @throws NumberFormatException if orderNumString cannot be parsed to an integer
   * @throws IllegalArgumentException if statusString is not one of the valid status values
   */
  @Given("the order {string} is marked as {string}")
  public void the_order_is_marked_as(String orderNumString, String statusString) {

   Order order = Order.getWithNumber(Integer.parseInt(orderNumString));

    switch (statusString) {
      case "Started":
        break;
      case "Paid":
        order.pay("3972");
        break;
      case "Prepared":
        order.pay("3972");
        order.startSchoolYear();
        break;
      case "Penalized":
        order.startSchoolYear();
        break;
      case "PickedUp":
        order.pay("3972");
        order.startSchoolYear();
        order.pickUp();
    }
  }

  /**
   * Attempts to update an order with a specified order number to a specified purchase level and a specified student name.
   * 
   * @param orderNumber the order number for the order we wish to update
   * @param purchaseLevel the purchase level we wish to update the order for
   * @param studentName the student name we wish to update the order for
   * 
   * @return None
   * @author Shayan Yamanidouzi Sorkhabi
   */
  @When("the parent attempts to update an order with number {string} to purchase level {string} and student with name {string}")
  public void the_parent_attempts_to_update_an_order_with_number_to_purchase_level_and_student_with_name(
      String orderNumber, String purchaseLevel, String studentName) {
    // Write code here that turns the phrase above into concrete actions
    int aOrderNumber = Integer.parseInt(orderNumber);
    callController(CoolSuppliesFeatureSet8Controller.updateOrder(aOrderNumber, purchaseLevel, studentName));
  }

  /**
   * Attempts to add an item to an existing order with a specified quantity.
   * This method parses the item name, quantity, and order number from the input strings,
   * then calls the controller to add the item to the specified order.
   * If the operation fails, an error message is stored in the {@code error} field.
   *
   * @author Jack McDonald
   * @param string the name of the item to add to the order.
   * @param string2 the quantity of the item to add, parsed as an integer.
   * @param string3 the order number to which the item will be added, parsed as an integer.
   * @throws NumberFormatException if {@code string2} or {@code string3} cannot be parsed as integers.
   * @throws RuntimeException if the addition to the order fails.
   */
  @When("the parent attempts to add an item {string} with quantity {string} to the order {string}")
  public void the_parent_attempts_to_add_an_item_with_quantity_to_the_order(String string,
      String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    int quantity = Integer.parseInt(string2);
    int orderNumber = Integer.parseInt(string3);

    error = CoolSuppliesFeatureSet8Controller.addItemToOrder(string, quantity, orderNumber);
  }

  /**
   * Attempts to update the quantity of an existing specified item from a specified order to a specified quantity.
   * 
   * @param itemName the name of the item we wish to change the quantity (in string)
   * @param quantity the quantity we would like to update to (in string)
   * @param orderNum the order number that contains the item we wish to change the quantity (in string)
   * 
   * @return None
   * @author Jun Ho Oh
   */
  @When("the parent attempts to update an item {string} with quantity {string} in the order {string}")
  public void the_parent_attempts_to_update_an_item_with_quantity_in_the_order(String itemName,
  String quantity, String orderNum) {
    // Write code here that turns the phrase above into concrete actions
    //transform the strings into integers
    int aQuantity = Integer.parseInt(quantity);
    int orderNumber = Integer.parseInt(orderNum);
    callController(CoolSuppliesFeatureSet8Controller.updateQuantityOfAnExistingItemOfOrder(orderNumber, itemName, aQuantity));
  }

  /**
   * attempts to delete a specified item from a specified order.
   * 
   * @param itemName the name of the item we wish to delete (in string)
   * @param orderNum the order number that contains the item to delete (in string)
   * 
   * @return None
   * @author Shayan Yamanidouzi Sorkhabi, Jun Ho Oh
   */
  @When("the parent attempts to delete an item {string} from the order {string}")
  public void the_parent_attempts_to_delete_an_item_from_the_order(String itemName, String orderNum) {
    // Write code here that turns the phrase above into concrete actions
    error = CoolSuppliesFeatureSet8Controller.deleteOrderItem(itemName, orderNum);
  }

  /**
   * @author David Zhou
   * Attempts to retrieve an individual order from the system by its number.
   * Sets the lastRetrievedOrder if successful, or captures error message if failed.
   *
   * @param orderNumberStr String representation of the order number to retrieve
   * @throws NumberFormatException if {@code orderNumberStr} cannot be parsed as an integer.
   * @throws RuntimeException if the order retrieval fails, storing the error message in {@code error}.
   *
   */
  @When("the parent attempts to get from the system the order with number {string}")
  public void the_parent_attempts_to_get_from_the_system_the_order_with_number(String orderNumberStr) {
    // Write code here that turns the phrase above into concrete actions
    int orderNumber = Integer.parseInt(orderNumberStr);

    try {

      TOOrder orderDetails = controller.viewIndividualOrder(orderNumber);

      lastRetrievedOrder = orderDetails;

      error = null;

    } catch (RuntimeException e) {

      error = e.getMessage();

    }
  }

  /**
   * @author David Vo
   * Attempts to cancel an order in the system based on the provided order number.
   * This method parses the order number from a string, then calls the controller to
   * cancel the order. If the cancellation fails, the resulting error message is stored
   * in the {@code error} field.
   *
   *
   * @param string the order number as a string, which will be parsed to an integer.
   * @throws NumberFormatException if {@code string} cannot be parsed as an integer.
   * @throws RuntimeException if the order cancellation fails, with the error message stored in {@code error}.
   */
  @When("the parent attempts to cancel the order {string}")
  public void the_parent_attempts_to_cancel_the_order(String string) {
    // Write code here that turns the phrase above into concrete actions
      int orderNumber = Integer.parseInt(string);
      error = controller.cancelOrder(orderNumber);
  }

  /**
   * Attempts to pay for a specified order with a specified authorization code.
   * 
   * @param orderString the order number as a string, which will be parsed to an integer.
   * @param authCodeString the authorization code to attempt to pay the order
   * 
   * @return None
   * @author Hamza Khalfi
   */
  @When("the parent attempts to pay for the order {string} with authorization code {string}")
  public void the_parent_attempts_to_pay_for_the_order_with_authorization_code(String orderString,
                                                                               String authCodeString) {
    callController(CoolSuppliesFeatureSet8Controller.payForOrder(Integer.parseInt(orderString),authCodeString));

  }

  /**
   * Attempts to start a school year for an order, by specifying its order number
   * 
   * @param orderString the order number for which we want to start a school year (as a string)
   * @return None
   * 
   * @author Hamza Khalfi
   */
  @When("the admin attempts to start a school year for the order {string}")
  public void the_admin_attempts_to_start_a_school_year_for_the_order(String orderString) {
    int orderNr = Integer.parseInt(orderString);
    callController(CoolSuppliesFeatureSet8Controller.startSchoolYear(orderNr));
  }

  /**
   * @author David Zhou
   * Attempts to process penalty payment for an order with both penalty authorization
   * and regular authorization codes.
   *
   * @param orderNumberStr String representation of the order number
   * @param penaltyAuthCode Penalty authorization code for the order
   * @param authCode Regular authorization code for the order
   */
  @When("the parent attempts to pay penalty for the order {string} with penalty authorization code {string} and authorization code {string}")
  public void the_parent_attempts_to_pay_penalty_for_the_order_with_penalty_authorization_code_and_authorization_code(
      String orderNumberStr, String penaltyAuthCode, String authCode) {

    int orderNumber = Integer.parseInt(orderNumberStr);

    // Capture the result message from the controller
    String resultMessage = controller.payPenaltyForOrder(orderNumber, authCode, penaltyAuthCode);

    // Check if the result message indicates an error
    if (resultMessage.contains("Penalty payment successful. The order is now prepared.")) {
      error = null;  // No error, the payment was successful
    } else {
      error = resultMessage;  // Store the error message if there was an issue
    }
  }

  /**
   * @author David Vo
   * Attempts to cancel an order in the system based on the provided order number.
   * This method parses the order number from a string, then calls the controller to
   * cancel the order. If the cancellation fails, the resulting error message is stored
   * in the {@code error} field.
   *
   * @param string the order number as a string, which will be parsed to an integer.
   * @throws NumberFormatException if {@code string} cannot be parsed as an integer.
   * @throws RuntimeException if the order cancellation fails, with the error message stored in {@code error}.
   */
  @When("the student attempts to pickup the order {string}")
  public void the_student_attempts_to_pickup_the_order(String string) {
    // Write code here that turns the phrase above into concrete actions
    int orderNumber = Integer.parseInt(string);
    error = controller.pickUpOrder(orderNumber);
  }

  /** 
  * @author David Wang
  */
  @When("the school admin attempts to get from the system all orders")
  public void the_school_admin_attempts_to_get_from_the_system_all_orders() {
    // Write code here that turns the phrase above into concrete actions
    lastRetrievedOrders = CoolSuppliesFeatureSet8Controller.getOrders();
  }

  /**
   * @author David Zhou
   * Verifies that an order contains the specified penalty authorization code.
   *
   * @param orderNumberStr String representation of the order number
   * @param penaltyAuthCode Expected penalty authorization code
   */
  @Then("the order {string} shall contain penalty authorization code {string}")
  public void the_order_shall_contain_penalty_authorization_code(String orderNumberStr, String penaltyAuthCode) {
    // Write code here that turns the phrase above into concrete actions
    int orderNumber = Integer.parseInt(orderNumberStr);
    Order order = Order.getWithNumber(orderNumber);
    assertNotNull(order,"Order should exist");
    assertEquals(penaltyAuthCode, order.getPenaltyAuthorizationCode(), "Penalty authorization code does not match");
  }

  /**
   * @author David Zhou
   * Verifies that an order does not contain the specified penalty authorization code.
   *
   * @param orderNumberStr String representation of the order number
   * @param penaltyAuthCode Penalty authorization code that should not be present
   */
  @Then("the order {string} shall not contain penalty authorization code {string}")
  public void the_order_shall_not_contain_penalty_authorization_code(String orderNumberStr,
      String penaltyAuthCode) {
    // Write code here that turns the phrase above into concrete actions
    int orderNumber = Integer.parseInt(orderNumberStr);
    Order order = Order.getWithNumber(orderNumber);
    assertNotNull(order, "Order should exist");
    assertNotEquals(penaltyAuthCode, order.getPenaltyAuthorizationCode());
  }

  /**
   * Verifies that the specified order does not contain a certain authorization code (with a specified string)
   * 
   * @param orderNumberStr the order number that we want to check the authoration code
   * @param authCode the authorization code that we do not want to contain in the specified order
   * 
   * @return None
   * @author Hamza Khalfi
   */
  @Then("the order {string} shall not contain authorization code {string}")
  public void the_order_shall_not_contain_authorization_code(String orderNumberStr, String authCode) {
    int orderNumber = Integer.parseInt(orderNumberStr);
    Order order = Order.getWithNumber(orderNumber);
    assertNotNull(order, "Order should exist");
    assertNotEquals(authCode, order.getAuthorizationCode());
  }

  /**
   * @author David Vo
   * Verifies that an order with the specified order number does not exist in the system.
   * This method parses the order number from a string, retrieves the order from the system,
   * and asserts that the order is null, indicating it does not exist (e.g., after cancellation).
   *
   * @param string the order number as a string, which will be parsed as an integer.
   * @since 1.0
   * @throws NumberFormatException if {@code string} cannot be parsed as an integer.
   * @throws AssertionError if the order exists in the system when it should not.
   */
  @Then("the order {string} shall not exist in the system")
  public void the_order_shall_not_exist_in_the_system(String string) {
    // Write code here that turns the phrase above into concrete actions
    int orderNumber = Integer.parseInt(string);
    Order order = Order.getWithNumber(orderNumber);
    assertNull(order, "The order should not exist in the system after cancellation.");
  }

  /** 
  * @author Hamza Khalfi
  * @author David Zhou
  */
  @Then("the order {string} shall contain authorization code {string}")
  public void the_order_shall_contain_authorization_code(String orderString, String authString) {
    // Write code here that turns the phrase above into concrete actions
    Order order = Order.getWithNumber(Integer.parseInt(orderString));
    assertNotNull(order, "Order should exist");
    assertEquals(order.getAuthorizationCode(),authString);
  }

  /**
   * Verifies that the specified order contains the specified number of item. 
   * 
   * @param orderNumberStr the order number which we would want to verify the quantity of item
   * @param qtyStr the quantity of item we wish to verify 
   * 
   * @return None
   * @author Hamza Khalfi, Shayan Yamanidouzi Sorkhabi, Jun Ho Oh
   */
  @Then("the order {string} shall contain {string} item")
  public void the_order_shall_contain_item(String orderNumberStr, String qtyStr) {
    Order order = Order.getWithNumber(Integer.parseInt(orderNumberStr));
    int qty = Integer.parseInt(qtyStr);
    assertNotNull(order, "Order should exist");
    assertEquals(qty, order.getOrderItems().size());
  }

  /** 
   * Verifies that the specified order does not contain the specified item.
   * 
   * @param orderNumberStr the order number that we wish to check existence of item
   * @param itemName the item name we wish to verify it's existence
   * 
   * @return None
   * @author David Zhou, Shayan Yamanidouzi Sorkhabi, Jun Ho Oh
  */
  @Then("the order {string} shall not contain {string}")
  public void the_order_shall_not_contain(String orderNumberStr, String itemName) {
    // Write code here that turns the phrase above into concrete actions
    Order order = Order.getWithNumber(Integer.parseInt(orderNumberStr));
    List<OrderItem> itemsInOrder = order.getOrderItems();
    InventoryItem item = InventoryItem.getWithName(itemName);

    for (OrderItem orderItem : itemsInOrder) {
      if (orderItem.getItem() == item) {
        fail();
      }
    }
  }

  /**
   * @author David Zhou
   * Verifies that the system contains the expected number of order items.
   *
   * @param expectedCountStr String representation of the expected number of order items
   */
  @Then("the number of order items in the system shall be {string}")
  public void the_number_of_order_items_in_the_system_shall_be(String expectedCountStr) {
    List<OrderItem> orderItems = coolSupplies.getOrderItems();
    assertEquals(Integer.parseInt(expectedCountStr), orderItems.size(),
        "Number of order items in the system is incorrect");
  }

  /**
   * 
   * @param orderNumberStr
   * @param size
   * 
   * @return None
   * @author Shayan Yamanidouzi Sorkhabi, Jun Ho Oh
   */
  @Then("the order {string} shall contain {string} items")
  public void the_order_shall_contain_items(String orderNumberStr, String size) {
    // Write code here that turns the phrase above into concrete actions
    int expectedItemQty = Integer.parseInt(size);
    int orderNumber = Integer.parseInt(orderNumberStr);
    int actualItemQty= Order.getWithNumber(orderNumber).getOrderItems().size();
    assertEquals(expectedItemQty, actualItemQty);
  }

  /**
   * Verifies if the specified order does not contain a specified item with a specified quantity
   * 
   * @param string the order number we wish to check the item with quantity
   * @param string2 the item name we wish to check existence in order
   * @param string3 the quantity we wish to check for the item in order
   * 
   * @return None
   * @author Shayan Yamanidouzi Sorkhabi, Jun Ho Oh
   */
  @Then("the order {string} shall not contain {string} with quantity {string}")
  public void the_order_shall_not_contain_with_quantity(String string, String string2,
      String string3) {
    // Write code here that turns the phrase above into concrete actions
    Order order = Order.getWithNumber(Integer.parseInt(string));

    if (order == null) {
      fail("Order " + string + " not found");
    }

    for (OrderItem orderItem : order.getOrderItems()) {
      if (orderItem.getItem().getName().equals(string2)) {
        assertNotEquals(Integer.parseInt(string3), orderItem.getQuantity());
        return;
      }
    }
  }

  /** 
  * @author Jack McDonald
  */
  @Then("the order {string} shall contain {string} with quantity {string}")
  public void the_order_shall_contain_with_quantity(String orderNumberStr, String itemName, String expectedQtyStr) {
    // Write code here that turns the phrase above into concrete actions
    Order order = Order.getWithNumber(Integer.parseInt(orderNumberStr));
    int expectedQty = Integer.parseInt(expectedQtyStr);
    InventoryItem actualItem = InventoryItem.getWithName(itemName);
    List<OrderItem> itemsInOrder = order.getOrderItems();

    for (OrderItem item : itemsInOrder) {
      if (item.getItem().equals(actualItem)) {
        assertEquals(expectedQty, item.getQuantity());
        return;
      }
    }
    fail();
  }

  /** 
  * @author David Vo
  * @author David Zhou
  */
  @Then("the order {string} shall be marked as {string}")
  public void the_order_shall_be_marked_as(String orderNumberStr, String expectedStatus) {
    // Write code here that turns the phrase above into concrete actions
    int orderNumber = Integer.parseInt(orderNumberStr);
    Order order = Order.getWithNumber(orderNumber);
    assertNotNull(order, "Order should exist.");
    assertEquals(expectedStatus, order.getStatusFullName(), "Order status does not match expected.");
  }

  /** 
  * @author David Vo
  * @author David Zhou
  */
  @Then("the number of orders in the system shall be {string}")
  public void the_number_of_orders_in_the_system_shall_be(String expectedCountStr) {
    // Write code here that turns the phrase above into concrete actions
    // Convert expected count to an integer
    int expectedCount = Integer.parseInt(expectedCountStr);

    // Retrieve the actual number of orders in the system
    int actualCount = CoolSuppliesApplication.getCoolSupplies().getOrders().size();

    // Assert that the actual count matches the expected count
    assertEquals(expectedCount, actualCount, "The number of orders in the system does not match the expected count.");
  }

  /**
   * Verifies that the specified order contains the specified level and specified student
   * 
   * @param orderNumberString the number of the order we wish to check level and student
   * @param expectedLevel the level to check if present in the specified order
   * @param expectedStudentName the student to check if present in specified order
   * 
   * @return None
   * @author Shayan Yamanidouzi Sorkhabi
   */
  @Then("the order {string} shall contain level {string} and student {string}")
  public void the_order_shall_contain_level_and_student(String orderNumberString, String expectedLevel,
      String expectedStudentName) {
    // Write code here that turns the phrase above into concrete actions
    int orderNumber = Integer.parseInt(orderNumberString);
    Order order = coolSupplies.getOrders().stream()
    .filter(o -> o.getNumber() == orderNumber)
    .findFirst()
    .orElseThrow(() -> new RuntimeException("Order " + orderNumber + " not found"));

    String actualLevel = order.getLevel().toString();
    assertEquals(expectedLevel, actualLevel); //verify the level

    String actualStudentName = order.getStudent().getName();
    assertEquals(expectedStudentName, actualStudentName); //verify the student
  }

  /** 
  * @author David Vo
  */
  @Then("the error {string} shall be raised")
  public void the_error_shall_be_raised(String string) {
    // Write code here that turns the phrase above into concrete actions
    assertEquals(string, error);
  }

  /** 
  * @author David Wang
  * @author David Zhou
  */
  @Then("the following order entities shall be presented")
  public void the_following_order_entities_shall_be_presented(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> expectedOrders = dataTable.asMaps(String.class, String.class);
    if (lastRetrievedOrders == null && lastRetrievedOrder == null) {
      fail("No order was retrieved.");
    }
    if (lastRetrievedOrders == null) {
      for (Map<String, String> expectedOrder : expectedOrders) {
        assertEquals(expectedOrder.get("parentEmail"), lastRetrievedOrder.getParentEmail(), "Parent email does not match");
        assertEquals(expectedOrder.get("studentName"), lastRetrievedOrder.getStudentName(), "Student name does not match");
        assertEquals(expectedOrder.get("status"), lastRetrievedOrder.getStatus(), "Order status does not match");
        assertEquals(Integer.parseInt(expectedOrder.get("number")), lastRetrievedOrder.getNumber(), "Order number does not match");
        assertEquals(expectedOrder.get("date"), lastRetrievedOrder.getDate().toString(), "Order date does not match");
        assertEquals(expectedOrder.get("level"), lastRetrievedOrder.getLevel(), "Order level does not match");
        assertEquals(expectedOrder.get("authorizationCode"), lastRetrievedOrder.getAuthorizationCode(), "Authorization code does not match");
        assertEquals(expectedOrder.get("penaltyAuthorizationCode"), lastRetrievedOrder.getPenaltyAuthorizationCode(), "Penalty authorization code does not match");
        assertEquals(Double.parseDouble(expectedOrder.get("totalPrice")), lastRetrievedOrder.getTotalPrice(), 0.01, "Total price does not match");
      }
    } else {
      assertEquals(expectedOrders.size(), lastRetrievedOrders.size());
      for (var row : expectedOrders) {
        String number = row.get("number");
        String date = row.get("date");
        String level = row.get("level");
        String parentEmail = row.get("parentEmail");
        String studentName = row.get("studentName");
        String status = row.get("status");
        String authorizationCode = row.get("authorizationCode");
        String penaltyAuthorizationCode = row.get("penaltyAuthorizationCode");

        boolean found = false;
        for (TOOrder order : lastRetrievedOrders) {
          if (order.getNumber() == Integer.parseInt(number)) {
            assertEquals(date, order.getDate().toString());
            assertEquals(level, order.getLevel());
            assertEquals(parentEmail, order.getParentEmail());
            assertEquals(studentName, order.getStudentName());
            assertEquals(status, order.getStatus());
            assertEquals(authorizationCode, order.getAuthorizationCode());
            assertEquals(penaltyAuthorizationCode, order.getPenaltyAuthorizationCode());
            found = true;
            break;
          }
        }
        assertTrue(found);
      }
    }
  }

  /** 
  * @author David Zhou
  * @author David Wang
  */
  @Then("the following order items shall be presented for the order with number {string}")
  public void the_following_order_items_shall_be_presented_for_the_order_with_number(String orderNumberStr,
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    int orderNumber = Integer.parseInt(orderNumberStr);
    List<Map<String, String>> expectedOrderItems = dataTable.asMaps(String.class, String.class);

    if (lastRetrievedOrder == null && lastRetrievedOrders == null) {
      fail("No order was retrieved at all.");
    }

    if (lastRetrievedOrders == null) { // In the case that there is only one item being retrieved.
      assertEquals(orderNumber, lastRetrievedOrder.getNumber(), "Order number does not match the retrieved order");
      List<TOOrderItem> actualOrderItems = lastRetrievedOrder.getItems();
      assertEquals(expectedOrderItems.size(), actualOrderItems.size(), "Number of order items does not match");

      for (int i = 0; i < expectedOrderItems.size(); i++) {
        Map<String, String> expectedItem = expectedOrderItems.get(i);
        TOOrderItem actualItem = actualOrderItems.get(i);

        assertEquals(Integer.parseInt(expectedItem.get("quantity")), actualItem.getQuantity(), "Quantity does not match for item " + actualItem.getItemName());
        assertEquals(expectedItem.get("itemName"), actualItem.getItemName(), "Item name does not match");
        assertEquals(expectedItem.get("gradeBundleName"), actualItem.getGradeBundleName(), "Grade bundle name does not match for item " + actualItem.getItemName());
        assertEquals(Integer.parseInt(expectedItem.get("price")), actualItem.getPrice(), "Price does not match for item " + actualItem.getItemName());

        // Discount can be null, so we handle both cases
        if (expectedItem.get("discount") != null && !expectedItem.get("discount").isEmpty()) {
          assertEquals(expectedItem.get("discount"), actualItem.getDiscount());
        } else {
          assertNull(actualItem.getDiscount(), "Expected discount to be null for item " + actualItem.getItemName());
        }
      }
    }

    else { // In the case there are multiple items that have been added into the system and that must be retrieved.
      for (TOOrder order : CoolSuppliesFeatureSet8Controller.getOrders()) {
        if (order.getNumber() == Integer.parseInt(orderNumberStr)) {
          List<TOOrderItem> orderItems = order.getItems();
          assertEquals(expectedOrderItems.size(), orderItems.size());

          for (var row : expectedOrderItems) {
            String itemName = row.get("itemName");
            String bundleName = row.get("gradeBundleName");
            String quantity = row.get("quantity");
            String price = row.get("price");
            String discount = row.get("discount");

            Boolean found = false;
            for (TOOrderItem orderItem : orderItems) {
              if (orderItem.getItemName().equals(itemName)) {
                if ((orderItem.getGradeBundleName() == null && bundleName != null) ||
                        (orderItem.getGradeBundleName() != null && !orderItem.getGradeBundleName().equals(bundleName))) {
                  continue;
                }
                assertEquals(quantity, String.valueOf(orderItem.getQuantity()));
                assertEquals(price, String.valueOf(orderItem.getPrice()));
                assertEquals(Objects.requireNonNullElse(discount, ""), orderItem.getDiscount());
                found = true;
                break;
              }
            }
            assertEquals(true, found);
          }
          return;
        }
      }
    }
  }

  /** 
  * @author David Zhou
  */
  @Then("no order entities shall be presented")
  public void no_order_entities_shall_be_presented() {
    // Write code here that turns the phrase above into concrete actions
    assertTrue(lastRetrievedOrder == null, "Expected no order entities to be presented, but found an order.");
  }

  /**
   * helper method for error handling
   * @param result
   * @author David Zhou
   */
  private void callController(String result) {
    error = "";
    if (!result.isEmpty()) {
      error += result;
    }
  }
}
