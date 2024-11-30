package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.javafx.pages.AdminPageController;
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
import java.util.ResourceBundle;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet7Controller;
import ca.mcgill.ecse.coolsupplies.controller.CoolSuppliesFeatureSet8Controller;
import ca.mcgill.ecse.coolsupplies.controller.TOGrade;
import ca.mcgill.ecse.coolsupplies.controller.TOOrder;
import ca.mcgill.ecse.coolsupplies.controller.TOStudent;

/**
 * GradePageController.java is the starting point for all Grade related operations. It is the controller for GradePage.fxml, and allows the user to view and manage grades.
 * Supported features include viewing all grades, adding a grade, updating a grade, deleting a grade, starting a new school year, and updating the user's password.
 * 
 * Redirection:
 * -Grade.fxml (each individual Grade component is rendered in GradePage.fxml)
 * -AddGrade.fxml (redirects the user to the AddGrade.fxml page)
 * -UpdateGrade.fxml (redirects the user to the UpdateGrade.fxml page)
 * -UpdatePassword.fxml (redirects the user to the UpdatePassword.fxml page)
 * -StudentOfGrade.fxml (each individual StudentOfGrade component of a Grade is rendered in GradePage.fxml)
 * 
 * @author David Vo
 */

public class GradePageController implements Initializable {

    @FXML
    GridPane rightSideGridPane;

    @FXML
    private Button settingsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private VBox chosenGradeCard;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Button addGradeButton;

    @FXML
    private Button updateGradeButton;

    @FXML
    private Button updatePassword;

    @FXML
    private Button startSchoolYear;

    @FXML
    private Label gradeNameLabel;

    @FXML
    private GridPane grid;

    @FXML
    private GridPane grid1;

    @FXML
    private Button gradeSort;

    @FXML
    private ImageView email;

    @FXML
    private ImageView phone;

    @FXML
    private TOGrade selectedGrade; 

    private ArrayList<TOGrade> grades = new ArrayList<>();
    private EventListener listener;
    private AnchorPane lastSelectedCard;

     private enum Sort {
        SYSTEM_DEFAULT,
        NAME_ASCENDING,
        NAME_DESCENDING,
        HAS_BUNDLE
    }
    Sort sort = Sort.SYSTEM_DEFAULT;

    private ArrayList<TOGrade> getData() {
        return new ArrayList<TOGrade>(CoolSuppliesFeatureSet7Controller.getGrades());
    }

    /**
     * Set the chosen grade and highlight the selected card
     * @param grade
     * @param card
     */
    private void setChosenGrade(TOGrade grade, AnchorPane card) {
        selectedGrade = grade; 
        gradeNameLabel.setText("> Students | " + grade.getLevel() + " Grade");

        if (lastSelectedCard != null) {
            lastSelectedCard.getStyleClass().remove("highlight");
            lastSelectedCard.getStyleClass().add("non-highlight");
        }

        card.getStyleClass().remove("non-highlight");
        card.getStyleClass().add("highlight");
        lastSelectedCard = card;

        initializeStudentList(grade.getLevel());
        selectedGrade = grade;
    }

    /**
     * Initialize the page with the grades
     * @param location
     * @param resources
     */
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grades = getData();

        grid.getChildren().clear();
        switch (sort) {
            case SYSTEM_DEFAULT:
                break;
            case NAME_ASCENDING:
                grades.sort((TOGrade g1, TOGrade g2) -> g1.getLevel().toLowerCase().compareTo(g2.getLevel().toLowerCase()));
                break;
            case NAME_DESCENDING:
                grades.sort((TOGrade g1, TOGrade g2) -> g2.getLevel().toLowerCase().compareTo(g1.getLevel().toLowerCase()));
                break;
            case HAS_BUNDLE:
                grades.sort((TOGrade g1, TOGrade g2) -> {
                    boolean g1HasBundle = CoolSuppliesFeatureSet7Controller.getBundleOfGrade(g1.getLevel()) != null;
                    boolean g2HasBundle = CoolSuppliesFeatureSet7Controller.getBundleOfGrade(g2.getLevel()) != null;
    
                    if (g1HasBundle && !g2HasBundle) return -1; 
                    if (!g1HasBundle && g2HasBundle) return 1;  
                    return 0; 
                });
                break;
        }

        if (!grades.isEmpty()) {
            listener = new EventListener() {
                @Override
                public void onClickListener(TOGrade grade) {
                    setChosenGrade(grade, lastSelectedCard);
                }
            };
        }

        grid1.setMinHeight(Region.USE_COMPUTED_SIZE);
        grid1.setPrefHeight(700);
        grid1.setMaxHeight(Region.USE_COMPUTED_SIZE);

