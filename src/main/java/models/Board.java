package models;

import models.cards.Card;
import models.cards.Location;
import models.cards.monsters.Mode;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;

import java.util.ArrayList;
import java.util.Collections;

import controller.duel.singlePlayer.AI;


public class Board {

    private final ArrayList<MonsterCard> monsterBoard = new ArrayList<>(5);
    private final ArrayList<SpellTrapCard> spellAndTrapBoard = new ArrayList<>(5);
    private final ArrayList<Card> graveyard = new ArrayList<>();
    private final ArrayList<Card> cardsInHand = new ArrayList<>();
    private Player owner;
    private AI botOwner;
    private SpellTrapCard fieldZone = null;
    private Deck deck;
    private ArrayList<Card> mainDeckCards;
    private ArrayList<Card> sideDeckCards;
    private int lifePoints;
    private EffectsStatus effectsStatus;

    public Board(Player owner) throws CloneNotSupportedException {
        setOwner(owner);
        setBotOwner(null);
        setDeck((Deck) owner.getPlayerDeck().clone());
        setLifePoints(8000);
        initializeZones();
        setMainDeckCards();
        setSideDeckCards();
        shuffle();
        beginDeck();
        effectsStatus = new EffectsStatus();
    }

    public Board(AI bot) throws CloneNotSupportedException {
        setOwner(null);
        setBotOwner(bot);
        setDeck((Deck) bot.getDeck().clone());
        setLifePoints(8000);
        initializeZones();
        setMainDeckCards();
        setSideDeckCards();
        shuffle();
        beginDeck();
        effectsStatus = new EffectsStatus();
    }

    public void initializeZones() {
        monsterBoard.clear();
        spellAndTrapBoard.clear();
        graveyard.clear();
        cardsInHand.clear();
        fieldZone = null;
        for (int i = 0; i < 5; i++) {
            monsterBoard.add(null);
            spellAndTrapBoard.add(null);
        }
    }

