package controller.GUI;

import controller.menucontroller.CheatMenuController;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import view.GUI.AlertBox;
import view.StatusEnum;

import java.io.IOException;

public class CheatControllerGUI {
    public TextField cheatBox;

    private final CheatMenuController cheatMenuController = new CheatMenuController();

    public void commitCheat(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String cheatCommand = cheatBox.getText();
            String res = cheatMenuController.run(cheatCommand);
            if (cheatCommand.equals("")) {
                AlertBox.display("Please write a cheat command!");
            } else {
                if (res.equals(StatusEnum.INVALID_COMMAND.getStatus())) {
                    AlertBox.display("Please write a valid cheat code!");
                } else {
                    AlertBox.display("cheat activated");
                }
                cheatBox.clear();
            }
        }

    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/gameField.fxml", actionEvent);
    }
}
