package models.cards.spelltrap;

import java.util.HashMap;

public enum Icon {
    EQUIP("Equip"),
    FIELD("Field"),
    QUICK_PLAY("Quick-Play"),
    RITUAL("Ritual"),
    CONTINUOUS("Continuous"),
    COUNTER("Counter"),
    NORMAL("Normal");

    private static final HashMap<String, Icon> MAP = new HashMap<>();
    private final String label;

    Icon(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static Icon getByName(String name) {
        return MAP.get(name);
    }

    static {
        for (Icon field : Icon.values()) {
            MAP.put(field.getLabel(), field);
        }
    }
}
