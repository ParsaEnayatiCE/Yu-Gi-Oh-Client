package view;


/*-------------------REMEMBER TO CORRECT SKIPS----------------------*/


public enum StatusEnum {
    INVALID_COMMAND("invalid command"),
    PLEASE_LOGIN_FIRST("please login first"),
    MENU_NAVIGATION_NOT_POSSIBLE("menu navigation is not possible"),
    USER_CREATE_SUCCESSFULLY("user created successfully!"),
    USER_LOGIN_SUCCESSFULLY("user logged in successfully!"),
    USERNAME_AND_PASSWORD_MISMATCH("Username and password didn’t match!"),
    USER_LOGOUT_SUCCESSFULLY("user logged out successfully!"),
    CHANGE_USERNAME_SUCCESSFULLY("username changed successfully!"),
    CHANGE_NICKNAME_SUCCESSFULLY("nickname changed successfully!"),
    CHANGE_PASSWORD_SUCCESSFULLY("password changed successfully!"),
    CURRENT_PASSWORD_INVALIDITY("current password is invalid"),
    ENTER_A_NEW_PASSWORD("please enter a new password"),
    DECK_CREATE_SUCCESSFULLY("deck created successfully!"),
    DECK_DELETED_SUCCESSFULLY("deck deleted successfully"),
    DECK_ACTIVATED_SUCCESSFULLY("deck activated successfully"),
    CARD_ADDED_TO_DECK_SUCCESSFULLY("card added to deck successfully"),
    FULL_MAIN_DECK("main deck is full"),
    FULL_SIDE_DECK("side deck is full"),
    CARD_REMOVED_SUCCESSFULLY("card removed form deck successfully"),
    INVALID_CARD_IN_SHOP("there is no card with this name"),
    NOT_ENOUGH_MONEY("not enough money"),
    NO_EXISTENCE_OF_PLAYER2("there is no player with this username"),
    ROUNDS_NOT_SUPPORTED("number of rounds is not supported"),
    INVALID_SELECTION("invalid selection"),
    CARD_SELECTED("card selected"),
    NO_CARD_FOUND_IN_POSITION("no card found in the given position"),
    NO_CARD_SELECTED_YET("no card is selected yet"),
    CARD_DESELECTED("card deselected"),
    NO_CARD_IS_SELECTED_YET("no card is selected yet"),
    CANT_DO_THIS_ACTION_IN_THIS_PHASE("you can’t do this action in this phase"),
    CANT_ATTACK_WITH_CARD("you can’t attack with this card"),
    CANT_ATTACK_DIRECTLY("you can't attack the opponent directly"),
    CARD_ALREADY_ATTACKED("this card already attacked"),
    NO_CARD_TO_ATTACK_HERE("there is no card to attack here"),
    BOTH_RECEIVED_DAMAGE_OO("both you and your opponent monster cards are destroyed and no one receives damage"),
    DEFENSE_POSITION_DESTROYED_DO("the defense position monster is destroyed"),
    NO_CARD_DESTROYED_EQUAL_DEFENSES_DO("no card is destroyed"),
    DEFENSE_POSITION_DESTROYED_DH("the defense position monster is destroyed"),
    PREPARATION_OF_SPELL_NOT_DONE("preparations of this spell are not done yet"),
    SPELL_ACTIVATED("spell activated"),
    SPELL_OR_TRAP_ACTIVATED(""),//SKIP--------------------------
    NO_WAY_TO_RITUAL("there is no way you could ritual summon a monster"),
    NO_WAY_TO_SPECIAL_SUMMON("there is no way you could special summon a monster"),
    CARD_BOUGHT_SUCCESSFULLY("you bought the card successfully"),
    IMPORTED_SUCCESSFULLY("card imported successfully!"),
    EXPORTED_SUCCESSFULLY("card exported successfully!"),
    CARD_NOT_FOUND("card not found!");
    private String status;

    StatusEnum(String status){
        setStatus(status);
    }



    public String getStatus(){
        return  status;
    }
    public void setStatus(String status){
        this.status = status;
    }

}
