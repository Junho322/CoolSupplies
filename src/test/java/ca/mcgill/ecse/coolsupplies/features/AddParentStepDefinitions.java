package ca.mcgill.ecse.coolsupplies.features;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddParentStepDefinitions {
  @Given("the following parent entities exists in the system \\(p1)")
  public void the_following_parent_entities_exists_in_the_system_p1(
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

  @When("the parent attempts to add a new parent in the system with email {string}, password {string}, name {string}, and phone number {string} \\(p1)")
  public void the_parent_attempts_to_add_a_new_parent_in_the_system_with_email_password_name_and_phone_number_p1(
      String string, String string2, String string3, String string4) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the number of parent entities in the system shall be {string} \\(p1)")
  public void the_number_of_parent_entities_in_the_system_shall_be_p1(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the error {string} shall be raised \\(p1)")
  public void the_error_shall_be_raised_p1(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the parent {string} with password {string}, name {string}, and phone number {string} shall exist in the system \\(p1)")
  public void the_parent_with_password_name_and_phone_number_shall_exist_in_the_system_p1(
      String string, String string2, String string3, String string4) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }


}
