package ci553.happyshop.utility;

import ci553.happyshop.client.customer.CustomerView;
import ci553.happyshop.client.emergency.EmergencyExit;
import ci553.happyshop.client.orderTracker.OrderTracker;
import ci553.happyshop.client.picker.PickerView;
import ci553.happyshop.client.warehouse.WarehouseView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.scene.control.Label;

import java.util.ArrayList;

/**
 * Connects to customer, warehouse and picker view, plus order tracker and emergency exit to change background from light to dark
 *
 * @version final edition
 * @author Hannah Virgo University of Brighton
 */

public class ThemeToggleChanger extends Application {

    // Used an arrayList for all the different views to apply the theme changes dynamically
    // They are all static because they are shared at the class level and not per instance.
    static ArrayList<CustomerView> customerViewlist = new ArrayList<>();
    static ArrayList<PickerView> pickerViewlist = new ArrayList<>();
    static ArrayList<WarehouseView> warehouseViewlist = new ArrayList<>();
    static ArrayList<OrderTracker> orderTrackerlist = new ArrayList<>();
    static ArrayList<EmergencyExit> emergencyExitlist = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Theme Toggle");
        Pane root = new Pane();

        // Creates a hint label for dark mode
        Label hintLabel = new Label("Try Dark Mode!");
        hintLabel.setLayoutX(80);
        hintLabel.setLayoutY(40);
        hintLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 16px;");


        // Toggle button is used for switching back and forth between light and dark theme
        ToggleButton toggle = new ToggleButton("Light");
        toggle.setLayoutX(100);
        toggle.setLayoutY(80);

        // When toggle button is clicked, code will run to switch theme based on its state.
        // Use setOnAction and check isSelected() inside it
        toggle.setOnAction(event -> {
            if (toggle.isSelected()) { // if clicked, switch to dark theme.
                toggle.setText("Dark");
                hintLabel.setText("Try Light Mode!");
                toggle.setStyle("-fx-background-color: grey"); // changes the style colour of Dark button to differentiate from Light button
                System.out.println("Dark mode ON"); // logs dark theme is ON
                //Loop through the views and apply the dark background style to root container
                // call method to switch to dark theme
                for(CustomerView cv: customerViewlist){
                    cv.hbRoot.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: #063840"); // Very dark cyan
                }
                for(PickerView pv: pickerViewlist) {
                    pv.vbOrderMapRoot.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: #DAA520"); // Vivid orange
                    pv.vbOrderDetailRoot.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: #DAA520"); // Vivid orange
                }
                for(WarehouseView wv: warehouseViewlist){
                    wv.hbRoot.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: #FF1261"); // Vivid pink
                }
                for(OrderTracker ot: orderTrackerlist){
                    ot.vbox.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: #6E6E6E"); // Very dark gray
                }
                for(EmergencyExit ev: emergencyExitlist){
                    ev.borderPane.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: #006F6F"); // Very dark cyan
                }

            // If toggle button is not clicked, switch to light theme for all views
            } else {
                toggle.setText("Light");
                toggle.setStyle(""); // this allows the original light button to remain the same, when switching back and forth to Dark button
                hintLabel.setText("Try Dark Mode!");
                System.out.println("Light mode ON"); // logs light theme is ON
                // call method to switch to light theme
                for(CustomerView cv: customerViewlist){
                    cv.hbRoot.setStyle(UIStyle.rootStyle);
                }
                for(PickerView pv: pickerViewlist) {
                    pv.vbOrderMapRoot.setStyle(UIStyle.rootStyleYellow);
                    pv.vbOrderDetailRoot.setStyle(UIStyle.rootStyleBlue);
                }
                for(WarehouseView wv: warehouseViewlist){
                    wv.hbRoot.setStyle(UIStyle.rootStyleWarehouse);
                }
                for(OrderTracker ot: orderTrackerlist){
                    ot.vbox.setStyle(UIStyle. rootStyleGray);
                }
                for(EmergencyExit ev: emergencyExitlist){
                    ev.borderPane.setStyle(UIStyle.rootStyle);
                }
            }
        });

        root.getChildren().addAll(hintLabel, toggle); // Adds hintLabel and toggle button to the root pane

        Scene scene = new Scene(root, 300, 200);

        primaryStage.setScene(scene);
        WinPosManager.registerWindow(primaryStage, 300,200); //calculate position x and y for this window
        primaryStage.show();
    }

    // Methods to register the views, in order to enable light/dark theme switching
    public static void registerCustomerView(CustomerView cusView){
        customerViewlist.add(cusView);
    }
    public static void registerPickerView(PickerView pickerView){
        pickerViewlist.add(pickerView);
    }
    public static void registerWarehouseView(WarehouseView warehouseView){
        warehouseViewlist.add(warehouseView);
    }
    public static void registerOrderTracker(OrderTracker orderTracker){
        orderTrackerlist.add(orderTracker);
    }
    public static void registerEmergencyExit(EmergencyExit emergencyExit){
        emergencyExitlist.add(emergencyExit);
    }

    // opens the themetogglechanger from anywhere in the application.
    public static void openToggleWindow() {
        new ThemeToggleChanger().start(new Stage());
    }

    // Used for testing the Main method independently
    public static void main(String[] args) {
        launch(args);
    }
}