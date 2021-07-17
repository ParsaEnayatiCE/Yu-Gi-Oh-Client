package models.cards;

import models.cards.monsters.Attribute;
import models.cards.monsters.MonsterCard;
import models.cards.monsters.MonsterType;
import models.cards.monsters.Trait;
import models.cards.spelltrap.Icon;
import models.cards.spelltrap.SpellTrapCard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MakeCards {
    public static Card makeCard(String name) {
        try {
            Card card = makeMonsterCard(name);
            if (card != null)
                return card;
            return makeSpellTrapCards(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Card makeMonsterCard(String name) throws IOException {
        String line;
        boolean isFirstLine = true;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("Monster.csv"));
        while ((line = bufferedReader.readLine()) != null) {
            String[] values = line.split(",");
            if (isFirstLine)
                isFirstLine = false;
            else if (name.equals(values[0])) {
                return new MonsterCard(name, values[7], Integer.parseInt(values[values.length - 1]), Integer.parseInt(values[1]),
                        Attribute.getByName(values[2]), MonsterType.getByName(values[3]), Integer.parseInt(values[5]),
                        Integer.parseInt(values[6]), Trait.getByName(values[4]));
            }
        }
        return null;
    }

    public static Card makeSpellTrapCards(String name) throws IOException {
        String line;
        boolean isFirstLine = true;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("SpellTrap.csv"));
        while ((line = bufferedReader.readLine()) != null) {
            String[] values = line.split(",");
            if (isFirstLine)
                isFirstLine = false;
            else if (name.equals(values[0])) {
                if (values[4].equals("Limited"))
                    return new SpellTrapCard(name, values[3], Integer.parseInt(values[values.length - 1]), CardType.getByName(values[1]), Icon.getByName(values[2]), true);
                else
                    return new SpellTrapCard(name, values[3], Integer.parseInt(values[values.length - 1]), CardType.getByName(values[1]), Icon.getByName(values[2]), false);
            }
        }
        return null;
    }
}