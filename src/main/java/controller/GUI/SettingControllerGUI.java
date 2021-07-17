package controller.GUI;

import controller.duel.PhaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingControllerGUI {

    public void volumeUp() {
        double currentVolume = LoginControllerGUI.player.getVolume();
        if (currentVolume == 1)
            return;
        LoginControllerGUI.player.setVolume(currentVolume + 0.1);
    }

    public void volumeDown() {
        double currentVolume = LoginControllerGUI.player.getVolume();
        if (currentVolume <= 0)
            return;
        LoginControllerGUI.player.setVolume(currentVolume - 0.1);
    }

    public void mute() {
        LoginControllerGUI.player.setMute(!LoginControllerGUI.player.isMute());
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(SceneController.class.getResource("/fxml/gameField.fxml"));
        Pane pane = fxmlLoader.load();
        stage.setScene(new Scene(pane));
        stage.getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.BACK_QUOTE) {
                FXMLLoader fxmlLoader2 = new FXMLLoader(SceneController.class.getResource("/fxml/cheat.fxml"));
                try {
                    Pane pane2 = fxmlLoader2.load();
                    stage.setScene(new Scene(pane2));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public void surrender(MouseEvent event){
        if (DuelViewSceneController.isMultiPlayer)
            DuelViewSceneController.phaseController.endGame(PhaseController.playerAgainst, PhaseController.playerInTurn, event);
        else
            DuelViewSceneController.gameController.endGame("bot", event);
        LoginControllerGUI.player.stop();
    }
}
