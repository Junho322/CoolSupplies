package ca.mcgill.ecse.coolsupplies.javafx;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;


public class CoolSuppliesFxmlView extends Application {

    public static final EventType<Event> REFRESH_EVENT = new EventType<>("REFRESH");
    private static CoolSuppliesFxmlView instance;
    private List<Node> refreshableNodes = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
            root.setStyle(CoolSuppliesApplication.DARK_MODE ? "-fx-base: rgba(20, 20, 20, 255);" : "");

            var scene = new Scene(root, Color.BLACK);
            primaryStage.setScene(scene);

            primaryStage.setTitle("CoolSupplies");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Register the node for receiving refresh events
    public void registerRefreshEvent(Node node) {
        refreshableNodes.add(node);
    }

    // Register multiple nodes for receiving refresh events
    public void registerRefreshEvent(Node... nodes) {
        for (var node: nodes) {
            refreshableNodes.add(node);
        }
    }

    // remove the node from receiving refresh events
    public void removeRefreshableNode(Node node) {
        refreshableNodes.remove(node);
    }

    // fire the refresh event to all registered nodes
    public void refresh() {
        for (Node node : refreshableNodes) {
            node.fireEvent(new Event(REFRESH_EVENT));
        }
    }

    public static CoolSuppliesFxmlView getInstance() {
        return instance;
    }
}
