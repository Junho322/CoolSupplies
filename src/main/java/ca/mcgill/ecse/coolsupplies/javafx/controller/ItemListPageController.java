package ca.mcgill.ecse.coolsupplies.javafx.controller;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet3Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import ca.mcgill.ecse.coolsupplies.controller.TOItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ItemListPageController implements Initializable {

    @FXML
    private TextField Price;

    @FXML
    private TextField itemAdd;

    @FXML
    private TextField itemDelete;

    @FXML
    private TextField itemUpdate;

    @FXML
    private ListView<String> listview;

    @FXML
    private TextField newName;

    @FXML
    private TextField newPrice;

    private ObservableList<String> items = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

        List<TOItem> exisitingItems = CoolSuppliesFeatureSet3Controller.getItems();
        for (int i = 0; i < exisitingItems.size(); i++){
            items.add(exisitingItems.get(i).getName());
            populateListView();
        }
    }
    
    @FXML
    void addItem(ActionEvent event) {
        // Get user input
        String itemName = itemAdd.getText().trim();
        String itemPrice = Price.getText().trim();
        
        // Check if input is empty
        if (itemName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item name cannot be empty.");
            return;
        }
        if (itemPrice.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item price cannot be empty.");
            return;
        }

        
        try{
          for (int i = 0; i < listview.getItems().size(); i++) {
            String item = listview.getItems().get(i);
            if (itemName.equals(item)){
              showAlert(Alert.AlertType.ERROR, "Input Error", "The adding item must not already exist");
              
              return;
            }
          }
            Integer price = Integer.parseInt(Price.getText());

            // Call pre-existing addGrade method in CoolSuppliesFeatureSet3Controller
            CoolSuppliesFeatureSet3Controller.addItem(itemName, price);

            // Add the item to the ListView
            //String itemEntry = itemName;// + " - $" + String.format("%.2f", price);
            //listview.getItems().setItem(itemEntry);
        
            
            items.add(itemName);
            
            // Show success message (optional)
            showAlert(AlertType.INFORMATION, "Success", "Item added successfully!");

        } catch (NumberFormatException e) {
            // Handle invalid price input
            showAlert(AlertType.ERROR, "Input Error", "Price must be a valid number.");
        } finally {
            // Clear the text fields
            itemAdd.clear();
            Price.clear();
            populateListView();
        }
    }
    // Helper method to show alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void deleteItem(ActionEvent event) {
        String itemNameDeletion = itemDelete.getText().trim();

        //check if its empty
        if (itemNameDeletion.isEmpty()){
            showAlert(AlertType.ERROR, "Input Error", "Item name cannot be empty.");
            return;
        }
        // Search for the item in the ListView
        // boolean itemView = false;
        // for (String item : listview.getItems()) {
        //     if (item.equalsIgnoreCase(itemNameDeletion)) { // Check if item name matches
        //         listview.getItems().remove(item); // Remove the item
        //         CoolSuppliesFeatureSet3Controller.deleteItem(itemNameDeletion);
        //         itemView = true;
        //         break;
        //     }
        // }

        // if (itemView) {
        //     showAlert(AlertType.INFORMATION, "Success", "Item '" + itemNameDeletion + "' has been deleted.");
        // } else {
        //     showAlert(AlertType.ERROR, "Not Found", "Item '" + itemNameDeletion + "' does not exist in the list.");
        // }
        CoolSuppliesFeatureSet3Controller.deleteItem(itemNameDeletion);
        listview.getItems().remove(itemNameDeletion);
        // Clear the input field
        itemDelete.clear();
        populateListView();

    }

    @FXML
    void updateItem(ActionEvent event) {
        // Get input values
        String currentItemName = itemUpdate.getText().trim();
        String updatedName = newName.getText().trim();
        String updatedPrice = newPrice.getText().trim();

        // Validate inputs
        if (currentItemName.isEmpty() || updatedName.isEmpty() || updatedPrice.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled to update an item.");
            return;
        }
        // Validate negative cost inputs
        if (Integer.parseInt(updatedPrice) <= 0) {
          showAlert(Alert.AlertType.ERROR, "Input Error", "Costs must not be negative values nor zeros");
          return;
        }
        
        try {
            // Parse the price
            Integer price = Integer.parseInt(updatedPrice);
            
            // Search for the item in the ListView
            boolean itemFound = false;
            for (int i = 0; i < listview.getItems().size(); i++) {
                String item = listview.getItems().get(i);
                if (updatedName.equals(item) ){
                  showAlert(Alert.AlertType.ERROR, "Input Error", "The new name must not already exist");

                  return;
                }
                // Check if the item's name matches the one to be updated
                if (item.equalsIgnoreCase(currentItemName)) {
                    // Update the item
                    String updatedItem = updatedName; //+ " - $" + String.format("%.2f", price);
                    listview.getItems().set(i, updatedItem);
                    CoolSuppliesFeatureSet3Controller.updateItem(currentItemName, updatedName, price);
                    itemFound = true;
                    break;
                }
            } 
            
            
            // Show appropriate message based on result
            if (itemFound) {
                showAlert(AlertType.INFORMATION, "Success", "Item updated successfully!");
            } else {
                showAlert(AlertType.ERROR, "Not Found", "Item '" + currentItemName + "' does not exist in the list.");
            }

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Input Error", "Price must be a valid number.");
        } finally {
            // Clear the input fields
            itemUpdate.clear();
            newName.clear();
            newPrice.clear();
        }
    }

    private void populateListView() {listview.setItems(items);}

    

}