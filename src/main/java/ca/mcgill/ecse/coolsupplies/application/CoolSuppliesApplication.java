package ca.mcgill.ecse.coolsupplies.application;

import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.persistence.CoolSuppliesPersistence;

public class CoolSuppliesApplication {

  private static CoolSupplies coolSupplies;

  public static void main(String[] args) {
    // TODO Start the application user interface here
  }

  public static CoolSupplies getCoolSupplies() {
    if (coolSupplies == null) {
      // these attributes are default, you should set them later with the setters
      coolSupplies = CoolSuppliesPersistence.load();
    }
    return coolSupplies;
  }

}
