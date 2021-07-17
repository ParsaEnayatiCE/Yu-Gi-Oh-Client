package models;

import java.util.ArrayList;

public class Player {
    private static final ArrayList<Player> allPlayers;
    private Deck playerDeck;
    private Board playerBoard;
    private String userName;
    private String nickName;
    private int maxLifePoint = 0;

    static {
        allPlayers = new ArrayList<>();
    }

    public Player(User user) throws CloneNotSupportedException {
        allPlayers.add(this);
        setPlayerDeck(user.getActiveDeck());
        setPlayerBoard(new Board(this));
        setUserName(user.getUserName());
        setNickName(user.getNickName());
    }

    public static Player getFirstPlayer() {
        return allPlayers.get(0);
    }

    public static Player getSecondPlayer() {
        return allPlayers.get(1);
    }

    public static void removePlayers() {
        allPlayers.clear();
    }

    public Deck getPlayerDeck() {
        return this.playerDeck;
    }

    private void setPlayerDeck(Deck playerDeck) {
        this.playerDeck = playerDeck;
    }

    public Board getPlayerBoard() {
        return this.playerBoard;
    }

    public void setPlayerBoard(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public String getUserName() {
        return this.userName;
    }

    private void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public User getUser() {
        return User.getUserByUserName(this.userName);
    }

    public int getMaxLifePoint() {
        return this.maxLifePoint;
    }

    public void setMaxLifePoint(int lifePoint) {
        if (lifePoint > this.maxLifePoint)
            this.maxLifePoint = lifePoint;
    }
}
