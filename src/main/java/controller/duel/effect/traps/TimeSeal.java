package controller.duel.effect.traps;

import models.Board;
import models.Chain;
import models.cards.spelltrap.SpellTrapCard;

public class TimeSeal {

    public static boolean activate(SpellTrapCard trapCard, Board myBoard) {
        if (trapCard.getName().equals("Time Seal"))
            Chain.addSpell(trapCard, myBoard, null, null);
        else
            return false;
        return true;
    }

    public static void activateTimeSeal(Board myBoard) {
        myBoard.getEffectsStatus().setCanRivalPickCard(false);
    }

}
