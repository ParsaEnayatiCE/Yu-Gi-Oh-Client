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

public class MakeMonsterSceneController implements Initializable {
    MakeCardController makeCardController = new MakeCardController();
    private Image image = null;

    public Label pullImageLabel;
    public ImageView cardImage;
    public TextField cardName;
    public TextArea cardDescription;
    public Spinner<Integer> attackPointSpinner;
    public Spinner<Integer> defensePointSpinner;
    public ChoiceBox<String> attributeChoiceBox;
    public ChoiceBox<String> monsterTypeChoiceBox;
    public Label cardTypeLabel;
    public Label levelLabel;
    public Label priceLabel;
    public Label statusLabel;
    public CheckBox killMonsterCheckBox;
    public CheckBox killSpellCheckBox;
    public CheckBox increaseLPCheckBox;
    public CheckBox decreaseLPCheckBox;
    public CheckBox increaseAttackCheckBox;
    public CheckBox increaseDefenseCheckBox;

    public void handleDroppedImage(DragEvent dragEvent) {
        List<File> files = dragEvent.getDragboard().getFiles();
        try {
            image = new Image(new FileInputStream(files.get(files.size() - 1)));
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

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/Make_CardScene.fxml", actionEvent);
    }

    public void calculate() {
        CheckBox[] effects = {killMonsterCheckBox, killSpellCheckBox, increaseLPCheckBox, decreaseLPCheckBox,
                increaseAttackCheckBox, increaseDefenseCheckBox};
        levelLabel.setText(makeCardController.calculateMonsterLevel(attackPointSpinner, defensePointSpinner, effects));
        priceLabel.setText(makeCardController.calculateMonsterPrice(attackPointSpinner, defensePointSpinner, effects));
        cardTypeLabel.setText(makeCardController.calculateMonsterCardType(effects));
    }

    public void makeMonster() {
        CheckBox[] effects = {killMonsterCheckBox, killSpellCheckBox, increaseLPCheckBox, decreaseLPCheckBox,
                increaseAttackCheckBox, increaseDefenseCheckBox};
        if (makeCardController.doesHaveProblemMakingMonster(image, cardName, cardDescription, attackPointSpinner,
                defensePointSpinner, effects) != null) {
            statusLabel.setText(makeCardController.doesHaveProblemMakingMonster(image, cardName, cardDescription, attackPointSpinner,
                    defensePointSpinner, effects));
            return;
        }
        makeCardController.makeMonsterCard(cardName, cardDescription, cardImage, attackPointSpinner, defensePointSpinner
                , effects, attributeChoiceBox, monsterTypeChoiceBox);
        statusLabel.setText("Monster is made successfully");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //attribute
        String[] attributes = {"DARK", "EARTH", "FIRE", "LIGHT", "WATER", "WIND"};
        attributeChoiceBox.getItems().addAll(attributes);
        attributeChoiceBox.setValue("EARTH");
        //monster type
        String[] monsterTypes = {"Beast-Warrior", "Warrior", "Fiend", "Aqua", "Beast", "Pyro", "Spellcaster", "Thunder",
                "Dragon", "Machine", "Rock", "Insect", "Cyberse", "Fairy", "Sea Serpent"};
        monsterTypeChoiceBox.getItems().addAll(monsterTypes);
        monsterTypeChoiceBox.setValue("Warrior");
        //attack point & defense point
        SpinnerValueFactory<Integer> attValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 4000);
        attValueFactory.increment(50);
        attValueFactory.setValue(100);
        SpinnerValueFactory<Integer> defValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(100, 4000);
        defValueFactory.increment(50);
        defValueFactory.setValue(100);
        attackPointSpinner.setValueFactory(attValueFactory);
        defensePointSpinner.setValueFactory(defValueFactory);
    }
}
