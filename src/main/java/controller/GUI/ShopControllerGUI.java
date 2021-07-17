package controller.GUI;

import controller.menucontroller.LoginMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.cards.Card;
import models.cards.MakeCards;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ShopControllerGUI {


    static Card selectedCard = null;
    static ImageView selectedImage = null;

    private static ImageView buy;
    private static Label money;
    private static Label price;
    private static Label amount;
    @FXML
    private AnchorPane anchor;

    public static void setSelectedCard(Card card, ImageView imageView) {
        resetSelect(selectedImage);
        selectedCard = card;
        selectedImage = imageView;
        updateAmount(LoginMenuController.currentUser.getCardAmount(selectedCard));
        updatePrice(selectedCard.getPrice());
        selectedImage.setEffect(new Bloom());
        resetButton();
    }

    public static Card getSelectedCard() {
        return selectedCard;
    }

    public static void resetButton() {
        buy.setVisible(getSelectedCard().getPrice() <= LoginMenuController.currentUser.getMoney());
    }

    public static void resetSelect(ImageView imageView) {
        if (imageView != null)
            imageView.setEffect(null);
    }

    public static void updateMoney(int amount) {
        LoginMenuController.currentUser.setMoney(amount);
        money.setText("Money: " + amount);
    }

    public static void updateAmount(int amountAmount) {
        amount.setText("Amount: " + amountAmount);
    }

    public static void updatePrice(int amount) {
        price.setText("Price: " + amount);
    }

    public void buyNewCard() {
        updateMoney(LoginMenuController.currentUser.getMoney() - selectedCard.getPrice());
        addCard();
        resetButton();
    }

    public void addCard() {
        Card card = MakeCards.makeCard(selectedCard.getName());
        LoginMenuController.currentUser.addCard(card);
        updateAmount(LoginMenuController.currentUser.getCardAmount(card));
    }

    public void initialize() {
        try {
            resetSelect(selectedImage);
            selectedImage = null;
            selectedCard = null;
            buy = new ImageView(Objects.requireNonNull(getClass().getResource("/image/buy.png")).toExternalForm());
            buy.setVisible(false);
            buy.setOnMouseClicked(event -> {
                if (buy.isVisible())
                    buyNewCard();
            });
            buy.setLayoutX(630);
            buy.setLayoutY(-18);
            buy.setFitWidth(100);
            buy.setFitHeight(90);
            price = new Label();
            price.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/shop_money.css")).toExternalForm());
            price.setLayoutX(270);
            price.setLayoutY(10);
            amount = new Label();
            amount.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/shop_money.css")).toExternalForm());
            amount.setLayoutX(470);
            amount.setLayoutY(10);
            money = new Label();
            money.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/shop_money.css")).toExternalForm());
            money.setLayoutX(10);
            money.setLayoutY(10);
            anchor.getChildren().add(price);
            anchor.getChildren().add(amount);
            anchor.getChildren().add(buy);
            anchor.getChildren().add(money);
            updateMoney(LoginMenuController.currentUser.getMoney());
            ArrayList<MonsterCard> monsters = MonsterCard.getAllMonsterCardsToShow();
            ArrayList<SpellTrapCard> spells = SpellTrapCard.getAllSpellTrapCardsToShow();
            HBox monstersBox = new HBox(50);
            HBox spellsBox = new HBox(50);
            ScrollPane monstersPane = new ScrollPane();
            ScrollPane spellsPane = new ScrollPane();
            for (MonsterCard monster: monsters) {
                ImageView imageView = new ImageView(monster.getImage());
                imageView.setCursor(Cursor.HAND);
                imageView.setFitHeight(240);
                imageView.setFitWidth(160);
                imageView.setOnMouseClicked(event -> setSelectedCard(monster, imageView));
                monstersBox.getChildren().add(imageView);
            }
            for (SpellTrapCard spellTrap: spells) {
                ImageView imageView = new ImageView(spellTrap.getImage());
                imageView.setCursor(Cursor.HAND);
                imageView.setFitHeight(240);
                imageView.setFitWidth(160);
                imageView.setOnMouseClicked(event -> setSelectedCard(spellTrap, imageView));
                spellsBox.getChildren().add(imageView);
            }
            String scrollPaneStyleAddress = Objects.requireNonNull(getClass().getResource("/css/scroll_pane.css")).toExternalForm();
            monstersPane.setLayoutY(70);
            monstersPane.setMaxWidth(800);
            monstersPane.setPrefHeight(260);
            monstersPane.getStylesheets().add(scrollPaneStyleAddress);
            spellsPane.setLayoutY(340);
            spellsPane.setMaxWidth(800);
            spellsPane.setPrefHeight(260);
            spellsPane.getStylesheets().add(scrollPaneStyleAddress);
            monstersBox.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            spellsBox.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            monstersPane.setContent(monstersBox);
            spellsPane.setContent(spellsBox);
            anchor.getChildren().add(monstersPane);
            anchor.getChildren().add(spellsPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
        Pane pane = fxmlLoader.load();
        stage.setScene(new Scene(pane));
    }
}
