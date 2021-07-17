package controller.menucontroller;

import controller.duel.PhaseController;
import controller.duel.SelectionController;
import controller.duel.singlePlayer.GameController;
import models.Player;
import models.User;
import models.cards.Card;
import view.DuelView;
import view.Regex;
import view.StatusEnum;

import java.util.regex.Matcher;

public class CheatMenuController {

    PhaseController phaseController = new PhaseController();
    GameController gameController = new GameController();

    public String run(String command) {
        Player cheater;
        if (DuelView.isMultiPlayer)
            cheater = PhaseController.playerInTurn;
        else
            cheater = GameController.player;
        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.CHEAT_INCREASE_MONEY)).matches())
            return increaseMoney(Integer.parseInt(matcher.group(2)), cheater);
        else if ((matcher = Regex.getMatcher(command, Regex.CHEAT_INCREASE_LP)).matches())
            return increaseLP(Integer.parseInt(matcher.group(2)), cheater);
        else if ((matcher = Regex.getMatcher(command, Regex.CHEAT_SELECT_MORE_CARDS_1)).matches())
            return selectCardForce(matcher.group(2), cheater);
        else if ((matcher = Regex.getMatcher(command, Regex.CHEAT_SELECT_MORE_CARDS_2)).matches())
            return selectCardForce(matcher.group(3), cheater);
        else
            return StatusEnum.INVALID_COMMAND.getStatus();
    }


    private String increaseMoney(int amount, Player cheater) {
        User cheaterUser = User.getUserByUserName(cheater.getUserName());
        assert cheaterUser != null;
        cheaterUser.setMoney(cheaterUser.getMoney() + amount);
        return "Cheat Activated Successfully";
    }

    private String increaseLP(int amount, Player cheater) {
        cheater.getPlayerBoard().setLifePoints(cheater.getPlayerBoard().getLifePoints() + amount);
        return "Cheat Activated Successfully";
    }

    private String selectCardForce(String cardName, Player cheater) {
        for (Card card: cheater.getPlayerBoard().getHandCards())
            if (card.getName().equals(cardName)) SelectionController.selectedCard = card;
        return "Cheat Activated Successfully";
    }
}
