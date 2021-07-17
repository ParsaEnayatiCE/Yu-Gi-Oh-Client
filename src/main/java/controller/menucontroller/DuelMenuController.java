package controller.menucontroller;

import controller.duel.PhaseController;
import controller.duel.singlePlayer.EasyBot;
import controller.duel.singlePlayer.GameController;
import controller.duel.singlePlayer.HardBot;
import javafx.scene.input.MouseEvent;
import models.Player;
import models.User;
import view.DuelView;
import view.MenuEnum;
import view.ProgramController;
import view.StatusEnum;

import java.util.Objects;

public class DuelMenuController {

    PhaseController phaseController = new PhaseController();
    GameController gameController = new GameController();

    public String startTwoPlayer(User currentUser, String secondPlayer, String rounds) throws CloneNotSupportedException {
        if (!User.isUserNameTaken(secondPlayer))
            return StatusEnum.NO_EXISTENCE_OF_PLAYER2.getStatus();
        if (currentUser.getActiveDeck() == null)
            return currentUser.getUserName() + " has no active deck";
        if (Objects.requireNonNull(User.getUserByUserName(secondPlayer)).getActiveDeck() == null)
            return secondPlayer + " has no active deck";
        if (!currentUser.getActiveDeck().isDeckValid())
            return currentUser.getUserName() + "'s deck is invalid";
        if (!Objects.requireNonNull(User.getUserByUserName(secondPlayer)).getActiveDeck().isDeckValid())
            return secondPlayer + "'s deck is invalid";
        int round = Integer.parseInt(rounds);
        if (round != 3 && round != 1)
            return StatusEnum.ROUNDS_NOT_SUPPORTED.getStatus();
        DuelView.rounds = round;
        DuelView.isMultiPlayer = true;
        new Player(currentUser);
        new Player(Objects.requireNonNull(User.getUserByUserName(secondPlayer)));
        ProgramController.currentMenu = MenuEnum.DUEL_VIEW;
        phaseController.startTheGame();
        return "";
    }

    public String startSinglePlayer(User currentUser, String rounds, String difficulty, MouseEvent event) throws CloneNotSupportedException {
        if (currentUser.getActiveDeck() == null)
            return currentUser.getUserName() + " has no active deck";
        if (!currentUser.getActiveDeck().isDeckValid())
            return currentUser.getUserName() + "'s deck is invalid";
        int round = Integer.parseInt(rounds);
        if (round != 3 && round != 1)
            return StatusEnum.ROUNDS_NOT_SUPPORTED.getStatus();
        DuelView.isMultiPlayer = false;
        Player player = new Player(currentUser);
        DuelView.rounds = round;
        if (difficulty.equals("easy"))
            new EasyBot(player);
        else
            new HardBot(player);
        ProgramController.currentMenu = MenuEnum.DUEL_VIEW;
        gameController.startTheGame(event);
        return "";
    }
}
