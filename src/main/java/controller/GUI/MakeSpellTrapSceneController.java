package controller.GUI;

import controller.menucontroller.MakeCardController;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MakeSpellTrapSceneController implements Initializable {

    MakeCardController makeCardController = new MakeCardController();

    public Label pullImageLabel;
    public TextField cardName = null;
    public ImageView cardImage = null;
    public TextArea cardDescription = null;
    public CheckBox killMonsterCheckBox;
    public CheckBox killSpellCheckBox;
    public CheckBox increaseLPCheckBox;
    public CheckBox decreaseLPCheckBox;
    public Label statusLabel;
    public Label priceLabel;
    public ToggleButton isTrapToggleButton;
    public ChoiceBox<String> iconChoiceBox;
    public ToggleButton isUnlimitedToggleButton;

    public void handleDroppedImage(DragEvent dragEvent) {
        List<File> files = dragEvent.getDragboard().getFiles();
        try {
            Image image = new Image(new FileInputStream(files.get(files.size() - 1)));
            cardImage.setImage(image);
            pullImageLabel.setText("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles())
            dragEvent.acceptTransferModes(TransferMode.ANY);
    }

    public void calculate() {
        CheckBox[] effects = {killMonsterCheckBox, killSpellCheckBox, increaseLPCheckBox, decreaseLPCheckBox};
        priceLabel.setText(makeCardController.calculateSpellTrapPrice(isTrapToggleButton, isUnlimitedToggleButton, effects));
    }

    public void makeSpellTrap() {
        CheckBox[] effects = {killMonsterCheckBox, killSpellCheckBox, increaseLPCheckBox, decreaseLPCheckBox};
        if (makeCardController.doesHaveProblemMakingSpellTrap(cardImage, cardName, cardDescription,isTrapToggleButton,
                isUnlimitedToggleButton, effects) != null) {
            statusLabel.setText(makeCardController.doesHaveProblemMakingSpellTrap(cardImage, cardName, cardDescription,isTrapToggleButton,
                    isUnlimitedToggleButton, effects));
            return;
        }
        makeCardController.makeSpellTrapCard(cardName, cardDescription, cardImage, isTrapToggleButton, isUnlimitedToggleButton
                , effects, iconChoiceBox);
        statusLabel.setText("Monster is made successfully");
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/Make_CardScene.fxml", actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String[] icons = {"Equip", "Quick-Play", "Ritual", "Continuous", "Counter", "Normal"};
        iconChoiceBox.getItems().addAll(icons);
        iconChoiceBox.setValue("Normal");
    }
}
