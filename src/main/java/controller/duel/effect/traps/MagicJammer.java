package controller.duel.effect.traps;

import models.Board;
import models.Chain;
import models.cards.Location;
import models.cards.spelltrap.SpellTrapCard;

public class MagicJammer {
    public static boolean activate(SpellTrapCard trapCard, Board myBoard, Board rivalBoard) {
        if (!trapCard.getName().equals("Magic Jammer"))
            return false;
        Chain.addSpell(trapCard, myBoard, rivalBoard, null);
        return true;
    }

    public static void activateMagicJammer(SpellTrapCard card, Board board) {
        board.getEffectsStatus().setRivalSpellBlocked(true);
        card.setLocation(Location.GRAVEYARD);
        board.removeSpellAndTrap(board.getSpellTrapIndexInSpellTrapBoard(card));
    }
}
