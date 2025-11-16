package ci553.happyshop.utility;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ThemeToggleChanger {

    public static void openToggleWindow() {
        Stage toggleStage = new Stage();
        toggleStage.setTitle("Theme Toggle Window");
        Pane root = new Pane();
        Scene scene = new Scene(root, 300, 200);
        toggleStage.setScene(scene);
        toggleStage.show();
    }
}
