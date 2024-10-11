package ca.mcgill.ecse.coolsupplies.features;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GetGradeBundleStepDefinitions {
  @When("the school admin attempts to get from the system all the bundles \\(p9)")
  public void the_school_admin_attempts_to_get_from_the_system_all_the_bundles_p9() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @When("the school admin attempts to get from the system the grade bundle with name {string} \\(p9)")
  public void the_school_admin_attempts_to_get_from_the_system_the_grade_bundle_with_name_p9(
      String string) {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }

  @Then("the following grade bundle entities shall be presented \\(p9)")
  public void the_following_grade_bundle_entities_shall_be_presented_p9(
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

  @Then("no grade bundle entities shall be presented \\(p9)")
  public void no_grade_bundle_entities_shall_be_presented_p9() {
    // Write code here that turns the phrase above into concrete actions
    throw new io.cucumber.java.PendingException();
  }
}
