package controller.duel.effect;

import models.Board;
import models.cards.Card;
import models.cards.Location;
import models.cards.monsters.MonsterCard;

import java.util.LinkedList;

public class CustomEffects {
    private static final LinkedList<String> killMonsters = new LinkedList<>();
    private static final LinkedList<String> killSpellTraps = new LinkedList<>();
    private static final LinkedList<String> increaseLife = new LinkedList<>();
    private static final LinkedList<String> decreaseLife = new LinkedList<>();
    private static final LinkedList<String> increaseAttackPoints = new LinkedList<>();
    private static final LinkedList<String> increaseDefensePoints = new LinkedList<>();

    public static void addEffect(CustomEffect customEffect, String cardName) {
        if (customEffect == CustomEffect.KILL_MONSTER)
            killMonsters.add(cardName);
        else if (customEffect == CustomEffect.KILL_SPELL_TRAP)
            killSpellTraps.add(cardName);
        else if (customEffect == CustomEffect.INCREASE_LIFE_POINT)
            increaseLife.add(cardName);
        else if (customEffect == CustomEffect.DECREASE_LIFE_POINT)
            decreaseLife.add(cardName);
        else if (customEffect == CustomEffect.INCREASE_ATTACK_POINT)
            increaseAttackPoints.add(cardName);
        else if (customEffect == CustomEffect.INCREASE_DEFENSE_POINT)
            increaseDefensePoints.add(cardName);
    }

    private static boolean isCardNameInList(LinkedList<String> list, String cardName) {
        for (String name : list) {
            if (name.equals(cardName))
                return true;
        }
        return false;
    }

    public static boolean activate(Card card, Board myBoard, Board rivalBoard) {
        boolean status = false;
        if (isCardNameInList(killMonsters, card.getName())) {
            activateKillMonster(rivalBoard);
            status = true;
        }
        if (isCardNameInList(killSpellTraps, card.getName())) {
            activeKillSpellTraps(rivalBoard);
            status = true;
        }
        if (isCardNameInList(increaseLife, card.getName())) {
            activateIncreaseLife(myBoard);
            status = true;
        }
        if (isCardNameInList(decreaseLife, card.getName())) {
            activateDecreaseLife(rivalBoard);
            status = true;
        }
        if (isCardNameInList(increaseAttackPoints, card.getName())) {
            activateIncreaseAttack(card);
            status = true;
        }
        if (isCardNameInList(increaseDefensePoints, card.getName())) {
            activateIncreaseDefense(card);
            status = true;
        }
        return status;
    }

    private static void activateKillMonster(Board rivalBoard) {
        int maxPrice = 0, maxIndex = -1;
        for (int i = rivalBoard.getMonsterBoard().size(); i >= 0; i--) {
            if (rivalBoard.getMonsterBoard().get(i).getPrice() > maxPrice) {
                maxIndex = i;
                maxPrice = rivalBoard.getMonsterBoard().get(i).getPrice();
            }
        }
        if (maxIndex == -1)
            return;
        rivalBoard.getMonsterBoard().get(maxIndex).setLocation(Location.GRAVEYARD);
        rivalBoard.removeMonster(maxIndex);
    }

    private static void activeKillSpellTraps(Board rivalBoard) {
        int maxPrice = 0, maxIndex = -1;
        for (int i = rivalBoard.getSpellTraps().size(); i >= 0; i--) {
            if (rivalBoard.getSpellTraps().get(i).getPrice() > maxPrice) {
                maxIndex = i;
                maxPrice = rivalBoard.getSpellTraps().get(i).getPrice();
            }
        }
        if (maxIndex == -1)
            return;
        rivalBoard.getSpellTraps().get(maxIndex).setLocation(Location.GRAVEYARD);
        rivalBoard.removeSpellAndTrap(maxIndex);
    }

    private static void activateIncreaseLife(Board myBoard) {
        myBoard.setLifePoints(myBoard.getLifePoints() + 500);
    }

    private static void activateDecreaseLife(Board rivalBoard) {
        rivalBoard.setLifePoints(rivalBoard.getLifePoints() - 500);
    }

    private static void activateIncreaseAttack(Card card) {
        if (!(card instanceof MonsterCard))
            return;
        MonsterCard monsterCard = (MonsterCard) card;
        monsterCard.setAttackPoint(monsterCard.getAttackPoint() + 500);
    }

    private static void activateIncreaseDefense(Card card) {
        if (!(card instanceof MonsterCard))
            return;
        MonsterCard monsterCard = (MonsterCard) card;
        monsterCard.setDefensePoint(monsterCard.getDefensePoint() + 500);
    }
}
