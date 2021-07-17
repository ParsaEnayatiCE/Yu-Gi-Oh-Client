package controller.GUI;

import controller.menucontroller.LoginMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Bloom;
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

public class ChangeDeckControllerGUI {

    private final DeckControllerGUI deckControllerGUI = new DeckControllerGUI();

    public static Deck currentDeck;

    private static Card selectedCard;
    private static ImageView selectedImage;
    private static boolean isCardFromDeck;
    private static boolean isCardFromMain;

    private final ArrayList<ImageView> cardImages = new ArrayList<>();
    private final ArrayList<ImageView> mainImages = new ArrayList<>();
    private final ArrayList<ImageView> sideImages = new ArrayList<>();
    private final VBox mainDeck = new VBox(2);
    private final VBox sideDeck = new VBox(2);
    private final VBox deckInfo = new VBox(10);
    private final VBox cardsBox = new VBox(1);
    private final HBox[] mainBoxes = new HBox[6];
    private final HBox[] sideBoxes = new HBox[2];
    private final HBox[] cardsBoxes = new HBox[(int) Math.ceil
            (LoginMenuController.currentUser.getUserCards().size() / 7.0)];
    private final HBox buttons = new HBox(5);

    private final Pane cardPane = new Pane();

    private final ImageView bigImage = new ImageView();

    @FXML
    private AnchorPane anchor;
    @FXML
    private Label deckName;
    @FXML
    private Pane deckPane;
    @FXML
    private ScrollPane cardsPane;

    private void setSelectedCard(Card card, boolean isFromDeck, boolean isFromMain, ImageView imageView) {
        isCardFromDeck = isFromDeck;
        isCardFromMain = isFromMain;
        resetSelect(selectedImage);
        selectedCard = card;
        selectedImage = imageView;
        if (selectedImage != null)
            selectedImage.setEffect(new Bloom());
    }

    public void resetSelect(ImageView imageView) {
        if (imageView != null)
            imageView.setEffect(null);
    }

    public void initialize() {
        getMainList();
        getSideList();
        deckPane.getChildren().setAll(deckInfo);
        createHBoxes();
        initCardImages();
        hideCard();
        resetSelect(selectedImage);
        selectedImage = null;
        selectedCard = null;
        deckName.setText("Deck Name: " + currentDeck.getName());
        deckName.setLayoutX(10);
        deckName.setLayoutY(10);
        deckPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/scroll_pane.css")).toExternalForm());
        cardsPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/scroll_pane.css")).toExternalForm());
        sideDeck.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/deck.css")).toExternalForm());
        mainDeck.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/deck.css")).toExternalForm());
        cardsBox.setPrefHeight(600);
        cardsBox.setPrefWidth(300);
        cardsBox.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        cardsBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/deck.css")).toExternalForm());
    }

    public void initCardImages() {
        bigImage.setFitWidth(160);
        bigImage.setFitHeight(240);

        ArrayList<Card> cards = new ArrayList<>(LoginMenuController.currentUser.getUserCards());
        for (Card card : currentDeck.getMainDeck())
            for (Card card2 : cards)
                if (card2.getName().equals(card.getName())) {
                    cards.remove(card2);
                    break;
                }
        for (Card card : currentDeck.getSideDeck())
            for (Card card2 : cards)
                if (card2.getName().equals(card.getName())) {
                    cards.remove(card2);
                    break;
                }
        getCards(cards);
        showCards();

        Button addMain = new Button("Add to Main");
        addMain.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/button.css")).toExternalForm());
        addMain.setOnAction(event -> addCard(true));
        Button addSide = new Button("Add to Side");
        addSide.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/button.css")).toExternalForm());
        addSide.setOnAction(event -> addCard(false));
        Button remove = new Button("Remove Card");
        remove.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/button.css")).toExternalForm());
        remove.setOnAction(event -> removeCard());
        buttons.getChildren().setAll(addMain, addSide, remove);
        showDeck();
    }

    public void back(MouseEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/deck_menu.fxml"));
        Pane pane = fxmlLoader.load();
        stage.setScene(new Scene(pane));
    }

