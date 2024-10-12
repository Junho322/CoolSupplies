package ca.mcgill.ecse.coolsupplies.features;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import java.util.Map;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DeleteGradeStepDefinitions {
    private String lastError;
    private static CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

    @Given("the following grade entities exists in the system \\(p2)")
    public void the_following_grade_entities_exists_in_the_system_p2(
        //TODO: USE MODEL LAYER
        io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (var elem : rows) {
            String level = elem.get("level");
            // Assuming a method addGrade in the controller adds grades to the system.
            coolSupplies.addGrade(level);
        }
    }

    @When("the school admin attempts to delete from the system the grade with level {string} \\(p2)")
    public void the_school_admin_attempts_to_delete_from_the_system_the_grade_with_level_p2(
        String string) {
        lastError = CoolSuppliesFeatureSet7Controller.deleteGrade(string);
    }

    @Then(value = "the number of grade entities in the system shall be {string} \\(p2)")
    public void the_number_of_grade_entities_in_the_system_shall_be_p2(String string) {

        int numGrades = Integer.parseInt(string);
        System.out.println(CoolSuppliesFeatureSet7Controller.getGrades().size());
        assertEquals(CoolSuppliesFeatureSet7Controller.getGrades().size(), numGrades);
    }

    @Then("the following grade entities shall exist in the system \\(p2)")
    public void the_following_grade_entities_shall_exist_in_the_system_p2(
        io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> expectedGrades = dataTable.asMaps();
        List<TOGrade> actualGrades = CoolSuppliesFeatureSet7Controller.getGrades();
        for (int i = 0; i < expectedGrades.size(); i++) {
            String expectedLevel = expectedGrades.get(i).get("level");
            TOGrade actualGrade = actualGrades.get(i);
            assertEquals(expectedLevel, actualGrade.getLevel());
        }
    }

    @Then("the error {string} shall be raised \\(p2)")
    public void the_error_shall_be_raised_p2(String string) {
        // Write code here that turns the phrase above into concrete actions
        assertEquals(string, lastError);
    }
}