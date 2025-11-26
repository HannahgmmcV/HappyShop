package ci553.happyshop.client.emergency;

import ci553.happyshop.utility.ButtonSounds; // added
import ci553.happyshop.utility.ThemeToggleChanger; // added
import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import javafx.animation.PauseTransition; // added
import javafx.util.Duration; // added
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The class EmergencyExit used to immediately shut down the entire application.
 * It is a singleton with static access, instantiation is restricted.
 */
public class EmergencyExit {
    // created from private to public
    public BorderPane borderPane= new BorderPane(); // allows UIstyle to become public for themetogglechanger

    private final int WIDTH = UIStyle.EmergencyExitWinWidth;
    private final int HEIGHT = UIStyle.EmergencyExitWinHeight;
    private static EmergencyExit emergencyExit;

    //used by Main class to get the single instance
    public static EmergencyExit getEmergencyExit() {
        if (emergencyExit == null)
            emergencyExit = new EmergencyExit();
        return emergencyExit;
    }

    //Private constructor creates a shutdown window.
    //The window displays a single button with a shutdown image,positioned via `WinPosManager`,
    private EmergencyExit() {
        ImageView ivExit = new ImageView("ShutDown.jpg");
        ivExit.setFitWidth(WIDTH-100);
        ivExit.setFitHeight(WIDTH-100);
        ivExit.setPreserveRatio(true);

    /*
    Author: Hannah Virgo
    Changes: Added the ability to call a sound to be played once the exit button is clicked inside the emergency package.
    Changes: Also includes a pause transition, duration and delay that allows the button to be delayed for 0.7 secs, in order to allow the shutdown.mp3 to be heard without being cut off.
    */
        Button btnExit = new Button();
        btnExit.setGraphic(ivExit);
        btnExit.setOnAction(event -> {
            ButtonSounds.play("/Shutdown.mp3");
            PauseTransition delay = new PauseTransition(Duration.seconds(0.7)); // Allows a delay to happen, in order for the sound to be fully heard before exiting
            delay.setOnFinished(e -> { // After the pause transition, this triggers the application to exit
                Platform.exit(); // Gracefully exit JavaFX
                System.exit(0); // Forcefully shut down JVM (in case there are non-JavaFX threads)
            });
            delay.play(); // Calls the delay to come into effect
        });

        borderPane.setCenter(btnExit);

        borderPane.setStyle(UIStyle.rootStyle);
        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
        Stage window = new Stage();
        window.setScene(scene);
        window.setTitle("🛒 EXIT");
        WinPosManager.registerWindow(window,WIDTH,HEIGHT); //calculate position x and y for this window
        window.show();
    }
    // used to register emergency exit, so that themetogglechanger can apply changes to it.
    public  void registerWithToggle(){
        ThemeToggleChanger.registerEmergencyExit(this);
    }

}
