package controller.duel;

import controller.duel.singlePlayer.GameController;
import models.cards.Card;
import view.DuelView;
import view.StatusEnum;

public class SelectionController {
    public static Card selectedCard;

    public String selectMyMonster(String monsterNum) {
        int monsterIndex = Integer.parseInt(monsterNum);
        if (monsterIndex > 5)
            return StatusEnum.INVALID_SELECTION.getStatus();
        if (DuelView.isMultiPlayer)
            selectedCard = PhaseController.playerInTurn.getPlayerBoard().getMonsterBoard().get(monsterIndex - 1);
        else
            selectedCard = GameController.player.getPlayerBoard().getMonsterBoard().get(monsterIndex - 1);
        if (selectedCard == null)
            return StatusEnum.NO_CARD_FOUND_IN_POSITION.getStatus();
        return StatusEnum.CARD_SELECTED.getStatus();
    }

    public String selectRivalMonster(String monsterNum) {
        int monsterIndex = Integer.parseInt(monsterNum);
        if (monsterIndex > 5)
            return StatusEnum.INVALID_SELECTION.getStatus();
        if (DuelView.isMultiPlayer)
            selectedCard = PhaseController.playerAgainst.getPlayerBoard().getMonsterBoard().get(monsterIndex - 1);
        else
            selectedCard = GameController.bot.getBoard().getMonsterBoard().get(monsterIndex - 1);
        if (selectedCard == null)
            return StatusEnum.NO_CARD_FOUND_IN_POSITION.getStatus();
        return StatusEnum.CARD_SELECTED.getStatus();
    }

    public String selectMySpell(String spellNum) {
        int spellIndex = Integer.parseInt(spellNum);
        if (spellIndex > 5)
            return StatusEnum.INVALID_SELECTION.getStatus();
        if (DuelView.isMultiPlayer)
            selectedCard = PhaseController.playerInTurn.getPlayerBoard().getSpellAndTrapBoard().get(spellIndex - 1);
        else
            selectedCard = GameController.player.getPlayerBoard().getSpellAndTrapBoard().get(spellIndex - 1);
        if (selectedCard == null)
            return StatusEnum.NO_CARD_FOUND_IN_POSITION.getStatus();
        return StatusEnum.CARD_SELECTED.getStatus();
    }

    public String selectRivalSpell(String spellNum) {
        int spellIndex = Integer.parseInt(spellNum);
        if (spellIndex > 5)
            return StatusEnum.INVALID_SELECTION.getStatus();
        if (DuelView.isMultiPlayer)
            selectedCard = PhaseController.playerAgainst.getPlayerBoard().getSpellAndTrapBoard().get(spellIndex - 1);
        else
                selectedCard = GameController.bot.getBoard().getSpellAndTrapBoard().get(spellIndex - 1);
        if (selectedCard == null)
            return StatusEnum.NO_CARD_FOUND_IN_POSITION.getStatus();
        return StatusEnum.CARD_SELECTED.getStatus();
    }

    public String selectMyFieldCard() {
        if (DuelView.isMultiPlayer)
            selectedCard = PhaseController.playerInTurn.getPlayerBoard().getFieldZone();
        else
            selectedCard = GameController.player.getPlayerBoard().getFieldZone();
        if (selectedCard == null)
            return StatusEnum.NO_CARD_FOUND_IN_POSITION.getStatus();
        return StatusEnum.CARD_SELECTED.getStatus();
    }

    public String selectRivalFieldCard() {
        if (DuelView.isMultiPlayer)
            selectedCard = PhaseController.playerAgainst.getPlayerBoard().getFieldZone();
        else
            selectedCard = GameController.bot.getBoard().getFieldZone();
        if (selectedCard == null)
            return StatusEnum.NO_CARD_FOUND_IN_POSITION.getStatus();
        return StatusEnum.CARD_SELECTED.getStatus();
    }

    public String selectHandCard(String cardNum) {
        int index = Integer.parseInt(cardNum);
        if (DuelView.isMultiPlayer) {
            if (index > PhaseController.playerInTurn.getPlayerBoard().getHandCards().size())
                return StatusEnum.INVALID_SELECTION.getStatus();
            selectedCard = PhaseController.playerInTurn.getPlayerBoard().getHandCards().get(index - 1);
        } else {
            if (index > GameController.player.getPlayerBoard().getHandCards().size())
                return StatusEnum.INVALID_SELECTION.getStatus();
            selectedCard = GameController.player.getPlayerBoard().getHandCards().get(index - 1);
        }
        return StatusEnum.CARD_SELECTED.getStatus();
    }

    public String deSelect() {
        if (selectedCard == null)
            return StatusEnum.NO_CARD_IS_SELECTED_YET.getStatus();
        selectedCard = null;
        return StatusEnum.CARD_DESELECTED.getStatus();
    }
}
