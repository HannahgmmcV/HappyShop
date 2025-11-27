package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product; // added
import ci553.happyshop.utility.ThemeToggleChanger; // added
import ci553.happyshop.utility.*;
import javafx.collections.FXCollections; // added
import javafx.collections.ObservableList; // added
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert; // added
import javafx.scene.control.Alert.AlertType; // added
import javafx.scene.control.ButtonType; // added
import javafx.scene.control.TextArea; // added
import javafx.scene.control.TextField; // added
import javafx.scene.control.Label; // added
import javafx.scene.control.Button; // added

import java.nio.file.Path; // added
import java.nio.file.Paths; // added
import java.util.ArrayList; // added

import java.util.Optional; // added
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


/**
 * The CustomerView is separated into two sections by a line :
 *
 * 1. Search Page – Always visible, allowing customers to browse and search for products.
 * 2. the second page – display either the Trolley Page or the Receipt Page
 *    depending on the current context. Only one of these is shown at a time.
 */

public class CustomerView  {
    public CustomerController cusController;

    private final int WIDTH = UIStyle.customerWinWidth;
    private final int HEIGHT = UIStyle.customerWinHeight;
    private final int COLUMN_WIDTH = WIDTH / 2 - 10;

    // change from private to public
    public HBox hbRoot; // Top-level layout manager

    private VBox vbTrolleyPage;  //vbTrolleyPage and vbReceiptPage will swap with each other when need
    private VBox vbReceiptPage;

    //for user input where search keywords are either the product ID or the product name.
    // Made accessible so it can be accessed or modified by CustomerModel
    // tfId was refactored to tfSearchKeyword, the same name from warehouse view
    TextField tfSearchKeyword;

    // taken from warehouse view
    // shows the search results summary for products found
    private Label laSearchSummary;

    //taken from warehouse view
    // changes and updates the ListView automatically
    private ObservableList<Product> obeProductList; //observable product list
    // displays products from ListView<Product>
    ListView<Product> obrLvProducts; //A ListView observes the product list

    // modified
    //now only two controllers needs updating when program going on
    private TextArea taTrolley; //in trolley Page
    private TextArea taReceipt;//in receipt page

    // Holds a reference to this CustomerView window for future access and management
    // (e.g., positioning the removeProductNotifier when needed).
    private Stage viewWindow;

