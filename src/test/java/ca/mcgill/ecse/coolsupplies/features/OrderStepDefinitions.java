package ca.mcgill.ecse.coolsupplies.features;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.*;

import ca.mcgill.ecse.coolsupplies.model.BundleItem.PurchaseLevel;
import ca.mcgill.ecse.coolsupplies.model.Order.Status;
import ca.mcgill.ecse.coolsupplies.controller.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet8Controller;
import ca.mcgill.ecse.coolsupplies.model.Order;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OrderStepDefinitions {
  private CoolSuppliesFeatureSet8Controller controller = new CoolSuppliesFeatureSet8Controller();
  private static CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
  public static String error = "";
  private TOOrder lastRetrievedOrder;

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


  @Given("the following grade entities exist in the system")
  public void the_following_grade_entities_exist_in_the_system(

      io.cucumber.datatable.DataTable grades) {
    List<Map<String, String>> entities = grades.asMaps();

    for (var entity : entities) {
      String level = entity.get("level");
      coolSupplies.addGrade(level);
    }
  }

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

  @Given("the order {string} is marked as {string}")
  public void the_order_is_marked_as(String orderNumString, String statusString) {

    Order order = Order.getWithNumber(Integer.parseInt(orderNumString));


    if (order != null) {
      order.delete();
    }

    //create an order with state STARTED, and the specified order number

    order = new Order(Integer.parseInt(orderNumString), Date.valueOf("2020-10-10"), BundleItem.PurchaseLevel.Mandatory,
        coolSupplies.getStudent(0).getParent(), coolSupplies.getStudent(0), coolSupplies);

    switch (statusString) {
      case "Paid":
        order.pay("1234");
        break;
      case "Prepared":
        order.pay("1234");
        order.startSchoolYear();
        break;
      case "Penalized":
        order.startSchoolYear();
        break;
      case "PickedUp":
        order.pay("1234");
        order.startSchoolYear();
        order.pickUp();
    }
  }

  @When("the parent attempts to update an order with number {string} to purchase level {string} and student with name {string}")
  public void the_parent_attempts_to_update_an_order_with_number_to_purchase_level_and_student_with_name(
      String string, String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the parent attempts to add an item {string} with quantity {string} to the order {string}")
  public void the_parent_attempts_to_add_an_item_with_quantity_to_the_order(String string,
      String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the parent attempts to update an item {string} with quantity {string} in the order {string}")
  public void the_parent_attempts_to_update_an_item_with_quantity_in_the_order(String string,
      String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the parent attempts to delete an item {string} from the order {string}")
  public void the_parent_attempts_to_delete_an_item_from_the_order(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the parent attempts to get from the system the order with number {string}")
  public void the_parent_attempts_to_get_from_the_system_the_order_with_number(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }



  @When("the parent attempts to cancel the order {string}")
  public void the_parent_attempts_to_cancel_the_order(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the parent attempts to pay for the order {string} with authorization code {string}")
  public void the_parent_attempts_to_pay_for_the_order_with_authorization_code(String string,
      String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the admin attempts to start a school year for the order {string}")
  public void the_admin_attempts_to_start_a_school_year_for_the_order(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the parent attempts to pay penalty for the order {string} with penalty authorization code {string} and authorization code {string}")
  public void the_parent_attempts_to_pay_penalty_for_the_order_with_penalty_authorization_code_and_authorization_code(
      String string, String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the student attempts to pickup the order {string}")
  public void the_student_attempts_to_pickup_the_order(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the school admin attempts to get from the system all orders")
  public void the_school_admin_attempts_to_get_from_the_system_all_orders() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the order {string} shall contain penalty authorization code {string}")
  public void the_order_shall_contain_penalty_authorization_code(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the order {string} shall not contain penalty authorization code {string}")
  public void the_order_shall_not_contain_penalty_authorization_code(String string,
      String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }


  @Then("the order {string} shall not contain authorization code {string}")
  public void the_order_shall_not_contain_authorization_code(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    // throw new io.cucumbser.java.PendingException();
  }

  @Then("the order {string} shall not exist in the system")
  public void the_order_shall_not_exist_in_the_system(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the order {string} shall contain authorization code {string}")
  public void the_order_shall_contain_authorization_code(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the order {string} shall contain {string} item")
  public void the_order_shall_contain_item(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the order {string} shall not contain {string}")
  public void the_order_shall_not_contain(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }



  @Then("the number of order items in the system shall be {string}")
  public void the_number_of_order_items_in_the_system_shall_be(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the order {string} shall contain {string} items")
  public void the_order_shall_contain_items(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the order {string} shall not contain {string} with quantity {string}")
  public void the_order_shall_not_contain_with_quantity(String string, String string2,
      String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }


  @Then("the order {string} shall contain {string} with quantity {string}")
  public void the_order_shall_contain_with_quantity(String string, String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }


  @Then("the order {string} shall be marked as {string}")
  public void the_order_shall_be_marked_as(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }


  @Then("the number of orders in the system shall be {string}")
  public void the_number_of_orders_in_the_system_shall_be(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the order {string} shall contain level {string} and student {string}")
  public void the_order_shall_contain_level_and_student(String string, String string2,
      String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Given("order {string} is marked as {string}")
  public void order_is_marked_as(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the error {string} shall be raised")
  public void the_error_shall_be_raised(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

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
    throw new io.cucumber.java.PendingException();
  }

  @Then("the following order items shall be presented for the order with number {string}")
  public void the_following_order_items_shall_be_presented_for_the_order_with_number(String string,
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    throw new io.cucumber.java.PendingException();
  }

  @Then("no order entities shall be presented")
  public void no_order_entities_shall_be_presented() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

}
