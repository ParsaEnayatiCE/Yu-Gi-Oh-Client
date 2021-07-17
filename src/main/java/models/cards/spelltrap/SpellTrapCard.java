package models.cards.spelltrap;

import javafx.scene.image.Image;
import models.cards.Card;
import models.cards.CardImage;
import models.cards.CardType;
import models.cards.MakeCards;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SpellTrapCard extends Card {
    private static ArrayList<SpellTrapCard> allSpellTrapCards = new ArrayList<>();
    private static final ArrayList<SpellTrapCard> allSpellTrapCardsToShow = new ArrayList<>();
    Icon icon;
    boolean isLimited;

    public SpellTrapCard(String name, String description, int price, CardType cardType, Icon icon, boolean isLimited) {
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setCardType(cardType);
        this.setIcon(icon);
        this.setLimited(isLimited);
        this.setCardNumber(++cardCounter);
        allSpellTrapCards.add(this);
        allCards.add(this);
    }

    public SpellTrapCard(String name, String description, int price, CardType cardType, Icon icon, boolean isLimited, int cardNumber) {
        this.setName(name);
        this.setDescription(description);
        this.setPrice(price);
        this.setCardType(cardType);
        this.setIcon(icon);
        this.setLimited(isLimited);
        this.setCardNumber(cardNumber);
    }

    private SpellTrapCard(String name, int price) {
        this.setName(name);
        this.setPrice(price);
    }

    public Image getImage() {
        return CardImage.getImageByName(getName());
    }

    public static ArrayList<SpellTrapCard> getAllSpellTrapCards() {
        return allSpellTrapCards;
    }

    public static ArrayList<SpellTrapCard> getAllSpellTrapCardsToShow() throws IOException {
        if (allSpellTrapCardsToShow.size() > 0)
            return allSpellTrapCardsToShow;

        String line;
        boolean isFirstLine = true;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("SpellTrap.csv"));
        while ((line = bufferedReader.readLine()) != null) {
            String[] values = line.split(",");
            if (isFirstLine)
                isFirstLine = false;
            else
            allSpellTrapCardsToShow.add(new SpellTrapCard(values[0], Integer.parseInt(values[values.length - 1])));
        }
        return allSpellTrapCardsToShow;
    }

    public static void setAllSpellTrapCards(ArrayList<SpellTrapCard> cards) {
        try {
            allCards.addAll(cards);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        allSpellTrapCards = cards;
    }

    @Override
    public Object clone() {
        return MakeCards.makeCard(getName());
    }

    private void setIcon(Icon icon) {
        this.icon = icon;
    }

    public Icon getIcon() {
        return this.icon;
    }

    private void setLimited(boolean isLimited) {
        this.isLimited = isLimited;
    }

    public boolean getLimited() {
        return this.isLimited;
    }

    @Override
    public String toString() {
        return "Name: " + this.getName() + "\n" +
                this.getCardType().getLabel() + "\n" +
                "Type: " + this.getIcon().getLabel() + "\n" +
                "Description: " + this.getDescription();
    }
}
