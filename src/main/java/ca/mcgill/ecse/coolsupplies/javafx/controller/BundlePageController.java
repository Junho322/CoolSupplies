package ca.mcgill.ecse.coolsupplies.javafx.controller;

import ca.mcgill.ecse.coolsupplies.application.CoolSuppliesApplication;
import ca.mcgill.ecse.coolsupplies.controller.*;
import ca.mcgill.ecse.coolsupplies.javafx.pages.AdminPageController;
import ca.mcgill.ecse.coolsupplies.model.CoolSupplies;
import ca.mcgill.ecse.coolsupplies.model.GradeBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code BundlePageController} class is the controller for managing the bundle-related user interactions in the
 * CoolSupplies JavaFX application. It provides functionality to add, edit, delete, and manage grade bundles in the system.
 *
 * <li>Initialize UI components, populate dropdowns, and birth event listeners upon page loading.</li>
 * <li>Add new bundles by specifying a name, discount, and grade.</li>
 * <li>Edit existing bundles with validation for fields like discount and grade selection.</li>
 * <li>Delete selected bundles with a confirmation step to prevent accidental deletions.</li>
 * <li>Switch between different pages such as Admin Page, Order Page, Inventory Page, Grade Page, and more.</li>
 * <li>Handle user logouts and UI transitions with smooth navigation between scenes.</li>
 * <li>Ensure data integrity by validating bundle discounts and refreshing lists</li>
 * </ul>
 * <p>
 * The lifecycle of this controller starts whenever this page is loaded from another controller and ends when it is exited.
 * This page links with {@code BundleItemPageController} and passes relevant information with TOs via a controller method that
 * launches the new page, calls its controller and passes the information in it.
 * <p>
 * The BundlePageController is automatically instantiated and managed by JavaFX when the associated FXML file
 * is loaded. The developer does not need to manually create or manage an instance of this controller.
 *
 * @author David Wang (dyune)
 */

public class BundlePageController {

    private Stage stage;

    private Scene scene;

    private Parent root;

    @FXML
    private Button settingsButton;

    @FXML
    private Button logoutButton;

    private final ArrayList<String> discountApplyChoices = new ArrayList<>();

    private CoolSupplies coolSupplies = CoolSuppliesApplication.getCoolSupplies();

    private String selectedBundleName;

    // Fields linked to FXML elements
    @FXML
    private TextField nameField;

    @FXML
    private TextField discountField;

    @FXML
    private ComboBox<String> gradeChoice;

    @FXML
    private TextField editBundle;

    @FXML
    private ComboBox<String> editGrade;

    @FXML
    private TextField editDiscount;

    @FXML
    private Button buttonToEdit;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<String> listView;

    private ObservableList<String> bundles = FXCollections.observableArrayList();

