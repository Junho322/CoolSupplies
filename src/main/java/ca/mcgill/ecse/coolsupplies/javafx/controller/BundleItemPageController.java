package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet4Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet5Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet5Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOBundleItem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

public class BundleItemPageController {

    @FXML
    private Label BundleNameLabel;

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
    @FXML
    void AddBundleItem(ActionEvent event) {
      handleAddBundle();
        
        //String bundleName = BundleNameLabel.getText();
        String bundleName = "Pencil";
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
        String bundleName = BundleNameLabel.getText();
        String itemName = DeleteItemName.getText();

        String result = CoolSuppliesFeatureSet5Controller.deleteBundleItem(itemName, bundleName);
        showAlert("Delete Item", result);
        refreshBundleItems();
    }

    @FXML
    void updateBundleItem(ActionEvent event) {
        String bundleName = BundleNameLabel.getText();
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
        String bundleName = BundleNameLabel.getText();
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

    private void handleAddBundle(){
      CoolSuppliesFeatureSet4Controller.addBundle("pencil", 2, "4");
    }
}
