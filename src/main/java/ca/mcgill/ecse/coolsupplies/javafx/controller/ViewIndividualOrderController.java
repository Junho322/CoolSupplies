package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.controller.*;
import ca.mcgill.ecse.coolsupplies.javafx.controller.OrderPageController.EventListener;
import ca.mcgill.ecse.coolsupplies.model.BundleItem;
import ca.mcgill.ecse.coolsupplies.model.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ViewIndividualOrderController {

    @FXML
    private TextField DeleteItemName;


    @FXML
    private TextField ItemName;

    @FXML
    private TextField QuantityNumber;

    @FXML
    private VBox paymentForm;

    @FXML
    private Button togglePaymentButton;

    @FXML
    private TextField authCodeField;

    @FXML
    private Button payButton;

    @FXML
    private Label totalCostLabel;

    @FXML
    private Label penaltyCostLabel;

    @FXML
    private Label orderNumberLabel;

    @FXML
    private Label parentEmailLabel;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label levelLabel;

    @FXML
    private Label authCodeLabel;

    @FXML
    private Label penaltyAuthCodeLabel;

    @FXML
    private TableView<TOOrderItem> itemsTable;

    @FXML
    private TableColumn<TOOrderItem, Integer> quantityColumn;

    @FXML
    private TableColumn<TOOrderItem, String> itemNameColumn;

    @FXML
    private TableColumn<TOOrderItem, String> bundleNameColumn;

    @FXML
    private TableColumn<TOOrderItem, Double> priceColumn;

    @FXML
    private TableColumn<TOOrderItem, String> discountColumn;

    private TOOrder currentOrder;
    private TOOrder selectedOrder;
    private EventListener listener;
    private TOOrder order;
    @FXML
    private void click(MouseEvent event) {
        listener.onClickListener(order);
    }

    /**
     * Sets the selected order and its associated event listener, then initializes the view.
     *
     * @param order    the selected order to display.
     * @param listener the event listener associated with the order.
     * @author David Vo
     */
    public void setSelectedOrder(TOOrder order, EventListener listener) {
        this.selectedOrder = order;
        this.listener = listener;
        initialize();
    }

    /**
     * Initializes the order view by setting up table columns, loading order details,
     * and configuring the payment form.
     * Handles exceptions if the order is not found.
     *
     * @author Hamza Khalfi
     */

    @FXML
    public void initialize() {
        try {

            int orderNumber = selectedOrder.getNumber();
            currentOrder = CoolSuppliesFeatureSet8Controller.viewIndividualOrder(orderNumber);
            if (currentOrder == null) {
                throw new RuntimeException("Order not found.");
            }

            setupTableColumns();
            populateOrderDetails();
        } catch (RuntimeException e) {

        }


        if (paymentForm != null) {
            paymentForm.setVisible(false);
            paymentForm.setManaged(false);
            togglePaymentButton.setOnAction(event -> togglePaymentForm());
            payButton.setOnAction(event -> processPayment());
        }
    }


    /**
     * Configures the table columns to display order item details such as quantity, item name,
     * bundle name, price, and discount.
     *
     * @author Hamza Khalfi
     */
    private void setupTableColumns() {
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        bundleNameColumn.setCellValueFactory(new PropertyValueFactory<>("gradeBundleName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
    }


    /**
     * Populates the order details such as order number, parent email, student name, status,
     * date, level, and total cost. Also, initializes the table with order items.
     *
     * @author Hamza Khalfi
     */
    private void populateOrderDetails() {
        orderNumberLabel.setText("Order Number: " + currentOrder.getNumber());
        parentEmailLabel.setText("Parent Email: " + currentOrder.getParentEmail());
        studentNameLabel.setText("Student Name: " + currentOrder.getStudentName());
        statusLabel.setText("Status: " + currentOrder.getStatus());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(currentOrder.getDate());

        dateLabel.setText("Date: " + formattedDate);
        levelLabel.setText("Level: " + currentOrder.getLevel());
        totalCostLabel.setText(String.format("Order Cost: $%.2f", currentOrder.getTotalPrice()));
        penaltyCostLabel.setVisible(false); // Assuming penalty cost isn't dynamically available


        authCodeLabel.setText("Authorization Code: " + (currentOrder.getAuthorizationCode() != null ? currentOrder.getAuthorizationCode() : "N/A"));
        penaltyAuthCodeLabel.setText("Penalty Authorization Code: " + (currentOrder.getPenaltyAuthorizationCode() != null ? currentOrder.getPenaltyAuthorizationCode() : "N/A"));


        ObservableList<TOOrderItem> items = FXCollections.observableArrayList(currentOrder.getItems());
        itemsTable.setItems(items);
    }

    /**
     * Toggles the visibility of the payment form and updates the toggle button's text.
     *
     * @author Hamza Khalfi
     */
    private void togglePaymentForm() {
        boolean isVisible = paymentForm.isVisible();
        paymentForm.setVisible(!isVisible);
        paymentForm.setManaged(!isVisible);
        togglePaymentButton.setText(isVisible ? "Pay Now" : "Cancel");
    }

    /**
     * Processes the payment for the current order. Handles normal payments and penalties.
     * Displays appropriate alerts for payment success or failure.
     *
     * @author Hamza Khalfi
     */
    private void processPayment() {
        String authCode = authCodeField.getText();

        if (authCode == null || authCode.trim().isEmpty()) {
            showAlert("Invalid Authorization Code", "Payment Failed: Please enter a valid authorization code.");
            return;
        }

        try {
            if (currentOrder.getStatus().equals("Penalized")) {
                // Order is penalized, collect penalty auth code and call payPenaltyForOrder
                showPenaltyPaymentDialog(authCode);
            } else {
                // Proceed with normal payment
                String result = CoolSuppliesFeatureSet8Controller.payForOrder(currentOrder.getNumber(), authCode);

                // Check the response from the controller
                if (result.equals("Payment processed")) {
                    showAlert("Payment Successful", "Your order has been paid successfully!");
                    currentOrder = CoolSuppliesFeatureSet8Controller.viewIndividualOrder(currentOrder.getNumber());
                    populateOrderDetails();

                    // Reset payment form visibility
                    paymentForm.setVisible(false);
                    paymentForm.setManaged(false);
                    togglePaymentButton.setText("Pay Now");
                } else {
                    showAlert("Payment Failed", result);
                }
            }
        } catch (RuntimeException e) {
            showAlert("Error", "An error occurred while processing the payment: " + e.getMessage());
        }
    }

    /**
     * Displays a dialog for penalty payment where the user enters a penalty authorization code.
     *
     * @param authorizationCode the normal authorization code for the payment.
     * @author Hamza Khalfi
     */
    private void showPenaltyPaymentDialog(String authorizationCode) {
        // Create a custom dialog for penalty payment
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Pay Penalty");
        dialog.setHeaderText("Your order is penalized. Please enter the penalty authorization code to pay.");

        // Set the button types
        ButtonType payButtonType = new ButtonType("Pay Penalty", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(payButtonType, ButtonType.CANCEL);

        // Create the penalty auth code input field
        TextField penaltyAuthCodeField = new TextField();
        penaltyAuthCodeField.setPromptText("Enter penalty authorization code");

        VBox content = new VBox();
        content.setSpacing(10);
        content.getChildren().addAll(new Label("Penalty Authorization Code:"), penaltyAuthCodeField);

        dialog.getDialogPane().setContent(content);

        // Convert the result to the auth code when the pay button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == payButtonType) {
                return penaltyAuthCodeField.getText();
            }
            return null;
        });

        // Show the dialog and wait for the user input
        dialog.showAndWait().ifPresent(penaltyAuthCode -> {
            if (penaltyAuthCode != null && !penaltyAuthCode.trim().isEmpty()) {
                processPenaltyPayment(authorizationCode, penaltyAuthCode);
            } else {
                showAlert("Invalid Authorization Code", "Payment Failed: Please enter a valid penalty authorization code.");
            }
        });
    }

    /**
     * Processes the penalty payment for the current order. Handles both penalty and normal payment.
     *
     * @param authorizationCode      the normal authorization code for the payment.
     * @param penaltyAuthorizationCode the penalty authorization code for the payment.
     * @author Hamza Khalfi
     */
    private void processPenaltyPayment(String authorizationCode, String penaltyAuthorizationCode) {
        try {
            // Attempt to pay the penalty and the order using the controller
            String result = CoolSuppliesFeatureSet8Controller.payPenaltyForOrder(currentOrder.getNumber(), authorizationCode, penaltyAuthorizationCode);

            // Check the response from the controller
            if (result.equals("Penalty payment successful. The order is now prepared.")) {
                showAlert("Payment Successful", "Your order and penalty have been paid successfully!");
                currentOrder = CoolSuppliesFeatureSet8Controller.viewIndividualOrder(currentOrder.getNumber());
                populateOrderDetails();

                // Reset payment form visibility
                paymentForm.setVisible(false);
                paymentForm.setManaged(false);
                togglePaymentButton.setText("Pay Now");
            } else {
                showAlert("Payment Failed", result);
            }
        } catch (RuntimeException e) {
            showAlert("Error", "An error occurred while processing the penalty payment: " + e.getMessage());
        }
    }



    /**
     * Displays an alert dialog with the given title and content.
     *
     * @param title   the title of the alert dialog.
     * @param content the content message of the alert dialog.
     * @author Hamza Khalfi
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    /**
     * Adds desired Item to the individual Order that was selected. Validates
     * user inputs, ensuring item exists, quantities are positive and updating the order
     * details. Proper error messages are displayed
     * 
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     * 
     * @author Jun Ho Oh
     */
    @FXML
    void AddOrderItem(ActionEvent event) {
        if (validateInputFields()) {
            String itemName = ItemName.getText().trim();
            int quantity;
            
            try {
                //quantity should be positive
                quantity = Integer.parseInt(QuantityNumber.getText());
                if (quantity <= 0) {
                    showAlert("Invalid Input", "Quantity must be a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Quantity must be a valid number.");
                return;
            }

            try {
                String result = CoolSuppliesFeatureSet8Controller.addItemToOrder(itemName, quantity, currentOrder.getNumber());
                //uses controller feature set 8 method to add to order
                if (result.equals("Successfully added item to order")) {


                    currentOrder = CoolSuppliesFeatureSet8Controller.viewIndividualOrder(currentOrder.getNumber());
                    ObservableList<TOOrderItem> items = FXCollections.observableArrayList(currentOrder.getItems());
                    itemsTable.setItems(items);
                    itemsTable.refresh();
                    populateOrderDetails();

                    ItemName.clear();
                    QuantityNumber.clear();
                } else {
                    showAlert("Error", result);
                }
            } catch (Exception e) {
                showAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Updates the item in the order (its quantity). Validates user inputs and displays 
     * appropriate error message. Updates order details 
     * 
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     * 
     * @author Jun Ho Oh
     */
    @FXML
    void updateOrderItem(ActionEvent event) {
        if (validateUpdateInputFields()) {
            String itemName = ItemName.getText().trim();
            int quantity;

            try {
                quantity = Integer.parseInt(QuantityNumber.getText());
            } catch (NumberFormatException e) {
                showAlert("Error", "Quantity must be a number.");
                return;
            }

            try {
                String result = CoolSuppliesFeatureSet8Controller.updateQuantityOfAnExistingItemOfOrder(
                        currentOrder.getNumber(), itemName, quantity
                );

                showAlert(result.contains("successfully") ? "Success" : "Error", result);
                //updates the item in the order details
                if (result.contains("successfully")) {
                    currentOrder = CoolSuppliesFeatureSet8Controller.viewIndividualOrder(currentOrder.getNumber());
                    ObservableList<TOOrderItem> items = FXCollections.observableArrayList(currentOrder.getItems());
                    itemsTable.setItems(items);
                    itemsTable.refresh();
                    populateOrderDetails();

                    ItemName.clear();
                    QuantityNumber.clear();
                }
            } catch (Exception e) {
                showAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Deletes the desired item from the order. Validates user input and updates order details 
     * and prints appropriate error messages
     * 
     * @param event this is an action event triggered by method Add Item Button defined in FXML page
     * 
     * @author Jun Ho Oh
     */
    @FXML
    void DeleteOrderItem(ActionEvent event) {
        if (validateDeleteInputFields()) {
            String itemName = DeleteItemName.getText().trim();

            try {
                String result = CoolSuppliesFeatureSet8Controller.deleteOrderItem(
                        itemName, String.valueOf(currentOrder.getNumber())
                );

                if (result.contains("deleted successfully")) {
                    currentOrder = CoolSuppliesFeatureSet8Controller.viewIndividualOrder(currentOrder.getNumber());
                    ObservableList<TOOrderItem> items = FXCollections.observableArrayList(currentOrder.getItems());
                    itemsTable.setItems(items);
                    itemsTable.refresh();
                    populateOrderDetails();

                    showAlert("Success", result);
                    DeleteItemName.clear();
                } else {
                    showAlert("Error", result);
                }
            } catch (Exception e) {
                showAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Validates the input fields for creating a new order.
     * Ensures that an order is selected, and the item name and quantity are not empty or null.
     *
     * @return true if all input fields are valid; false otherwise.
     * @author Hamza Khalfi
     */

    private boolean validateInputFields() {
        if (currentOrder == null) {
            showAlert("Error", "No order selected.");
            return false;
        }

        if (ItemName == null || ItemName.getText() == null || ItemName.getText().trim().isEmpty()) {
            showAlert("Error", "Item name cannot be empty.");
            return false;
        }

        if (QuantityNumber == null || QuantityNumber.getText() == null || QuantityNumber.getText().trim().isEmpty()) {
            showAlert("Error", "Quantity cannot be empty.");
            return false;
        }

        return true;
    }

    /**
     * Validates the input fields for updating an existing order.
     * Ensures that an order is selected, and the item name and quantity are not empty or null.
     *
     * @return true if all input fields are valid; false otherwise.
     * @author Hamza Khalfi
     */

    private boolean validateUpdateInputFields() {
        if (currentOrder == null) {
            showAlert("Error", "No order selected.");
            return false;
        }

        if (ItemName == null || ItemName.getText() == null || ItemName.getText().isEmpty()) {
            showAlert("Error", "Item name cannot be empty.");
            return false;
        }

        if (QuantityNumber == null || QuantityNumber.getText() == null || QuantityNumber.getText().isEmpty()) {
            showAlert("Error", "Quantity cannot be empty.");
            return false;
        }

        return true;
    }

    /**
     * Validates the input fields for deleting an item from an order.
     * Ensures that an order is selected and the item name to delete is not empty or null.
     *
     * @return true if all input fields are valid; false otherwise.
     * @author Hamza Khalfi
     */

    private boolean validateDeleteInputFields() {
        if (currentOrder == null) {
            showAlert("Error", "No order selected.");
            return false;
        }
        if (DeleteItemName == null || DeleteItemName.getText() == null || DeleteItemName.getText().isEmpty()) {
            showAlert("Error", "Item name to delete cannot be empty.");

            return false;
        }

        return true;
    }

    
}