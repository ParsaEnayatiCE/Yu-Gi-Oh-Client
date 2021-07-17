package models.cards.monsters;

import java.util.HashMap;

public enum MonsterType {
    BEAST_WARRIOR("Beast-Warrior"),
    WARRIOR("Warrior"),
    FIEND("Fiend"),
    AQUA("Aqua"),
    BEAST("Beast"),
    PYRO("Pyro"),
    SPELL_CASTER("Spellcaster"),
    THUNDER("Thunder"),
    DRAGON("Dragon"),
    MACHINE("Machine"),
    ROCK("Rock"),
    INSECT("Insect"),
    CYBERSE("Cyberse"),
    FAIRY("Fairy"),
    SEA_SERPENT("Sea Serpent");

    private static final HashMap<String, MonsterType> MAP = new HashMap<>();
    private final String label;

    MonsterType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static MonsterType getByName(String name) {
        return MAP.get(name);
    }

    static {
        for (MonsterType field : MonsterType.values()) {
            MAP.put(field.getLabel(), field);
        }
    }
}
