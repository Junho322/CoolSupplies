package ca.mcgill.ecse.coolsupplies.features;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.model.*;
import io.cucumber.datatable.DataTable;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class OrderStepDefinitions {
  private static CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();
  @Given("the following parent entities exist in the system")
  public void the_following_parent_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> parentData = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> parentEntry : parentData) {
      String email = parentEntry.get("email");
      String password = parentEntry.get("password");
      String name = parentEntry.get("name");
      int phoneNumber = Integer.parseInt(parentEntry.get("phoneNumber"));
      coolSupplies.addParent(email, password, name, phoneNumber);
    }
  }

  @Given("the following grade entities exist in the system")
  public void the_following_grade_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> gradeData = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> gradeEntry : gradeData) {
      String level = gradeEntry.get("level");
      coolSupplies.addGrade(level);
    }
  }

  @Given("the following student entities exist in the system")
  public void the_following_student_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> studentData = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> studentEntry : studentData) {
      String name = studentEntry.get("name");
      String gradeLevel = studentEntry.get("gradeLevel");
      Grade grade = coolSupplies.getGrades().stream()
          .filter(g -> g.getLevel().equals(gradeLevel))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Grade " + gradeLevel + " not found"));
      coolSupplies.addStudent(name, grade);
    }
  }

  @Given("the following student entities exist for a parent in the system")
  public void the_following_student_entities_exist_for_a_parent_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> studentParentData = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> entry : studentParentData) {
      String name = entry.get("name");
      String parentEmail = entry.get("parentEmail");
      Parent parent = coolSupplies.getParents().stream()
          .filter(p -> p.getEmail().equals(parentEmail))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Parent with email " + parentEmail + " not found"));
      Student student = coolSupplies.getStudents().stream()
          .filter(s -> s.getName().equals(name))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Student " + name + " not found"));
      parent.addStudent(student);
    }
  }

  @Given("the following item entities exist in the system")
  public void the_following_item_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> itemData = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> itemEntry : itemData) {
      String name = itemEntry.get("name");
      int price = Integer.parseInt(itemEntry.get("price"));
      coolSupplies.addItem(name, price);
    }
  }

  @Given("the following grade bundle entities exist in the system")
  public void the_following_grade_bundle_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> bundleData = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> bundleEntry : bundleData) {
      String name = bundleEntry.get("name");
      int discount = Integer.parseInt(bundleEntry.get("discount"));
      String gradeLevel = bundleEntry.get("gradeLevel");
      Grade grade = coolSupplies.getGrades().stream()
          .filter(g -> g.getLevel().equals(gradeLevel))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Grade " + gradeLevel + " not found"));
      coolSupplies.addBundle(name, discount, grade);
    }
  }

  @Given("the following bundle item entities exist in the system")
  public void the_following_bundle_item_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> bundleItemData = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> bundleItemEntry : bundleItemData) {
      int quantity = Integer.parseInt(bundleItemEntry.get("quantity"));
      String level = bundleItemEntry.get("level");
      String gradeBundleName = bundleItemEntry.get("gradeBundleName");
      String itemName = bundleItemEntry.get("itemName");

      BundleItem.PurchaseLevel purchaseLevel = BundleItem.PurchaseLevel.valueOf(level);
      GradeBundle bundle = coolSupplies.getBundles().stream()
          .filter(b -> b.getName().equals(gradeBundleName))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Bundle " + gradeBundleName + " not found"));
      Item item = coolSupplies.getItems().stream()
          .filter(i -> i.getName().equals(itemName))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Item " + itemName + " not found"));
      coolSupplies.addBundleItem(quantity, purchaseLevel, bundle, item);
    }
  }

  @Given("the following order entities exist in the system")
  public void the_following_order_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> orderData = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> orderEntry : orderData) {
      int number = Integer.parseInt(orderEntry.get("number"));
      Date date = Date.valueOf(orderEntry.get("date"));
      String level = orderEntry.get("level");
      String parentEmail = orderEntry.get("parentEmail");
      String studentName = orderEntry.get("studentName");

      BundleItem.PurchaseLevel purchaseLevel = BundleItem.PurchaseLevel.valueOf(level);
      Parent parent = coolSupplies.getParents().stream()
          .filter(p -> p.getEmail().equals(parentEmail))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Parent with email " + parentEmail + " not found"));
      Student student = coolSupplies.getStudents().stream()
          .filter(s -> s.getName().equals(studentName))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Student " + studentName + " not found"));
      coolSupplies.addOrder(number, date, purchaseLevel, parent, student);
    }
  }

  @Given("the following order item entities exist in the system")
  public void the_following_order_item_entities_exist_in_the_system(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
    // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> orderItemData = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> orderItemEntry : orderItemData) {
      int quantity = Integer.parseInt(orderItemEntry.get("quantity"));
      int orderNumber = Integer.parseInt(orderItemEntry.get("orderNumber"));
      String itemName = orderItemEntry.get("itemName");

      Order order = coolSupplies.getOrders().stream()
          .filter(o -> o.getNumber() == orderNumber)
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Order " + orderNumber + " not found"));
      Item item = coolSupplies.getItems().stream()
          .filter(i -> i.getName().equals(itemName))
          .findFirst()
          .orElseThrow(() -> new RuntimeException("Item " + itemName + " not found"));
      coolSupplies.addOrderItem(quantity, order, item);
    }
  }

  @Given("the order {string} is marked as {string}")
  public void the_order_is_marked_as(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
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
