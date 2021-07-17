package controller.duel.effect.spells;

import models.Board;
import models.cards.spelltrap.SpellTrapCard;

import java.util.HashMap;
import java.util.Map;

public class TurnSpells {
    private static final HashMap<SpellTrapCard, Integer> turnsActivated = new HashMap<>();

    public static boolean activate(SpellTrapCard spellCard, Board myBoard, boolean isAnyMonsterDead) {
        if (turnsActivated.containsKey(spellCard))
            return false;
        if (spellCard.getName().equals("Swords of Revealing Light"))
            activateSwordsOfRevealingLight(myBoard);
        else if (spellCard.getName().equals("Supply Squad"))
            activateSupplySquad(isAnyMonsterDead, myBoard);
        else
            return false;
        turnsActivated.put(spellCard, 0);
        return true;
    }

    private static void activateSupplySquad(boolean isAnyMonsterDead, Board myBoard) {
        if (!isAnyMonsterDead)
            return;
        myBoard.drawCard();
    }

    public static void checkTurn(Board board) {

        for (Map.Entry<SpellTrapCard, Integer> turnsCard : turnsActivated.entrySet()) {
            SpellTrapCard spellCard = turnsCard.getKey();

            if (!board.getSpellTraps().contains(spellCard))
                continue;

            if (turnsCard.getValue() < 3) {
                turnsCard.setValue(turnsCard.getValue() + 1);
                continue;
            }

            if (spellCard.getName().equals("Swords of Revealing Light"))
                deActivateSwordsOfRevealingLight(board);
        }
    }

    private static void activateSwordsOfRevealingLight(Board myBoard) {
        myBoard.getEffectsStatus().setRivalReveled(true);
        myBoard.getEffectsStatus().setCanRivalAttack(false);
    }

    private static void deActivateSwordsOfRevealingLight(Board board) {
        board.getEffectsStatus().setRivalReveled(false);
        board.getEffectsStatus().setCanRivalAttack(true);
    }
}
