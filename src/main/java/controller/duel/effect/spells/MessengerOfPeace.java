package controller.duel.effect.spells;

import models.Board;
import models.cards.spelltrap.SpellTrapCard;

import java.util.ArrayList;

public class MessengerOfPeace {
    private static final ArrayList<SpellTrapCard> messengers = new ArrayList<>();

    public static boolean activate(SpellTrapCard spellTrapCard, Board myBoard) {
        if (!spellTrapCard.getName().equals("Messenger of peace"))
            return false;

        if (messengers.contains(spellTrapCard))
            return false;

        myBoard.getEffectsStatus().setCanStrongRivalAttack(false);
        messengers.add(spellTrapCard);
        return true;
    }


    public static void checkStandBy(Board myBoard) {
        for (SpellTrapCard spellTrapCard : myBoard.getSpellTraps()) {
            if(messengers.contains(spellTrapCard))
                myBoard.getOwner().getPlayerBoard().setLifePoints(myBoard.getLifePoints() - 100);
        }
    }
}
