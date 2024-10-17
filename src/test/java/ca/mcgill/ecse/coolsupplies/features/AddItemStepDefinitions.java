package ca.mcgill.ecse.coolsupplies.features;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddItemStepDefinitions {
  @Given("the following item entities exists in the system \\(p14)")
  public void the_following_item_entities_exists_in_the_system_p14(
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    throw new io.cucumber.java.PendingException();
  }

  @When("the school admin attempts to add a new item in the system with name {string} and price {string} \\(p14)")
  public void the_school_admin_attempts_to_add_a_new_item_in_the_system_with_name_and_price_p14(
      String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the item {string} with price {string} shall exist in the system \\(p14)")
  public void the_item_with_price_shall_exist_in_the_system_p14(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the number of item entities in the system shall be {string} \\(p14)")
  public void the_number_of_item_entities_in_the_system_shall_be_p14(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the error {string} shall be raised \\(p14)")
  public void the_error_shall_be_raised_p14(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

}
