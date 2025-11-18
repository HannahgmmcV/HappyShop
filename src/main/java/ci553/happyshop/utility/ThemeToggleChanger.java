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

import java.util.ArrayList;

public class ThemeToggleChanger extends Application {

    static ArrayList<CustomerView> customerViewlist = new ArrayList<>();
    static ArrayList<PickerView> pickerViewlist = new ArrayList<>();
    static ArrayList<WarehouseView> warehouseViewlist = new ArrayList<>();
    static ArrayList<OrderTracker> orderTrackerlist = new ArrayList<>();
    static ArrayList<EmergencyExit> emergencyExitlist = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Theme Toggle Window");
        Pane root = new Pane();

        ToggleButton toggle = new ToggleButton("Light"); // initial text
        toggle.setLayoutX(100);
        toggle.setLayoutY(80);

        // Use setOnAction and check isSelected() inside it
        toggle.setOnAction(event -> {
            if (toggle.isSelected()) {
                toggle.setText("Dark");
                System.out.println("Dark mode ON");
                // call your method to switch to dark theme
                for(CustomerView cv: customerViewlist){
                    cv.hbRoot.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: black");
                }
                for(PickerView pv: pickerViewlist) {
                    pv.vbOrderMapRoot.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: black");
                    pv.vbOrderDetailRoot.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: black");
                }
                for(WarehouseView wv: warehouseViewlist){
                    wv.hbRoot.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: black");
                }
                for(OrderTracker ot: orderTrackerlist){
                    ot.vbox.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: black");
                }
                for(EmergencyExit ev: emergencyExitlist){
                    ev.borderPane.setStyle("-fx-padding: 8px; " +
                            "-fx-background-color: black");
                }


            } else {
                toggle.setText("Light");
                System.out.println("Light mode ON");
                // call your method to switch to light theme
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

        root.getChildren().add(toggle);

        Scene scene = new Scene(root, 300, 200);
        //scene.getStylesheets().add("dark-theme.css");

        primaryStage.setScene(scene);
        WinPosManager.registerWindow(primaryStage, 300,200); //calculate position x and y for this window
        primaryStage.show();
    }

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

    public static void openToggleWindow() {
        new ThemeToggleChanger().start(new Stage());
    }

    // comment out once theme is complete
    public static void main(String[] args) {
        launch(args);
    }
}