package controller.duel.singlePlayer;

import java.util.ArrayList;
import java.util.HashMap;

import controller.duel.GamePhase;
import models.Board;
import models.Deck;
import models.Player;
import models.cards.Card;
import models.cards.CardType;
import models.cards.monsters.Attribute;
import models.cards.monsters.Mode;
import models.cards.monsters.MonsterCard;
import models.cards.monsters.MonsterType;
import models.cards.monsters.Trait;
import models.cards.spelltrap.Icon;
import models.cards.spelltrap.SpellTrapCard;

abstract public class AI {
    protected Deck deck;
    protected Board board;
    protected String nickname;
    protected Player opponent;
    protected static AI aiBot;

    public static AI getInstance() {
        return aiBot;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return this.board;
    }

    public void setName(String name) {
        this.nickname = name;
    }

    public String getName() {
        return this.nickname;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Player getOpponent() {
        return this.opponent;
    }

    public ArrayList<MonsterCard> getOpponentMonsters() {
        return this.opponent.getPlayerBoard().getMonsters();
    }

    public ArrayList<MonsterCard> getAIMonsters() {
        return this.board.getMonsters();
    }

    public ArrayList<SpellTrapCard> getOpponentSpellTraps() {
        return this.opponent.getPlayerBoard().getSpellTraps();
    }

    public ArrayList<SpellTrapCard> getAISpellTraps() {
        return this.board.getSpellTraps();
    }

    public int getOpponentMostPowerfulMonsterAttackPoint() {
        int maximum = 0;
        for (MonsterCard monster: getOpponentMonsters())
            if (maximum < monster.getAttackPoint()) maximum = monster.getAttackPoint();

            return maximum;
    }

    public int getOpponentLeastPowerfulMonsterAttackPoint() {
        int minimum = Integer.MAX_VALUE;
        for (MonsterCard monster: getOpponentMonsters())
            if (minimum > monster.getAttackPoint()) minimum = monster.getAttackPoint();

            return minimum;
    }

    protected int getBestSpecialMonsterIndex(ArrayList<Card> cards, boolean canSpecialSummon2) {
        int maximum = 0;
        int index = -1;
        for (int i = 0; i < cards.size(); i++)
            if (cards.get(i).getCardType() == CardType.MONSTER) {
                MonsterCard monster = (MonsterCard) cards.get(i);
                if (monster.getLevel() > 6 && monster.getLevel() > maximum && canSpecialSummon2 && monster.getTrait() != Trait.RITUAL) {
                    maximum = monster.getLevel();
                    index = i;
                } else if (monster.getLevel() > 4 && monster.getLevel() < 7 && monster.getLevel() > maximum) {
                    maximum = monster.getLevel();
                    index = i;
                }
            }
        return index;
    }

    protected boolean canSpecialSummonLevel2() {
        ArrayList<MonsterCard> cards = getAIMonsters();
        int counter = 0;
        for (MonsterCard card : cards) {
            if (card.getLevel() < 5)
                counter++;
        }
        return counter > 1;
    }

    protected boolean cantSpecialSummonLevel1() {
        ArrayList<MonsterCard> cards = getAIMonsters();
        int counter = 0;
        for (MonsterCard card : cards) {
            if (card.getLevel() < 5)
                counter++;
        }
        return counter <= 0;
    }

    protected void sacrificeWeakestMonster() {
        ArrayList<MonsterCard> cards = getAIMonsters();
        int minimum = 10;
        int index = -1;
        for (int i = 0; i < cards.size(); i++)
            if (cards.get(i).getLevel() < minimum) {
                minimum = cards.get(i).getLevel();
                index = i;
            }
        if (index != -1)
            this.board.removeMonster(index);
    }

    protected Card getOpponentBestDeckCard() {
        int maximum = 0;
        Card key = null;
        for (Card card: opponent.getPlayerBoard().getMainDeckCards())
            if (card.getCardType() == CardType.MONSTER) {
                MonsterCard monster = (MonsterCard) card;
                if (monster.getLevel() > maximum) {
                    maximum = monster.getLevel();
                    key = card;
                }
            }
        return key;
    }

    protected int getBestMonsterCard(ArrayList<Card> cards) {
        int maximum = 0;
        int index = -1;
        for (int i = 0; i < cards.size(); i++)
            if (cards.get(i).getCardType() == CardType.MONSTER) {
                MonsterCard monster = (MonsterCard) cards.get(i);
                if (monster.getLevel() > maximum) {
                    if (monster.getLevel() > 6 && !canSpecialSummonLevel2())
                        continue;
                    if (monster.getLevel() < 7 && monster.getLevel() > 4 && cantSpecialSummonLevel1())
                        continue;
                    maximum = monster.getLevel();
                    index = i;
                }
            }

        return index;

    }

    protected int getBestMonsterToAttack(ArrayList<MonsterCard> monsters) {
        int maximum = 0;
        int index = -1;
        for (int i = 0; i < monsters.size(); i++)
            if (!monsters.get(i).getHasAttacked() && monsters.get(i).getMode() == Mode.ATTACK
                    && monsters.get(i).getAttackPoint() > maximum) {
                maximum = monsters.get(i).getAttackPoint();
                index = i;
            }

        return index;
    }

    protected boolean canDirectAttack() {
        return !GameController.isFirstPlay && opponent.getPlayerBoard().getMonsters().size() <= 0;
    }

    protected boolean isThereASetWithValueEqualToRitualLevel(ArrayList<MonsterCard> cards, int level) {
        if (level == 0)
            return true;
        if (cards.size() == 0)
            return false;

        for (int i = 0; i < cards.size(); i++) {
            cards.remove(i);
            if (cards.get(i).getLevel() <= level)
                return isThereASetWithValueEqualToRitualLevel(cards, level - cards.get(i).getLevel());
        }

        return false;
    }

    protected ReasonableLevel isReasonableToAttack(MonsterCard aiMonster, MonsterCard opponentMonster) {
        if (opponentMonster.getMode() == Mode.ATTACK && aiMonster.getAttackPoint() > opponentMonster.getAttackPoint())
            return ReasonableLevel.REASONABLE_FOR_HARD;
        if (opponentMonster.getMode() == Mode.DEFENSE) {
            /*------that is based on the chance that a card with a certain
            attack point could be able to destroy a set (DEFENSED) card------*/
            int attackChance = (int) (Math.random() * 100);
            if (aiMonster.getAttackPoint() > 3400)
                return ReasonableLevel.REASONABLE_FOR_HARD;
            if (aiMonster.getAttackPoint() > 3000 && attackChance > 1)
                return ReasonableLevel.REASONABLE_FOR_HARD;
            if (aiMonster.getAttackPoint() > 2500 && attackChance > 11)
                return ReasonableLevel.REASONABLE_FOR_HARD;
            if (aiMonster.getAttackPoint() > 2000 && attackChance > 23)
                return ReasonableLevel.REASONABLE_FOR_HARD;
            if (aiMonster.getAttackPoint() > 1500 && attackChance > 33)
                return ReasonableLevel.REASONABLE_FOR_HARD;
            if (aiMonster.getAttackPoint() > 1000 && attackChance > 65)
                return ReasonableLevel.REASONABLE_FOR_HARD;
            if (aiMonster.getAttackPoint() > 500 && attackChance > 90)
                return ReasonableLevel.REASONABLE_FOR_HARD;
            if (aiMonster.getAttackPoint() > 0 && attackChance > 98)
                return ReasonableLevel.REASONABLE_FOR_HARD;
            /*------Our HARD Bot takes Risk for Attacking a DEFENSED Card or not
                    but based on what its card attack point is------*/
            if (attackChance > 49)
                return ReasonableLevel.REASONABLE_FOR_EASY;
            /*------But EASY Bot Attacks without Caring about card attack point------*/
        }
        return ReasonableLevel.NOT_REASONABLE;
    }

    public int getOpponentMonsterIndexEasy(Player opponent) {
        int index = getBestMonsterToAttack(getAIMonsters());
        if (index != -1) {
            for (int i = 0; i < opponent.getPlayerBoard().getMonsters().size(); i++)
                if (isReasonableToAttack(getAIMonsters().get(index),
                        opponent.getPlayerBoard().getMonsters().get(i)) != ReasonableLevel.NOT_REASONABLE)
                    return i;
        }
        return -1;
    }

    public int getOpponentMonsterIndexHard(Player opponent) {
        int index = getBestMonsterToAttack(getAIMonsters());
        if (index != -1) {
            for (int i = 0; i < opponent.getPlayerBoard().getMonsters().size(); i++)
                if (isReasonableToAttack(getAIMonsters().get(index),
                        opponent.getPlayerBoard().getMonsters().get(i)) == ReasonableLevel.REASONABLE_FOR_HARD)
                    return i;
        }
        return -1;
    }

    protected ReasonableLevel isSpellReasonableToActive(String name, GamePhase phase) {
        switch (name) {
            case "Monster Reborn":
                ArrayList<Card> graveyardCards = new ArrayList<>();
                graveyardCards.addAll(this.board.getGraveyardCards());
                graveyardCards.addAll(opponent.getPlayerBoard().getGraveyardCards());
                return isReasonableToActiveMonsterReborn(graveyardCards);
            case "Terraforming":
                return isReasonableToActiveTerraforming();
            case "Pot of Greed":
                return isReasonableToActivePotOfGreed();
            case "Raigeki":
                return isReasonableToActiveRaigeki();
            case "Change of Heart":
                return isReasonableToActiveChangeOfHeart(phase);
            case "Swords of Revealing Light":
                return isReasonableToActiveSwordsOfRevealingLight();
            case "Harpie's Feather Duster":
                return isReasonableToActiveHarpiesFeatherDuster();
            case "Dark Hole":
                return isReasonableToActiveDarkHole();
            case "Supply Squad":
                return isReasonableToActiveSupplySquad();
            case "Spell Absorption":
                return ReasonableLevel.REASONABLE_FOR_HARD;
            case "Messenger of peace":
                return isReasonableToActiveMessengerOfPeace();
            case "Twin Twisters":
                return isReasonableToActiveTwinTwisters();
            case "Mystical space typhoon":
                return isReasonableToActiveMysticalSpaceTyphoon();
            case "Ring of defense":
                return isReasonableToActiveRingOfDefense();
            case "Yami":
                return isReasonableToActiveYami();
            case "Forest":
                return isReasonableToActiveForest();
            case "Closed Forest":
                return isReasonableToActiveClosedForest();
            case "Umiiruka":
                return isReasonableToActiveUmiiruka();
            case "Sword of dark destruction":
                return isReasonableToActiveSwordOfDarkDestruction();
            case "Black Pendant":
                return isReasonableToActiveBlackPendant();
            case "United We Stand":
                return isReasonableToActiveUnitedWeStand();
            case "Magnum Shield":
                return isReasonableToActiveMagnumShield();
            case "Advanced Ritual Art":
                return isReasonableToActiveAdvancedRitualArt();
            default:
                return ReasonableLevel.NOT_REASONABLE;
        }
    }

    protected ReasonableLevel isTrapReasonableToActive(String name) {
        switch (name) {
            case "Mind Crush":
                return isReasonableToActiveMindCrush();
            case "Call of The Haunted":
                return isReasonableToActiveCallOfTheHaunted();
            case "Vanity's Emptiness":
                return isReasonableToActiveVanitysEmptiness();
            case "Wall of Revealing Light":
                return isReasonableToActiveWallOfRevealingLight();
            default:
                return ReasonableLevel.NOT_REASONABLE;
        }
    }

    public void destroyAllAttackingOpponentMonsters() {
        while (true) {
            ArrayList<MonsterCard> monsters = getOpponentMonsters();
            MonsterCard destroying = null;
            for (MonsterCard monster: monsters)
                if (!monster.getIsHidden() && monster.getMode() == Mode.ATTACK) {
                    destroying = monster;
                    break;
                }

            if (destroying != null)
                opponent.getPlayerBoard().removeMonster(
                        opponent.getPlayerBoard().getMonsterIndexInMonsterBoard(destroying));
            else
                break;
        }

    }

    public boolean activateTimeSeal() {
        ArrayList<SpellTrapCard> spellTraps = getAISpellTraps();
        SpellTrapCard timeSeal = null;
        for (SpellTrapCard spellTrapCard: spellTraps)
            if (spellTrapCard.getName().equals("Time Seal") &&
                    isReasonableToActiveTimeSeal() != ReasonableLevel.NOT_REASONABLE) {
                destroyAllAttackingOpponentMonsters();
                timeSeal = spellTrapCard;
                break;
            }

        if (timeSeal != null) {
            board.removeSpellAndTrap(board.getSpellTrapIndexInSpellTrapBoard(timeSeal));
            return true;
        }
        return false;
    }

    public void activeMindCrush() {
        if (nickname.equals("EasyBot")) {
            int random = (int) (Math.random() * opponent.getPlayerBoard().getMainDeckCards().size());
            opponent.getPlayerBoard().discardAll(opponent.getPlayerBoard().getMainDeckCards().get(random));
        } else {
            if (getOpponentBestDeckCard() != null)
                opponent.getPlayerBoard().discardAll(getOpponentBestDeckCard());
            else
                opponent.getPlayerBoard().discardAll(opponent.getPlayerBoard().getMainDeckCards().get(0));
        }
    }

    public void activeCallOfTheHaunted() {
        int index = getBestSpecialMonsterIndex(board.getGraveyardCards(), canSpecialSummonLevel2());
        board.recoverMonsterFromGraveyard(index);
    }

    public void activeSpellTrap(SpellTrapCard spellTrap) {
        if (spellTrap.getName().equals("Mind Crush")) {
            activeMindCrush();
            board.removeSpellAndTrap(board.getSpellTrapIndexInSpellTrapBoard(spellTrap));
        } else if (spellTrap.getName().equals("Call of The Haunted")) {
            activeCallOfTheHaunted();
        }
    }

    public void summonSpellTrapIfCan() {
        while (true) {
            SpellTrapCard toSet = null;
            for (Card card: board.getHandCards()) {
                if (card.getCardType() != CardType.MONSTER && board.hasSpellTrapZoneSpace()) {
                    toSet = (SpellTrapCard) card;
                    card.setIsHidden(true);
                    break;
                }
            }
            if (toSet != null)
                board.summonOrSetSpellAndTrap(toSet);
            else
                break;
        }
    }

    public void resetAttacks() {
        for (MonsterCard monster: getAIMonsters())
            monster.setHasAttacked(false);
    }

    /*------spell & trap checking------*/
    /*------spell & trap checking------*/
    /*------spell & trap checking------*/
    /*------spell & trap checking------*/
    /*------spell & trap checking------*/
    /*------spell & trap checking------*/
    /*------spell & trap checking------*/
    /*------spell & trap checking------*/
    /*------spell & trap checking------*/
    /*------spell & trap checking------*/

    public ReasonableLevel isReasonableToActiveMonsterReborn(ArrayList<Card> graveyardCards) {
        if (cantSpecialSummonLevel1() && !canSpecialSummonLevel2())
            return ReasonableLevel.NOT_REASONABLE;
        if (canSpecialSummonLevel2() && getBestSpecialMonsterIndex(graveyardCards, true) != -1)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        if (getBestSpecialMonsterIndex(graveyardCards, false) != -1)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.NOT_REASONABLE;
    }

    public ReasonableLevel isReasonableToActiveTerraforming() {
        ArrayList<Card> aiMainDeck = this.board.getMainDeckCards();
        for (Card card : aiMainDeck)
            if (card.getCardType() == CardType.SPELL) {
                SpellTrapCard spell = (SpellTrapCard) card;
                if (spell.getIcon() == Icon.FIELD)
                    switch (aiMainDeck.size()) {
                        case 0:
                        case 1:
                            return ReasonableLevel.NOT_REASONABLE;
                        case 2:
                            int counter = 0;
                            ArrayList<MonsterCard> aiMonsters = getAIMonsters();
                            ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
                            switch (spell.getName()) {
                                case "Yami":
                                    for (MonsterCard monster : aiMonsters) {
                                        if (monster.getMonsterType() == MonsterType.FIEND
                                                || monster.getMonsterType() == MonsterType.SPELL_CASTER)
                                            counter++;
                                        if (monster.getMonsterType() == MonsterType.FAIRY)
                                            counter--;
                                    }
                                    for (MonsterCard monster : opponentMonsters) {
                                        if (monster.getMonsterType() == MonsterType.FIEND
                                                || monster.getMonsterType() == MonsterType.SPELL_CASTER)
                                            counter--;
                                        if (monster.getMonsterType() == MonsterType.FAIRY)
                                            counter++;
                                    }
                                    if (counter * 200 > opponent.getPlayerBoard().getLifePoints())
                                        return ReasonableLevel.REASONABLE_FOR_HARD;
                                    return ReasonableLevel.REASONABLE_FOR_EASY;
                                case "Forest":
                                    for (MonsterCard monster : aiMonsters)
                                        if (monster.getMonsterType() == MonsterType.INSECT
                                                || monster.getMonsterType() == MonsterType.BEAST
                                                || monster.getMonsterType() == MonsterType.BEAST_WARRIOR)
                                            counter++;
                                    for (MonsterCard monster : opponentMonsters)
                                        if (monster.getMonsterType() == MonsterType.INSECT
                                                || monster.getMonsterType() == MonsterType.BEAST
                                                || monster.getMonsterType() == MonsterType.BEAST_WARRIOR)
                                            counter--;
                                    if (counter * 200 > opponent.getPlayerBoard().getLifePoints())
                                        return ReasonableLevel.REASONABLE_FOR_HARD;
                                    return ReasonableLevel.REASONABLE_FOR_EASY;
                                case "Closed Forest":
                                    int monsterCounter = 0;
                                    for (Card graveyardCard : this.board.getGraveyardCards())
                                        if (graveyardCard.getCardType() == CardType.MONSTER)
                                            monsterCounter++;
                                    for (MonsterCard monster : aiMonsters)
                                        if (monster.getMonsterType() == MonsterType.BEAST)
                                            counter++;
                                    if (counter * monsterCounter * 100 > opponent.getPlayerBoard().getLifePoints())
                                        return ReasonableLevel.REASONABLE_FOR_HARD;
                                    return ReasonableLevel.REASONABLE_FOR_EASY;
                                case "Umiiruka":
                                    for (MonsterCard monster : aiMonsters)
                                        if (monster.getAttribute() == Attribute.WATER)
                                            counter++;
                                    for (MonsterCard monster : opponentMonsters)
                                        if (monster.getAttribute() == Attribute.WATER)
                                            counter--;
                                    if (counter * 500 > opponent.getPlayerBoard().getLifePoints())
                                        return ReasonableLevel.REASONABLE_FOR_HARD;
                                    return ReasonableLevel.REASONABLE_FOR_EASY;
                            }
                        default:
                            return ReasonableLevel.REASONABLE_FOR_HARD;
                    }
            }

        return ReasonableLevel.NOT_REASONABLE;
    }

    public ReasonableLevel isReasonableToActivePotOfGreed() {
        switch (this.board.getMainDeckCards().size()) {
            case 0:
            case 1:
            case 2:
                return ReasonableLevel.NOT_REASONABLE;
            case 3:
            case 4:
                return ReasonableLevel.REASONABLE_FOR_EASY;
            default:
                return ReasonableLevel.REASONABLE_FOR_HARD;
        }
    }

    public ReasonableLevel isReasonableToActiveRaigeki() {
        if (getOpponentMonsters().size() > 0)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.NOT_REASONABLE;
    }

    public ReasonableLevel isReasonableToActiveChangeOfHeart(GamePhase phase) {
        if (getOpponentMonsters().size() == 0)
            return ReasonableLevel.NOT_REASONABLE;
        if (phase == GamePhase.BATTLE)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.REASONABLE_FOR_EASY;
    }

    public ReasonableLevel isReasonableToActiveSwordsOfRevealingLight() {
        boolean hasHiddenCard = false;
        int betterCounter = 0;
        HashMap<MonsterCard, Boolean> checkedCards = new HashMap<>();
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
        for (MonsterCard monster : aiMonsters)
            checkedCards.put(monster, false);
        for (MonsterCard monster : opponentMonsters)
            if (monster.getIsHidden()) {
                hasHiddenCard = true;
                break;
            }
        nextOpponentCard: for (MonsterCard opponentMonster : opponentMonsters)
            for (MonsterCard aiMonster : aiMonsters)
                if (!checkedCards.get(aiMonster) && opponentMonster.getAttackPoint() > aiMonster.getAttackPoint()
                        && !opponentMonster.getIsHidden()) {
                    betterCounter++;
                    checkedCards.put(aiMonster, true);
                    continue nextOpponentCard;
                }

        if (betterCounter > 1)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        if (hasHiddenCard || betterCounter == 1)
            return ReasonableLevel.REASONABLE_FOR_EASY;
        return ReasonableLevel.NOT_REASONABLE;
    }

    public ReasonableLevel isReasonableToActiveHarpiesFeatherDuster() {
        if (getOpponentSpellTraps().size() == 0)
            return ReasonableLevel.NOT_REASONABLE;
        return ReasonableLevel.REASONABLE_FOR_HARD;
    }

    public ReasonableLevel isReasonableToActiveDarkHole() {
        int heaviestDamageToOpponent = 0;
        int heaviestDamageToSelf = 0;
        int totalAIDamage = 0;
        int totalOpponentDamage = 0;
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
        for (MonsterCard aiMonster : aiMonsters)
            for (MonsterCard opponentMonster : opponentMonsters) {
                int difference = aiMonster.getAttackPoint() - opponentMonster.getAttackPoint();
                if (difference > heaviestDamageToOpponent)
                    heaviestDamageToOpponent = difference;
                else if (difference < heaviestDamageToSelf)
                    heaviestDamageToSelf = difference;
            }

        for (MonsterCard monster : aiMonsters)
            totalAIDamage += monster.getAttackPoint();
        for (MonsterCard monster : opponentMonsters)
            totalOpponentDamage += monster.getAttackPoint();

        if (heaviestDamageToSelf * (-1) > this.board.getLifePoints())
            return ReasonableLevel.REASONABLE_FOR_HARD;
        if (heaviestDamageToOpponent > opponent.getPlayerBoard().getLifePoints())
            return ReasonableLevel.NOT_REASONABLE;
        if (totalAIDamage > totalOpponentDamage)
            return ReasonableLevel.NOT_REASONABLE;
        if (totalAIDamage < totalOpponentDamage)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.REASONABLE_FOR_EASY;
    }

    public ReasonableLevel isReasonableToActiveSupplySquad() {
        switch (this.board.getMainDeckCards().size()) {
            case 0:
            case 1:
                return ReasonableLevel.NOT_REASONABLE;
            default:
                return ReasonableLevel.REASONABLE_FOR_HARD;
        }
    }

    public ReasonableLevel isReasonableToActiveMessengerOfPeace() {
        if (iskeepingMessengerOfPeaceReasonable() == ReasonableLevel.REASONABLE_FOR_HARD)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.NOT_REASONABLE;
    }

    public ReasonableLevel isReasonableToActiveTwinTwisters() {
        int opponentSpellTrapCards = getOpponentSpellTraps().size();
        if (opponentSpellTrapCards == 0)
            return ReasonableLevel.NOT_REASONABLE;
        if (opponentSpellTrapCards == 1)
            return ReasonableLevel.REASONABLE_FOR_EASY;
        return ReasonableLevel.REASONABLE_FOR_HARD;
    }

    public ReasonableLevel isReasonableToActiveMysticalSpaceTyphoon() {
        if (getOpponentSpellTraps().size() == 0)
            return ReasonableLevel.NOT_REASONABLE;
        return ReasonableLevel.REASONABLE_FOR_HARD;
    }

    public ReasonableLevel isReasonableToActiveRingOfDefense() {
        ArrayList<SpellTrapCard> aiSpellTrapCards = getAISpellTraps();
        ArrayList<SpellTrapCard> opponentSpellTrapCards = getOpponentSpellTraps();

        for (SpellTrapCard aiTrap : aiSpellTrapCards)
            if (aiTrap.getCardType() == CardType.TRAP)
                if (aiTrap.getName().equals("Solemn Warning") || aiTrap.getName().equals("Wall of Revealing Light"))
                    return ReasonableLevel.REASONABLE_FOR_HARD;

        for (SpellTrapCard opponentTrap : opponentSpellTrapCards)
            if (opponentTrap.getCardType() == CardType.TRAP && opponentTrap.getName().equals("Magic Cylinder"))
                return ReasonableLevel.REASONABLE_FOR_HARD;

        return ReasonableLevel.NOT_REASONABLE;
    }

    public ReasonableLevel isReasonableToActiveYami() {
        int counter = 0;
        int winningBeforeUsage = 0;
        int winningAfterUsage = 0;
        boolean isChanged = false;
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
        for (MonsterCard aiMonster : aiMonsters) {
            int aiMonsterPowerBeforeChange = aiMonster.getAttackPoint();
            int aiMonsterPowerAfterChange = 0;
            if (aiMonster.getMonsterType() == MonsterType.FIEND
                    || aiMonster.getMonsterType() == MonsterType.SPELL_CASTER) {
                aiMonsterPowerAfterChange = aiMonsterPowerBeforeChange + 200;
                counter++;
            } else if (aiMonster.getMonsterType() == MonsterType.FAIRY) {
                aiMonsterPowerAfterChange = aiMonsterPowerBeforeChange - 200;
                counter--;
            }
            for (MonsterCard opponentMonster : opponentMonsters) {
                int opponentMonsterPowerBeforeChange = opponentMonster.getAttackPoint();
                int opponentMonsterPowerAfterChange = 0;
                if (opponentMonster.getMonsterType() == MonsterType.FIEND
                        || opponentMonster.getMonsterType() == MonsterType.SPELL_CASTER) {
                    opponentMonsterPowerAfterChange = opponentMonsterPowerBeforeChange + 200;
                    if (!isChanged)
                        counter--;
                } else if (opponentMonster.getMonsterType() == MonsterType.FAIRY) {
                    opponentMonsterPowerAfterChange = opponentMonsterPowerBeforeChange - 200;
                    if (!isChanged)
                        counter++;
                }
                if (aiMonsterPowerBeforeChange > opponentMonsterPowerBeforeChange)
                    winningBeforeUsage++;
                if (aiMonsterPowerAfterChange > opponentMonsterPowerAfterChange)
                    winningAfterUsage++;
            }
            isChanged = true;

        }
        if (winningAfterUsage > winningBeforeUsage && counter > 0)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        if (winningAfterUsage <= winningBeforeUsage && counter <= 0)
            return ReasonableLevel.NOT_REASONABLE;
        return ReasonableLevel.REASONABLE_FOR_EASY;

    }

    public ReasonableLevel isReasonableToActiveForest() {
        int counter = 0;
        int winningBeforeUsage = 0;
        int winningAfterUsage = 0;
        boolean isChanged = false;
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
        for (MonsterCard aiMonster : aiMonsters) {
            int aiMonsterPowerBeforeChange = aiMonster.getAttackPoint();
            int aiMonsterPowerAfterChange = 0;
            if (aiMonster.getMonsterType() == MonsterType.INSECT || aiMonster.getMonsterType() == MonsterType.BEAST
                    || aiMonster.getMonsterType() == MonsterType.BEAST_WARRIOR) {
                aiMonsterPowerAfterChange = aiMonsterPowerBeforeChange + 200;
                counter++;
            }
            for (MonsterCard opponentMonster : opponentMonsters) {
                int opponentMonsterPowerBeforeChange = opponentMonster.getAttackPoint();
                int opponentMonsterPowerAfterChange = 0;
                if (opponentMonster.getMonsterType() == MonsterType.INSECT
                        || opponentMonster.getMonsterType() == MonsterType.BEAST
                        || opponentMonster.getMonsterType() == MonsterType.BEAST_WARRIOR) {
                    opponentMonsterPowerAfterChange = opponentMonsterPowerBeforeChange + 200;
                    if (!isChanged)
                        counter--;
                }
                if (aiMonsterPowerBeforeChange > opponentMonsterPowerBeforeChange)
                    winningBeforeUsage++;
                if (aiMonsterPowerAfterChange > opponentMonsterPowerAfterChange)
                    winningAfterUsage++;
            }
            isChanged = true;

        }
        if (winningAfterUsage > winningBeforeUsage && counter > 0)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        if (winningAfterUsage <= winningBeforeUsage && counter <= 0)
            return ReasonableLevel.NOT_REASONABLE;
        return ReasonableLevel.REASONABLE_FOR_EASY;
    }

    public ReasonableLevel isReasonableToActiveClosedForest() {
        int amount = 0;
        int counter = 0;
        int winningBeforeUsage = 0;
        int winningAfterUsage = 0;
        boolean isChanged = false;
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
        ArrayList<Card> graveyard = this.board.getGraveyardCards();
        for (Card card : graveyard)
            if (card.getCardType() == CardType.MONSTER)
                amount += 100;
        for (MonsterCard aiMonster : aiMonsters) {
            int aiMonsterPowerBeforeChange = aiMonster.getAttackPoint();
            int aiMonsterPowerAfterChange = 0;
            if (aiMonster.getMonsterType() == MonsterType.BEAST) {
                aiMonsterPowerAfterChange = aiMonsterPowerBeforeChange + amount;
                counter++;
            }
            for (MonsterCard opponentMonster : opponentMonsters) {
                int opponentMonsterPowerBeforeChange = opponentMonster.getAttackPoint();
                int opponentMonsterPowerAfterChange = 0;
                if (opponentMonster.getMonsterType() == MonsterType.BEAST) {
                    opponentMonsterPowerAfterChange = opponentMonsterPowerBeforeChange + amount;
                    if (!isChanged)
                        counter--;
                }
                if (aiMonsterPowerBeforeChange > opponentMonsterPowerBeforeChange)
                    winningBeforeUsage++;
                if (aiMonsterPowerAfterChange > opponentMonsterPowerAfterChange)
                    winningAfterUsage++;
            }
            isChanged = true;

        }
        if (winningAfterUsage > winningBeforeUsage && counter > 0)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        if (winningAfterUsage <= winningBeforeUsage && counter <= 0)
            return ReasonableLevel.NOT_REASONABLE;
        return ReasonableLevel.REASONABLE_FOR_EASY;
    }

    public ReasonableLevel isReasonableToActiveUmiiruka() {
        int counter = 0;
        int winningBeforeUsage = 0;
        int winningAfterUsage = 0;
        boolean isChanged = false;
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
        for (MonsterCard aiMonster : aiMonsters) {
            int aiMonsterPowerBeforeChange = aiMonster.getAttackPoint();
            int aiMonsterPowerAfterChange = 0;
            if (aiMonster.getAttribute() == Attribute.WATER) {
                aiMonsterPowerAfterChange = aiMonsterPowerBeforeChange + 500;
                counter++;
            }
            for (MonsterCard opponentMonster : opponentMonsters) {
                int opponentMonsterPowerBeforeChange;
                if (opponentMonster.getIsHidden())
                    opponentMonsterPowerBeforeChange = opponentMonster.getDefensePoint();
                else
                    opponentMonsterPowerBeforeChange = opponentMonster.getAttackPoint();
                int opponentMonsterPowerAfterChange = 0;
                if (opponentMonster.getAttribute() == Attribute.WATER) {
                    if (opponentMonster.getIsHidden()) {
                        opponentMonsterPowerAfterChange = opponentMonsterPowerBeforeChange - 400;
                        if (!isChanged)
                            counter++;
                    } else {
                        opponentMonsterPowerAfterChange = opponentMonsterPowerBeforeChange + 500;
                        if (!isChanged)
                            counter--;
                    }
                }
                if (aiMonsterPowerBeforeChange > opponentMonsterPowerBeforeChange)
                    winningBeforeUsage++;
                if (aiMonsterPowerAfterChange > opponentMonsterPowerAfterChange)
                    winningAfterUsage++;
            }
            isChanged = true;

        }
        if (winningAfterUsage > winningBeforeUsage && counter > 0)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        if (winningAfterUsage <= winningBeforeUsage && counter <= 0)
            return ReasonableLevel.NOT_REASONABLE;
        return ReasonableLevel.REASONABLE_FOR_EASY;
    }

    public ReasonableLevel isReasonableToActiveSwordOfDarkDestruction() {
        int winningBeforeUsage = 0;
        int winningAfterUsage = 0;
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
        for (MonsterCard aiMonster : aiMonsters)
            if (aiMonster.getAttribute() == Attribute.DARK)
                for (MonsterCard opponentMonster : opponentMonsters) {
                    if (opponentMonster.getIsHidden()) {
                        if (aiMonster.getAttackPoint() > opponentMonster.getDefensePoint())
                            winningBeforeUsage++;
                        if (aiMonster.getAttackPoint() + 400 > opponentMonster.getDefensePoint())
                            winningAfterUsage++;
                    } else {
                        if (aiMonster.getAttackPoint() > opponentMonster.getAttackPoint())
                            winningBeforeUsage++;
                        if (aiMonster.getAttackPoint() + 400 > opponentMonster.getAttackPoint())
                            winningAfterUsage++;
                    }
                }
        if (winningAfterUsage == 0 && winningBeforeUsage == 0)
            return ReasonableLevel.NOT_REASONABLE;
        if (winningAfterUsage > winningBeforeUsage)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.REASONABLE_FOR_EASY;
    }

    public ReasonableLevel isReasonableToActiveBlackPendant() {
        int winningBeforeUsage = 0;
        int winningAfterUsage = 0;
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
        for (MonsterCard aiMonster : aiMonsters)
            for (MonsterCard opponentMonster : opponentMonsters) {
                if (opponentMonster.getIsHidden()) {
                    if (aiMonster.getAttackPoint() > opponentMonster.getDefensePoint())
                        winningBeforeUsage++;
                    if (aiMonster.getAttackPoint() + 500 > opponentMonster.getDefensePoint())
                        winningAfterUsage++;
                } else {
                    if (aiMonster.getAttackPoint() > opponentMonster.getAttackPoint())
                        winningBeforeUsage++;
                    if (aiMonster.getAttackPoint() + 500 > opponentMonster.getAttackPoint())
                        winningAfterUsage++;
                }
            }
        if (winningAfterUsage > winningBeforeUsage)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.REASONABLE_FOR_EASY;
    }

    public ReasonableLevel isReasonableToActiveUnitedWeStand() {
        int amount = 0;
        int winningBeforeUsage = 0;
        int winningAfterUsage = 0;
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();

        for (MonsterCard monster : aiMonsters)
            if (!monster.getIsHidden())
                amount += 800;
        for (MonsterCard aiMonster : aiMonsters)
            for (MonsterCard opponentMonster : opponentMonsters) {
                if (opponentMonster.getIsHidden()) {
                    if (aiMonster.getAttackPoint() > opponentMonster.getDefensePoint())
                        winningBeforeUsage++;
                    if (aiMonster.getAttackPoint() + amount > opponentMonster.getDefensePoint())
                        winningAfterUsage++;
                } else {
                    if (aiMonster.getAttackPoint() > opponentMonster.getAttackPoint())
                        winningBeforeUsage++;
                    if (aiMonster.getAttackPoint() + amount > opponentMonster.getAttackPoint())
                        winningAfterUsage++;
                }
            }
        if (winningAfterUsage > winningBeforeUsage)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.REASONABLE_FOR_EASY;
    }

    public ReasonableLevel isReasonableToActiveMagnumShield() {
        int winningBeforeUsage = 0;
        int winningAfterUsage = 0;
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
        for (MonsterCard aiMonster : aiMonsters) {
            int amount = aiMonster.getAttackPoint() + aiMonster.getDefensePoint();
            for (MonsterCard opponentMonster : opponentMonsters) {
                if (opponentMonster.getIsHidden()) {
                    if (aiMonster.getAttackPoint() > opponentMonster.getDefensePoint())
                        winningBeforeUsage++;
                    if (amount > opponentMonster.getDefensePoint())
                        winningAfterUsage++;
                } else {
                    if (aiMonster.getAttackPoint() > opponentMonster.getAttackPoint())
                        winningBeforeUsage++;
                    if (amount > opponentMonster.getAttackPoint())
                        winningAfterUsage++;
                }
            }
        }
        if (winningAfterUsage > winningBeforeUsage)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.REASONABLE_FOR_EASY;
    }

    public ReasonableLevel isReasonableToActiveAdvancedRitualArt() {
        MonsterCard ritualMonster = null;
        ArrayList<Card> aiHand = this.board.getHandCards();
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> aiMonstersToSacrifice = new ArrayList<>();
        for (Card card : aiHand)
            if (card.getCardType() == CardType.MONSTER) {
                MonsterCard monster = (MonsterCard) card;
                if (monster.getTrait() != Trait.RITUAL)
                    aiMonstersToSacrifice.add(monster);
                else
                    ritualMonster = monster;
            }
        aiMonstersToSacrifice.addAll(aiMonsters);

        if (ritualMonster == null)
            return ReasonableLevel.NOT_REASONABLE;
        if (isThereASetWithValueEqualToRitualLevel(aiMonstersToSacrifice, ritualMonster.getLevel()))
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.NOT_REASONABLE;

    }

    public ReasonableLevel isReasonableToActiveMindCrush() {
        return ReasonableLevel.REASONABLE_FOR_HARD;
    }

    public ReasonableLevel isReasonableToActiveTimeSeal() {
        if (opponent.getPlayerBoard().getMonsters().size() == 0)
            return ReasonableLevel.NOT_REASONABLE;

        return ReasonableLevel.REASONABLE_FOR_HARD;
    }

    public ReasonableLevel isReasonableToActiveCallOfTheHaunted() {
        boolean hasAnySpecialMonster = false;

        ArrayList<Card> aiGraveyard = this.board.getGraveyardCards();

        for (Card card : aiGraveyard)
            if (card.getCardType() == CardType.MONSTER) {
                MonsterCard monster = (MonsterCard) card;
                if (monster.getLevel() > 4 && monster.getTrait() != Trait.RITUAL) {
                    hasAnySpecialMonster = true;
                    break;
                }
            }

        if (hasAnySpecialMonster && getAIMonsters().size() < 5)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.NOT_REASONABLE;
    }

    public ReasonableLevel isReasonableToActiveVanitysEmptiness() {
        boolean hasSpecialMonster = false;
        for (MonsterCard monster: getAIMonsters())
            if (monster.getLevel() > 4) {
                hasSpecialMonster = true;
                break;
            }
        if (hasSpecialMonster)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.NOT_REASONABLE;
    }

    public ReasonableLevel isReasonableToActiveWallOfRevealingLight() {
        if (this.board.getLifePoints()/1000 > getOpponentMostPowerfulMonsterAttackPoint()/1000)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        if (this.board.getLifePoints()/1000 > getOpponentLeastPowerfulMonsterAttackPoint()/1000)
            return ReasonableLevel.REASONABLE_FOR_EASY;
        return ReasonableLevel.NOT_REASONABLE;
    }

    /*------Some Exceptions------*/
    /*------Some Exceptions------*/
    /*------Some Exceptions------*/
    /*------Some Exceptions------*/
    /*------Some Exceptions------*/

    public ReasonableLevel iskeepingMessengerOfPeaceReasonable() {
        int aiMonstersCounter = 0;
        int opponentMonsterCounter = 0;
        ArrayList<MonsterCard> aiMonsters = getAIMonsters();
        ArrayList<MonsterCard> opponentMonsters = getOpponentMonsters();
        for (MonsterCard aiMonster : aiMonsters)
            if (aiMonster.getAttackPoint() > 1500)
                aiMonstersCounter++;
        for (MonsterCard opponentMonster : opponentMonsters)
            if (opponentMonster.getAttackPoint() > 1500)
                opponentMonsterCounter++;

        if (this.board.getLifePoints() <= 100)
            return ReasonableLevel.NOT_REASONABLE;
        if (opponentMonsterCounter > aiMonstersCounter)
            return ReasonableLevel.REASONABLE_FOR_HARD;
        return ReasonableLevel.REASONABLE_FOR_EASY;
    }

}