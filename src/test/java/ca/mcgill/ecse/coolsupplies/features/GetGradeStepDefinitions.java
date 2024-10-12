package ca.mcgill.ecse.coolsupplies.features;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class GetGradeStepDefinitions {
    private List<TOGrade> resultGrades;
    private TOGrade resultGrade;
    @When("the school admin attempts to get from the system all the grades \\(p2)")
    public void the_school_admin_attempts_to_get_from_the_system_all_the_grades_p2() {
        // Write code here that turns the phrase above into concrete actions
        resultGrades = CoolSuppliesFeatureSet7Controller.getGrades();
        resultGrade = null;
    }

    @When("the school admin attempts to get from the system the grade with level {string} \\(p2)")
    public void the_school_admin_attempts_to_get_from_the_system_the_grade_with_level_p2(
        String string) {
        // Write code here that turns the phrase above into concrete actions
        try {
            resultGrade = CoolSuppliesFeatureSet7Controller.getGrade(string);
            resultGrades = new ArrayList<>();
            resultGrades.add(resultGrade);
        } catch (IllegalArgumentException e) {
            resultGrade = null;
            resultGrades = new ArrayList<>();
        }
    }

    @Then("the following grade entities shall be presented \\(p2)")
    public void the_following_grade_entities_shall_be_presented_p2(
        io.cucumber.datatable.DataTable dataTable) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // E, List[E], List[List[E]], List[Map[K,V]], Map[K,V] or
        // Map[K, List[V]]. E,K,V must be a String, Integer, Float,
        // Double, Byte, Short, Long, BigInteger or BigDecimal.
        //
        // For other transformations you can register a DataTableType.
        List<Map<String, String>> expectedGrades = dataTable.asMaps();

        assertEquals(expectedGrades.size(), resultGrades.size(), "Number of grades doesn't match");

        for (int i = 0; i < expectedGrades.size(); i++) {
            String expectedLevel = expectedGrades.get(i).get("level");
            String actualLevel = findGrade(resultGrades, expectedLevel).getLevel();
            assertEquals(expectedLevel, actualLevel, "Could not find Grade: " + expectedLevel);
        }
    }

    @Then("no grade entities shall be presented \\(p2)")
    public void no_grade_entities_shall_be_presented_p2() {
        // Write code here that turns the phrase above into concrete actions
        assertTrue(resultGrades.isEmpty() && resultGrade == null, "Expected no grades, but found some");
    }

    public TOGrade findGrade(List<TOGrade> grades, String target) {
        for (TOGrade grade : grades) {
            if (target.equals(grade.getLevel())) {
                return grade;
            }
        }
        return null;
    }
}