package controller.GUI;


import controller.duel.*;
import controller.duel.singlePlayer.GameController;
import controller.menucontroller.LoginMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.User;
import models.cards.Card;
import models.cards.CardImage;
import models.cards.monsters.Mode;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DuelViewSceneController implements Initializable {

    private static Image firstPlayerImage;
    private static Image secondPlayerImage;
    private static Image keepMyImage = null;
    private static Image keepRivalImage = null;

    private static final Image closedForestFiled = new Image("./image/Field/fie_closed_forest.png");
    private static final Image forestFiled = new Image("./image/Field/fie_forest.png");
    private static final Image normalFiled = new Image("./image/Field/fie_normal.png");
    private static final Image umirukaFiled = new Image("./image/Field/fie_umiruka.png");
    private static final Image yamiFiled = new Image("./image/Field/fie_yami.png");

    private static final Image unknownCardImage = new Image("./image/Unknown.jpg");

    public static boolean isMultiPlayer = true;

    public Label myLPLabel;
    public ImageView myMonster5;
    public ImageView myMonster3;
    public ImageView myMonster1;
    public ImageView myMonster2;
    public ImageView myMonster4;
    public ImageView mySpell5;
    public ImageView mySpell3;
    public ImageView mySpell1;
    public ImageView mySpell2;
    public ImageView mySpell4;
    public ImageView rivalMonster4;
    public ImageView rivalMonster1;
    public ImageView rivalMonster2;
    public ImageView rivalMonster3;
    public ImageView rivalMonster5;
    public ImageView rivalSpell5;
    public ImageView rivalSpell3;
    public ImageView rivalSpell1;
    public ImageView rivalSpell2;
    public ImageView rivalSpell4;
    public ImageView myGraveyard;
    public ImageView rivalGraveyard;
    public ImageView rivalDeck;
    public ImageView selectedCard;
    public ProgressBar myLP;
    public ProgressBar rivalLP;
    public Label rivalName;
    public Label myName;
    public ImageView backGround;
    public ImageView myHand1;
    public ImageView myHand2;
    public ImageView myHand3;
    public ImageView myHand4;
    public ImageView myHand5;
    public ImageView rivalHand4;
    public ImageView rivalHand3;
    public ImageView rivalHand2;
    public ImageView rivalHand1;
    public ImageView myDeck;
    public Label rivalUserName;
    public Label myUserName;
    public Label rivalLPLabel;
    public ImageView myAvatar;
    public ImageView rivalAvatar;
    public AnchorPane pane;
    public ImageView myHand6;
    public ImageView myHand7;
    public ImageView rivalHand9;
    public ImageView rivalHand5;
    public ImageView rivalHand8;
    public ImageView myHand8;
    public ImageView myHand9;
    public ImageView rivalHand7;
    public ImageView myHand10;
    public ImageView rivalHand6;
    public ImageView rivalHand10;
    public ImageView rivalFieldSpell;
    public ImageView myFieldSpell;
    public Label status;
    public Label currentPhase;
    public TextField killOwnMonsters;

    public static SelectionController selectionController = new SelectionController();
    public static SummonController summonController = new SummonController();
    public static SettingController settingController = new SettingController();
    public static PhaseController phaseController = new PhaseController();
    public static AttackController attackController = new AttackController();
    public static ActivationController activationController = new ActivationController();
    public static GameController gameController = new GameController();

    private static final Pane rivalMonsterPane = new Pane();
    private static final ImageView rivalMonsterSelect1 = new ImageView();
    private static final ImageView rivalMonsterSelect2 = new ImageView();
    private static final ImageView rivalMonsterSelect3 = new ImageView();
    private static final ImageView rivalMonsterSelect4 = new ImageView();
    private static final ImageView rivalMonsterSelect5 = new ImageView();

    private static final Rectangle backgroundRectangle = new Rectangle();

    private static DuelViewSceneController instance;

    public static void setInstance(DuelViewSceneController duelViewSceneController) {
        instance = duelViewSceneController;
    }

    public static DuelViewSceneController getInstance() {
        return instance;
    }

    public static void end(User winnerUser, MouseEvent event) throws IOException {
        if (LoginMenuController.currentUser == winnerUser)
            SceneController.switchSceneMouse("/fxml/YouWon.fxml", event);
        else
            SceneController.switchSceneMouse("/fxml/GameOver.fxml", event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundRectangle.setFill(Color.BLACK);
        backgroundRectangle.setOpacity(0.5);
        backgroundRectangle.setWidth(backGround.getFitWidth());
        backgroundRectangle.setHeight(backGround.getFitHeight());
        rivalMonsterPane.setLayoutX(backGround.getLayoutX());
        rivalMonsterPane.setLayoutY(backGround.getLayoutY());
        rivalMonsterPane.setPrefSize(backGround.getFitWidth(), backGround.getFitHeight());
        setSelections();
        setLifePoint();
        setInstance(this);
        if (isMultiPlayer) {
            myUserName.setText(PhaseController.playerInTurn.getUserName());
            myName.setText(PhaseController.playerInTurn.getNickName());
            rivalUserName.setText(PhaseController.playerAgainst.getUserName());
            rivalName.setText(PhaseController.playerAgainst.getNickName());
            if (PhaseController.playerInTurn.getUser().getAvatarImage() == null)
                firstPlayerImage = new Image(PhaseController.playerInTurn.getUser().getMiniAvatar());
            else
                firstPlayerImage = PhaseController.playerInTurn.getUser().getAvatarImage();
            if (PhaseController.playerAgainst.getUser().getAvatarImage() == null)
                secondPlayerImage = new Image(PhaseController.playerAgainst.getUser().getMiniAvatar());
            else
                secondPlayerImage = PhaseController.playerAgainst.getUser().getAvatarImage();
            myAvatar.setImage(firstPlayerImage);
            rivalAvatar.setImage(secondPlayerImage);
            currentPhase.setText(PhaseController.currentPhase.getLabel());
        } else {
            myUserName.setText(GameController.player.getUserName());
            rivalName.setText(GameController.bot.getName());
            myName.setText(GameController.player.getNickName());
            rivalUserName.setText(GameController.bot.getName());
            if (GameController.player.getUser().getAvatarImage() == null)
                myAvatar.setImage(new Image(GameController.player.getUser().getMiniAvatar()));
            else
                myAvatar.setImage(GameController.player.getUser().getAvatarImage());
            rivalAvatar.setImage(new Image("./image/Avatars/bot.png"));
            currentPhase.setText(GameController.currentPhase.getLabel());
        }
        myDeck.setImage(unknownCardImage);
        rivalDeck.setImage(unknownCardImage);
        setCards();
    }

    public void setSelections() {
        setSelectMyMonster(myMonster1, "1");
        setSelectMyMonster(myMonster2, "2");
        setSelectMyMonster(myMonster3, "3");
        setSelectMyMonster(myMonster4, "4");
        setSelectMyMonster(myMonster5, "5");
        setSelectRivalMonster(rivalMonster1, "1");
        setSelectRivalMonster(rivalMonster2, "2");
        setSelectRivalMonster(rivalMonster3, "3");
        setSelectRivalMonster(rivalMonster4, "4");
        setSelectRivalMonster(rivalMonster5, "5");
        setSelectMySpell(mySpell1, "1");
        setSelectMySpell(mySpell2, "2");
        setSelectMySpell(mySpell3, "3");
        setSelectMySpell(mySpell4, "4");
        setSelectMySpell(mySpell5, "5");
        setSelectRivalSpell(rivalSpell1, "1");
        setSelectRivalSpell(rivalSpell2, "2");
        setSelectRivalSpell(rivalSpell3, "3");
        setSelectRivalSpell(rivalSpell4, "4");
        setSelectRivalSpell(rivalSpell5, "5");
        setSelectMyHand(myHand1, "1");
        setSelectMyHand(myHand2, "2");
        setSelectMyHand(myHand3, "3");
        setSelectMyHand(myHand4, "4");
        setSelectMyHand(myHand5, "5");
        setSelectMyHand(myHand6, "6");
        setSelectMyHand(myHand7, "7");
        setSelectMyHand(myHand8, "8");
        setSelectMyHand(myHand9, "9");
        setSelectMyHand(myHand10, "10");
    }

    public void setSelectMyMonster(ImageView imageView, String index) {
        imageView.setOnMouseClicked(event -> {
            selectionController.selectMyMonster(index);
            resetEffects();
            selectedCard.setImage(CardImage.getImageByName(SelectionController.selectedCard.getName()));
            imageView.setEffect(new Bloom());
        });
    }

    public void setSelectRivalMonster(ImageView imageView, String index) {
        imageView.setOnMouseClicked(event -> {
            selectionController.selectRivalMonster(index);
            resetEffects();
            selectedCard.setImage(imageView.getImage());
            imageView.setEffect(new Bloom());
        });
    }

    public void setSelectMySpell(ImageView imageView, String index) {
        imageView.setOnMouseClicked(event -> {
            selectionController.selectMySpell(index);
            resetEffects();
            selectedCard.setImage(CardImage.getImageByName(SelectionController.selectedCard.getName()));
            imageView.setEffect(new Bloom());
        });
    }

    public void setSelectRivalSpell(ImageView imageView, String index) {
        imageView.setOnMouseClicked(event -> {
            selectionController.selectRivalSpell(index);
            resetEffects();
            selectedCard.setImage(imageView.getImage());
            imageView.setEffect(new Bloom());
        });
    }

    public void setSelectMyHand(ImageView imageView, String index) {
        imageView.setOnMouseClicked(event -> {
            selectionController.selectHandCard(index);
            resetEffects();
            selectedCard.setImage(imageView.getImage());
            imageView.setEffect(new Bloom());
        });
    }

    public void resetEffects() {
        myMonster1.setEffect(null);
        myMonster2.setEffect(null);
        myMonster3.setEffect(null);
        myMonster4.setEffect(null);
        myMonster5.setEffect(null);
        rivalMonster1.setEffect(null);
        rivalMonster2.setEffect(null);
        rivalMonster3.setEffect(null);
        rivalMonster4.setEffect(null);
        rivalMonster5.setEffect(null);
        mySpell1.setEffect(null);
        mySpell2.setEffect(null);
        mySpell3.setEffect(null);
        mySpell4.setEffect(null);
        mySpell5.setEffect(null);
        rivalSpell1.setEffect(null);
        rivalSpell2.setEffect(null);
        rivalSpell3.setEffect(null);
        rivalSpell4.setEffect(null);
        rivalSpell5.setEffect(null);
        myHand1.setEffect(null);
        myHand2.setEffect(null);
        myHand3.setEffect(null);
        myHand4.setEffect(null);
        myHand5.setEffect(null);
        myHand6.setEffect(null);
        myHand7.setEffect(null);
        myHand8.setEffect(null);
        myHand9.setEffect(null);
        myHand10.setEffect(null);
    }

    public void changePhase(MouseEvent event) {
        if (isMultiPlayer) {
            phaseController.changePhase(event);
            currentPhase.setText(PhaseController.currentPhase.getLabel());
            myUserName.setText(PhaseController.playerInTurn.getUserName());
            myName.setText(PhaseController.playerInTurn.getNickName());
            rivalUserName.setText(PhaseController.playerAgainst.getUserName());
            rivalName.setText(PhaseController.playerAgainst.getNickName());
            if (PhaseController.currentPhase == GamePhase.DRAW || PhaseController.currentPhase == GamePhase.MAIN2 ||
                    PhaseController.currentPhase == GamePhase.RIVAL_TURN) {
                if (myAvatar.getImage().equals(firstPlayerImage)) {
                    myAvatar.setImage(secondPlayerImage);
                    rivalAvatar.setImage(firstPlayerImage);
                } else {
                    myAvatar.setImage(firstPlayerImage);
                    rivalAvatar.setImage(secondPlayerImage);
                }
            }
        } else {
            gameController.changePhase(event);
            currentPhase.setText(GameController.currentPhase.getLabel());
        }
        setCards();
        setLifePoint();
    }

    public void setPreperations() {
        setInstance(this);
    }

    public void setAttack(MouseEvent event) {
        status.setText(settingController.set());
        setCards();
        attackController.checkEndGame(event);
    }

    public void changePosition(MouseEvent event) {
        status.setText(settingController.changePosition());
        setCards();
        attackController.checkEndGame(event);
    }

    public void activateEffect(MouseEvent event) {
        status.setText(activationController.activate());
        setCards();
        setLifePoint();
        attackController.checkEndGame(event);
    }

    public void flipSummon(MouseEvent event) {
        status.setText(summonController.flipSummon());
        setCards();
        setLifePoint();
        attackController.checkEndGame(event);
    }

    public void normalSummon(MouseEvent event) {
        if (SelectionController.selectedCard == null)
            return;
        if (SelectionController.selectedCard instanceof SpellTrapCard)
            return;
        if (((MonsterCard) SelectionController.selectedCard).getLevel() < 5)
            status.setText(summonController.summon());
        else {
            killOwnMonsters.setVisible(true);
        }
        setCards();
        setLifePoint();
        attackController.checkEndGame(event);
    }

    public void specialSummon(MouseEvent event) {
        status.setText(summonController.specialSummon());
        setCards();
        setLifePoint();
        attackController.checkEndGame(event);
    }

    public void directAttack(MouseEvent event) {
        status.setText(attackController.directAttack());
        setCards();
        setLifePoint();
        attackController.checkEndGame(event);
    }

    public void setCards() {
        resetEffects();
        pane.getChildren().remove(rivalMonsterPane);
        ImageView[] myHands = {myHand1, myHand2, myHand3, myHand4, myHand5, myHand6, myHand7, myHand8, myHand9, myHand10};
        ImageView[] rivalHands = {rivalHand1, rivalHand2, rivalHand3, rivalHand4, rivalHand5, rivalHand6, rivalHand7,
                rivalHand8, rivalHand9, rivalHand10};
        ImageView[] myMonsters = {myMonster1, myMonster2, myMonster3, myMonster4, myMonster5};
        ImageView[] rivalMonsters = {rivalMonster1, rivalMonster2, rivalMonster3, rivalMonster4, rivalMonster5};
        ImageView[] mySpellTraps = {mySpell1, mySpell2, mySpell3, mySpell4, mySpell5};
        ImageView[] rivalSpellTraps = {rivalSpell1, rivalSpell2, rivalSpell3, rivalSpell4, rivalSpell5};
        if (isMultiPlayer) {
            setMyHandImages(myHands, PhaseController.playerInTurn.getPlayerBoard().getHandCards());
            setRivalHandImages(rivalHands, PhaseController.playerAgainst.getPlayerBoard().getHandCards());
            setMonsterCardImages(myMonsters, PhaseController.playerInTurn.getPlayerBoard().getMonsterBoard());
            setMonsterCardImages(rivalMonsters, PhaseController.playerAgainst.getPlayerBoard().getMonsterBoard());
            setSpellTrapCardImages(mySpellTraps, PhaseController.playerInTurn.getPlayerBoard().getSpellAndTrapBoard());
            setSpellTrapCardImages(rivalSpellTraps, PhaseController.playerAgainst.getPlayerBoard().getSpellAndTrapBoard());
            if (PhaseController.playerInTurn.getPlayerBoard().getFieldZone() != null) {
                myFieldSpell.setImage(CardImage.getImageByName(PhaseController.playerInTurn.getPlayerBoard().getFieldZone().getName()));
                setBackGround(PhaseController.playerInTurn.getPlayerBoard().getFieldZone().getName());
            } else {
                myFieldSpell.setImage(null);
                backGround.setImage(normalFiled);
            }
            if (PhaseController.playerAgainst.getPlayerBoard().getFieldZone() != null)
                rivalFieldSpell.setImage(CardImage.getImageByName(PhaseController.playerAgainst.getPlayerBoard().getFieldZone().getName()));
            else
                rivalFieldSpell.setImage(null);
        } else {
            setMyHandImages(myHands, GameController.player.getPlayerBoard().getHandCards());
            setRivalHandImages(rivalHands, GameController.bot.getBoard().getHandCards());
            setMonsterCardImages(myMonsters, GameController.player.getPlayerBoard().getMonsterBoard());
            setMonsterCardImages(rivalMonsters, GameController.bot.getBoard().getMonsterBoard());
            setSpellTrapCardImages(mySpellTraps, GameController.player.getPlayerBoard().getSpellAndTrapBoard());
            setSpellTrapCardImages(rivalSpellTraps, GameController.bot.getBoard().getSpellAndTrapBoard());
            if (GameController.player.getPlayerBoard().getFieldZone() != null) {
                myFieldSpell.setImage(CardImage.getImageByName(GameController.player.getPlayerBoard().getFieldZone().getName()));
                setBackGround(PhaseController.playerInTurn.getPlayerBoard().getFieldZone().getName());
            }
            if (GameController.bot.getBoard().getFieldZone() != null)
                myFieldSpell.setImage(CardImage.getImageByName(GameController.bot.getBoard().getFieldZone().getName()));
        }
    }

    private void setBackGround(String name) {
        if (name.equals("Yami"))
            backGround.setImage(yamiFiled);
        else if (name.equals("Forest"))
            backGround.setImage(forestFiled);
        else if (name.equals("Closed Forest"))
            backGround.setImage(closedForestFiled);
        else if (name.equals("Umiiruka"))
            backGround.setImage(umirukaFiled);
    }

    private void setRivalHandImages(ImageView[] imageViews, ArrayList<Card> cards) {
        int i;
        for (i = 0; i < cards.size(); i++)
            imageViews[i].setImage(unknownCardImage);
        for (int j = i; j < imageViews.length; j++)
            imageViews[j].setImage(null);
    }

    private void setMyHandImages(ImageView[] imageViews, ArrayList<Card> cards) {
        int i;
        for (i = 0; i < cards.size(); i++) {
            if (!cards.get(i).getIsHidden())
                imageViews[i].setImage(CardImage.getImageByName(cards.get(i).getName()));
            else
                imageViews[i].setImage(unknownCardImage);
        }
        for (int j = i; j < imageViews.length; j++)
            imageViews[j].setImage(null);
    }

    private void setMonsterCardImages(ImageView[] imageViews, ArrayList<MonsterCard> cards) {
        int i;
        for (i = 0; i < cards.size(); i++) {
            if (cards.get(i) == null)
                imageViews[i].setImage(null);
            else {
                if (cards.get(i).getMode() == Mode.DEFENSE)
                    imageViews[i].setRotate(90);
                else
                    imageViews[i].setRotate(0);
                if (!cards.get(i).getIsHidden())
                    imageViews[i].setImage(CardImage.getImageByName(cards.get(i).getName()));
                else
                    imageViews[i].setImage(unknownCardImage);
            }
        }
        for (int j = i; j < imageViews.length; j++) {
            imageViews[i].setRotate(0);
            imageViews[j].setImage(null);
        }
    }

    private void setSpellTrapCardImages(ImageView[] imageViews, ArrayList<SpellTrapCard> cards) {
        int i;
        for (i = 0; i < cards.size(); i++) {
            if (cards.get(i) == null)
                imageViews[i].setImage(null);
            else {
                if (!cards.get(i).getIsHidden())
                    imageViews[i].setImage(CardImage.getImageByName(cards.get(i).getName()));
                else
                    imageViews[i].setImage(unknownCardImage);
            }
        }
        for (int j = i; j < imageViews.length; j++)
            imageViews[j].setImage(null);
    }

    private void setLifePoint() {
        if (isMultiPlayer) {
            myLPLabel.setText(String.valueOf(PhaseController.playerInTurn.getPlayerBoard().getLifePoints()));
            myLP.setProgress((double) PhaseController.playerInTurn.getPlayerBoard().getLifePoints() / 8000.0);
            rivalLPLabel.setText(String.valueOf(PhaseController.playerAgainst.getPlayerBoard().getLifePoints()));
            rivalLP.setProgress((double) PhaseController.playerAgainst.getPlayerBoard().getLifePoints() / 8000.0);
        } else {
            myLPLabel.setText(String.valueOf(GameController.player.getPlayerBoard().getLifePoints()));
            myLP.setProgress((double) GameController.player.getPlayerBoard().getLifePoints() / 8000.0);
            rivalLPLabel.setText(String.valueOf(GameController.bot.getBoard().getLifePoints()));
            rivalLP.setProgress((double) GameController.bot.getBoard().getLifePoints() / 8000.0);
        }
    }

    public void openSettingScene(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/setting.fxml", actionEvent);
    }

    public void showMyGraveYard(MouseEvent mouseEvent) throws IOException {
        SceneController.switchSceneMouse("/fxml/graveyard.fxml", mouseEvent);
        if (isMultiPlayer)
            new GraveyardControllerGUI().showCards(PhaseController.playerInTurn.getPlayerBoard().getGraveyardCards());
        else
            new GraveyardControllerGUI().showCards(GameController.player.getPlayerBoard().getGraveyardCards());
    }


    public void attack(MouseEvent event) {
        pane.getChildren().remove(rivalMonsterPane);
        if (isMultiPlayer) {
            if (PhaseController.currentPhase == GamePhase.BATTLE && PhaseController.playerInTurn
                    .getPlayerBoard().getMonsters().contains(SelectionController.selectedCard) &&
                    PhaseController.playerAgainst.getPlayerBoard().getMonsters().size() > 0) {
                ImageView[] newMonsters = {rivalMonsterSelect1, rivalMonsterSelect2, rivalMonsterSelect3, rivalMonsterSelect4, rivalMonsterSelect5};
                ImageView[] monsters = {rivalMonster1, rivalMonster2, rivalMonster3, rivalMonster4, rivalMonster5};
                setRivalMonstersOnNewPane(newMonsters, monsters, true, event);
            }
        } else {
            if (GameController.currentPhase == GamePhase.BATTLE && GameController.player.getPlayerBoard()
                    .getMonsters().contains(SelectionController.selectedCard) && GameController.bot.getBoard()
                    .getMonsters().size() > 0) {
                ImageView[] newMonsters = {rivalMonsterSelect1, rivalMonsterSelect2, rivalMonsterSelect3, rivalMonsterSelect4, rivalMonsterSelect5};
                ImageView[] monsters = {rivalMonster1, rivalMonster2, rivalMonster3, rivalMonster4, rivalMonster5};
                setRivalMonstersOnNewPane(newMonsters, monsters, true, event);
            }
        }
    }

    private void setRivalMonstersOnNewPane(ImageView[] newMonsters, ImageView[] monsters, boolean isAttack, MouseEvent event) {
        DuelViewSceneController.rivalMonsterPane.getChildren().clear();
        rivalMonsterPane.getChildren().add(backgroundRectangle);
        for (int i = 0; i < monsters.length; i++) {
            newMonsters[i].setPreserveRatio(true);
            newMonsters[i].setLayoutX(monsters[i].getLayoutX() - rivalMonsterPane.getLayoutX());
            newMonsters[i].setLayoutY(monsters[i].getLayoutY() - rivalMonsterPane.getLayoutY());
            newMonsters[i].setImage(monsters[i].getImage());
            newMonsters[i].setFitWidth(monsters[i].getFitWidth());
            newMonsters[i].setFitHeight(monsters[i].getFitHeight());
            DuelViewSceneController.rivalMonsterPane.getChildren().add(newMonsters[i]);
            int index = i + 1;
            if (isAttack)
                newMonsters[i].setOnMouseClicked(E -> attackHandler(index, event));
            else
                newMonsters[i].setOnMouseClicked(E -> activateOnMonsterHandler(index, event));
        }
        pane.getChildren().add(rivalMonsterPane);
    }

    private void activateOnMonsterHandler(int index, MouseEvent event) {
        status.setText(activationController.activateOnMonster(index));
        setCards();
        setLifePoint();
        attackController.checkEndGame(event);
    }

    private void attackHandler(int index, MouseEvent event) {
        status.setText(attackController.attackMonsterToMonster("" + index + ""));
        setCards();
        setLifePoint();
        attackController.checkEndGame(event);
    }

    public void showChangesForBot() {
        setCards();
        setLifePoint();
    }

    public void activateOnMonster(MouseEvent event) {
        pane.getChildren().remove(rivalMonsterPane);
        if (isMultiPlayer) {
            if (PhaseController.currentPhase == GamePhase.BATTLE && PhaseController.playerInTurn
                    .getPlayerBoard().getMonsters().contains(SelectionController.selectedCard) &&
                    PhaseController.playerAgainst.getPlayerBoard().getMonsters().size() > 0) {
                ImageView[] newMonsters = {rivalMonsterSelect1, rivalMonsterSelect2, rivalMonsterSelect3, rivalMonsterSelect4, rivalMonsterSelect5};
                ImageView[] monsters = {rivalMonster1, rivalMonster2, rivalMonster3, rivalMonster4, rivalMonster5};
                setRivalMonstersOnNewPane(newMonsters, monsters, false, event);
            }
        } else {
            if (GameController.currentPhase == GamePhase.BATTLE && GameController.player.getPlayerBoard()
                    .getMonsters().contains(SelectionController.selectedCard) && GameController.bot.getBoard()
                    .getMonsters().size() > 0) {
                ImageView[] newMonsters = {rivalMonsterSelect1, rivalMonsterSelect2, rivalMonsterSelect3, rivalMonsterSelect4, rivalMonsterSelect5};
                ImageView[] monsters = {rivalMonster1, rivalMonster2, rivalMonster3, rivalMonster4, rivalMonster5};
                setRivalMonstersOnNewPane(newMonsters, monsters, false, event);
            }
        }
    }

    public void tributeOrRitualSummon(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String monstersToKill = killOwnMonsters.getText();
            status.setText(summonController.tributeSummon(monstersToKill));
            killOwnMonsters.setVisible(false);
        }
    }
}
