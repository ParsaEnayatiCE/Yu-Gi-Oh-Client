package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

import models.cards.Card;
import models.cards.CardType;
import models.cards.MakeCards;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;


public class Deck {

    public static ArrayList<Deck> allDecks;
    private ArrayList<MonsterCard> mainDeckMonsters = new ArrayList<>();
    private ArrayList<SpellTrapCard> mainDeckSpellTraps = new ArrayList<>();
    private ArrayList<MonsterCard> sideDeckMonsters = new ArrayList<>();
    private ArrayList<SpellTrapCard> sideDeckSpellTraps = new ArrayList<>();
    private final HashMap<String, Integer> cardsAmount = new HashMap<>();
    private String name;
    private String ownerName;

    static {
        allDecks = new ArrayList<>();
    }

    public Object clone() throws CloneNotSupportedException {
        Deck newDeck = new Deck(this.name, this.ownerName, this.getCardsAmount());
        for (Card mainDeckCard : this.getMainDeck())
            newDeck.addCardToDeck(true, (Card) mainDeckCard.clone());
        for (Card sideDeckCard : this.getSideDeck())
            newDeck.addCardToDeck(false, (Card) sideDeckCard.clone());
        return newDeck;
    }

    public Deck(String name, String ownerName) {
        allDecks.add(this);
        setName(name);
        setOwner(ownerName);
    }

    public Deck(String name, String ownerName, ArrayList<Card> mainDeck, ArrayList<Card> sideDeck, HashMap<Card, Integer> cards) {
        allDecks.add(this);
        setName(name);
        setOwner(ownerName);
        setMainDeck(mainDeck);
        setSideDeck(sideDeck);
        setCardsAmount(cards);
    }

    public Deck(String name, String ownerName, HashMap<Card, Integer> cards) {
        setName(name);
        setOwner(ownerName);
        setCardsAmount(cards);
    }

    public static void loadAllDecks(ArrayList<Deck> decks) {
        allDecks = decks;
    }

    public static Deck getDeckByName(String name) {
        for (Deck allDeck : allDecks)
            if (allDeck.getName().equals(name))
                return allDeck;

        return null;
    }

    public boolean isMainFull() {
        return getMainDeck().size() > 59;
    }

    public boolean isSideFull() {
        return getSideDeck().size() > 14;
    }

    public boolean hasEnoughSpace(Card card) {
        if (hasUsedBefore(card))
            return cardsAmount.get(card.getName()) <= 2;

        return true;
    }

    private boolean hasUsedBefore(Card card) {
        for (Card key : getCardsAmount().keySet())
            if (key.getName().equals(card.getName()))
                return true;

        return false;
    }

    public boolean isThisCardUsedInMain(Card card) {
        if (card == null)
            return false;
        for (Card key : getMainDeck())
            if (key.getName().equals(card.getName()))
                return true;

        return false;
    }

    public boolean isThisCardUsedInSide(Card card) {
        for (Card key : getSideDeck())
            if (key.getName().equals(card.getName()))
                return true;

        return false;
    }

    public boolean isDeckValid() {
        return getMainDeck().size() > 39;
    }

    public static ArrayList<Deck> getAllDecks() {
        return allDecks;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private void setOwner(String ownerName) {
        this.ownerName = ownerName;
    }

    private void setMainDeck(ArrayList<Card> mainCards) {
        ArrayList<MonsterCard> monsters = new ArrayList<>();
        ArrayList<SpellTrapCard> spellTraps = new ArrayList<>();
        for (Card mainCard: mainCards) {
            if (mainCard.getCardType() == CardType.MONSTER)
                monsters.add((MonsterCard) mainCard);
            else
                spellTraps.add((SpellTrapCard) mainCard);
        }
        this.mainDeckMonsters = monsters;
        this.mainDeckSpellTraps = spellTraps;
    }

    public ArrayList<MonsterCard> getMainSortedMonsters() {
        mainDeckMonsters.sort(Comparator.comparing(Card::getName));
        return mainDeckMonsters;
    }

    public ArrayList<MonsterCard> getSideSortedMonsters() {
        sideDeckMonsters.sort(Comparator.comparing(Card::getName));
        return sideDeckMonsters;
    }

    public ArrayList<SpellTrapCard> getMainSortedSpellTraps() {
        mainDeckSpellTraps.sort(Comparator.comparing(Card::getName));
        return mainDeckSpellTraps;
    }

    public ArrayList<SpellTrapCard> getSideSortedSpellTraps() {
        sideDeckSpellTraps.sort(Comparator.comparing(Card::getName));
        return sideDeckSpellTraps;
    }

    public ArrayList<Card> getMainDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(mainDeckMonsters);
        cards.addAll(mainDeckSpellTraps);
        return cards;
    }

