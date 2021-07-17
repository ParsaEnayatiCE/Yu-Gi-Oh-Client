package controller.duel.effect.traps;

import models.Board;
import models.Chain;
import models.cards.Location;
import models.cards.monsters.MonsterCard;
import models.cards.monsters.SummonType;
import models.cards.spelltrap.SpellTrapCard;

public class SummonTraps {
    public static boolean activate(SpellTrapCard spellTrapCard, MonsterCard summonedCard, Board myBoard, Board rivalBoard) {
        if (spellTrapCard.getName().equals("Trap Hole"))
            Chain.addSpell(spellTrapCard, myBoard, rivalBoard, summonedCard);
        else if (spellTrapCard.getName().equals("Torrential Tribute"))
            Chain.addSpell(spellTrapCard, myBoard, rivalBoard, summonedCard);
        else if (spellTrapCard.getName().equals("Solemn Warning"))
            Chain.addSpell(spellTrapCard, myBoard, rivalBoard, summonedCard);
        else
            return false;

        spellTrapCard.setLocation(Location.GRAVEYARD);
        myBoard.removeSpellAndTrap(myBoard.getSpellTrapIndexInSpellTrapBoard(spellTrapCard));
        return true;
    }

    public static void activateTrapHole(MonsterCard summonedCard, Board rivalBoard) {
        if (summonedCard.getAttackPoint() > 1000 &&
                (summonedCard.getSummonType().equals(SummonType.FLIP_SUMMON) || summonedCard.getSummonType().equals(SummonType.NORMAL_SUMMON))) {
            summonedCard.setLocation(Location.GRAVEYARD);
            rivalBoard.removeMonster(rivalBoard.getMonsterIndexInMonsterBoard(summonedCard));
        }
    }

    public static void activateTorrentialTribute(Board myBoard, Board rivalBoard) {
        removeAllMonsters(myBoard);
        removeAllMonsters(rivalBoard);
    }

    public static void activateSolemnWarning(MonsterCard summonedCard, Board myBoard, Board rivalBoard) {
        myBoard.getOwner().getPlayerBoard().setLifePoints(myBoard.getOwner().getPlayerBoard().getLifePoints() - 2000);
        summonedCard.setLocation(Location.GRAVEYARD);
        rivalBoard.removeMonster(rivalBoard.getMonsterIndexInMonsterBoard(summonedCard));
    }

    private static void removeAllMonsters(Board board) {
        for (int i = board.getMonsters().size() - 1; i >= 0; i--) {
            board.getMonsters().get(i).setLocation(Location.GRAVEYARD);
            board.removeMonster(i);
        }
    }
}
