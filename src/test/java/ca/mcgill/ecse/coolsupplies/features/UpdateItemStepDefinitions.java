package ca.mcgill.ecse.coolsupplies.features;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UpdateItemStepDefinitions {
  @When("the school admin attempts to update item {string} in the system with name {string} and price {string} \\(p14)")
  public void the_school_admin_attempts_to_update_item_in_the_system_with_name_and_price_p14(
      String string, String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }



  @Then("the item {string} with price {string} shall not exist in the system \\(p14)")
  public void the_item_with_price_shall_not_exist_in_the_system_p14(String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the following item entities shall exist in the system \\(p14)")
  public void the_following_item_entities_shall_exist_in_the_system_p14(
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
}