    private void setSideDeck(ArrayList<Card> sideCards) {
        ArrayList<MonsterCard> monsters = new ArrayList<>();
        ArrayList<SpellTrapCard> spellTraps = new ArrayList<>();
        for (Card sideCard: sideCards) {
            if (sideCard.getCardType() == CardType.MONSTER)
                monsters.add((MonsterCard) sideCard);
            else
                spellTraps.add((SpellTrapCard) sideCard);
        }
        this.sideDeckMonsters = monsters;
        this.sideDeckSpellTraps = spellTraps;
    }

    public ArrayList<Card> getSideDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(sideDeckMonsters);
        cards.addAll(sideDeckSpellTraps);
        return cards;
    }

    private void setCardsAmount(HashMap<Card, Integer> cards) {
        cardsAmount.clear();
        for (Card card : cards.keySet()) {
            cardsAmount.put(card.getName(), cards.get(card));
        }
    }

    public HashMap<Card, Integer> getCardsAmount() {
        HashMap<Card, Integer> cards = new HashMap<>();
        for (String cardName: cardsAmount.keySet())
            cards.put(MakeCards.makeCard(cardName), cardsAmount.get(cardName));

        return cards;
    }

    public void addCardToDeck(boolean isMain, Card card) {
        if (hasUsedBefore(card))
            cardsAmount.put(card.getName(), cardsAmount.get(card.getName()) + 1);
        else
            cardsAmount.put(card.getName(), 1);
        if (card.getCardType() == CardType.MONSTER) {
            if (isMain)
                mainDeckMonsters.add((MonsterCard) card);
            else
                sideDeckMonsters.add((MonsterCard) card);
        } else {
            if (isMain)
                mainDeckSpellTraps.add((SpellTrapCard) card);
            else
                sideDeckSpellTraps.add((SpellTrapCard) card);
        }
    }

    public void removeCardFromDeck(boolean isMain, Card card) {
            cardsAmount.put(card.getName(), cardsAmount.get(card.getName()) - 1);
        if (isMain) {
            if (card.getCardType() == CardType.MONSTER) {
                for (int i = 0; i < mainDeckMonsters.size(); i++)
                    if (mainDeckMonsters.get(i).getName().equals(card.getName())) {
                        mainDeckMonsters.remove(i);
                        break;
                    }
            } else
                for (int i = 0; i < mainDeckSpellTraps.size(); i++)
                    if (mainDeckSpellTraps.get(i).getName().equals(card.getName())) {
                        mainDeckSpellTraps.remove(i);
                        break;
                    }
        } else {
            if (card.getCardType() == CardType.MONSTER) {
                for (int i = 0; i < sideDeckMonsters.size(); i++)
                    if (sideDeckMonsters.get(i).getName().equals(card.getName())) {
                        sideDeckMonsters.remove(i);
                        break;
                    }
            } else
                for (int i = 0; i < sideDeckSpellTraps.size(); i++)
                    if (sideDeckSpellTraps.get(i).getName().equals(card.getName())) {
                        sideDeckSpellTraps.remove(i);
                        break;
                    }
        }
    }

    public void removeDeck() {
        Objects.requireNonNull(User.getUserByUserName(ownerName)).removeDeck(this);
        allDecks.remove(this);
    }

    public void removeCopiedDeck() {
        allDecks.remove(this);
    }

    public String toString() {
        if (isDeckValid())
            return this.name + ": main deck " + getMainDeck().size() + ", side deck " + getSideDeck().size()
                    + ", valid";
        else
            return this.name + ": main deck " + getMainDeck().size() + ", side deck " + getSideDeck().size()
                    + ", invalid";
    }
}
