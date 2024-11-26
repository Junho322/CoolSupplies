package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet1Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet8Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOOrder;
import ca.mcgill.ecse.coolsupplies.controller.TOParent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ParentHomePageController implements Initializable {

    @FXML
    private ComboBox<String> orderComboBox;

    static private String parentEmail;

    @FXML
    void doConfirmOrder(ActionEvent event) {
        String orderNumber = orderComboBox.getValue();
        if (orderNumber == null) {
            return;
        }
        int orderNum = Integer.parseInt(orderNumber);
        ViewIndividualOrderController.setOrderNumber(orderNum);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/ViewIndividualOrder.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) orderComboBox.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Cool Supplies");
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doStartNewOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Order/StartOrder.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) orderComboBox.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Cool Supplies");
            stage.setMaximized(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TOParent targetParent = CoolSuppliesFeatureSet1Controller.getParent(parentEmail);
        ArrayList<TOOrder> orders = new ArrayList<>(CoolSuppliesFeatureSet8Controller.getOrders());

        for (TOOrder order : orders) {
            if (order.getParentEmail().equals(targetParent.getEmail())) {
                orderComboBox.getItems().add(Integer.toBinaryString(order.getNumber()));
            }
        }
    }

    public static void setParentEmail(String email) {
        parentEmail = email;
    }
}
