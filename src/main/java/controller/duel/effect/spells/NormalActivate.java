package controller.duel.effect.spells;

import models.Board;
import models.Chain;
import models.cards.CardType;
import models.cards.Location;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.Icon;
import models.cards.spelltrap.SpellTrapCard;

public class NormalActivate {

    public static boolean activate(SpellTrapCard spellCard, Board myBoard, Board rivalBoard) {
        if (spellCard.getIsHidden())
            return false;
        if (spellCard.getName().equals("Pot of Greed"))
            Chain.addSpell(spellCard, myBoard, rivalBoard,null);
        else if (spellCard.getName().equals("Raigeki"))
            Chain.addSpell(spellCard, myBoard, rivalBoard, null);
        else if (spellCard.getName().equals("Harpie's Feather Duster"))
            Chain.addSpell(spellCard, myBoard, rivalBoard, null);
        else if (spellCard.getName().equals("Dark Hole"))
            Chain.addSpell(spellCard, myBoard, rivalBoard, null);
        else if (spellCard.getName().equals("Terraforming"))
            Chain.addSpell(spellCard, myBoard, rivalBoard, null);
        else if (spellCard.getName().equals("Advanced Ritual Art"))
            Chain.addSpell(spellCard, myBoard, rivalBoard, null);
        else
            return false;
        removeSpellCard(spellCard, myBoard);
        return true;
    }

    public static void activateTeraforming(Board myBoard) {
        for (int i = myBoard.getDeck().getMainDeck().size() - 1; i >= 0 ; i--) {
            if (myBoard.getDeck().getMainDeck().get(i).getCardType() == CardType.SPELL) {
                SpellTrapCard spellTrapCard = (SpellTrapCard) myBoard.getDeck().getMainDeck().get(i);
                if (spellTrapCard.getIcon() == Icon.FIELD)
                    myBoard.setFieldZone(spellTrapCard);
            }
        }
    }

    public static void affectDarkHole(Board myBoard, Board rivalBoard) {
        for (int i = myBoard.getMonsters().size() - 1; i >= 0; i--) {
            myBoard.getMonsters().get(i).setLocation(Location.GRAVEYARD);
            myBoard.removeMonster(i);
        }
        for (int i = rivalBoard.getMonsters().size() - 1; i >= 0; i--) {
            rivalBoard.getMonsters().get(i).setLocation(Location.GRAVEYARD);
            rivalBoard.removeMonster(i);
        }
    }

    public static void affectHarpie(Board rivalBoard) {
        for (int i = rivalBoard.getSpellTraps().size() - 1; i >= 0; i--) {
            rivalBoard.getSpellTraps().get(i).setLocation(Location.GRAVEYARD);
            rivalBoard.removeSpellAndTrap(i);
        }
    }


    public static void affectReigeki(Board rivalBoard) {
        for (int i = rivalBoard.getMonsters().size() - 1; i >= 0; i--) {
            rivalBoard.getMonsters().get(i).setLocation(Location.GRAVEYARD);
            rivalBoard.removeMonster(i);
        }
    }

    public static void affectPotOfGreed(Board myBoard) {
        if (myBoard.getDeck().getMainDeck().size() < 2)
            return;
        for (int i = 0; i < 2; i++) {
            if (myBoard.getDeck().getMainDeck().get(i).getCardType() == CardType.SPELL)
                myBoard.summonOrSetSpellAndTrap((SpellTrapCard) myBoard.getDeck().getMainDeck().get(i));
            else
                myBoard.summonOrSetMonster((MonsterCard) myBoard.getDeck().getMainDeck().get(i));
        }
    }

    public static void removeSpellCard(SpellTrapCard spellCard, Board myBoard) {
        spellCard.setLocation(Location.GRAVEYARD);
        myBoard.removeSpellAndTrap(myBoard.getSpellTrapIndexInSpellTrapBoard(spellCard));
    }

    public static void activateRitual(Board myBoard) {
        myBoard.getEffectsStatus().setCanRitualSummon(true);
    }
}
