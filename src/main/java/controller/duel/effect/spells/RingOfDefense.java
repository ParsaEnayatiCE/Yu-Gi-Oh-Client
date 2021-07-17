package controller.duel.effect.spells;

import models.Board;
import models.Chain;
import models.cards.spelltrap.SpellTrapCard;

public class RingOfDefense {
    public static boolean activate(SpellTrapCard spellTrapCard, Board myBoard) {
        if (spellTrapCard.getName().equals("Ring of defense"))
            Chain.addSpell(spellTrapCard, myBoard, null, null);
        else
            return false;
        return true;
    }

    public static void activate(Board myBoard) {
        myBoard.getEffectsStatus().setRivalSpellBlocked(true);
    }
}
