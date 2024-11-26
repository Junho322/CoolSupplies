package ca.mcgill.ecse.coolsupplies.javafx.pages.Order;

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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet8Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOOrder;
import ca.mcgill.ecse.coolsupplies.controller.TOOrderItem;
import ca.mcgill.ecse.coolsupplies.controller.TOOrder;
import ca.mcgill.ecse.coolsupplies.controller.TOStudent;
import ca.mcgill.ecse.coolsupplies.javafx.pages.Order.*;


public class OrderPageController implements Initializable {

    // @FXML
    // GridPane rightSideGridPane;

    // @FXML
    // private VBox chosenOrderCard;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button addOrderButton;

    @FXML
    private Button updateOrderButton;

    @FXML
    private Button viewOrderButton;

    @FXML
    private Label orderNameLabel;

    @FXML
    private GridPane grid;

    @FXML
    private Button orderSort;

    @FXML
    private TOOrder selectedOrder; 

    @FXML
    private Button cancelOrderButton;

    @FXML
    private Button pickUpButton;

    @FXML
    private Button startOrderButton;

    @FXML
    private Button viewIndividualOrderButton;


    private ArrayList<TOOrder> orders = new ArrayList<>();
    private EventListener listener;
    private AnchorPane lastSelectedCard;



    private enum Sort {
        SYSTEM_DEFAULT,
        NAME_ASCENDING,
        NAME_DESCENDING,
        ITEMS_ASCENDING,
        ITEMS_DESCENDING,
    }
    Sort sort = Sort.SYSTEM_DEFAULT;

    private ArrayList<TOOrder> getData() {
        return new ArrayList<TOOrder>(CoolSuppliesFeatureSet8Controller.getOrders());
    }

