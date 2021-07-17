package controller.GUI;

import javafx.event.ActionEvent;

import java.io.IOException;


public class MakeCardSceneController {

    public void makeMonster(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/Make_MonsterScene.fxml", actionEvent);
    }

    public void makeSpellTrap(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/Make_SpellTrapScene.fxml", actionEvent);
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/mainMenu.fxml", actionEvent);
    }
}
