package controller.duel.effect.traps;

import models.Board;
import models.Chain;
import models.cards.Card;
import models.cards.Location;
import models.cards.monsters.SpecialSummonStatus;
import models.cards.spelltrap.SpellTrapCard;

import java.util.Random;
import java.util.Scanner;

public class NormalTraps {
    public static boolean activate(SpellTrapCard trapCard, Board myBoard, Board rivalBoard) {
        if (trapCard.getName().equals("Mind Crush"))
            Chain.addSpell(trapCard, myBoard, rivalBoard, null);
        else if (trapCard.getName().equals("Call of The Haunted"))
            Chain.addSpell(trapCard, myBoard, rivalBoard, null);
        else
            return false;

        trapCard.setLocation(Location.GRAVEYARD);
        myBoard.removeSpellAndTrap(myBoard.getSpellTrapIndexInSpellTrapBoard(trapCard));
        return true;
    }

    public static void activateCallOfHaunted(Board myBoard) {
        myBoard.getEffectsStatus().setSpecialSummonStatus(SpecialSummonStatus.FROM_GRAVEYARD);
    }

    public static void activateMindCrush(Board myBoard, Board rivalBoard) {
        boolean hasFoundName = false;
        Scanner scanner = new Scanner(System.in);
        String cardName = scanner.nextLine();
        for (int i = rivalBoard.getHandCards().size() - 1; i >= 0; i--) {
            if (rivalBoard.getHandCards().get(i).getName().equals(cardName)) {
                rivalBoard.getHandCards().get(i).setLocation(Location.GRAVEYARD);
                rivalBoard.removeCardsFromHand(i);
                hasFoundName = true;
            }
        }
        if (!hasFoundName) {
            Random random = new Random();
            int index = random.nextInt(myBoard.getHandCards().size());
            Card card = myBoard.getHandCards().get(index);
            card.setLocation(Location.GRAVEYARD);
            myBoard.getHandCards().remove(card);
            myBoard.addToGraveyard(card);
        }
    }
}
