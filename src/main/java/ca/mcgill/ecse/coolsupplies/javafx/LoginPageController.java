package ca.mcgill.ecse.coolsupplies.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToAdminPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pages/AdminPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public void switchToBundlePage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pages/BundlePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public void switchToInventoryPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pages/InventoryPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public void switchToOrdersPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pages/OrdersPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public void switchToParentPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("pages/ParentPage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
