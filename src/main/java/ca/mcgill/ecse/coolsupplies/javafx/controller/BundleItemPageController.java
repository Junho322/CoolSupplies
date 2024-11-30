package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet5Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOBundleItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
/**
 * Controller for the Bundle Item Page.
 * 
 * This controller manages the user interface for viewing, adding, updating, and deleting items
 * in a specific bundle within the Cool Supplies system. It interacts with the back-end
 * controller to perform these operations and updates the UI accordingly.
 * @author Shayan Yamanidouzi Sorkhabi
 */

public class BundleItemPageController implements Initializable {

    private String fetchedBundleName;

    @FXML
    private Label BundleNameLable;

    @FXML
    private TextField DeleteItemName;

    @FXML
    private TextField ItemName;

    @FXML
    private ComboBox<String> LevelName;

    @FXML
    private TextField QuantityNumber;

    @FXML
    private ListView<String> listView;

    /**
     * Initializes the controller after the root element has been completely processed.
     * 
     * @param location  the location used to resolve relative paths for the root object
     * @param resources the resources used to localize the root object
     * @author Shayan Yamanidouzi Sorkhabi
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate ComboBox with options
        LevelName.getItems().addAll("Recommended", "Optional", "Mandatory");
        // Perform setup actions, such as clearing the list or setting defaults
        listView.getItems().clear();
        //fetchedBundleName = ""; // Set a default value for the bundle name if needed
        BundleNameLable.setText(fetchedBundleName);
    }

    /**
     * Adds a new item to the selected bundle.
     * 
     * @param event
     * @author Shayan Yamanidouzi Sorkhabi
     */
    @FXML
    void AddBundleItem(ActionEvent event) {
        //String bundleName = BundleNameLabel.getText();
        String bundleName = fetchedBundleName;
        String itemName = ItemName.getText();
        String level = LevelName.getValue();
        int quantity;

        try {
            quantity = Integer.parseInt(QuantityNumber.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Quantity must be a number.");
            return;
        }

        String result = CoolSuppliesFeatureSet5Controller.addBundleItem(quantity, level, itemName, bundleName);
        showAlert("Add Item", result);
        refreshBundleItems();
    }

    /**
     * 
     * Deletes an item from the selected bundle
     * 
     * @param event
     * @author Shayan Yamanidouzi Sorkhabi
     */

    @FXML
    void DeleteBundleItem(ActionEvent event) {
        String bundleName = fetchedBundleName;
        String itemName = DeleteItemName.getText();

        String result = CoolSuppliesFeatureSet5Controller.deleteBundleItem(itemName, bundleName);
        showAlert("Delete Item", result);
        refreshBundleItems();
    }
    
    /**
     * Updates an existing item in the selected bundle
     * 
     * @param event
     * @author Shayan Yamanidouzi Sorkhabi
     */
    @FXML
    void updateBundleItem(ActionEvent event) {
        String bundleName = fetchedBundleName;
        String itemName = ItemName.getText();
        String level = LevelName.getValue();
        int quantity;

        try {
            quantity = Integer.parseInt(QuantityNumber.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Quantity must be a number.");
            return;
        }

        String result = CoolSuppliesFeatureSet5Controller.updateBundleItem(itemName, bundleName, quantity, level);
        showAlert("Update Item", result);
        refreshBundleItems();
    }

    /**
     * refreshes the list of itmes desplayed in listview
     * @author Shayan Yamanidouzi Sorkhabi
     */
    private void refreshBundleItems() {
        String bundleName = fetchedBundleName;
        List<TOBundleItem> bundleItems = CoolSuppliesFeatureSet5Controller.getBundleItems(bundleName);

        listView.getItems().clear();
        for (TOBundleItem item : bundleItems) {
            listView.getItems().add(item.getItemName() + " - " + item.getQuantity() + " - " + item.getLevel());
        }
    }

    /**
     * Displayes an alert messasge to ther user
     * 
     * @param title the title of the alert
     * @param message the message content of the alert
     * @author Shayan Yamanidouzi Sorkhabi
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Sets the name oif the hbudnle that is selected in bundle page to be edited for the bundleNameLabel
     * 
     * @param name the name of the bundle
     * @author Shayan Yamanidouzi Sorkhabi
     */
    public void getBundleName(String name) {
        fetchedBundleName = name;
        if (BundleNameLable != null) {
            BundleNameLable.setText("> "+fetchedBundleName+" Bundle");
        }
        refreshBundleItems();
    }


    /**
     * Switches the user to the bundle page view
     * 
     * @param event
     * @author Shayan Yamanidouzi Sorkhabi
     */
    @FXML
    void doSwitchToBundlePage(ActionEvent event) {
        try {
        // Load the FXML file for the Bundle Page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/BundlePage.fxml"));
        Parent bundlePageRoot = loader.load();

        // Get the current stage from any control
        Stage stage = (Stage) listView.getScene().getWindow();

        // Set the new scene
        Scene bundlePageScene = new Scene(bundlePageRoot);
        stage.setScene(bundlePageScene);
        
        // Show the updated stage
        stage.show();
    } catch (IOException e) {
        showAlert("Error", "Failed to load the Bundle Page.");
        e.printStackTrace();
    }

    }


}
