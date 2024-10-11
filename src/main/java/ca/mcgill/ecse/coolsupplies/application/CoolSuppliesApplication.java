package ca.mcgill.ecse.coolsupplies.application;

import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;

public class CoolSuppliesApplication {

  private static CoolSupplies coolSupplies;

  public static void main(String[] args) {
    // TODO Start the application user interface here
  }

  public static CoolSupplies getCoolSupplies() {
    if (coolSupplies == null) {
      // these attributes are default, you should set them later with the setters
      coolSupplies = new CoolSupplies();
    }
    return coolSupplies;
  }

}
