package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet8Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOOrder;
import ca.mcgill.ecse.coolsupplies.controller.TOOrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;

public class ViewIndividualOrderController {

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
    private static int orderNumber = 1;

    @FXML
    public void initialize() {
        try {
            // Use viewIndividualOrder to get the full details
            currentOrder = CoolSuppliesFeatureSet8Controller.viewIndividualOrder(orderNumber);
            if (currentOrder == null) {
                throw new RuntimeException("Order not found.");
            }

            setupTableColumns();
            populateOrderDetails();
        } catch (RuntimeException e) {
            showAlert("Error", "Failed to load order details: " + e.getMessage());
        }

        // Set up toggle button
        paymentForm.setVisible(false);
        paymentForm.setManaged(false);
        togglePaymentButton.setOnAction(event -> togglePaymentForm());
        payButton.setOnAction(event -> processPayment());
    }

    private void setupTableColumns() {
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        bundleNameColumn.setCellValueFactory(new PropertyValueFactory<>("gradeBundleName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
    }

    private void populateOrderDetails() {
        orderNumberLabel.setText("Order Number: " + currentOrder.getNumber());
        parentEmailLabel.setText("Parent Email: " + currentOrder.getParentEmail());
        studentNameLabel.setText("Student Name: " + currentOrder.getStudentName());
        statusLabel.setText("Status: " + currentOrder.getStatus());

        // Format date if needed
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(currentOrder.getDate());

        dateLabel.setText("Date: " + formattedDate);
        levelLabel.setText("Level: " + currentOrder.getLevel());
        totalCostLabel.setText(String.format("Order Cost: $%.2f", currentOrder.getTotalPrice()));
        penaltyCostLabel.setVisible(false); // Assuming penalty cost isn't dynamically available

        // Authorization codes
        authCodeLabel.setText("Authorization Code: " + (currentOrder.getAuthorizationCode() != null ? currentOrder.getAuthorizationCode() : "N/A"));
        penaltyAuthCodeLabel.setText("Penalty Authorization Code: " + (currentOrder.getPenaltyAuthorizationCode() != null ? currentOrder.getPenaltyAuthorizationCode() : "N/A"));

        // Populate items table
        ObservableList<TOOrderItem> items = FXCollections.observableArrayList(currentOrder.getItems());
        itemsTable.setItems(items);
    }

    private void togglePaymentForm() {
        boolean isVisible = paymentForm.isVisible();
        paymentForm.setVisible(!isVisible);
        paymentForm.setManaged(!isVisible);
        togglePaymentButton.setText(isVisible ? "Pay Now" : "Cancel");
    }

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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void setOrderNumber(int i) { orderNumber = i; }
}
