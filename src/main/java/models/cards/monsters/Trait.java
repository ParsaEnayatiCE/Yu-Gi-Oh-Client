package models.cards.monsters;

import java.util.HashMap;

public enum Trait {
    NORMAL("Normal"),
    EFFECT("Effect"),
    RITUAL("Ritual");

    private static final HashMap<String, Trait> MAP = new HashMap<>();
    private final String label;

    Trait(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static Trait getByName(String name) {
        return MAP.get(name);
    }

    static {
        for (Trait field : Trait.values()) {
            MAP.put(field.getLabel(), field);
        }
    }
}
