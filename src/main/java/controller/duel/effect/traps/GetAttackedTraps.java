package controller.duel.effect.traps;

import models.Board;
import models.cards.Location;
import models.cards.monsters.Mode;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;

public class GetAttackedTraps {

    public static boolean activate(MonsterCard attackerCard, Board myBoard, Board rivalBoard) {
        boolean hasActivated = false;
        for (int i = myBoard.getSpellTraps().size() - 1; i >= 0 ; i--) {
            SpellTrapCard trapCard = myBoard.getSpellTraps().get(i);
            if (trapCard.getName().equals("Magic Cylinder"))
                hasActivated = activateMagicCylinder(attackerCard, rivalBoard);
            else if (trapCard.getName().equals("Mirror Force"))
                hasActivated = activateMirrorForce(rivalBoard);
            else if (trapCard.getName().equals("Negate Attack"))
                hasActivated = activateNegateAttack();
            if (hasActivated) {
                trapCard.setLocation(Location.GRAVEYARD);
                myBoard.removeSpellAndTrap(i);
            }
        }
        return hasActivated;
    }

    private static boolean activateMagicCylinder(MonsterCard attacker, Board rivalBoard) {
        rivalBoard.getOwner().getPlayerBoard().setLifePoints(rivalBoard.getOwner().getPlayerBoard().getLifePoints() - attacker.getAttackPoint());
        return true;
    }

    private static boolean activateMirrorForce(Board rivalBoard) {
        for (int i = rivalBoard.getMonsters().size() - 1; i >= 0; i--) {
            if (rivalBoard.getMonsters().get(i).getMode().equals(Mode.ATTACK)) {
                rivalBoard.getMonsters().get(i).setLocation(Location.GRAVEYARD);
                rivalBoard.removeMonster(rivalBoard.getMonsterIndexInMonsterBoard(rivalBoard.getMonsters().get(i)));
            }
        }
        return true;
    }

    private static boolean activateNegateAttack() {
        return true;
    }
}
