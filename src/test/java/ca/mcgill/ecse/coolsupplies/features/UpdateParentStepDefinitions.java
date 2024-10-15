package ca.mcgill.ecse.coolsupplies.features;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UpdateParentStepDefinitions {
  @When("the school Administrator attempts to update parent {string} in the system with password {string}, name {string}, and phone number {string} \\(p1)")
  public void the_school_administrator_attempts_to_update_parent_in_the_system_with_password_name_and_phone_number_p1(
      String string, String string2, String string3, String string4) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the school admin attempts to update parent {string} in the system with password {string}, name {string}, and phone number {string} \\(p1)")
  public void the_school_admin_attempts_to_update_parent_in_the_system_with_password_name_and_phone_number_p1(
      String string, String string2, String string3, String string4) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the parent {string} with password {string}, name {string}, and phone number {string} shall not exist in the system \\(p1)")
  public void the_parent_with_password_name_and_phone_number_shall_not_exist_in_the_system_p1(
      String string, String string2, String string3, String string4) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the following parent entities shall exist in the system \\(p1)")
  public void the_following_parent_entities_shall_exist_in_the_system_p1(
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
