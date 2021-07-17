package controller.duel.effect.spells;

import models.Board;
import models.cards.Card;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;

import java.util.ArrayList;

public class OnMonsterSpells {

    private static final ArrayList<MonsterCard> controlledMonsters = new ArrayList<>();

    public static boolean activate(SpellTrapCard spellCard, Board myBoard, Board rivalBoard, int monsterIndex) {
        if (spellCard.getName().equals("Change of Heart"))
            activateChangeOfHeart(monsterIndex, myBoard, rivalBoard);
        else if (spellCard.getName().equals("Monster Reborn"))
            activateMonsterReborn(monsterIndex, myBoard);
        else
            return false;
        NormalActivate.removeSpellCard(spellCard, myBoard);
        return true;
    }

    private static void activateMonsterReborn(int monsterIndex, Board myBoard) {
        MonsterCard monsterCard;
        if (monsterIndex < myBoard.getGraveyardCards().size()) {
            Card card = myBoard.getGraveyardCards().get(monsterIndex);
            if (card == null || card instanceof SpellTrapCard) {
                System.out.println("monster number invalid");
                return;
            }
            monsterCard = (MonsterCard) card;
        }
        else return;
        myBoard.summonOrSetMonster(monsterCard);
        myBoard.getGraveyardCards().remove(monsterIndex);
    }

    private static void activateChangeOfHeart(int monsterIndex, Board myBoard, Board rivalBoard) {
        MonsterCard monsterCard;
        if (monsterIndex < rivalBoard.getMonsters().size()) {
            monsterCard = rivalBoard.getMonsters().get(monsterIndex);
            if (monsterCard == null) {
                System.out.println("monster number invalid");
                return;
            }
        }
        else return;
        myBoard.summonOrSetMonster(monsterCard);
        rivalBoard.getMonsters().remove(monsterCard);
        controlledMonsters.add(monsterCard);
    }

    public static void deactivate(Board myBoard, Board rivalBoard) {
        for (int i = controlledMonsters.size() - 1; i >= 0; i--) {
            rivalBoard.summonOrSetMonster(controlledMonsters.get(i));
            myBoard.getMonsters().remove(controlledMonsters.get(i));
        }
    }

}
