package controller.GUI;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    private SceneController() {}

    public static void switchScene(String sceneName, ActionEvent inputEvent) throws IOException {
        Stage stage = (Stage) ((Node)inputEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(sceneName));
        Pane pane = fxmlLoader.load();
        stage.setScene(new Scene(pane));
    }

    public static void switchSceneMouse(String sceneName, MouseEvent inputEvent) throws IOException {
        Stage stage = (Stage) ((Node)inputEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource(sceneName));
        Pane pane = fxmlLoader.load();
        stage.setScene(new Scene(pane));
    }
}
