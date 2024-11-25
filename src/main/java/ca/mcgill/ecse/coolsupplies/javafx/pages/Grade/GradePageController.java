package ca.mcgill.ecse.coolsupplies.javafx.pages.Grade;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet1Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet6Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import ca.mcgill.ecse.coolsupplies.controller.TOParent;
import ca.mcgill.ecse.coolsupplies.controller.TOStudent;
import ca.mcgill.ecse.coolsupplies.javafx.pages.ParentController;
import ca.mcgill.ecse.coolsupplies.javafx.pages.StudentController;
import ca.mcgill.ecse.coolsupplies.javafx.pages.Grade.*;


public class GradePageController implements Initializable {

    // @FXML
    // private ListView<String> gradeListView;

    @FXML
    private VBox chosenGradeCard;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button addGradeButton;

    @FXML
    private Button updateGradeButton;

    @FXML
    private Label gradeNameLabel;

    @FXML
    private GridPane grid;

    @FXML
    private GridPane grid1;

    @FXML
    private TOGrade selectedGrade; // Track the currently selected grade

    private ArrayList<TOGrade> grades = new ArrayList<>();
    private EventListener listener;
    private AnchorPane lastSelectedCard;

    private ArrayList<TOGrade> getData() {
        return new ArrayList<TOGrade>(CoolSuppliesFeatureSet7Controller.getGrades());
    }

    private void setChosenGrade(TOGrade grade, AnchorPane card) {
        selectedGrade = grade; // Track the selected grade
        gradeNameLabel.setText("> " + grade.getLevel());

        if (lastSelectedCard != null) {
            lastSelectedCard.getStyleClass().remove("highlight");
            lastSelectedCard.getStyleClass().add("non-highlight");
        }

        card.getStyleClass().remove("non-highlight");
        card.getStyleClass().add("highlight");
        lastSelectedCard = card;

        initializeStudentList(grade.getLevel());
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grades = getData();

        if (!grades.isEmpty()) {
            listener = new EventListener() {
                @Override
                public void onClickListener(TOGrade grade) {
                    setChosenGrade(grade, lastSelectedCard);
                }
            };
        }

        //set grid height
        grid.getChildren().clear();
        grid1.setMinHeight(Region.USE_COMPUTED_SIZE);
        grid1.setPrefHeight(700);
        grid1.setMaxHeight(Region.USE_COMPUTED_SIZE);

        int students = 0;
        int i = 0;
        for (TOGrade grade : grades) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Grade.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                if (lastSelectedCard == null) {
                    setChosenGrade(grade, anchorPane);
                    lastSelectedCard = anchorPane;
                }

                students = CoolSuppliesFeatureSet7Controller.getStudentsOfGrade(grade.getLevel()).size();
                GradeController gradeController = fxmlLoader.getController();
                gradeController.setGrade(grade, students, listener);

                anchorPane.setOnMouseClicked(event -> {
                    setChosenGrade(grade, anchorPane);
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

    private void initializeStudentList(String gradeLevel) {
        int i = 0;

        ArrayList<TOStudent> students = new ArrayList<>(CoolSuppliesFeatureSet7Controller.getStudentsOfGrade(gradeLevel));

        grid1.getChildren().clear();

        for (TOStudent student: students) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("StudentOfGrade.fxml")); //??
                AnchorPane anchorPane = fxmlLoader.load();

                StudentOfGradeController studentOfGradeController = fxmlLoader.getController();
                
                studentOfGradeController.setStudent(student, listener);

                anchorPane.setMinWidth(scroll.getWidth() - 17);
                anchorPane.setMaxWidth(scroll.getWidth() - 17);
                studentOfGradeController.setSize(scroll.getWidth() - 222);

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



    /**
     * Handles the Add Grade button click by opening the Grade.fxml scene.
     *
     * @param event the ActionEvent triggered by the Add Grade button
     */
    @FXML
    void switchToAddGrade(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("./AddGrade.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();

            stage.setTitle("Add Grade");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL); // Set the modality to APPLICATION_MODAL
            stage.showAndWait(); // Use showAndWait to block the admin page until the register parent window is closed

            grades = getData();
            initialize(null, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the Update Grade button click.
     * Opens the UpdateGrade.fxml scene and passes the selected grade.
     */
    @FXML
    void switchToUpdateGrade(ActionEvent event) throws IOException {
    if (selectedGrade == null) {
        showAlert(Alert.AlertType.WARNING, "No Grade Selected", "Please select a grade to update.");
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./UpdateGrade.fxml"));
        Parent root = loader.load();

        // Get UpdateGradeController instance and pass the selected grade
        UpdateGradeController updateGradeController = loader.getController();
        updateGradeController.setSelectedGrade(selectedGrade);

        Stage stage = new Stage();
        stage.setTitle("Update Grade");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        grades = getData();
        // Refresh the grades after the update
        initialize(null, null);

    } catch (IOException e) {
        e.printStackTrace();
    }
}

    public interface EventListener {
        void onClickListener(TOGrade grade);
    }

    /**
     * Displays an alert message to the user.
     *
     * @param type    the AlertType (e.g., INFORMATION, ERROR)
     * @param title   the title of the alert
     * @param content the message content of the alert
     */
    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
