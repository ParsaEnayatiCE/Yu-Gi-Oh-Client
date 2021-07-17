package controller.duel.effect.spells;

import models.Board;
import models.cards.spelltrap.SpellTrapCard;

public class SpellAbsorption {
    public static void check(Board myBoard , Board rivalBoard) {
        activateSpellAbsorption(myBoard);
        activateSpellAbsorption(rivalBoard);
    }

    private static void activateSpellAbsorption(Board board) {
        for (SpellTrapCard spellTrapCard : board.getSpellTraps()) {
            if (spellTrapCard.getName().equals("Spell Absorption"))
                board.setLifePoints(board.getLifePoints() + 500);
        }
    }
}