    public void addCard(boolean isMain) {
        if (selectedCard == null)
            AlertBox.display("Select a card to add!");
        else if (isCardFromDeck)
            AlertBox.display("this card is already in your Deck!");
        else {
            if (isMain) {
                if (currentDeck.isMainFull())
                    AlertBox.display("Main Deck is FULL!");
                else if (!currentDeck.hasEnoughSpace(selectedCard))
                    AlertBox.display("You Can't use a Card more than 3 Times!");
                else {
                    deckControllerGUI.deckMenuController.addCard(currentDeck.getName(),
                            selectedCard.getName(), "main");
                    mainImages.add(selectedImage);
                    cardImages.remove(selectedImage);
                    ImageView imageView = mainImages.get(mainImages.size() - 1);
                    Card card = selectedCard;
                    imageView.setOnMouseClicked(event ->
                            setSelectedCard(card, true, true,
                                    imageView));
                    imageView.setOnMouseEntered(event -> hideCard());
                    imageView.setOnMouseExited(event -> hideCard());
                }
            } else {
                if (currentDeck.isSideFull())
                    AlertBox.display("Side Deck is FULL!");
                else if (!currentDeck.hasEnoughSpace(selectedCard))
                    AlertBox.display("You can't use a card more than 3 Times!");
                else {
                    deckControllerGUI.deckMenuController.addCard(currentDeck.getName(),
                            selectedCard.getName(), "side");
                    sideImages.add(selectedImage);
                    cardImages.remove(selectedImage);
                    ImageView imageView = sideImages.get(sideImages.size() - 1);
                    Card card = selectedCard;
                    imageView.setOnMouseClicked(event ->
                            setSelectedCard(card, true, false,
                                    imageView));
                    imageView.setOnMouseEntered(event -> hideCard());
                    imageView.setOnMouseExited(event -> hideCard());
                }
            }
            setSelectedCard(null, false, false, null);
            showDeck();
            showCards();
        }
    }

    public void removeCard() {
        if (selectedCard == null)
            AlertBox.display("Select a card to remove!");
        else if (!isCardFromDeck)
            AlertBox.display("You should remove a card from your Deck");
        else {
            if (isCardFromMain) {
                deckControllerGUI.deckMenuController.removeCardFromDeck(currentDeck.getName(),
                        selectedCard.getName(), "main");
                mainImages.remove(selectedImage);
                cardImages.add(selectedImage);
                ImageView imageView = cardImages.get(cardImages.size() - 1);
                Card card = selectedCard;
                imageView.setOnMouseClicked(event ->
                        setSelectedCard(card,
                                false, false, imageView));
                imageView.setOnMouseEntered(event ->
                        showCard(event.getX() +
                                        imageView.getLayoutX(),
                                event.getY() +
                                        imageView.getLayoutY(),
                                imageView));
                imageView.setOnMouseExited(event -> hideCard());
            } else {
                deckControllerGUI.deckMenuController.removeCardFromDeck(currentDeck.getName(),
                        selectedCard.getName(), "side");
                sideImages.remove(selectedImage);
                cardImages.add(selectedImage);
                ImageView imageView = cardImages.get(cardImages.size() - 1);
                Card card = selectedCard;
                imageView.setOnMouseClicked(event ->
                        setSelectedCard(card,
                                false, false, imageView));
                imageView.setOnMouseEntered(event ->
                        showCard(event.getX() +
                                        imageView.getLayoutX(),
                                event.getY() +
                                        imageView.getLayoutY(),
                                imageView));
                imageView.setOnMouseExited(event -> hideCard());
            }
            setSelectedCard(null, false, false, null);
            showDeck();
            showCards();
        }
    }

    public void getMainList() {
        for (int i = 0; i < currentDeck.getMainDeck().size(); i++) {
            Card card = currentDeck.getMainDeck().get(i);
            if (currentDeck.getMainDeck().get(i) instanceof MonsterCard) {
                ImageView imageView = new ImageView(((MonsterCard) currentDeck.getMainDeck().get(i)).getImage());
                mainImages.add(imageView);
                imageView.setFitWidth(40);
                imageView.setFitHeight(60);
                imageView.setOnMouseClicked(event -> setSelectedCard(card, true, true, imageView));
            } else {
                ImageView imageView = new ImageView(((SpellTrapCard) currentDeck.getMainDeck().get(i)).getImage());
                mainImages.add(imageView);
                imageView.setFitWidth(40);
                imageView.setFitHeight(60);
                imageView.setOnMouseClicked(event -> setSelectedCard(card, true, true, imageView));
            }
        }
    }

