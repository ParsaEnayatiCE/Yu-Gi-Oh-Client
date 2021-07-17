package controller.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.cards.Card;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;

import java.io.IOException;
import java.util.ArrayList;

public class GraveyardControllerGUI {

    @FXML
    private AnchorPane anchor;

    public void showCards(ArrayList<Card> graveyard) {
        anchor.getChildren().clear();
        anchor.getChildren().add(getList(graveyard));
    }

    public VBox getList(ArrayList<Card> graveyard) {
        VBox cards = new VBox(10);
        HBox[] rows = new HBox[(int) Math.ceil(graveyard.size() / 8.0)];
        for (int i = 0; i < graveyard.size(); i++) {
            if (i % 8 == 0)
                rows[i / 8] = new HBox(10);
            ImageView imageView;
            if (graveyard.get(i) instanceof MonsterCard)
                imageView = new ImageView(((MonsterCard) graveyard.get(i)).getImage());
            else
                imageView = new ImageView(((SpellTrapCard) graveyard.get(i)).getImage());
            imageView.setFitWidth(60);
            imageView.setFitHeight(90);
            rows[i / 8].getChildren().add(imageView);
        }
        cards.getChildren().setAll(rows);
        cards.setAlignment(Pos.CENTER);
        return cards;
    }

    public void back(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/gameField.fxml", actionEvent);
    }
}