    /**
     * Initializes the page and loads all elements with data.
     *
     * @author David Wang
     */
    @FXML
    private void initialize() {
        // Initialize the list with any preexisting bundles
        populateListView();

        // Set styles for ComboBox to be readable
        gradeChoice.setStyle("-fx-background-color: #006EA6; " +
                "-fx-background-radius: 10; " +
                "-fx-text-base-color: white;");
        editGrade.setStyle("-fx-background-color: #006EA6; " +
                "-fx-background-radius: 10; " +
                "-fx-text-base-color: white;");

        // Set all dropdown menu values
        ArrayList<TOGrade> grades = new ArrayList<>();
        ArrayList<String> gradeNames = new ArrayList<>();
        grades.addAll(CoolSuppliesFeatureSet7Controller.getGrades());
        for (TOGrade grade : grades) {
            gradeNames.add(grade.getLevel());
        }
        gradeChoice.getItems().addAll(gradeNames);
        editGrade.getItems().addAll(gradeNames);

        // Add a listener for bundle selection
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                handleBundleSelection(newVal);
            }
        });
        initializeButtonGraphics();

        // Ensure all bundle discounts are valid
        refreshAllBundleDiscounts();
    }

    /**
     * Handles Add button event to add a bundle to the CoolSupplies system
     *
     * @author David Wang
     */
    @FXML
    private void handleAddButton() {
        String name = nameField.getText();
        String discount = discountField.getText();
        String grade = gradeChoice.getValue();

        if (name.isEmpty() || discount.isEmpty() || grade == null) {
            showErrorAlert("Error", "All fields must be filled to add a bundle.");
            return;
        }

        try {
            int discountValue = Integer.parseInt(discount);

            String result = CoolSuppliesFeatureSet4Controller.addBundle(name, discountValue, grade);
            if (!result.equals("GradeBundle added successfully.")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(result);
                alert.showAndWait();
                return;
            }
            bundles.add(name);

            System.out.println("Bundle added: " + name);

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid discount value. Enter a number.");
            alert.showAndWait();
        } finally {
            nameField.clear();
            discountField.clear();
            populateListView();
        }
    }

    /**
     * Handles Edit button event to pass to the linked page of BundleItemPage
     *
     * @param event is triggered by user interaction to determine the source of the event
     * @throws IOException occurs whenever a resource cannot be loaded properly, in this case, the FXML file for the
     *                     linked page
     * @author David Wang
     */
    @FXML
    private void handleEditItems(ActionEvent event) throws IOException {
        // Correctly initialize the FXMLLoader with the resource location
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/BundleItemPage.fxml"));
        Parent root = fxmlLoader.load();

        // Get the controller and pass data to it
        BundleItemPageController passer = fxmlLoader.getController();
        passer.getBundleName(selectedBundleName);

        // Set up the scene and stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../resources/styles.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }


    /**
     * Responsible for validating update bundle input fields to then update a bundle if all is valid
     *
     * @author David Wang
     */
    @FXML
    private void handleSaveButton() {
        TOGradeBundle oldBundle = CoolSuppliesFeatureSet4Controller.getBundle(selectedBundleName);
        String oldName = oldBundle.getName();
        String oldDiscount = String.valueOf(oldBundle.getDiscount());
        String oldGrade = oldBundle.getGradeLevel();
        String newName = editBundle.getText();
        String newDiscount = editDiscount.getText();
        String newGrade = editGrade.getValue();

        if (newName.isEmpty() || newDiscount.isEmpty() || newGrade == null) {
            showErrorAlert("Error", "All fields must be filled to edit a bundle.");
            return;
        }

        try {
            int discountValue = Integer.parseInt(newDiscount);
            String result = CoolSuppliesFeatureSet4Controller.updateBundle(
                    selectedBundleName,
                    newName,
                    Integer.parseInt(newDiscount),
                    newGrade
            );
            if (!result.equals("Successfully updated Bundle: " + selectedBundleName)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(result);
                alert.showAndWait();
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid discount value. Enter a number.");
            alert.showAndWait();
        } finally {
            editBundle.clear();
            editDiscount.clear();
            populateListView();

        }
    }

    /**
     * Handles Delete button event to delete a selected bundle if all is valid
     *
     * @author David Wang
     */
    @FXML
    private void handleDeleteButton(ActionEvent event) {

        // Create a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Delete Bundle");
        confirmationAlert.setContentText("Are you sure you want to delete the bundle?");

        // Wait for the user's response
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println(selectedBundleName + " deleted.");
                String result = CoolSuppliesFeatureSet4Controller.deleteBundle(selectedBundleName);
                populateListView();
                showSuccessAlert("Success", result);
                selectedBundleName = null;
                editBundle.clear();
                editDiscount.clear();
                editGrade.setValue("");

            } else {
                // User canceled the action
                System.out.println("Deletion canceled by the user.");
            }
        });
    }

    /**
     * Displays the selected item with an event listener
     *
     * @author David Wang
     */
    private void handleBundleSelection(String selected) {
        selectedBundleName = selected;
        System.out.println("Selected bundle: " + selected);
        TOGradeBundle selectedBundle = CoolSuppliesFeatureSet4Controller.getBundle(selected);
        editBundle.setText(selectedBundle.getName());
        editDiscount.setText(String.valueOf(selectedBundle.getDiscount()));
        editGrade.setValue(selectedBundle.getGradeLevel());
    }

    /**
     * Helper method to populate the listview with all bundles from the system
     *
     * @author David Wang
     */
    private void populateListView() {
        listView.getItems().clear();
        List<TOGradeBundle> allBundles = CoolSuppliesFeatureSet4Controller.getBundles();
        System.out.println(allBundles);
        for (TOGradeBundle bundle : allBundles) {
            bundles.add(bundle.getName());
        }
        listView.setItems(bundles);
    }

    /**
     * Helper method that refreshes all bundle discounts in the system
     *
     * @author David Wang
     */
    private void refreshAllBundleDiscounts() {
        for (String bundleName : bundles) {
            refreshSelectedBundleDiscount(bundleName);
        }
    }

    private void refreshSelectedBundleDiscount(String bundleName) {
        GradeBundle bundle = (GradeBundle) GradeBundle.getWithName(bundleName);
        if (bundle.getBundleItems().size() == 1) {
            bundle.setDiscount(0);
        }
    }


    /**
     * Helper method to show an error modal
     *
     * @author David Wang
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Helper method to show a success modal
     *
     * @author David Wang
     */
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * All following methods are relevant for navigation between pages, each method is
     * associated to a specific page, for example: {@code doSwitchToAdminPage} will load the
     * admin page.
     *
     * @author Jack McDonald
     */

    @FXML
    void doSwitchToAdminPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/AdminPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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
            Stage stage = (Stage) nameField.getScene().getWindow();
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
            Stage stage = (Stage) nameField.getScene().getWindow();
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
    void doSwitchToParentPage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/ParentPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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
            Stage stage = (Stage) nameField.getScene().getWindow();
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
    void doSwitchToGradePage(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ca/mcgill/ecse/coolsupplies/javafx/pages/Grade/GradePage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nameField.getScene().getWindow();
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
            Stage stage = (Stage) nameField.getScene().getWindow();
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
