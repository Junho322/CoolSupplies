package ca.mcgill.ecse.coolsupplies.javafx.controller;
import ca.mcgill.ecse.coolsupplies.controller.TOOrder;
import ca.mcgill.ecse.coolsupplies.controller.TOOrderItem;
import ca.mcgill.ecse.coolsupplies.javafx.controller.OrderPageController.EventListener;
import java.util.Date;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class OrderController {

    // @FXML
    // private Label orderLabel;

    @FXML
    private Label parentEmaiLabel;

    @FXML
    private Label studentNameLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label orderNumberLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label levelLabel;

    @FXML
    private Label authorizationCodeLabel;

    @FXML
    private Label penaltyAuthorizationCodeLabel;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label numberOfItemsLabel;

    // @FXML
    // private Label bundleLabel;

    @FXML
    private void click(MouseEvent event) {
        listener.onClickListener(order);
    }

    private TOOrder order;
    private EventListener listener;

    private String parentEmail;
    private String studentName;
    private String status;
    private int number;
    private Date date;
    private String level;
    private String authorizationCode;
    private String penaltyAuthorizationCode;
    private double totalPrice;
    private List<TOOrderItem> items;

    public void setOrder(TOOrder order, EventListener listener) {
        this.order = order;
        this.parentEmail = order.getParentEmail();
        this.studentName = order.getStudentName();
        this.status = order.getStatus();
        this.number = order.getNumber();
        this.date = order.getDate();
        this.level = order.getLevel();
        this.items = order.getItems();
        //int numItems = items.size();
        // System.out.println("items" + items);
        // System.out.println("numItems" + numItems);
        int numItems = 0;
        for (TOOrderItem item : items) {
            numItems += item.getQuantity();
            double discount = 0;

            if (item.getDiscount() == null || item.getDiscount().equals("") || item.getDiscount().equals("0")) {
                discount = 0;
            }
            else {
                discount = Math.abs(Double.parseDouble(item.getDiscount()));
            }

            totalPrice += ( item.getPrice()- discount ) * item.getQuantity();
        }
        this.listener = listener;

        orderNumberLabel.setText("#" + number);
        parentEmaiLabel.setText(parentEmail);
        studentNameLabel.setText(studentName);
        statusLabel.setText(status);
        dateLabel.setText(date.toString());
        levelLabel.setText(level);

        totalPriceLabel.setText("$" + totalPrice);


        if (numItems != 1) {
            numberOfItemsLabel.setText(numItems + " items");
        }
        else {
            numberOfItemsLabel.setText(numItems + " item");
        }

    }
}