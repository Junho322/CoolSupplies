package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet4Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet5Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
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
import javafx.scene.Scene;

public class BundleItemPageController implements Initializable {

    private String fetchedBundleName;

    @FXML
    private Label BundleNameLable;

    @FXML
    private TextField DeleteItemName;

    @FXML
    private TextField ItemName;

    @FXML
    private TextField LevelName;

    @FXML
    private TextField QuantityNumber;

    @FXML
    private ListView<String> listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Perform setup actions, such as clearing the list or setting defaults
        listView.getItems().clear();
        fetchedBundleName = ""; // Set a default value for the bundle name if needed
    }

    @FXML
    void AddBundleItem(ActionEvent event) {
        //String bundleName = BundleNameLabel.getText();
        String bundleName = fetchedBundleName;
        String itemName = ItemName.getText();
        String level = LevelName.getText();
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

    @FXML
    void DeleteBundleItem(ActionEvent event) {
        String bundleName = fetchedBundleName;
        String itemName = DeleteItemName.getText();

        String result = CoolSuppliesFeatureSet5Controller.deleteBundleItem(itemName, bundleName);
        showAlert("Delete Item", result);
        refreshBundleItems();
    }

    @FXML
    void updateBundleItem(ActionEvent event) {
        String bundleName = fetchedBundleName;
        String itemName = ItemName.getText();
        String level = LevelName.getText();
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

    private void refreshBundleItems() {
        String bundleName = fetchedBundleName;
        List<TOBundleItem> bundleItems = CoolSuppliesFeatureSet5Controller.getBundleItems(bundleName);

        listView.getItems().clear();
        for (TOBundleItem item : bundleItems) {
            listView.getItems().add(item.getItemName() + " - " + item.getQuantity() + " - " + item.getLevel());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void getBundleName(String name) {
        fetchedBundleName = name;
        refreshBundleItems();
    }

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
