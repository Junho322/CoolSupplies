package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet1Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet8Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOOrder;
import ca.mcgill.ecse.coolsupplies.controller.TOParent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.ViewIndividualOrderController;
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

/**
 * Controller class that handles operations related to the Parent Home Page in the system.
 *
 * @author Jack McDonald
 */
public class ParentHomePageController implements Initializable {

    static private String parentEmail;
    @FXML
    private ComboBox<String> orderComboBox;

    /**
     * Sets the parent email
     *
     * @param email The email to set the parent email to
     * @author Jack McDonald
     */
    public static void setParentEmail(String email) {
        parentEmail = email;
    }

    /**
     * Handles the click event on the Confirm Order button
     *
     * @param event The ActionEvent that triggered the event
     * @author Jack McDonald
     */
    @FXML
    void doConfirmOrder(ActionEvent event) {
        String orderNumber = orderComboBox.getValue();
        if (orderNumber == null) {
            return;
        }
        int orderNum = Integer.parseInt(orderNumber);
        ViewIndividualOrderController controller = new ViewIndividualOrderController();
        controller.setSelectedOrder(CoolSuppliesFeatureSet8Controller.viewIndividualOrder(orderNum), null);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Order/ViewIndividualOrder.fxml"));
            Parent root = loader.load();
            ViewIndividualOrderController controller1 = loader.getController();
            controller1.initialize();
            controller1.setSelectedOrder(CoolSuppliesFeatureSet8Controller.viewIndividualOrder(orderNum), null);
            controller1.initialize();

            Stage stage = (Stage) orderComboBox.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Cool Supplies");
            stage.setMaximized(true);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the click event on the Start New Order button
     *
     * @param event The ActionEvent that triggered the event
     * @author Jack McDonald
     */
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

    /**
     * Handles the click event on the View Orders button
     *
     * @param event The ActionEvent that triggered the event
     * @author Jack McDonald
     */
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
}