    private void setChosenOrder(TOOrder order, AnchorPane card) {
        selectedOrder = order; 
        // orderNameLabel.setText("> Students in Order: " + order.getNumber());
        if (order.getStatus().equals("Prepared")) {
            pickUpButton.setText("Ready for Pickup!");
            pickUpButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        } else {
            pickUpButton.setText("Not Ready for Pickup");
            pickUpButton.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
        }

        if (order.getStatus().equals("Started")||order.getStatus().equals("Paid")) {
            cancelOrderButton.setText("Cancel Order");
            cancelOrderButton.setStyle("-fx-background-color: #FF4C4C; -fx-text-fill: white;");
        } else {
            cancelOrderButton.setText("Cannot Cancel");
            cancelOrderButton.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
        }

        if (lastSelectedCard != null) {
            lastSelectedCard.getStyleClass().remove("highlight");
            lastSelectedCard.getStyleClass().add("non-highlight");
        }

        card.getStyleClass().remove("non-highlight");
        card.getStyleClass().add("highlight");
        lastSelectedCard = card;

        selectedOrder = order;
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orders = getData();

        grid.getChildren().clear();
        switch (sort) {
            case SYSTEM_DEFAULT:
                break;
            case NAME_ASCENDING:
                orders.sort((TOOrder g1, TOOrder g2) -> g1.getLevel().toLowerCase().compareTo(g2.getLevel().toLowerCase()));
                break;
            case NAME_DESCENDING:
                orders.sort((TOOrder g1, TOOrder g2) -> g2.getLevel().toLowerCase().compareTo(g1.getLevel().toLowerCase()));
                break;
            case ITEMS_ASCENDING:
                orders.sort((TOOrder g1, TOOrder g2) -> g1.getLevel().compareTo(g2.getLevel()));
                break;
            case ITEMS_DESCENDING:
                orders.sort((TOOrder g1, TOOrder g2) -> g2.getLevel().compareTo(g1.getLevel()));
                break;
        }

        if (!orders.isEmpty()) {
            listener = new EventListener() {
                @Override
                public void onClickListener(TOOrder order) {
                    setChosenOrder(order, lastSelectedCard);
                }
            };
        }

        // grid1.setMinHeight(Region.USE_COMPUTED_SIZE);
        // grid1.setPrefHeight(700);
        // grid1.setMaxHeight(Region.USE_COMPUTED_SIZE);

        int numItems = 0;
        List<TOOrderItem> items = new ArrayList<TOOrderItem>();
        //String bundleName = null;
        int i = 0;
        for (TOOrder order : orders) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Order.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                if (lastSelectedCard == null) {
                    setChosenOrder(order, anchorPane);
                    lastSelectedCard = anchorPane;
                }

                numItems = order.getItems().size();
                items = order.getItems();
                // if (CoolSuppliesFeatureSet8Controller.getBundleOfOrder(order.getLevel()) != null) {
                //     bundleName = CoolSuppliesFeatureSet8Controller.getBundleOfOrder(order.getLevel()).getName();
                // }
                // else {
                //     bundleName = null;
                // }
                
                OrderController orderController = fxmlLoader.getController();
                orderController.setOrder(order, listener);

                anchorPane.setOnMouseClicked(event -> {
                    setChosenOrder(order, anchorPane);
                    System.out.println(order);
                });

                grid.add(anchorPane, 0, i);
                i++;

                grid.setMinWidth(Region.USE_PREF_SIZE);
                grid.setPrefWidth(485);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(2000);
                grid.setMaxHeight(Region.USE_COMPUTED_SIZE);

                GridPane.setMargin(anchorPane, new Insets(3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void switchToAddOrder(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./AddOrder.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();

            stage.setTitle("Add Order");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.showAndWait(); 

            orders = getData();
            initialize(null, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @FXML
    // void switchToUpdateOrder(ActionEvent event) throws IOException {
    // if (selectedOrder == null) {
    //     showAlert(Alert.AlertType.WARNING, "No Order Selected", "Please select a order to update.");
    //     return;
    // }

    // try {
    //     FXMLLoader loader = new FXMLLoader(getClass().getResource("./UpdateOrder.fxml"));
    //     Parent root = loader.load();

    //     UpdateOrderController updateOrderController = loader.getController();
    //     updateOrderController.setSelectedOrder(selectedOrder);

    //     Stage stage = new Stage();
    //     stage.setTitle("Update Order");
    //     stage.setScene(new Scene(root));
    //     stage.initModality(Modality.APPLICATION_MODAL);
    //     stage.showAndWait();

    //     orders = getData();
    //     initialize(null, null);

    // } catch (IOException e) {
    //     e.printStackTrace();
    //     }
    // }

    @FXML
    void toggleOrderSort(ActionEvent event) {
        switch (sort) {
            case SYSTEM_DEFAULT:
                sort = Sort.NAME_ASCENDING;
                orderSort.setText("▼     Level");
                break;
            case NAME_ASCENDING:
                sort = Sort.NAME_DESCENDING;
                orderSort.setText("▲     Level");
                break;
            case NAME_DESCENDING:
                sort = Sort.ITEMS_ASCENDING;
                orderSort.setText("▼     Items");
                break;
            case ITEMS_ASCENDING:
                sort = Sort.ITEMS_DESCENDING;
                orderSort.setText("▼     Items");
                break;
            case ITEMS_DESCENDING:
                sort = Sort.SYSTEM_DEFAULT;
                orderSort.setText("▲     Filter");
                break;
        }
        initialize(null, null);
    }

    @FXML
    void pickUpOrder(ActionEvent event) {
        if (selectedOrder == null) {
            showAlert(AlertType.WARNING, "No Order Selected", "Please select an order to pick up.");
            return;
        }

        if (selectedOrder.getStatus().equals("Prepared")) {
            try {
                CoolSuppliesFeatureSet8Controller controller8 = new CoolSuppliesFeatureSet8Controller();
                String result = controller8.pickUpOrder(selectedOrder.getNumber());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Order picked up successfully!");
                alert.showAndWait();
                orders = getData();
                initialize(null, null);
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to pick up order");
                alert.showAndWait();
            }
        } else {
            showAlert(AlertType.WARNING, "Order Not Ready", "This order is not ready for pickup.");
        }
    }

    @FXML
    void cancelOrder(ActionEvent event) {
        if (selectedOrder == null) {
            showAlert(AlertType.WARNING, "No Order Selected", "Please select an order to cancel.");
            return;
        }

        if (selectedOrder.getStatus().equals("Started") || selectedOrder.getStatus().equals("Paid")) {
            try {
                CoolSuppliesFeatureSet8Controller controller8 = new CoolSuppliesFeatureSet8Controller();
                String result = controller8.cancelOrder(selectedOrder.getNumber());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Order cancelled successfully!");
                alert.showAndWait();
                orders = getData();
                initialize(null, null);
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to cancel order");
                alert.showAndWait();
            }
        } else {
            showAlert(AlertType.WARNING, "Order Cannot Be Cancelled", "This order cannot be cancelled.");
        }
    }

    @FXML
    void viewIndividualOrder(ActionEvent event) throws IOException{
        if (selectedOrder == null) {
            showAlert(Alert.AlertType.WARNING, "No Order Selected", "Please select an order to view.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewIndividualOrder.fxml"));
            Parent root = loader.load();

            // if (lastSelectedCard == null) {
            //     setChosenOrder(selectedOrder, anchorPane);
            //     lastSelectedCard = anchorPane;
            // }

            ViewIndividualOrderController viewIndividualOrderController = loader.getController();
            viewIndividualOrderController.setSelectedOrder(selectedOrder, listener);

            Stage stage = new Stage();
            stage.setTitle("View Order");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            orders = getData();
            initialize(null, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    @FXML
    void updateOrder(ActionEvent event) throws IOException{
        if (selectedOrder == null) {
            showAlert(Alert.AlertType.WARNING, "No Order Selected", "Please select an order to update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateOrder.fxml"));
            Parent root = loader.load();

            UpdateOrderController updateOrderController = loader.getController();
            updateOrderController.setExistingOrder(selectedOrder);

            Stage stage = new Stage();
            stage.setTitle("Update Order");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            orders = getData();
            initialize(null, null);

        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    @FXML
    void startOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartOrder.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();

            stage.setTitle("Start Order");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); 

            orders = getData();
            initialize(null, null);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void startSchoolYear(ActionEvent event) {
        try {
            ArrayList<TOOrder> orders = new ArrayList<>(CoolSuppliesFeatureSet8Controller.getOrders());

            for (TOOrder order : orders) {
                CoolSuppliesFeatureSet8Controller.startSchoolYear(order.getNumber());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("School year started successfully!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to start school year");
            alert.showAndWait();
        }
    }

    @FXML
    void updatePassword(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../UpdatePassword.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();

            stage.setTitle("Update Password");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface EventListener {
        void onClickListener(TOOrder order);
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}