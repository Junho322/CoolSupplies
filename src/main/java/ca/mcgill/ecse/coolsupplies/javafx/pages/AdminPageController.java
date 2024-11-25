package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.*;
import ca.mcgill.ecse.coolsupplies.javafx.controller.EventListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {

    @FXML
    GridPane rightSideGridPane;

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

    @FXML
    private Button registerParent;

    @FXML
    private Button registerStudent;

    @FXML
    private Button updatePassword;

    @FXML
    private Button startSchoolYear;

    @FXML
    private Button parentSort;

    @FXML
    private ImageView email;

    @FXML
    private ImageView phone;

    private ArrayList<TOParent> parents = new ArrayList<>();
    private EventListener listener;
    private AnchorPane lastSelectedCard;
    private TOParent chosenParent;

    //sort enum
    private enum Sort {
        SYSTEM_DEFAULT,
        NAME_ASCENDING,
        NAME_DESCENDING
    }
    Sort sort = Sort.SYSTEM_DEFAULT;

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
        chosenParent = parent;
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parents = getData();
        grid.getChildren().clear();
        switch (sort) {
            case SYSTEM_DEFAULT:
                break;
            case NAME_ASCENDING:
                parents.sort((TOParent p1, TOParent p2) -> p1.getName().toLowerCase().compareTo(p2.getName().toLowerCase()));
                break;
            case NAME_DESCENDING:
                parents.sort((TOParent p1, TOParent p2) -> p2.getName().toLowerCase().compareTo(p1.getName().toLowerCase()));
                break;
        }

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
                grid.setPrefHeight(2000);
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
        grid1.setPrefWidth(5000);
        scroll.fitToWidthProperty().set(true);
        scroll.fitToHeightProperty().set(true);
    }

    @FXML
    void registerParent(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterParent.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();

            stage.setTitle("Register Parent");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL); // Set the modality to APPLICATION_MODAL
            stage.showAndWait(); // Use showAndWait to block the admin page until the register parent window is closed

            // Refresh parent list
            parents = getData();
            initialize(null, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void toggleParentSort(ActionEvent event) {
        switch (sort) {
            case SYSTEM_DEFAULT:
                sort = Sort.NAME_ASCENDING;
                parentSort.setText("▼     Name");
                break;
            case NAME_ASCENDING:
                sort = Sort.NAME_DESCENDING;
                parentSort.setText("▲     Name");
                break;
            case NAME_DESCENDING:
                sort = Sort.SYSTEM_DEFAULT;
                parentSort.setText("▼     Filter");
                break;
        }
        initialize(null, null);
    }

    @FXML
    void copyEmail(ActionEvent event) {
        String email = parentNameLabel.getText().substring(2);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(email), null);
    }

    @FXML
    void copyPhone(ActionEvent event) {
        String phone = CoolSuppliesFeatureSet1Controller.getParent(parentNameLabel.getText().substring(2)).getPhoneNumber() + "";
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(phone), null);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdatePassword.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();

            stage.setTitle("Update Password");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL); // Set the modality to APPLICATION_MODAL
            stage.showAndWait(); // Use showAndWait to block the admin page until the update password window is closed

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void deleteParentAccount(ActionEvent event) {
        try {
            String email = chosenParent.getEmail();
            CoolSuppliesFeatureSet1Controller.deleteParent(email);
            parents = null;
            initialize(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
