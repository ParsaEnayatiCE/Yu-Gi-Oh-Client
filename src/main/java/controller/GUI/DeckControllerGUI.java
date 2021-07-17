package controller.GUI;

import controller.menucontroller.DeckMenuController;
import controller.menucontroller.LoginMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Deck;
import models.cards.Card;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;
import view.GUI.AlertBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class DeckControllerGUI {

    private static Deck selectedDeck;

    private final Image activatedImage = new Image(Objects.requireNonNull(getClass().getResource("/image/activated.png")).toExternalForm());
    private final Image defaultImage = new Image(Objects.requireNonNull(getClass().getResource("/image/set-activate.png")).toExternalForm());

    DeckMenuController deckMenuController = new DeckMenuController(LoginMenuController.currentUser);

    @FXML
    private AnchorPane anchor;
    @FXML
    private ImageView active;
    @FXML
    private TextField deckName;

    private final Pane deckInfo = new Pane();

    private final ScrollPane infoPane = new ScrollPane();

    private void setSelectedDeck(Deck deck) {
        selectedDeck = deck;
        if (selectedDeck == null || LoginMenuController.currentUser.getActiveDeck() == null)
            return;
        if (selectedDeck.getName().equals(LoginMenuController.currentUser.getActiveDeck().getName()))
            active.setImage(activatedImage);
        else
            active.setImage(defaultImage);
    }

    private Deck getSelectedDeck() {
        return selectedDeck;
    }

    public HBox getInfo(Deck deck, double height) {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(deck.getMainDeck());
        cards.addAll(deck.getSideDeck());
        HBox cardNodes = new HBox(1);
        cardNodes.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        for (Card card : cards) {
            ImageView imageView;
            if (card instanceof MonsterCard)
                imageView = new ImageView(((MonsterCard) card).getImage());
            else
                imageView = new ImageView(((SpellTrapCard) card).getImage());
            imageView.setFitHeight(height);
            imageView.setFitWidth((2.0 / 3) * height);
            cardNodes.getChildren().add(imageView);
        }
        return cardNodes;
    }

    public void showInfo(double x, double y, double height, double width, Deck deck) {
        infoPane.setLayoutX(x + 2);
        infoPane.setLayoutY(y + 52);
        infoPane.setPrefHeight(height);
        infoPane.setPrefWidth(width);
        infoPane.setVisible(true);
        infoPane.setContent(getInfo(deck, height));
        anchor.getChildren().add(infoPane);
    }

    public void hideInfo() {
        anchor.getChildren().remove(infoPane);
    }

    public void resetDeckList() {
        ArrayList<Deck> userDecks = LoginMenuController.currentUser.getUserDecks();
        VBox deckList = new VBox(20);
        deckList.setPrefWidth(250);
        deckList.setPrefHeight(550);
        deckList.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        for (Deck deck : userDecks) {
            Button button = new Button(deck.toString());
            deckList.getChildren().add(button);
            button.setOnAction(event -> {
                setSelectedDeck(deck);
                showDeck(deck);
            });
            button.setOnMouseEntered(event -> showInfo(event.getX() + button.getLayoutX(),
                    event.getY() + button.getLayoutY(), button.getHeight(), 200, deck));
            button.setOnMouseExited(event -> hideInfo());
        }
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefHeight(550);
        scrollPane.setPrefWidth(250);
        scrollPane.setLayoutX(0);
        scrollPane.setLayoutY(50);
        scrollPane.setContent(deckList);
        scrollPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/scroll_pane.css")).toExternalForm());
        anchor.getChildren().add(scrollPane);
    }

    public void initialize() {
        hideInfo();
        setSelectedDeck(null);
        resetDeckList();
    }

    public void addDeck() {
        if (deckName.getText().equals("")) {
            AlertBox.display("Deck name is Empty!");
            return;
        }
        if (deckName.getText().length() > 10) {
            AlertBox.display("Deck name should be Shorter!");
            return;
        } else
            AlertBox.display(deckMenuController.createDeck(deckName.getText()));

        resetDeckList();
    }

    public void removeDeck() {
        if (getSelectedDeck() == null)
            AlertBox.display("Choose a deck to remove!");
        else {
            if (getSelectedDeck() == LoginMenuController.currentUser.getActiveDeck())
                active.setImage(defaultImage);
            AlertBox.display(deckMenuController.deleteDeck(getSelectedDeck().getName()));
            setSelectedDeck(null);
            resetDeckList();
            showDeck(getSelectedDeck());
        }
    }

    public void setActive() {
        if (getSelectedDeck() == null)
            AlertBox.display("Choose a deck to activate!");
        else {
            LoginMenuController.currentUser.setActiveDeck(getSelectedDeck());
            setSelectedDeck(getSelectedDeck());
        }
    }

    public VBox getMainList(Deck deck) {
        VBox main = new VBox(2);
        main.getChildren().add(new Label("Main Deck:"));
        HBox[] listCards = new HBox[(int) Math.ceil(deck.getMainDeck().size() / 10.0)];
        for (int i = 0; i < deck.getMainDeck().size(); i++) {
            if (i % 10 == 0)
                listCards[i / 10] = new HBox(2);
            if (deck.getMainDeck().get(i) instanceof MonsterCard) {
                ImageView imageView = new ImageView(((MonsterCard) deck.getMainDeck().get(i)).getImage());
                imageView.setFitWidth(40);
                imageView.setFitHeight(60);
                listCards[i / 10].getChildren().add(imageView);
            } else {
                ImageView imageView = new ImageView(((SpellTrapCard) deck.getMainDeck().get(i)).getImage());
                imageView.setFitWidth(40);
                imageView.setFitHeight(60);
                listCards[i / 10].getChildren().add(imageView);
            }
        }
        main.getChildren().addAll(listCards);
        main.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/deck.css")).toExternalForm());
        return main;
    }

    public VBox getSideList(Deck deck) {
        VBox side = new VBox(2);
        side.getChildren().add(new Label("Side Deck:"));
        HBox[] listCards = new HBox[(int) Math.ceil(deck.getSideDeck().size() / 10.0)];
        for (int i = 0; i < deck.getSideDeck().size(); i++) {
            if (i % 10 == 0)
                listCards[i / 10] = new HBox(2);
            if (deck.getSideDeck().get(i) instanceof MonsterCard) {
                ImageView imageView = new ImageView(((MonsterCard) deck.getSideDeck().get(i)).getImage());
                imageView.setFitWidth(40);
                imageView.setFitHeight(60);
                listCards[i / 10].getChildren().add(imageView);
            } else {
                ImageView imageView = new ImageView(((SpellTrapCard) deck.getSideDeck().get(i)).getImage());
                imageView.setFitWidth(40);
                imageView.setFitHeight(60);
                listCards[i / 10].getChildren().add(imageView);
            }
        }
        side.getChildren().addAll(listCards);
        side.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/deck.css")).toExternalForm());
        return side;
    }

    public void showDeck(Deck deck) {
        if (getSelectedDeck() != null) {
            deckInfo.getChildren().clear();
            deckInfo.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/scroll_pane.css")).toExternalForm());
            deckInfo.setLayoutX(300);
            deckInfo.setLayoutY(50);
            deckInfo.setPrefHeight(550);
            deckInfo.setPrefWidth(420);
            Button button = new Button("Change Deck");
            button.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/button.css")).toExternalForm());
            button.setOnAction(event -> changeDeck(deck, event));
            VBox deckCards = new VBox(10);
            deckCards.getChildren().addAll(getMainList(deck), getSideList(deck), button);
            deckInfo.getChildren().add(deckCards);
            anchor.getChildren().remove(deckInfo);
            anchor.getChildren().add(deckInfo);
        } else
            anchor.getChildren().remove(deckInfo);
    }

    public void changeDeck(Deck deck, ActionEvent actionEvent) {
        ChangeDeckControllerGUI.currentDeck = deck;
        try {
            SceneController.switchScene("/fxml/change_deck.fxml", actionEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mainMenu.fxml"));
        Pane pane = fxmlLoader.load();
        stage.setScene(new Scene(pane));
    }
}
