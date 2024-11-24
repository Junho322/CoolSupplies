package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet1Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet6Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOParent;
import ca.mcgill.ecse.coolsupplies.controller.TOStudent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.EventListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {

    @FXML
    private VBox chosenParentCard;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    @FXML
    private GridPane grid1;

    @FXML
    private Label parentNameLabel;

    private ArrayList<TOParent> parents = new ArrayList<>();
    private EventListener listener;
    private AnchorPane lastSelectedCard;

    private ArrayList<TOParent> getData() {
        return new ArrayList<TOParent>(CoolSuppliesFeatureSet1Controller.getParents());
    }

    private void setChosenParent(TOParent parent, AnchorPane card) {
        parentNameLabel.setText("> " + parent.getName());

        if (lastSelectedCard != null) {
            lastSelectedCard.getStyleClass().remove("highlight");
            lastSelectedCard.getStyleClass().add("non-highlight");
        }

        card.getStyleClass().remove("non-highlight");
        card.getStyleClass().add("highlight");
        lastSelectedCard = card;

        initializeStudentList(parent.getEmail());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parents = getData();

        if (!parents.isEmpty()) {
            listener = new EventListener() {
                @Override
                public void onClickListener(TOParent parent) {
                    setChosenParent(parent, lastSelectedCard);
                }
            };
        }

        //set grid height
        grid1.setMinHeight(Region.USE_COMPUTED_SIZE);
        grid1.setPrefHeight(700);
        grid1.setMaxHeight(Region.USE_COMPUTED_SIZE);

        int students = 0;
        int i = 0;
        for (TOParent parent : parents) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Parent.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                if (lastSelectedCard == null) {
                    setChosenParent(parent, anchorPane);
                    lastSelectedCard = anchorPane;
                }

                students = CoolSuppliesFeatureSet6Controller.getStudentsOfParent(parent.getEmail()).size();
                ParentController parentController = fxmlLoader.getController();
                parentController.setParent(parent, students, listener);

                anchorPane.setOnMouseClicked(event -> {
                    setChosenParent(parent, anchorPane);
                });

                grid.add(anchorPane, 0, i);
                i++;

                //set grid width
                grid.setMinWidth(Region.USE_PREF_SIZE);
                grid.setPrefWidth(485);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(700);
                grid.setMaxHeight(Region.USE_COMPUTED_SIZE);

                GridPane.setMargin(anchorPane, new Insets(3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeStudentList(String parentEmail) {
        int i = 0;

        ArrayList<TOStudent> students = new ArrayList<>(CoolSuppliesFeatureSet6Controller.getStudentsOfParent(parentEmail));

        grid1.getChildren().clear();

        for (TOStudent student: students) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Student.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                StudentController studentController = fxmlLoader.getController();
                studentController.setStudent(student, listener);

                anchorPane.setMinWidth(scroll.getWidth() - 17);
                anchorPane.setMaxWidth(scroll.getWidth() - 17);
                studentController.setSize(scroll.getWidth() - 222);

                anchorPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                GridPane.setVgrow(anchorPane, Priority.NEVER);
                grid1.add(anchorPane, 0, i);
                i++;

                GridPane.setMargin(anchorPane, new Insets(3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Label label = new Label();
        label.setText("");
        label.setMinWidth(scroll.getWidth() - 4.5);
        grid1.setPrefWidth(scroll.getWidth() - 4.5);
        GridPane.setVgrow(label, Priority.NEVER);
        grid1.add(label, 0, i);
    }
}
