package controller.duel;

import controller.duel.singlePlayer.GameController;
import view.DuelView;

public class ShowController {

    public String showGraveyard() {
        StringBuilder graveyardFormat = new StringBuilder();
        if (DuelView.isMultiPlayer) {
            for (int i = 0; i < PhaseController.playerInTurn.getPlayerBoard().getGraveyardCards().size(); i++)
                graveyardFormat.append(i + 1).append(". ").append(SelectionController.selectedCard.getName()).
                        append(":").append(SelectionController.selectedCard.getDescription());
        } else {
            for (int i = 0; i < GameController.player.getPlayerBoard().getGraveyardCards().size(); i++)
                graveyardFormat.append(i + 1).append(". ").append(SelectionController.selectedCard.getName()).
                        append(":").append(SelectionController.selectedCard.getDescription());
        }
        return graveyardFormat.toString();
    }

    public String showSelectedCard() {
        if (SelectionController.selectedCard == null)
            return "no card is selected";
        if (DuelView.isMultiPlayer) {
            if (SelectionController.selectedCard.getIsHidden() &&
                    !PhaseController.playerInTurn.getPlayerBoard().getMonsters().contains(SelectionController.selectedCard) &&
                    !PhaseController.playerInTurn.getPlayerBoard().getSpellTraps().contains(SelectionController.selectedCard) &&
                    !PhaseController.playerInTurn.getPlayerBoard().getHandCards().contains(SelectionController.selectedCard) &&
                    !PhaseController.playerInTurn.getPlayerBoard().getFieldZone().equals(SelectionController.selectedCard)
                    && !PhaseController.playerInTurn.getPlayerBoard().getEffectsStatus().isRivalReveled())
                return "card is not visible";
        } else {
            if (SelectionController.selectedCard.getIsHidden() &&
                    !GameController.player.getPlayerBoard().getMonsters().contains(SelectionController.selectedCard) &&
                    !GameController.player.getPlayerBoard().getSpellTraps().contains(SelectionController.selectedCard) &&
                    !GameController.player.getPlayerBoard().getHandCards().contains(SelectionController.selectedCard) &&
                    !GameController.player.getPlayerBoard().getFieldZone().equals(SelectionController.selectedCard)
                    && !GameController.player.getPlayerBoard().getEffectsStatus().isRivalReveled())
                return "card is not visible";
        }
        return SelectionController.selectedCard.toString();
    }
}
