package ca.mcgill.ecse.coolsupplies.javafx.pages;

import ca.mcgill.ecse.coolsupplies.controller.*;
import ca.mcgill.ecse.coolsupplies.javafx.controller.BundlePageController;
import ca.mcgill.ecse.coolsupplies.javafx.controller.EventListenerParent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.EventListenerStudent;
import ca.mcgill.ecse.coolsupplies.javafx.controller.InventoryPageController;
import ca.mcgill.ecse.coolsupplies.javafx.pages.Grade.GradePageController;
import ca.mcgill.ecse.coolsupplies.javafx.pages.Order.OrderPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StudentPageController implements Initializable {

    @FXML
    private GridPane grid;

    @FXML
    private GridPane grid1;

    @FXML
    private Button settingsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button registerStudent;

    @FXML
    private GridPane rightSideGridPane;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label studentParentName;

    @FXML
    private Button studentSort;

    private EventListenerStudent listener;
    private AnchorPane lastSelectedCard;
    private ArrayList<TOStudent> students;
    private TOStudent chosenStudent;
    private enum Sort {SYSTEM_DEFAULT, NAME_ASCENDING, NAME_DESCENDING}
    Sort sort = Sort.SYSTEM_DEFAULT;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        students = getData();

        grid.getChildren().clear();
        switch (sort) {
            case SYSTEM_DEFAULT:
                break;
            case NAME_ASCENDING:
                students.sort((TOStudent s1, TOStudent s2) -> s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase()));
                break;
            case NAME_DESCENDING:
                students.sort((TOStudent s1, TOStudent s2) -> s2.getName().toLowerCase().compareTo(s1.getName().toLowerCase()));
                break;
        }

        if (!students.isEmpty()) {
            listener = new EventListenerStudent() {
                @Override
                public void onClickListener(TOStudent student) {
                    setChosenStudent(student, lastSelectedCard);
                }
            };
        }

        //set grid height
        grid1.setMinHeight(Region.USE_COMPUTED_SIZE);
        grid1.setPrefHeight(700);
        grid1.setMaxHeight(Region.USE_COMPUTED_SIZE);

        int i = 0;
        for (TOStudent student : students) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("StudentCard.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                if (lastSelectedCard == null) {
                    setChosenStudent(student, anchorPane);
                    lastSelectedCard = anchorPane;
                }

                StudentCardController studentCardController = fxmlLoader.getController();
                studentCardController.setStudent(student, student.getGradeLevel(), listener);

                anchorPane.setOnMouseClicked(event -> {
                    setChosenStudent(student, anchorPane);
                });

                grid.add(anchorPane, 0, i);
                i++;

                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(485);
                grid.setMaxWidth(Region.USE_COMPUTED_SIZE);

                //set grid height
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

    private void initializeButtonGraphics() {
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

    private ArrayList<TOStudent> getData() {
        return new ArrayList<>(CoolSuppliesFeatureSet2Controller.getStudents());
    }

    @FXML
    void doDeleteStudent(ActionEvent event) {
        try {
            CoolSuppliesFeatureSet2Controller.deleteStudent(chosenStudent.getName());
            initialize(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doLogout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/LoginPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
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
    }

    @FXML
    void doRegisterStudent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/RegisterStudent.fxml"));
            Parent root1 = loader.load();
            Stage stage = new Stage();

            UpdateStudentController.setExistingName(chosenStudent);

            stage.setTitle("Register Student");
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            students = getData();
            initialize(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doToggleStudentSort(ActionEvent event) {
        switch (sort) {
            case SYSTEM_DEFAULT:
                sort = Sort.NAME_ASCENDING;
                studentSort.setText("▼     Name");
                break;
            case NAME_ASCENDING:
                sort = Sort.NAME_DESCENDING;
                studentSort.setText("▲     Name");
                break;
            case NAME_DESCENDING:
                sort = Sort.SYSTEM_DEFAULT;
                studentSort.setText("▼     Filter");
                break;
        }
        initialize(null, null);
    }

    @FXML
    void doUpdateStudent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/UpdateStudent.fxml"));

            UpdateStudentController.setExistingName(chosenStudent);

            Parent root1 = loader.load();
            Stage stage = new Stage();

            stage.setTitle("Update Student");
            stage.setResizable(false);
            stage.setScene(new Scene(root1));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            students = getData();
            initialize(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setChosenStudent(TOStudent student, AnchorPane card) {
        studentNameLabel.setText("> " + student.getName());
        if (CoolSuppliesFeatureSet1Controller.getParentFromStudentName(student.getName()) != null) {
            studentParentName.setText("Parent: " + CoolSuppliesFeatureSet1Controller.getParentFromStudentName(student.getName()).getName());
        } else {
            studentParentName.setText("Parent: N/A");
        }

        if (lastSelectedCard != null) {
            lastSelectedCard.getStyleClass().remove("highlight");
            lastSelectedCard.getStyleClass().add("non-highlight");
        }
        card.getStyleClass().remove("non-highlight");
        card.getStyleClass().add("highlight");
        lastSelectedCard = card;

        initalizeOrderList(student.getName());
        chosenStudent = student;
    }

    private void initalizeOrderList(String name) {
        ArrayList<TOOrder> orders = new ArrayList<>(CoolSuppliesFeatureSet8Controller.getOrders());

        grid1.getChildren().clear();

        int i = 0;
        for (TOOrder order : orders) {
            if (order.getStudentName().equals(name)) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("OrderCard.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();

                    OrderCardController orderCardController = fxmlLoader.getController();
                    orderCardController.setOrder(order);

                    anchorPane.setMinWidth(scroll.getWidth() - 17);
                    anchorPane.setMaxWidth(scroll.getWidth() - 17);
                    orderCardController.setSize(scroll.getWidth() - 245);

                    anchorPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    GridPane.setVgrow(anchorPane, Priority.NEVER);
                    grid1.add(anchorPane, 0, i);
                    i++;

                    GridPane.setMargin(anchorPane, new Insets(3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            grid1.setPrefWidth(grid1.getScene().getWidth());
            scroll.fitToWidthProperty().set(true);
            scroll.fitToHeightProperty().set(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doSwitchToAdminPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/AdminPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) registerStudent.getScene().getWindow();
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

    @FXML
    void doSwitchToOrderPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Order/OrderPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) registerStudent.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setTitle("CoolSupplies");
        stage.setWidth(stage.getMaxWidth());
        stage.setHeight(stage.getMaxHeight());
        stage.show();
        OrderPageController controller = loader.getController();
        controller.initialize(null, null);
    }

    @FXML
    void doSwitchToInventoryPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/InventoryPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) registerStudent.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setTitle("CoolSupplies");
        stage.setWidth(stage.getMaxWidth());
        stage.setHeight(stage.getMaxHeight());
        stage.show();
        InventoryPageController controller = loader.getController();
        controller.initialize(null, null);
    }

    @FXML
    void doSwitchToBundlePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/BundlePage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) registerStudent.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setTitle("CoolSupplies");
        stage.setWidth(stage.getMaxWidth());
        stage.setHeight(stage.getMaxHeight());
        stage.show();
        BundlePageController controller = loader.getController();
//        controller.initialize(null, null);
    }

    @FXML
    void doSwitchToGradePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Grade/GradePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerStudent.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setResizable(true);
            stage.setTitle("CoolSupplies");
            stage.setWidth(stage.getMaxWidth());
            stage.setHeight(stage.getMaxHeight());
            stage.show();
            GradePageController controller = loader.getController();
            controller.initialize(null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doSwitchToShowStudentsPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowStudentsPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) registerStudent.getScene().getWindow();
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
}
