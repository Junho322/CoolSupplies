package ca.mcgill.ecse.coolsupplies.features;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UpdateGradeBundleStepDefinitions {
  @When("the school admin attempts to update grade bundle {string} in the system with name {string}, discount {string}, and grade level {string} \\(p10)")
  public void the_school_admin_attempts_to_update_grade_bundle_in_the_system_with_name_discount_and_grade_level_p10(
      String string, String string2, String string3, String string4) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the grade bundle {string} with discount {string} and grade level {string} shall not exist in the system \\(p10)")
  public void the_grade_bundle_with_discount_and_grade_level_shall_not_exist_in_the_system_p10(
      String string, String string2, String string3) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the following grade bundle entities shall exist in the system \\(p10)")
  public void the_following_grade_bundle_entities_shall_exist_in_the_system_p10(
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
