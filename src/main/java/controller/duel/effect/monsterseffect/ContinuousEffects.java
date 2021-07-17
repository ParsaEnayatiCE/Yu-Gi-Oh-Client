package controller.duel.effect.monsterseffect;

import models.Board;
import models.cards.Location;
import models.cards.monsters.MonsterCard;

import java.util.ArrayList;

// For Command Knight And Mirage Dragon
public class ContinuousEffects {

    private static final ArrayList<MonsterCard> continuousMonsters = new ArrayList<>();

    public static void run(Board myBoard, Board rivalBoard) {
        removeDeadMonsters(myBoard, rivalBoard);
        for (MonsterCard monsterCard : myBoard.getMonsters()) {
            if (!continuousMonsters.contains(monsterCard)) {
                if (monsterCard.getName().equals("Command Knight")) {
                    activateCommandKnight(myBoard, rivalBoard);
                    continuousMonsters.add(monsterCard);
                }
                else if (monsterCard.getName().equals("Mirage Dragon")) {
                    activateMirageDragon(myBoard);
                    continuousMonsters.add(monsterCard);
                }
            }
        }
    }

    private static void activateCommandKnight(Board myBoard, Board rivalBoard) {
        for (MonsterCard monsterCard : myBoard.getMonsters()) {
            monsterCard.setAttackPoint(monsterCard.getAttackPoint() + 400);
        }
        for (MonsterCard monsterCard : rivalBoard.getMonsters()) {
            monsterCard.setAttackPoint(monsterCard.getAttackPoint() + 400);
        }
    }

    private static void activateMirageDragon(Board myBoard) {
        myBoard.getEffectsStatus().setRivalTrapsBlocked(true);
    }

    private static void removeDeadMonsters(Board myBoard, Board rivalBoard) {
        for (int i = continuousMonsters.size() - 1; i >= 0; i--) {
            if (continuousMonsters.get(i).getLocation().equals(Location.GRAVEYARD)) {
                if (continuousMonsters.get(i).getName().equals("Command Knight"))
                    deactivateCommandKnight(myBoard, rivalBoard);
                else
                    deactivateMirageDragon(myBoard);
                continuousMonsters.remove(i);
            }
        }
    }

    private static void deactivateMirageDragon(Board myBoard) {
        myBoard.getEffectsStatus().setRivalTrapsBlocked(false);
    }

    private static void deactivateCommandKnight(Board myBoard, Board rivalBoard) {
        for (MonsterCard monsterCard : myBoard.getMonsters()) {
            monsterCard.setAttackPoint(monsterCard.getAttackPoint() - 400);
        }
        for (MonsterCard monsterCard : rivalBoard.getMonsters()) {
            monsterCard.setAttackPoint(monsterCard.getAttackPoint() - 400);
        }
    }

}
