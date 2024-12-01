package ca.mcgill.ecse.coolsupplies.javafx;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet1Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOParent;
import ca.mcgill.ecse.coolsupplies.javafx.pages.AdminPageController;
import ca.mcgill.ecse.coolsupplies.javafx.pages.ParentHomePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginPageController {

    @FXML
    private TextField emailTextField;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordTextField;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void doLogin(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        //admin login
        if (Objects.equals(email, "admin@cool.ca")) {
            if (CoolSuppliesFeatureSet1Controller.matchesCurrentPassword(password)) {
                try {
                    switchToAdminPage(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throwErrorWindow("Invalid email or password");
            }
        } else { //parent login
            TOParent targetParent = CoolSuppliesFeatureSet1Controller.getParent(email);
            if (targetParent != null && targetParent.getPassword().equals(password)) {
                try {
                    ParentHomePageController.setParentEmail(email);
                    switchToParentPage(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throwErrorWindow("Invalid email or password");
            }
        }
    }

    private void switchToAdminPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/AdminPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) loginButton.getScene().getWindow();
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
    }

    private void switchToBundlePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pages/BundlePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void switchToGradePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pages/Grade/GradePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void switchToInventoryPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pages/InventoryPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void switchToOrderPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pages/Order/OrderPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void switchToParentPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pages/ParentHomePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.show();
    }

    private void throwErrorWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void switchToLoginPage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
            stage = (Stage) loginButton.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