        int students = 0;
        String bundleName = null;
        int i = 0;
        for (TOGrade grade : grades) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Grade/Grade.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                if (lastSelectedCard == null) {
                    setChosenGrade(grade, anchorPane);
                    lastSelectedCard = anchorPane;
                }

                students = CoolSuppliesFeatureSet7Controller.getStudentsOfGrade(grade.getLevel()).size();
                if (CoolSuppliesFeatureSet7Controller.getBundleOfGrade(grade.getLevel()) != null) {
                    bundleName = CoolSuppliesFeatureSet7Controller.getBundleOfGrade(grade.getLevel()).getName();
                }
                else {
                    bundleName = null;
                }
                
                GradeController gradeController = fxmlLoader.getController();
                gradeController.setGrade(grade, students, bundleName, listener);

                anchorPane.setOnMouseClicked(event -> {
                    setChosenGrade(grade, anchorPane);
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
        initializeButtonGraphics();
    }

    private void initializeStudentList(String gradeLevel) {
        int i = 0;

        ArrayList<TOStudent> students = new ArrayList<>(CoolSuppliesFeatureSet7Controller.getStudentsOfGrade(gradeLevel));

        grid1.getChildren().clear();

        for (TOStudent student: students) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Grade/StudentOfGrade.fxml")); //??
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
        grid1.setPrefWidth(5000);
        scroll.fitToWidthProperty().set(true);
        scroll.fitToHeightProperty().set(true);
    }

    @FXML
    void switchToAddGrade(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Grade/AddGrade.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();

            stage.setTitle("Add Grade");
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.showAndWait(); 

            grades = getData();
            initialize(null, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switchToUpdateGrade(ActionEvent event) throws IOException {
    if (selectedGrade == null) {
        showAlert(Alert.AlertType.WARNING, "No Grade Selected", "Please select a grade to update.");
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Grade/UpdateGrade.fxml"));
        Parent root = loader.load();

        UpdateGradeController updateGradeController = loader.getController();
        updateGradeController.setSelectedGrade(selectedGrade);

        Stage stage = new Stage();
        stage.setTitle("Update Grade");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        grades = getData();
        initialize(null, null);

    } catch (IOException e) {
        e.printStackTrace();
        }
    }

    @FXML
    void toggleGradeSort(ActionEvent event) {
        switch (sort) {
            case SYSTEM_DEFAULT:
                sort = Sort.NAME_ASCENDING;
                gradeSort.setText("▼     Level");
                break;
            case NAME_ASCENDING:
                sort = Sort.NAME_DESCENDING;
                gradeSort.setText("▲     Level");
                break;
            case NAME_DESCENDING:
                sort = Sort.HAS_BUNDLE;
                gradeSort.setText("▼     Bundle");
                break;
            case HAS_BUNDLE:
                sort = Sort.SYSTEM_DEFAULT;
                gradeSort.setText("▼     Filter");
                break;
        }
        initialize(null, null);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/UpdatePassword.fxml"));
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
        void onClickListener(TOGrade grade);
    }

    private void showAlert(AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void doSwitchToAdminPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/AdminPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gradeNameLabel.getScene().getWindow();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doSwitchToOrderPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Order/OrderPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gradeNameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doSwitchToInventoryPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/InventoryPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gradeNameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doSwitchToBundlePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/BundlePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gradeNameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/LoginPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gradeNameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.setResizable(false);
            stage.setTitle("CoolSupplies");
            stage.setX(100);
            stage.setY(100);
            stage.setHeight(600);
            stage.setWidth(800);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doSwitchToParentPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/ParentPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gradeNameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doSwitchToShowStudentsPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/ShowStudentsPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) gradeNameLabel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeButtonGraphics() {
        ImageView settingsImage = new ImageView("ca/mcgill/ecse/coolsupplies/javafx/resources/settings.png");
        settingsImage.setFitHeight(30);
        settingsImage.setFitWidth(30);
        settingsImage.setEffect(new javafx.scene.effect.ColorAdjust(0, 0, 0.34, 0));

        ImageView logoutImage = new ImageView("ca/mcgill/ecse/coolsupplies/javafx/resources/logout.png");
        logoutImage.setFitHeight(30);
        logoutImage.setFitWidth(30);
        logoutImage.setEffect(new javafx.scene.effect.ColorAdjust(0, 0, 0.34, 0));

        settingsButton.setGraphic(settingsImage);
        settingsButton.setText("");
        settingsButton.setStyle("-fx-background-color: transparent;");
        settingsButton.setPadding(new Insets(0, 8, 0, 0));
        settingsButton.setPrefSize(30, 30);

        logoutButton.setGraphic(logoutImage);
        logoutButton.setText("");
        logoutButton.setStyle("-fx-background-color: transparent;");
        logoutButton.setPadding(new Insets(0, 8, 0, 0));
        logoutButton.setPrefSize(30, 30);
    }
}
