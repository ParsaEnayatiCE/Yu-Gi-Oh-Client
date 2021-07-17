package view.menus;

import controller.menucontroller.DeckMenuController;
import controller.menucontroller.LoginMenuController;
import models.User;
import models.cards.CardType;
import view.MenuEnum;
import view.ProgramController;
import view.Regex;
import view.StatusEnum;

import java.util.regex.Matcher;

public class DeckMenu {

    private final User currentUser = LoginMenuController.currentUser;
    private DeckMenuController deckMenuController;

    public void run(String command) {

        deckMenuController = new DeckMenuController(currentUser);

        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.CREATE_DECK)).matches()){
            String deckName = matcher.group(1);
            createDeck(deckName);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.DELETE_DECK)).matches()){
            String deckName = matcher.group(1);
            deleteDeck(deckName);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.ACTIVATE_DECK)).matches()){
            String deckName = matcher.group(1);
            activateDeck(deckName);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.ADD_CARD_TO_DECK_1)).matches()){
            String cardName = matcher.group(2);
            String deckName = matcher.group(4);
            String mainOrSide;
            if (matcher.group(5) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            addCard(deckName,cardName,mainOrSide);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.ADD_CARD_TO_DECK_2)).matches()){
            String cardName = matcher.group(2);
            String deckName = matcher.group(5);
            String mainOrSide;
            if (matcher.group(3) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            addCard(deckName,cardName,mainOrSide);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.ADD_CARD_TO_DECK_3)).matches()){
            String cardName = matcher.group(4);
            String deckName = matcher.group(2);
            String mainOrSide;
            if (matcher.group(5) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            addCard(deckName,cardName,mainOrSide);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.ADD_CARD_TO_DECK_4)).matches()){
            String cardName = matcher.group(5);
            String deckName = matcher.group(2);
            String mainOrSide;
            if (matcher.group(3) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            addCard(deckName,cardName,mainOrSide);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.ADD_CARD_TO_DECK_5)).matches()){
            String cardName = matcher.group(5);
            String deckName = matcher.group(3);
            String mainOrSide;
            if (matcher.group(1) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            addCard(deckName,cardName,mainOrSide);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.ADD_CARD_TO_DECK_6)).matches()){
            String cardName = matcher.group(5);
            String deckName = matcher.group(4);
            String mainOrSide;
            if (matcher.group(1) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            addCard(deckName,cardName,mainOrSide);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.REMOVE_CARD_FROM_DECK_1)).matches()){
            String cardName = matcher.group(2);
            String deckName = matcher.group(4);
            String mainOrSide;
            if (matcher.group(5) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            removeCardFromDeck(deckName,cardName,mainOrSide);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.REMOVE_CARD_FROM_DECK_2)).matches()){
            String cardName = matcher.group(2);
            String deckName = matcher.group(5);
            String mainOrSide;
            if (matcher.group(3) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            removeCardFromDeck(deckName,cardName,mainOrSide);

        }
        else if ((matcher = Regex.getMatcher(command, Regex.REMOVE_CARD_FROM_DECK_3)).matches()){
            String cardName = matcher.group(4);
            String deckName = matcher.group(2);
            String mainOrSide;
            if (matcher.group(5) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            removeCardFromDeck(deckName,cardName,mainOrSide);

        }
        else if ((matcher = Regex.getMatcher(command, Regex.REMOVE_CARD_FROM_DECK_4)).matches()){
            String cardName = matcher.group(5);
            String deckName = matcher.group(2);
            String mainOrSide;
            if (matcher.group(3) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            removeCardFromDeck(deckName,cardName,mainOrSide);

        }
        else if ((matcher = Regex.getMatcher(command, Regex.REMOVE_CARD_FROM_DECK_5)).matches()){
            String cardName = matcher.group(5);
            String deckName = matcher.group(3);
            String mainOrSide;
            if (matcher.group(1) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            removeCardFromDeck(deckName,cardName,mainOrSide);

        }
        else if ((matcher = Regex.getMatcher(command, Regex.REMOVE_CARD_FROM_DECK_6)).matches()){
            String cardName = matcher.group(5);
            String deckName = matcher.group(4);
            String mainOrSide;
            if (matcher.group(1) == null){
                mainOrSide = "main";
            }
            else{
                mainOrSide = "side";
            }
            removeCardFromDeck(deckName,cardName,mainOrSide);

        }
        else if ((Regex.getMatcher(command, Regex.SHOW_ALL_DECKS)).matches()){
            showAllUserDecks();
        }
        else if ((matcher = Regex.getMatcher(command, Regex.SHOW_OPTIONAL_DECK_1)).matches()){
            String deckName = matcher.group(2);
            String mainOrSide;
            if (matcher.group(3) == null){
                mainOrSide = "Main";
            }
            else{
                mainOrSide = "Side";
            }
            showOptionalDeck(deckName,mainOrSide);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.SHOW_OPTIONAL_DECK_2)).matches()){
            String deckName = matcher.group(3);
            String mainOrSide;
            if (matcher.group(1) == null){
                mainOrSide = "Main";
            }
            else{
                mainOrSide = "Side";
            }
            showOptionalDeck(deckName,mainOrSide);
        }
        else if ((Regex.getMatcher(command, Regex.SHOW_ALL_USER_CARDS)).matches()){
            showAllCards();
        }
        else if ((Regex.getMatcher(command, Regex.EXIT_MENU)).matches()) {
            ProgramController.currentMenu = MenuEnum.MAIN_MENU;
        }
        else if ((Regex.getMatcher(command, Regex.ENTER_MENU)).matches()) {
            System.out.println(StatusEnum.MENU_NAVIGATION_NOT_POSSIBLE.getStatus());
        }
        else if ((Regex.getMatcher(command, Regex.SHOW_CURRENT_MENU)).matches()) {
            System.out.println("Deck");
        }
        else {
            System.out.println(StatusEnum.INVALID_COMMAND.getStatus());
        }
    }


    private void showAllUserDecks(){
        System.out.println("Decks:");
        if (currentUser.getActiveDeck() != null)
            System.out.println("Active Deck: \n" + currentUser.getActiveDeck().toString());
        else
            System.out.println("Active Deck: ");
        System.out.println("Other decks:");
        for (int i = 0; i < currentUser.getUserDecks().size(); i++) {
            if (!currentUser.getUserDecks().get(i).equals(currentUser.getActiveDeck()))
                System.out.println(currentUser.getUserDecks().get(i).toString());
        }
    }

    private void createDeck(String deckName) {
        System.out.println(deckMenuController.createDeck(deckName));
    }

    private void deleteDeck(String deckName) {
        System.out.println(deckMenuController.deleteDeck(deckName));
    }

    private void activateDeck(String deckName) {
        System.out.println(deckMenuController.activateDeck(deckName));
    }

    private void addCard(String deckName, String cardName, String mainOrSide) {
        System.out.println(deckMenuController.addCard(deckName, cardName, mainOrSide));
    }

    private void removeCardFromDeck(String deckName, String cardName, String mainOrSide) {
        System.out.println(deckMenuController.removeCardFromDeck(deckName, cardName, mainOrSide));
    }

    private void showOptionalDeck(String deckName, String mainOrSide) {
        if (currentUser.getUserDeckByName(deckName) == null) {
            System.out.println("deck with name " + deckName + " does not exist");
            return;
        }
        System.out.println("Deck: " + deckName);
        System.out.println(mainOrSide + "deck:");
        System.out.println("Monsters:");
        if (mainOrSide.equals("Main")) {
            for (int i = 0; i < currentUser.getUserDeckByName(deckName).getMainDeck().size(); i++) {
                if (currentUser.getUserDeckByName(deckName).getMainDeck().get(i).getCardType().equals(CardType.MONSTER)) {
                    String cardName = currentUser.getUserDeckByName(deckName).getMainDeck().get(i).getName();
                    String cardDescription = currentUser.getUserDeckByName(deckName).getMainDeck().get(i).getDescription();
                    System.out.println(cardName + ": " + cardDescription);
                }
            }
            System.out.println("Spell and Traps:");
            for (int i = 0; i < currentUser.getUserDeckByName(deckName).getMainDeck().size(); i++) {
                if (currentUser.getUserDeckByName(deckName).getMainDeck().get(i).getCardType().equals(CardType.SPELL) || currentUser.getUserDeckByName(deckName).getMainDeck().get(i).getCardType().equals(CardType.TRAP)) {
                    String cardName = currentUser.getUserDeckByName(deckName).getMainDeck().get(i).getName();
                    String cardDescription = currentUser.getUserDeckByName(deckName).getMainDeck().get(i).getDescription();
                    System.out.println(cardName + ": " + cardDescription);
                }
            }
        } else {
            for (int i = 0; i < currentUser.getUserDeckByName(deckName).getSideDeck().size(); i++) {
                if (currentUser.getUserDeckByName(deckName).getSideDeck().get(i).getCardType().equals(CardType.MONSTER)) {
                    String cardName = currentUser.getUserDeckByName(deckName).getSideDeck().get(i).getName();
                    String cardDescription = currentUser.getUserDeckByName(deckName).getSideDeck().get(i).getDescription();
                    System.out.println(cardName + ": " + cardDescription);
                }
            }
            System.out.println("Spell and Traps:");
            for (int i = 0; i < currentUser.getUserDeckByName(deckName).getSideDeck().size(); i++) {
                if (currentUser.getUserDeckByName(deckName).getSideDeck().get(i).getCardType().equals(CardType.SPELL) || currentUser.getUserDeckByName(deckName).getSideDeck().get(i).getCardType().equals(CardType.TRAP)) {
                    String cardName = currentUser.getUserDeckByName(deckName).getSideDeck().get(i).getName();
                    String cardDescription = currentUser.getUserDeckByName(deckName).getSideDeck().get(i).getDescription();
                    System.out.println(cardName + ": " + cardDescription);
                }
            }
        }
    }

    private void showAllCards() {
        for (int i = 0; i < currentUser.getUserCards().size(); i++) {
            System.out.println(currentUser.getUserCards().get(i).getName() + ":" + currentUser.getUserCards().get(i).getDescription());
        }
    }
}
