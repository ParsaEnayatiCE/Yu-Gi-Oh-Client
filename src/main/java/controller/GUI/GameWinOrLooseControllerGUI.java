package controller.GUI;

import javafx.event.ActionEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

public class GameWinOrLooseControllerGUI {
    public void returnToMain(ActionEvent actionEvent) throws IOException {

        String musicFile = "./src/main/resources/sound/main2.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        LoginControllerGUI.player = new MediaPlayer(sound);
        LoginControllerGUI.player.setCycleCount(MediaPlayer.INDEFINITE);
        LoginControllerGUI.player.play();
        SceneController.switchScene("/fxml/mainMenu.fxml", actionEvent);
    }
}
