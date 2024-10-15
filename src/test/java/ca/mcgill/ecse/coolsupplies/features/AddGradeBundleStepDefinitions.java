package ca.mcgill.ecse.coolsupplies.features;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddGradeBundleStepDefinitions {
  @Given("the following grade entities exists in the system \\(p10)")
  public void the_following_grade_entities_exists_in_the_system_p10(
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

  @Given("the following grade bundle entities exists in the system \\(p10)")
  public void the_following_grade_bundle_entities_exists_in_the_system_p10(
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

  @When("the school admin attempts to add a new grade bundle in the system with name {string}, discount {string}, and grade level {string} \\(p10)")
  public void the_school_admin_attempts_to_add_a_new_grade_bundle_in_the_system_with_name_discount_and_grade_level_p10(
      String string, String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the grade bundle {string} with discount {string} and grade level {string} shall exist in the system \\(p10)")
  public void the_grade_bundle_with_discount_and_grade_level_shall_exist_in_the_system_p10(
      String string, String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the number of grade bundle entities in the system shall be {string} \\(p10)")
  public void the_number_of_grade_bundle_entities_in_the_system_shall_be_p10(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the error {string} shall be raised \\(p10)")
  public void the_error_shall_be_raised_p10(String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }


}