    public void start(Stage window) {
        VBox vbSearchPage = createSearchPage();
        vbTrolleyPage = CreateTrolleyPage();
        vbReceiptPage = createReceiptPage();

        // Create a divider line
        Line line = new Line(0, 0, 0, HEIGHT);
        line.setStrokeWidth(4);
        line.setStroke(Color.PINK);
        VBox lineContainer = new VBox(line);
        lineContainer.setPrefWidth(4); // Give it some space
        lineContainer.setAlignment(Pos.CENTER);

        // deleted HBox, as it is being called above
        hbRoot = new HBox(10, vbSearchPage, lineContainer, vbTrolleyPage); //initialize to show trolleyPage
        hbRoot.setAlignment(Pos.CENTER);
        hbRoot.setStyle(UIStyle.rootStyle);

        Scene scene = new Scene(hbRoot, WIDTH, HEIGHT);
        window.setScene(scene);
        window.setTitle("🛒 HappyShop Customer Client");
        WinPosManager.registerWindow(window,WIDTH,HEIGHT); //calculate position x and y for this window
        window.show();
        viewWindow=window;// Sets viewWindow to this window for future reference and management.
    }
    // taken from warehouse view
    /* allows a user to type either the product ID or product name.
    * when the search button is clicked or ENTER pressed in the text field, an action is files to the
    *controller to activate the search.
     */
    private VBox createSearchPage() {
        Label laPageTitle = new Label("Search by Product ID/Name");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);
        tfSearchKeyword = new TextField();
        tfSearchKeyword.setPromptText("eg. 0001 or TV");
        tfSearchKeyword.setStyle(UIStyle.textFiledStyle);
        tfSearchKeyword.setOnAction(actionEvent -> {
            try {
                cusController.doAction("🔍");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


        Label laName = new Label("Name:");
        laName.setStyle(UIStyle.labelStyle);

        /* Search button now a magnifying glass, just like from the warehouse search
         */
        Button btnSearch = new Button("🔍");
        btnSearch.setStyle(UIStyle.buttonStyle);
        btnSearch.setOnAction(this::buttonClicked);
        HBox hbId = new HBox(10,  tfSearchKeyword, btnSearch);

        Button btnAddToTrolley = new Button("Add to Trolley");
        btnAddToTrolley.setStyle(UIStyle.buttonStyle);
        btnAddToTrolley.setOnAction(this::buttonClicked);

        // label summary taken from warehouse view
        laSearchSummary = new Label("Search summary");
        laSearchSummary.setStyle(UIStyle.labelStyle);
        HBox hbBtns = new HBox(10, btnAddToTrolley, laSearchSummary);

        // taken from warehouse view
        // create an observable list of Products, tie it to ListView, and then configure the ListView’s size and
        // style for consistent display.
        // data, an observable ArrayList, observed by obrLvProducts
        obeProductList = FXCollections.observableArrayList();
        obrLvProducts = new ListView<>(obeProductList);//ListView proListView observes proList
        obrLvProducts.setPrefHeight(HEIGHT - 100);
        obrLvProducts.setFixedCellSize(50);
        obrLvProducts.setStyle(UIStyle.listViewStyle);

        // taken from warehouse view
        // define a custom cell factory so that each ListView row shows a product image + info by a
        // custom ListCell.
        obrLvProducts.setCellFactory(param -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (empty || product == null) {
                    setGraphic(null);
                    System.out.println("setCellFactory - empty item");
                } else {
                    String imageName = product.getProductImageName(); // Get image name (e.g. "0001.jpg")
                    String relativeImageUrl = StorageLocation.imageFolder + imageName;
                    // Get the full absolute path to the image
                    Path imageFullPath = Paths.get(relativeImageUrl).toAbsolutePath();
                    String imageFullUri = imageFullPath.toUri().toString();// Build the full image Uri

                    ImageView ivPro;
                    try {
                        ivPro = new ImageView(new Image(imageFullUri, 50,45, true,true)); // Attempt to load the product image
                    } catch (Exception e) {
                        // If loading fails, use a default image directly from the resources folder
                        ivPro = new ImageView(new Image("imageHolder.jpg",50,45,true,true)); // Directly load from resources
                    }

                    Label laProToString = new Label(product.toString()); // Create a label for product details
                    HBox hbox = new HBox(10, ivPro, laProToString); // Put ImageView and label in a horizontal layout
                    setGraphic(hbox);  // Set the whole row content
                }
            }
        });
        // modified
        VBox vbSearchPage = new VBox(15, laPageTitle, hbId,  hbBtns, obrLvProducts);
        vbSearchPage.setPrefWidth(COLUMN_WIDTH);
        vbSearchPage.setAlignment(Pos.TOP_CENTER);
        vbSearchPage.setStyle("-fx-padding: 15px;");

        return vbSearchPage;
    }

    private VBox CreateTrolleyPage() {
        Label laPageTitle = new Label("🛒🛒  Trolley 🛒🛒");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        taTrolley = new TextArea();
        taTrolley.setEditable(false);
        taTrolley.setPrefSize(WIDTH/2, HEIGHT-50);

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(this::buttonClicked);
        btnCancel.setStyle(UIStyle.buttonStyle);

        Button btnCheckout = new Button("Check Out");
        btnCheckout.setOnAction(this::buttonClicked);
        btnCheckout.setStyle(UIStyle.buttonStyle);

        HBox hbBtns = new HBox(10, btnCancel,btnCheckout);
        hbBtns.setStyle("-fx-padding: 15px;");
        hbBtns.setAlignment(Pos.CENTER);

        vbTrolleyPage = new VBox(15, laPageTitle, taTrolley, hbBtns);
        vbTrolleyPage.setPrefWidth(COLUMN_WIDTH);
        vbTrolleyPage.setAlignment(Pos.TOP_CENTER);
        vbTrolleyPage.setStyle("-fx-padding: 15px;");
        return vbTrolleyPage;
    }

