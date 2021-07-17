package models.cards;

import models.cards.monsters.Attribute;

import java.util.HashMap;

public enum CardType {
    MONSTER("Monster"),
    SPELL("Spell"),
    TRAP("Trap");

    private static final HashMap<String, CardType> MAP = new HashMap<>();
    private final String label;

    CardType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static CardType getByName(String name) {
        return MAP.get(name);
    }

    static {
        for (CardType field : CardType.values()) {
            MAP.put(field.getLabel(), field);
        }
    }

}
