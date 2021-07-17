package controller.duel.effect.monsterseffect;

import models.Board;
import models.cards.Location;
import models.cards.monsters.MonsterCard;
import models.cards.monsters.SpecialSummonStatus;

import java.util.ArrayList;
import java.util.Scanner;

public class GetAttackedEffects {

    private static final ArrayList<MonsterCard> hasBeenAttacked = new ArrayList<>();

    public static boolean run(MonsterCard attackerCard, MonsterCard attackedCard, Board myBoard, Board rivalBoard) {
        if ((attackedCard.getName().equals("Yomi Ship") || attackedCard.getName().equals("Exploder Dragon")))
            return killAttacker(attackedCard, attackerCard, myBoard);
        else if (attackedCard.getName().equals("Suijin") && !attackedCard.getIsHidden())
            return affectSuijin(attackedCard);
        else if (attackedCard.getName().equals("Marshmallon"))
            return affectMarshMelon(myBoard, attackedCard);
        else if (attackedCard.getName().equals("Texchanger"))
            return affectTexChanger(myBoard);
        else if (attackedCard.getName().equals("Command Knight") && !attackedCard.getIsHidden())
            return affectCommandKnight(rivalBoard);
        return false;
    }

    private static boolean affectCommandKnight(Board rivalBoard) {
        return rivalBoard.getMonsters().size() != 1;
    }

    private static boolean affectTexChanger(Board myBoard) {
        myBoard.getEffectsStatus().setSpecialSummonStatus(SpecialSummonStatus.NORMAL_CYBERSE);
        return true;
    }

    private static boolean affectMarshMelon(Board myBoard, MonsterCard attackedCard) {
        attackedCard.setLocation(Location.FIELD);

        if (!attackedCard.getIsHidden())
            return true;

        myBoard.getOwner().getPlayerBoard().setLifePoints(myBoard.getOwner().getPlayerBoard().getLifePoints() - 1000);
        return true;
    }

    private static boolean affectSuijin(MonsterCard attackedCard) {
        if (hasBeenAttacked.contains(attackedCard))
            return false;
        attackedCard.setLocation(Location.FIELD);
        hasBeenAttacked.add(attackedCard);
        return true;
    }

    private static boolean killAttacker(MonsterCard attackedCard, MonsterCard attackerCard, Board myBoard) {
        attackerCard.setLocation(Location.GRAVEYARD);
        myBoard.removeMonster(myBoard.getMonsterIndexInMonsterBoard(attackedCard));
        return !attackedCard.getName().equals("Yomi Ship");
    }
}