    private VBox createReceiptPage() {
        Label laPageTitle = new Label("Receipt");
        laPageTitle.setStyle(UIStyle.labelTitleStyle);

        taReceipt = new TextArea();
        taReceipt.setEditable(false);
        taReceipt.setPrefSize(WIDTH/2, HEIGHT-50);

        Button btnCloseReceipt = new Button("OK & Close"); //btn for closing receipt and showing trolley page
        btnCloseReceipt.setStyle(UIStyle.buttonStyle);

        btnCloseReceipt.setOnAction(this::buttonClicked);

        vbReceiptPage = new VBox(15, laPageTitle, taReceipt, btnCloseReceipt);
        vbReceiptPage.setPrefWidth(COLUMN_WIDTH);
        vbReceiptPage.setAlignment(Pos.TOP_CENTER);
        vbReceiptPage.setStyle(UIStyle.rootStyleYellow);
        return vbReceiptPage;
    }


    /*
    Author: Hannah Virgo
    Changes: Added the ability to call a sound to be played once a button is clicked inside the customer View
    Changes: Created an Alert (Window) for when the user clicks onto checkout, where it will ask them if they're sure that they want to check out their trolley,
    if yes, they will get a receipt and their items will be processed, but if they press cancel, they will go straight back to their trolley to add more items.
     */
    private void buttonClicked(ActionEvent event) {
        ButtonSounds.play("/CPWButtonSound.MP3");

        try {
            Button btn = (Button) event.getSource();
            String action = btn.getText();
            if(action.equals("Check Out")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Checkout");
                alert.setHeaderText("Are you sure you want to Checkout your Trolley?");
                alert.setContentText("Click OK to Checkout, or Cancel to return back to Trolley.");

                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK) {
                    // customer has clicked OK to proceed with checkout
                    cusController.doAction(action);
                } else {
                    // customer has clicked Cancel or closed the window and will go back to the trolley page only
                    showTrolleyOrReceiptPage(vbTrolleyPage);
                }
                return;
            }

            // Moved below the alert window to make sure that the receiptPage will only show after the customer has clicked OK to proceed to checkout
            if(action.equals("Add to Trolley")) {
                showTrolleyOrReceiptPage(vbTrolleyPage); // ensure trolleyPage shows if the last customer did not close their receiptPage
            }

            if(action.equals("OK & Close")) {
                showTrolleyOrReceiptPage(vbTrolleyPage);
            }

            cusController.doAction(action);
        }

        catch(SQLException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // taken from warehouse view
    //update the product listVew of searchPage
    void updateObservableProductList(ArrayList<Product> productList) {
        int proCounter = productList.size();
        System.out.println(proCounter);
        laSearchSummary.setText(proCounter + " products found");
        laSearchSummary.setVisible(true);
        obeProductList.clear();
        obeProductList.addAll(productList);
    }
    // modified
    // update the trolley and receipt
    public void update(String trolley, String receipt) {
        taTrolley.setText(trolley);
        if (!receipt.equals("")) {
            showTrolleyOrReceiptPage(vbReceiptPage);
            taReceipt.setText(receipt);
        }
    }
    // added to customer view
    // updates the UI to reflect new data.
    public void update(ArrayList<Product> productList, String trolley, String receipt) {
        int proCounter = productList.size();
        System.out.println(proCounter);
        laSearchSummary.setText(proCounter + " products found");
        laSearchSummary.setVisible(true);
        obeProductList.clear();
        obeProductList.addAll(productList);


        taTrolley.setText(trolley);
        if (!receipt.equals("")) {
            showTrolleyOrReceiptPage(vbReceiptPage);
            taReceipt.setText(receipt);
        }
    }

    // Replaces the last child of hbRoot with the specified page.
    // the last child is either vbTrolleyPage or vbReceiptPage.
    private void showTrolleyOrReceiptPage(Node pageToShow) {
        int lastIndex = hbRoot.getChildren().size() - 1;
        if (lastIndex >= 0) {
            hbRoot.getChildren().set(lastIndex, pageToShow);
        }
    }

    WindowBounds getWindowBounds() {
        return new WindowBounds(viewWindow.getX(), viewWindow.getY(),
                  viewWindow.getWidth(), viewWindow.getHeight());
    }

    // used to register customer view, so that themetogglechanger can apply changes to it.
    public void registerWithToggle(){
        ThemeToggleChanger.registerCustomerView(this);
    }
}