    public void getSideList() {
        for (int i = 0; i < currentDeck.getSideDeck().size(); i++) {
            Card card = currentDeck.getSideDeck().get(i);
            if (currentDeck.getSideDeck().get(i) instanceof MonsterCard) {
                ImageView imageView = new ImageView(((MonsterCard) currentDeck.getSideDeck().get(i)).getImage());
                sideImages.add(imageView);
                imageView.setFitWidth(40);
                imageView.setFitHeight(60);
                imageView.setOnMouseClicked(event -> setSelectedCard(card, true, false, imageView));
            } else {
                ImageView imageView = new ImageView(((SpellTrapCard) currentDeck.getSideDeck().get(i)).getImage());
                sideImages.add(imageView);
                imageView.setFitWidth(40);
                imageView.setFitHeight(60);
                imageView.setOnMouseClicked(event -> setSelectedCard(card, true, false, imageView));
            }
        }
    }

    public VBox updateMainDeck() {
        mainDeck.getChildren().clear();
        mainDeck.getChildren().add(new Label("Main Deck:"));
        for (HBox box : mainBoxes)
            box.getChildren().clear();
        for (int i = 0; i < currentDeck.getMainDeck().size(); i++)
            mainBoxes[i / 10].getChildren().add(mainImages.get(i));
        mainDeck.getChildren().addAll(mainBoxes);
        return mainDeck;
    }

    public VBox updateSideDeck() {
        sideDeck.getChildren().clear();
        sideDeck.getChildren().add(new Label("Side Deck:"));
        for (HBox box : sideBoxes)
            box.getChildren().clear();
        for (int i = 0; i < currentDeck.getSideDeck().size(); i++) {
            sideBoxes[i / 10].getChildren().add(sideImages.get(i));
        }
        sideDeck.getChildren().addAll(sideBoxes);
        return sideDeck;
    }

    public void createHBoxes() {
        for (int i = 0; i < 6; i++)
            mainBoxes[i] = new HBox(2);
        for (int i = 0; i < 2; i++)
            sideBoxes[i] = new HBox(2);
        for (int i = 0; i < cardsBoxes.length; i++)
            cardsBoxes[i] = new HBox(1);
    }

    public void showDeck() {
        deckInfo.getChildren().setAll(updateMainDeck(),
                updateSideDeck(), buttons);
    }

    public void getCards(ArrayList<Card> cards) {
        for (HBox box : cardsBoxes)
            box.getChildren().clear();
        for (Card card : cards) {
            if (card instanceof MonsterCard) {
                ImageView imageView = new ImageView(((MonsterCard) card).getImage());
                cardImages.add(imageView);
                imageView.setFitHeight(60);
                imageView.setFitWidth(40);
                imageView.setOnMouseClicked(event ->
                        setSelectedCard(card, false, false, imageView));
                imageView.setOnMouseEntered(event -> showCard(event.getX() + imageView.getLayoutX(),
                        event.getY() + imageView.getLayoutY(), imageView));
                imageView.setOnMouseExited(event -> hideCard());
            } else {
                ImageView imageView = new ImageView(((SpellTrapCard) card).getImage());
                cardImages.add(imageView);
                imageView.setFitHeight(60);
                imageView.setFitWidth(40);
                imageView.setOnMouseClicked(event ->
                        setSelectedCard(card, false, false, imageView));
                imageView.setOnMouseEntered(event -> showCard(event.getX() + imageView.getLayoutX(),
                        event.getY() + imageView.getLayoutY(), imageView));
                imageView.setOnMouseExited(event -> hideCard());
            }
        }
    }

    public void showCards() {
        cardsBox.getChildren().clear();
        cardsBox.getChildren().add(new Label("Your Cards:"));
        for (HBox box : cardsBoxes)
            box.getChildren().clear();
        for (int i = 0; i < cardImages.size(); i++) {
            cardsBoxes[i / 7].getChildren().add(cardImages.get(i));
        }
        cardsBox.getChildren().addAll(cardsBoxes);
        cardsPane.setContent(cardsBox);
    }

    public void showCard(double x, double y, ImageView imageView) {
        anchor.getChildren().add(cardPane);
        cardPane.getChildren().clear();
        cardPane.setLayoutX(x + 455);
        cardPane.setLayoutY(y + 5);
        bigImage.setImage(imageView.getImage());
        cardPane.getChildren().setAll(bigImage);
    }

    public void hideCard() {
        anchor.getChildren().remove(cardPane);
    }
}
