package ca.mcgill.ecse.coolsupplies.javafx.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet3Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet4Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet5Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import ca.mcgill.ecse.coolsupplies.controller.TOGradeBundle;
import ca.mcgill.ecse.coolsupplies.controller.TOBundleItem;
import ca.mcgill.ecse.coolsupplies.controller.TOItem;
import ca.mcgill.ecse.coolsupplies.javafx.pages.AdminPageController;
import ca.mcgill.ecse.coolsupplies.model.BundleItem;
import ca.mcgill.ecse.coolsupplies.model.GradeBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InventoryPageController implements Initializable {

    @FXML
    private AnchorPane pannel;

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

    @FXML
    private Button settingsButton;

    @FXML
    private Button logoutButton;

    private ObservableList<String> items = FXCollections.observableArrayList();


    /**
     * This method overwrites for the extended parent class, it initializes the Inventory Page by loading all existing items
     * and bundle items into the ListView and calls upon the button initialization defined below
     *
     * @param location location used to resolve relative paths for root object
     * @param resources resources used to localize the root object
     *
     * @author Jun Ho Oh
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        // System.out.println("InventoryPageController initialized");
        List<TOItem> exisitingItems = CoolSuppliesFeatureSet3Controller.getItems();
        //this is to add existing Items into the ListView
        listview.getItems().clear();
        for (int i = 0; i < exisitingItems.size(); i++){
            items.add(exisitingItems.get(i).getName());

        }

        //this is to add the bundle items into the ListView
        List<TOGradeBundle> existingBundles = CoolSuppliesFeatureSet4Controller.getBundles();
        for (int i = 0; i < existingBundles.size(); i++){
            List<TOBundleItem> existingBundleItems = CoolSuppliesFeatureSet5Controller.getBundleItems(existingBundles.get(i).getName());
            System.out.println(existingBundleItems);
            for (int j = 0; j < existingBundleItems.size(); j++){

                for (int l = 0; l < listview.getItems().size(); l++){
                    String item = listview.getItems().get(l);
                    if (!(existingBundleItems.get(j).getItemName().equals(item))){
                        items.add(existingBundleItems.get(j).getItemName());

                    }
                }
            }
        }
        populateListView(); //after adding all items into items, it populates ListView

        initializeButtonGraphics();
        listview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateUpdateFields(newValue);
                itemDelete.setText(newValue); // Set the delete field with the selected item
            }
        });
    }

    /**
     * Populates the update item fields with the details of the selected item.
     *
     * @param selectedItem the name of the selected item in the ListView.
     */
    private void populateUpdateFields(String selectedItem) {
        // Find the item details using the name
        TOItem selectedTOItem = CoolSuppliesFeatureSet3Controller.getItems()
                .stream()
                .filter(item -> item.getName().equals(selectedItem))
                .findFirst()
                .orElse(null);

        if (selectedTOItem != null) {
            itemUpdate.setText(selectedTOItem.getName());
            newName.setText(selectedTOItem.getName());
            newPrice.setText(String.valueOf(selectedTOItem.getPrice()));
        }
    }

    /**
     * Handles addition of new items into the inventory and populates the ListView for the user to see
     * Validates inputs from user and makes sure to not populate already existing items, updates inventory and ListView
     *
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     *
     * @author Jun Ho Oh
     */
    @FXML
    void addItem(ActionEvent event) {
        // Get user input
        String itemName = itemAdd.getText().trim();
        String itemPrice = Price.getText().trim();

        //Price must be greater than 0 (logical)
        if (Integer.parseInt(itemPrice) <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Item price cannot be negative or 0.");
            itemAdd.clear();
            Price.clear();
            return;
        }

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
            //makes sure that the item does not already exist in the inventory
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


    /**
     * Helper function to show the desired messages
     *
     * @param alertType the type of message displayed
     * @param title the title of the message displayed
     * @param message the content of the message displayed
     *
     * @author Jun Ho Oh
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Deletes desired Item from Inventory Item and updates the inventory as well as ListView
     * Validates user inputs
     *
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     *
     * @author Jun Ho Oh
     */
    @FXML
    void deleteItem(ActionEvent event) {
        String itemNameDeletion = itemDelete.getText().trim();

        //check if its empty
        if (itemNameDeletion.isEmpty()){
            showAlert(AlertType.ERROR, "Input Error", "Item name cannot be empty.");
            return;
        }

        //verifies that the inputed item name exists in inventory items
        for (int i = 0; i < listview.getItems().size(); i++) {
            String item = listview.getItems().get(i);
            if (itemNameDeletion.equals(item)){
                CoolSuppliesFeatureSet3Controller.deleteItem(itemNameDeletion);
                listview.getItems().remove(itemNameDeletion);
                // Clear the input field
                itemDelete.clear();
                populateListView();
            }
        }
        showAlert(AlertType.ERROR, "Input Error", "Item does not exist.");
        itemDelete.clear();
        return;



    }

    /**
     * Updates the desired item's quantity and name inputed. Validates user inputs
     * and assures proper udpating of inventory and ListView
     *
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     *
     * @author Jun Ho Oh
     */
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

    /**
     * Populates the ListView by adding the inventory item names
     *
     * takes no parameters
     *
     * @author Jun Ho Oh
     *
     */
    private void populateListView() {
        listview.setItems(items);
    }

    /**
     * Swithces to the Admin Page View
     *
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     *
     * @author Jun Ho Oh
     */
    @FXML
    void doSwitchToAdminPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/AdminPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) itemAdd.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
            AdminPageController controller = loader.getController();
            controller.initialize(null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Swithces to the Order Page View
     *
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     *
     * @author Jun Ho Oh
     */
    @FXML
    void doSwitchToOrderPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Order/OrderPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) itemAdd.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Swithces to the Grade Page View
     *
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     *
     * @author Jun Ho Oh
     */
    @FXML
    void doSwitchToGradePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Grade/GradePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) itemAdd.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Swithces to the Bundle Page View
     *
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     *
     * @author Jun Ho Oh
     */
    @FXML
    void doSwitchToBundlePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/BundlePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) itemAdd.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs the user out and brings the user to the Login Page View
     *
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     *
     * @author Jun Ho Oh
     */
    @FXML
    void doLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/LoginPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) itemAdd.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.setTitle("CoolSupplies");
            stage.setX(100);
            stage.setY(100);
            stage.setHeight(600);
            stage.setWidth(800);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Swithces to the Parent Page View
     *
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     *
     * @author Jun Ho Oh
     */
    @FXML
    void doSwitchToParentPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/ParentPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) itemAdd.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Swithces to the Show Student Page View
     *
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     *
     * @author Jun Ho Oh
     */
    @FXML
    void doSwitchToShowStudentsPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/ShowStudentsPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) itemAdd.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Initializes the buttons's graphics for Settings and Logout buttons
     *
     * @author Jun Ho Oh
     */
    public void initializeButtonGraphics() {
        // Settings Button Image
        ImageView settingsImage = new ImageView("ca/mcgill/ecse/coolsupplies/javafx/resources/settings.png");
        settingsImage.setPreserveRatio(true);
        settingsImage.fitWidthProperty().bind(settingsButton.widthProperty().multiply(0.8));
        settingsImage.fitHeightProperty().bind(settingsButton.heightProperty().multiply(0.8));

        // Logout Button Image
        ImageView logoutImage = new ImageView("ca/mcgill/ecse/coolsupplies/javafx/resources/logout.png");
        logoutImage.setPreserveRatio(true);
        logoutImage.fitWidthProperty().bind(logoutButton.widthProperty().multiply(0.8));
        logoutImage.fitHeightProperty().bind(logoutButton.heightProperty().multiply(0.8));

        // Settings Button
        settingsButton.setGraphic(settingsImage);
        settingsButton.setText("");
        settingsButton.setStyle("-fx-background-color: transparent;");
        settingsButton.setPadding(new Insets(0)); // Uniform padding
        settingsButton.setPrefSize(40, 40); // Default size

        // Logout Button
        logoutButton.setGraphic(logoutImage);
        logoutButton.setText("");
        logoutButton.setStyle("-fx-background-color: transparent;");
        logoutButton.setPadding(new Insets(0)); // Uniform padding
        logoutButton.setPrefSize(40, 40); // Default size
    }

}