package controller.duel.effect.monsterseffect;

import models.Board;
import models.cards.Card;
import models.cards.CardType;
import models.cards.Location;
import models.cards.monsters.MonsterCard;
import models.cards.monsters.SpecialSummonStatus;

import java.util.Scanner;

public class TurnEffects {
    public static void run(Board myBoard, Board rivalBoard) {
        for (MonsterCard monsterCard : myBoard.getMonsters()) {
            if (monsterCard.getName().equals("Scanner"))
                affectScanner(rivalBoard, monsterCard);
            else if (monsterCard.getName().equals("Herald of Creation"))
                affectHeraldOfCreation(myBoard);
        }
    }

    private static void affectHeraldOfCreation(Board myBoard) {
        if (!isAnyMonsterInGrave(myBoard))
            return;
        if (myBoard.getHandCards().size() < 1)
            return;
        myBoard.getHandCards().get(1).setLocation(Location.GRAVEYARD);
        myBoard.removeCardsFromHand(1);
        myBoard.getEffectsStatus().setSpecialSummonStatus(SpecialSummonStatus.LEVEL7H_FROM_GRAVE);
    }

    private static void affectScanner(Board rivalBoard, MonsterCard scannerCard) {
        if (!isAnyMonsterInGrave(rivalBoard))
            return;
        for (Card graveyardCard : rivalBoard.getGraveyardCards()) {
            MonsterCard graveyardMonster;
            if (graveyardCard instanceof MonsterCard) {
                graveyardMonster = (MonsterCard) graveyardCard;
                scannerCard.setName(graveyardMonster.getName());
                scannerCard.setDescription(graveyardMonster.getDescription());
                scannerCard.setPrice(graveyardMonster.getPrice());
                scannerCard.setLevel(graveyardMonster.getLevel());
                scannerCard.setAttribute(graveyardMonster.getAttribute());
                scannerCard.setCardType(CardType.MONSTER);
                scannerCard.setMonsterType(graveyardMonster.getMonsterType());
                scannerCard.setAttackPoint(graveyardMonster.getAttackPoint());
                scannerCard.setDefensePoint(graveyardMonster.getDefensePoint());
                scannerCard.setTrait(graveyardMonster.getTrait());
                break;
            }
        }
    }

    public static boolean isAnyMonsterInGrave(Board board) {
        for (Card graveyardCard : board.getGraveyardCards()) {
            if (graveyardCard instanceof MonsterCard)
                return true;
        }
        return false;
    }
}
