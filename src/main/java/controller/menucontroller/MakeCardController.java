package controller.menucontroller;

import controller.duel.effect.CustomEffects;
import controller.duel.effect.CustomEffect;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.User;
import models.cards.CardImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MakeCardController {

    private final User user = LoginMenuController.currentUser;

    public String calculateMonsterLevel(Spinner<Integer> attackPointSpinner, Spinner<Integer> defensePointSpinner, CheckBox[] effects) {
        int level = 1;
        for (CheckBox effect : effects) {
            if (effect.isSelected())
                level++;
        }
        if (attackPointSpinner.getValue() + defensePointSpinner.getValue() > 200)
            level += (((attackPointSpinner.getValue() + defensePointSpinner.getValue()) / 1000) + 1);
        return String.valueOf(level);
    }

    public String calculateMonsterPrice(Spinner<Integer> attackPointSpinner, Spinner<Integer> defensePointSpinner, CheckBox[] effects) {
        int price = 0;
        for (CheckBox effect : effects) {
            if (effect.isSelected())
                price += 1000;
        }
        price += (attackPointSpinner.getValue() + defensePointSpinner.getValue());
        return String.valueOf(price);
    }

    public String calculateMonsterCardType(CheckBox[] effects) {
        for (CheckBox effect : effects) {
            if (effect.isSelected())
                return "Effect";
        }
        return "Normal";
    }

    public String doesHaveProblemMakingMonster(Image cardImage, TextField cardName, TextArea cardDescription, Spinner<Integer> attackPointSpinner, Spinner<Integer> defensePointSpinner, CheckBox[] effects) {
        if (cardName.getText().equals(""))
            return "You should choose a name for the card";
        if (cardDescription.getText().equals(""))
            return "You should write some description for the card";
        if (cardImage == null)
            return "You should choose an image for the card";
        if (Integer.parseInt(calculateMonsterPrice(attackPointSpinner, defensePointSpinner, effects)) > user.getMoney() * 10)
            return "You don't have enough money to make the card";
        return null;
    }

    public void makeMonsterCard(TextField cardName, TextArea cardDescription, ImageView cardImage,
                                Spinner<Integer> attackPointSpinner, Spinner<Integer> defensePointSpinner,
                                CheckBox[] effects, ChoiceBox<String> attributeChoiceBox, ChoiceBox<String> monsterTypeChoiceBox) {
        String level = calculateMonsterLevel(attackPointSpinner, defensePointSpinner, effects);
        String price = calculateMonsterPrice(attackPointSpinner, defensePointSpinner, effects);
        String cardType = calculateMonsterCardType(effects);
        user.setMoney(user.getMoney() - (Integer.parseInt(price) / 10));
        addEffects(effects, cardName);
        String csvData = "\n" + cardName.getText() + "," + level + "," + attributeChoiceBox.getValue() + "," +
                monsterTypeChoiceBox.getValue() + "," + cardType + "," + attackPointSpinner.getValue() + "," +
                defensePointSpinner.getValue() + "," + cardDescription.getText() + "," + price;
        File outputFile = new File("./image/Cards/");
        BufferedImage bImage = SwingFXUtils.fromFXImage(cardImage.getImage(), null);
        new CardImage(cardName.getText(), cardImage.getImage());
        try {
            Files.write(Paths.get("Monster.csv"), csvData.getBytes(), StandardOpenOption.APPEND);
            String fileName = cardName.getText().replaceAll(" ", "") + ".jpg";
            ImageIO.write(bImage, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String calculateSpellTrapPrice(ToggleButton isTrapToggleButton, ToggleButton isUnlimitedToggleButton, CheckBox[] effects) {
        int price = 0;
        if (isTrapToggleButton.isSelected())
            price += 500;
        if (isUnlimitedToggleButton.isSelected())
            price += 1000;
        for (CheckBox effect : effects) {
            if (effect.isSelected())
                price += 1000;
        }
        return String.valueOf(price);
    }

    public String doesHaveProblemMakingSpellTrap(ImageView cardImage, TextField cardName, TextArea cardDescription, ToggleButton isTrapToggleButton, ToggleButton isUnlimitedToggleButton, CheckBox[] effects) {
        if (cardName == null)
            return "You should choose a name for the card";
        if (cardDescription == null)
            return "You should write some description for the card";
        if (cardImage == null)
            return "You should choose an image for the card";
        if (Integer.parseInt(calculateSpellTrapPrice(isTrapToggleButton, isUnlimitedToggleButton, effects)) > user.getMoney() * 10)
            return "You don't have enough money to make the card";
        return null;
    }

    public void makeSpellTrapCard(TextField cardName, TextArea cardDescription, ImageView cardImage,
                                  ToggleButton isTrapToggleButton, ToggleButton isUnlimitedToggleButton,
                                  CheckBox[] effects, ChoiceBox<String> iconChoiceBox) {
        String type = "Spell", status = "Limited", price = calculateSpellTrapPrice(isTrapToggleButton, isUnlimitedToggleButton, effects);
        user.setMoney(user.getMoney() - (Integer.parseInt(price) / 10));
        addEffects(effects, cardName);
        if (isTrapToggleButton.isSelected())
            type = "Trap";
        if (isUnlimitedToggleButton.isSelected())
            status = "Unlimited";
        String csvData = "\n" + cardName.getText() + "," + type + "," + iconChoiceBox.getValue() + "," +
                cardDescription.getText() + "," + status + "," + price;
        File outputFile = new File("./image/Cards/");
        BufferedImage bImage = SwingFXUtils.fromFXImage(cardImage.getImage(), null);
        new CardImage(cardName.getText(), cardImage.getImage());
        try {
            Files.write(Paths.get("SpellTrap.csv"), csvData.getBytes(), StandardOpenOption.APPEND);
            String fileName = cardName.getText().replaceAll(" ", "") + ".jpg";
            ImageIO.write(bImage, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addEffects(CheckBox[] effects, TextField cardName) {
        if (effects[0].isSelected())
            CustomEffects.addEffect(CustomEffect.KILL_MONSTER, cardName.getText());
        if (effects[1].isSelected())
            CustomEffects.addEffect(CustomEffect.KILL_SPELL_TRAP, cardName.getText());
        if (effects[2].isSelected())
            CustomEffects.addEffect(CustomEffect.INCREASE_LIFE_POINT, cardName.getText());
        if (effects[3].isSelected())
            CustomEffects.addEffect(CustomEffect.DECREASE_LIFE_POINT, cardName.getText());
        if (effects.length == 4)
            return;
        if (effects[4].isSelected())
            CustomEffects.addEffect(CustomEffect.INCREASE_ATTACK_POINT, cardName.getText());
        if (effects[5].isSelected())
            CustomEffects.addEffect(CustomEffect.INCREASE_DEFENSE_POINT, cardName.getText());
    }
}