    public void resetTheBoard(Integer mainIndex, Integer sideIndex) {
        removeCopiedDeck();
        setFieldZone(null);
        try {
            if (owner == null)
                setDeck((Deck) botOwner.getDeck().clone());
            else
                setDeck((Deck) owner.getPlayerDeck().clone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        setLifePoints(8000);
        initializeZones();
        if (mainIndex != null && sideIndex != null)
            changeDeck(sideIndex, mainIndex);
        setMainDeckCards();
        setSideDeckCards();
        shuffle();
        beginDeck();
        effectsStatus = new EffectsStatus();
    }

    public void setBotOwner(AI owner) {
        this.botOwner = owner;
    }

    public AI getBotOwner() {
        return this.botOwner;
    }

    public void setMainDeckCards() {
        this.mainDeckCards = deck.getMainDeck();
    }

    public ArrayList<Card> getMainDeckCards() {
        return mainDeckCards;
    }

    public void setSideDeckCards() {
        this.sideDeckCards = deck.getSideDeck();
    }

    public void removeCopiedDeck() {
        this.deck.removeCopiedDeck();
    }

    public void setLifePoints(int amount) {
        this.lifePoints = amount;
    }

    public int getLifePoints() {
        return this.lifePoints;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDeck() {
        return this.deck;
    }

    private void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return this.owner;
    }

    public ArrayList<MonsterCard> getMonsterBoard() {
        return this.monsterBoard;
    }

    public ArrayList<SpellTrapCard> getSpellAndTrapBoard() {
        return this.spellAndTrapBoard;
    }

    public void setFieldZone(SpellTrapCard fieldZone) {
        this.fieldZone = fieldZone;
        removeCardsFromHand(getCardIndexInHand(fieldZone));
    }

    public SpellTrapCard getFieldZone() {
        return this.fieldZone;
    }

    public ArrayList<Card> getHandCards() {
        return this.cardsInHand;
    }

    public ArrayList<Card> getGraveyardCards() {
        return this.graveyard;
    }

    public ArrayList<MonsterCard> getMonsters() {
        ArrayList<MonsterCard> monsters = new ArrayList<>();
        for (MonsterCard monsterCard : monsterBoard)
            if (monsterCard != null) monsters.add(monsterCard);
        return monsters;
    }

    public ArrayList<SpellTrapCard> getSpellTraps() {
        ArrayList<SpellTrapCard> spellTraps = new ArrayList<>();
        for (SpellTrapCard spellTrapCard : spellAndTrapBoard)
            if (spellTrapCard != null) spellTraps.add(spellTrapCard);
        return spellTraps;
    }

    public int getMonsterIndexInMonsterBoard(MonsterCard monster) {
        for (int i = 0; i < monsterBoard.size(); i++)
            if (monsterBoard.get(i) == monster &&
                    monsterBoard.get(i).getName().equals(monster.getName())) return i;

        return -1;
    }

    public int getSpellTrapIndexInSpellTrapBoard(SpellTrapCard spellTrapCard) {
        for (int i = 0 ; i < spellAndTrapBoard.size(); i++)
            if (spellAndTrapBoard.get(i) == spellTrapCard &&
                    spellAndTrapBoard.get(i).getName().equals(spellTrapCard.getName())) return i;

        return -1;
    }

    public int getCardIndexInHand(Card key) {
        for (int i = 0; i < cardsInHand.size(); i++)
            if (cardsInHand.get(i) == key &&
                    cardsInHand.get(i).getName().equals(key.getName())) return i;

        return -1;
    }

    public EffectsStatus getEffectsStatus() {
        return this.effectsStatus;
    }

    public boolean hasSpellTrapZoneSpace() {
        return getSpellTraps().size() != 5;
    }

    public boolean hasMonsterZoneSpace() {
        return getMonsters().size() != 5;
    }

    public void discardAll(Card card) {
        ArrayList<Card> removings = new ArrayList<>();
        for (Card key: mainDeckCards) {
            if (key.getName().equals(card.getName()))
                removings.add(key);
        }
        for (Card key: removings)
            addToGraveyard(key);

        mainDeckCards.removeAll(removings);
    }

    public void summonOrSetMonster(MonsterCard monster) {
        for (int i = 0; i < 5; i++)
            if (monsterBoard.get(i) == null) {
                monsterBoard.set(i, monster);
                removeCardsFromHand(getCardIndexInHand(monster));
                break;
            }
    }

    public void summonOrSetMonster(int index) {
        for (int i = 0; i < 5; i++)
            if (monsterBoard.get(i) == null) {
                monsterBoard.set(i, (MonsterCard) cardsInHand.get(index));
                removeCardsFromHand(index);
                break;
            }
    }

    public void recoverMonsterFromGraveyard(int index) {
        for (int i = 0 ; i < 5 ; i++)
            if (monsterBoard.get(i) == null) {
                monsterBoard.set(i, (MonsterCard) graveyard.get(index));
                removeFromGraveyard(graveyard.get(index));
                break;
            }
    }

    public void removeMonster(int index) {
        addToGraveyard(monsterBoard.get(index));
        monsterBoard.set(index, null);
    }

    public void summonOrSetSpellAndTrap(SpellTrapCard spellTrap) {
        for (int i = 0; i < 5; i++)
            if (spellAndTrapBoard.get(i) == null) {
                spellAndTrapBoard.set(i, spellTrap);
                removeCardsFromHand(getCardIndexInHand(spellTrap));
                break;
            }
    }

    public void removeSpellAndTrap(int index) {
        addToGraveyard(spellAndTrapBoard.get(index));
        spellAndTrapBoard.set(index, null);
    }

    public void addToGraveyard(Card card) {
        card.setLocation(Location.GRAVEYARD);
        graveyard.add(card);
    }

    public void removeFromGraveyard(Card card) {
        card.setLocation(Location.FIELD);
        graveyard.remove(card);
    }

    public void addCardsInHand(Card card) {
        cardsInHand.add(card);
    }

    public void removeCardsFromHand(int index) {
        cardsInHand.remove(index);
    }

    public Card drawCard() {
        Card card;
        try {
            card = mainDeckCards.get(0);
            mainDeckCards.remove(0);
        } catch (Exception exception) {
            card = null;
        }
        addCardsInHand(card);
        return card;
    }

    private void shuffle() {
        Collections.shuffle(mainDeckCards);
    }

    private void beginDeck() {
        for (int i = 0; i < 4; i++)
            drawCard();
    }

    public void changeDeck(int sideIndex, int mainIndex) {

        mainDeckCards.remove(mainIndex);
        mainDeckCards.add(sideDeckCards.get(sideIndex));
        sideDeckCards.remove(sideIndex);
        sideDeckCards.add(mainDeckCards.get(mainIndex));

    }

    public String toString() {
        StringBuilder boardString = new StringBuilder();
        if (this.fieldZone == null)
            boardString.append("E\t\t\t\t\t\t");
        else
            boardString.append("O\t\t\t\t\t\t");
        boardString.append(this.graveyard.size()).append("\n\t");
        for (int i = 4; i > -1; i -= 2) {
            if (monsterBoard.get(i) == null)
                boardString.append("E\t");
            else if (monsterBoard.get(i).getMode() == Mode.ATTACK)
                boardString.append("OO\t");
            else if (monsterBoard.get(i).getIsHidden())
                boardString.append("DH\t");
            else
                boardString.append("DO\t");
        }
        for (int i = 1; i < 4; i += 2) {
            if (monsterBoard.get(i) == null)
                boardString.append("E\t");
            else if (monsterBoard.get(i).getMode() == Mode.ATTACK)
                boardString.append("OO\t");
            else if (monsterBoard.get(i).getIsHidden())
                boardString.append("DH\t");
            else
                boardString.append("DO\t");
        }
        boardString.append("\n\t");
        for (int i = 4; i > -1; i -= 2) {
            if (spellAndTrapBoard.get(i) == null)
                boardString.append("E\t");
            else if (spellAndTrapBoard.get(i).getIsHidden())
                boardString.append("H\t");
            else
                boardString.append("O\t");
        }
        for (int i = 1; i < 4; i += 2) {
            if (spellAndTrapBoard.get(i) == null)
                boardString.append("E\t");
            else if (spellAndTrapBoard.get(i).getIsHidden())
                boardString.append("H\t");
            else
                boardString.append("O\t");
        }
        boardString.append("\n\t\t\t\t\t\t").append(mainDeckCards.size()).append("\n");

        for (int i = 0 ; i < cardsInHand.size(); i++)
            boardString.append("C\t");

        if (getBotOwner() == null)
            boardString.append("\n").append(owner.getNickName()).append(":").append(this.lifePoints);
        else
            boardString.append("\n").append(botOwner.getName()).append(":").append(this.lifePoints);

        return boardString.toString();
    }

    public String reverseToString() {
        StringBuilder boardString = new StringBuilder();
        if (getBotOwner() == null)
            boardString.append(owner.getNickName()).append(":").append(this.lifePoints).append("\n");
        else
            boardString.append(botOwner.getName()).append(":").append(this.lifePoints).append("\n");

        for (int i = 0 ; i < cardsInHand.size(); i++)
            boardString.append("C\t");

        boardString.append("\n").append(mainDeckCards.size()).append("\n\t");

        for (int i = 3; i > 0; i -= 2) {
            if (spellAndTrapBoard.get(i) == null)
                boardString.append("E\t");
            else if (spellAndTrapBoard.get(i).getIsHidden())
                boardString.append("H\t");
            else
                boardString.append("O\t");
        }

        for (int i = 0; i < 5; i += 2) {
            if (spellAndTrapBoard.get(i) == null)
                boardString.append("E\t");
            else if (spellAndTrapBoard.get(i).getIsHidden())
                boardString.append("H\t");
            else
                boardString.append("O\t");
        }

        boardString.append("\n\t");

        for (int i = 3; i > 0; i -= 2) {
            if (monsterBoard.get(i) == null)
                boardString.append("E\t");
            else if (monsterBoard.get(i).getMode() == Mode.ATTACK)
                boardString.append("OO\t");
            else if (monsterBoard.get(i).getIsHidden())
                boardString.append("DH\t");
            else
                boardString.append("DO\t");
        }

        for (int i = 0; i < 5; i += 2) {
            if (monsterBoard.get(i) == null)
                boardString.append("E\t");
            else if (monsterBoard.get(i).getMode() == Mode.ATTACK)
                boardString.append("OO\t");
            else if (monsterBoard.get(i).getIsHidden())
                boardString.append("DH\t");
            else
                boardString.append("DO\t");
        }

        boardString.append("\n").append(graveyard.size()).append("\t\t\t\t\t\t");
        if (fieldZone == null)
            boardString.append("E");
        else
            boardString.append("O");

        return boardString.toString();
    }

}
