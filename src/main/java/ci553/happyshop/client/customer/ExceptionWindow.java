package ci553.happyshop.client.customer;

import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WindowBounds;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class provides a simple exception window to display a list of events (notes down under payments or over quantity of items).
 *
 * - The scene is created only once to avoid unnecessary scene recreation.
 * - The window is created only when needed. If the window is already visible, it will not be recreated.
 * - The exception text is updated dynamically in the TextArea when new data is provided.
 * - The window is positioned relative to the customer window for a consistent UI experience.
 *
 * This design ensures that the exception view is efficient by not recreating the scene and only displaying the window when required.
 *
 * @version final edition
 * @author Hannah Virgo University of Brighton
 */



public class ExceptionWindow {
    private static int WIDTH = UIStyle.HistoryWinWidth;
    private static int HEIGHT = UIStyle.HistoryWinHeight;

    public CustomerView cusView;
    private  Stage window = new Stage();
    private  Scene scene;
    private  TextArea taExceptionMsg;

    // Create the scene only once (to avoid recreating it multiple times)
    private  void createScene() {
        // a TextArea to show stock management history
        taExceptionMsg = new TextArea();
        taExceptionMsg.setPrefSize(150,150);
        taExceptionMsg.setEditable(false);
        taExceptionMsg.setStyle(UIStyle.textFiledStyle);
        VBox vbHistory = new VBox(taExceptionMsg);
        scene = new Scene(vbHistory,WIDTH,HEIGHT);
    }

    // Create the window only when needed (i.e., when the window is not created or closed by user but we need it again)
    private  void createWindow(){
        if (scene == null) {
            createScene(); // create the scene only once
        }

        window = new Stage();
        window.setScene(scene);
        window.setTitle("\uD83C\uDFEC Why Checkout is rejected"); // for icon 🏬
        window.show();
        //get the bounds of warehouse window which trigers the history window
        //so that we can put the history window next to the warehouse window
        WindowBounds bounds = cusView.getWindowBounds();
        window.setX(bounds.x + bounds.width - 20);
        window.setY(bounds.y); // align vertically
    }

    public  void showExceptionMsg(String msg){
        if(window ==null ||!window.isShowing() ) {
            createWindow();  // Only create window if it's not created or unvisible
        }

        taExceptionMsg.setText(msg.toString());
        System.out.println(msg.toString());
    }
}

