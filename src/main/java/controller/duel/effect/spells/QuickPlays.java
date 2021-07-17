package controller.duel.effect.spells;

import models.Board;
import models.Chain;
import models.cards.Location;
import models.cards.spelltrap.SpellTrapCard;

import java.util.Scanner;

public class QuickPlays {

    public static boolean activate(SpellTrapCard spellTrapCard, Board myBoard, Board rivalBoard) {
        if (spellTrapCard.getName().equals("Twin Twisters")) {
            if (Chain.getSize() == 0) activateTwinTwister(myBoard, rivalBoard);
            else Chain.addSpell(spellTrapCard, myBoard, rivalBoard, null);
        } else if (spellTrapCard.getName().equals("Mystical space typhoon")) {
            if (Chain.getSize() == 0) activateMysticalSpaceTyphoon(rivalBoard);
            else Chain.addSpell(spellTrapCard, myBoard, rivalBoard, null);
        } else if (spellTrapCard.getName().equals("Ring of defense")) {
            if (Chain.getSize() == 0) activateRingOfDefenses(myBoard);
            else Chain.addSpell(spellTrapCard, myBoard, rivalBoard, null);
        } else
            return false;
        return true;
    }

    public static void activateRingOfDefenses(Board myBoard) {
        myBoard.getEffectsStatus().setRivalTrapsBlocked(true);
    }

    public static void activateMysticalSpaceTyphoon(Board rivalBoard) {
        removeTrapSpell(rivalBoard);
    }

    public static void activateTwinTwister(Board myBoard, Board rivalBoard) {
        if (myBoard.getHandCards().size() < 1 || rivalBoard.getSpellTraps().size() < 2)
            return;
        myBoard.getHandCards().get(1).setLocation(Location.GRAVEYARD);
        myBoard.removeCardsFromHand(1);
        System.out.println("Do you want to remove rival spells?(y/any key)");
        for (int i = 0; i < 2; i++)
            removeTrapSpell(rivalBoard);
    }

    private static void removeTrapSpell(Board rivalBoard) {
        if (rivalBoard.getSpellTraps().size() < 1)
            return;
        rivalBoard.getSpellTraps().get(1).setLocation(Location.GRAVEYARD);
        rivalBoard.removeSpellAndTrap(1);
    }
}
