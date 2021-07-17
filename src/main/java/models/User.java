package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import javafx.scene.image.Image;
import models.cards.Card;
import models.cards.CardType;
import models.cards.monsters.MonsterCard;
import models.cards.spelltrap.SpellTrapCard;

public class User {

    public static ArrayList<User> allUsers;
    private final ArrayList<MonsterCard> userMonsters = new ArrayList<>();
    private final ArrayList<SpellTrapCard> userSpellTraps = new ArrayList<>();
    private final ArrayList<String> userDecks = new ArrayList<>();
    private String activeDeckName;
    private String userName;
    private String nickName;
    private String password;
    private int score;
    private int money;
    private String avatar;
    private String miniAvatar;
    private Image avatarImage = null;

    private int rank;


    static {
        allUsers = new ArrayList<>();
    }

    public User(String userName, String nickName, String password) {
        allUsers.add(this);
        setActiveDeck(null);
        setUserName(userName);
        setNickName(nickName);
        setPassword(password);
        setScore(0);
        setMoney(100000);
        avatar = "./image/default_avatar.png";
        miniAvatar = "./image/default_avatar.png";
    }

    public void setAvatar(Image image) {
        this.avatarImage = image;
    }

    public Image getAvatarImage() {
        return this.avatarImage;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar, String miniAvatar) {
        this.avatar = avatar;
        this.miniAvatar = miniAvatar;
    }

    public String getMiniAvatar() {
        return miniAvatar;
    }

    public static void loadAllUsers(ArrayList<User> users) {
        allUsers = users;
    }

    public static User getUserByUserName(String userName) {
        for (User allUser : allUsers)
            if (allUser.getUserName().equals(userName))
                return allUser;

        return null;
    }

    public static boolean isUserNameTaken(String userName) {
        for (User allUser : allUsers)
            if (allUser.getUserName().equals(userName))
                return true;

        return false;
    }

    public static boolean isNickNameTaken(String nickName) {
        for (User allUser : allUsers)
            if (allUser.getNickName().equals(nickName))
                return true;

        return false;
    }

    public static ArrayList<User> getSortedUsers() {
        sortUsers();
        setRanks();
        return allUsers;
    }

    private void sortUserCards() {
        String[] monsterNames = new String[userMonsters.size()];
        String[] spellTrapNames = new String[userSpellTraps.size()];
        for (int i = 0; i < monsterNames.length; i++)
            monsterNames[i] = userMonsters.get(i).getName();
        for (int i = 0; i < spellTrapNames.length; i++)
            spellTrapNames[i] = userSpellTraps.get(i).getName();

        Arrays.sort(monsterNames);
        Arrays.sort(spellTrapNames);
        for (int i = 0; i < monsterNames.length; i++)
            userMonsters.set(i, (MonsterCard) Card.getCardByName(monsterNames[i]));
        for (int i = 0; i < spellTrapNames.length; i++)
            userSpellTraps.set(i, (SpellTrapCard) Card.getCardByName(spellTrapNames[i]));
    }

    private static void sortUsers() {
        for (int i = 1; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            int j = i - 1;
            while (j >= 0 && !User.compareTwoUsers(allUsers.get(j), user)) {
                allUsers.set(j + 1, allUsers.get(j));
                j--;
            }
            allUsers.set(j + 1, user);
        }
    }

    private static boolean compareTwoUsers(User first, User second) {
        if (first.getScore() > second.getScore())
            return true;
        else if (first.getScore() < second.getScore())
            return false;
        else return first.getNickName().compareTo(second.getNickName()) < 0;
    }

    public ArrayList<Deck> getUserDecks() {
        ArrayList<Deck> decks = new ArrayList<>();
        for (String deckName : userDecks)
            decks.add(Deck.getDeckByName(deckName));
        decks.sort(Comparator.comparing(Deck::getName));
        return decks;
    }

    public ArrayList<Card> getUserCards() {
        sortUserCards();
        ArrayList<Card> cards = new ArrayList<>();
        cards.addAll(userMonsters);
        cards.addAll(userSpellTraps);
        return cards;
    }

    public Card getUserCardByName(String name) {
        if (Card.getCardByName(name) == null)
            return null;
        else {
            if (Card.getCardByName(name).getCardType() == CardType.MONSTER) {
                for (MonsterCard monster : userMonsters)
                    if (monster.getName().equals(name))
                        return monster;
            } else {
                for (SpellTrapCard spellTrap : userSpellTraps)
                    if (spellTrap.getName().equals(name))
                        return spellTrap;
            }
        }


        return null;
    }

    public Deck getUserDeckByName(String name) {
        for (String deckName : userDecks)
            if (deckName.equals(name))
                return Deck.getDeckByName(name);

        return null;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setActiveDeck(Deck deck) {
        if (deck == null)
            this.activeDeckName = "";
        else
            this.activeDeckName = deck.getName();
    }

    public Deck getActiveDeck() {
        return Deck.getDeckByName(this.activeDeckName);
    }

    public void setScore(int amount) {
        this.score = amount;
    }

    public int getScore() {
        return this.score;
    }

    public void setMoney(int amount) {
        this.money = amount;
    }

    public int getMoney() {
        return this.money;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void addCard(Card card) {
        if (card instanceof MonsterCard)
            this.userMonsters.add((MonsterCard) card);
        else
            this.userSpellTraps.add((SpellTrapCard) card);
    }

    public void addDeck(Deck deck) {
        this.userDecks.add(deck.getName());
    }

    public void removeDeck(Deck deck) {
        this.userDecks.remove(deck.getName());
    }

    public void changePassword(String password) {
        setPassword(password);
    }

    private static void setRanks() {
        for (int i = 0, rank = 1; i < allUsers.size(); i++, rank++) {
            if (i > 0) {
                if (allUsers.get(i).getScore() == allUsers.get(i - 1).getScore())
                    rank--;
                else
                    rank = i + 1;
            }
            allUsers.get(i).setRank(rank);
        }
    }

    public String toString() {
        return this.nickName + ": " + this.score;
    }

    public int getCardAmount(Card card) {
        int counter = 0;
        if (card instanceof MonsterCard) {
            for (MonsterCard monster : userMonsters)
                if (monster.getName().equals(card.getName())) counter++;
        } else {
            for (SpellTrapCard spellTrap : userSpellTraps)
                if (spellTrap.getName().equals(card.getName())) counter++;
        }
        return counter;
    }
}
