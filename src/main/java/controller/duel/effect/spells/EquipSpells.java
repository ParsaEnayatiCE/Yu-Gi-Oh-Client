package controller.duel.effect.spells;

import models.cards.monsters.Mode;
import models.cards.monsters.MonsterCard;
import models.cards.monsters.MonsterType;
import models.cards.spelltrap.SpellTrapCard;

import java.util.HashMap;

public class EquipSpells {
    private static final HashMap<SpellTrapCard, MonsterCard> equipped = new HashMap<>();

    public static boolean equip(SpellTrapCard spellTrapCard, MonsterCard monsterCard) {
        boolean hasEquippedBefore = equipped.containsKey(spellTrapCard);
        if (hasEquippedBefore)
            return false;

        if (spellTrapCard.getName().equals("Sword of dark destruction"))
            equipSwordOfDarkDestruction(spellTrapCard, monsterCard);
        else if (spellTrapCard.getName().equals("Black Pendant"))
            equipBlackPendant(spellTrapCard, monsterCard);
        else if (spellTrapCard.getName().equals("United We Stand"))
            equipUnitedWeStand(monsterCard);
        else if (spellTrapCard.getName().equals("Magnum Shield"))
            equipMagnumShield(monsterCard);

        return true;
    }

    private static void equipSwordOfDarkDestruction(SpellTrapCard spellTrapCard, MonsterCard monsterCard) {
        if (monsterCard.getMonsterType().equals(MonsterType.FIEND) || monsterCard.getMonsterType().equals(MonsterType.SPELL_CASTER)) {
            monsterCard.setAttackPoint(monsterCard.getAttackPoint() + 400);
            monsterCard.setDefensePoint(monsterCard.getDefensePoint() - 200);
        }
        equipped.put(spellTrapCard, monsterCard);
    }

    private static void equipBlackPendant(SpellTrapCard spellTrapCard, MonsterCard monsterCard) {
        monsterCard.setAttackPoint(monsterCard.getAttackPoint() + 500);
        equipped.put(spellTrapCard, monsterCard);
    }

    private static void equipUnitedWeStand(MonsterCard monsterCard) {
        if (monsterCard.getIsHidden())
            return;
        monsterCard.setAttackPoint(monsterCard.getAttackPoint() + 800);
        monsterCard.setDefensePoint(monsterCard.getDefensePoint() + 800);
    }

    private static void equipMagnumShield(MonsterCard monsterCard) {
        if (!monsterCard.getMonsterType().equals(MonsterType.WARRIOR))
            return;
        if (monsterCard.getMode().equals(Mode.ATTACK))
            monsterCard.setAttackPoint(monsterCard.getAttackPoint() + monsterCard.getDefensePoint());
        else
            monsterCard.setDefensePoint(monsterCard.getDefensePoint() + monsterCard.getAttackPoint());
    }
}
