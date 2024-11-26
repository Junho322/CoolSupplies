package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet5Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet8Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class UpdateItemOrderPageController {

    @FXML
    private TextField DeleteItemName;

    @FXML
    private TextField DeleteOrderNumber;

    @FXML
    private TextField ItemName;

    @FXML
    private Label OrderNameLable;

    @FXML
    private TextField OrderNumber;

    @FXML
    private TextField QuantityNumber;

    @FXML
    private ListView<?> listView;

    @FXML
    void AddOrderItem(ActionEvent event) {
        String itemName = ItemName.
        CoolSuppliesFeatureSet8Controller.addItemToOrder(null, 0, 0);


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
    void DeleteOrderItem(ActionEvent event) {

    }

    @FXML
    void updateOrderItem(ActionEvent event) {

    }

}
