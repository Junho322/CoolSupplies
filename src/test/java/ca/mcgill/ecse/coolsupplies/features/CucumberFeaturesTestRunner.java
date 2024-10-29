package ca.mcgill.ecse.coolsupplies.features;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty",
        features = {
                "src/test/resources/UpdatePasswordOfSchoolAdmin.feature",
                "src/test/resources/AddParent.feature",
                "src/test/resources/UpdateParent.feature",
                "src/test/resources/DeleteParent.feature",
                "src/test/resources/GetParent.feature"
        },
        glue = "ca.mcgill.ecse.coolsupplies.features")
public class CucumberFeaturesTestRunner {

}