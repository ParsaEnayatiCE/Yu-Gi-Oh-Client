package view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    /*---LOGIN---*/

    public static final String LOGIN_USER_1 = "^user login (--username|-u) (\\S+) (--password|-p) (\\S+)$";
    public static final String LOGIN_USER_2 = "^user login (--password|-p) (\\S+) (--username|-u) (\\S+)$";
    //----------------------------------------------------------------------------------------------

    /*---ENTER MENU,EXIT MENU,SHOW MENU---*/

    public static final String ENTER_MENU = "^menu enter (\\S+)$";
    public static final String EXIT_MENU = "^menu exit$";
    public static final String SHOW_CURRENT_MENU = "^menu show-current$";
    //----------------------------------------------------------------------------------------------

    /*---CREATE USER---*/

    public static final String CREATE_USER_1 = "^user create (--username|-u) (\\S+) (--nickname|-n) (\\S+) (--password|-p) (\\S+)$";
    public static final String CREATE_USER_2 = "^user create (--username|-u) (\\S+) (--password|-p) (\\S+) (--nickname|-n) (\\S+)$";
    public static final String CREATE_USER_3 = "^user create (--nickname|-n) (\\S+) (--username|-u) (\\S+) (--password|-p) (\\S+)$";
    public static final String CREATE_USER_4 = "^user create (--nickname|-n) (\\S+) (--password|-p) (\\S+) (--username|-u) (\\S+)$";
    public static final String CREATE_USER_5 = "^user create (--password|-p) (\\S+) (--nickname|-n) (\\S+) (--username|-u) (\\S+)$";
    public static final String CREATE_USER_6 = "^user create (--password|-p) (\\S+) (--username|-u) (\\S+) (--nickname|-n) (\\S+)$";
    //----------------------------------------------------------------------------------------------

    /*---LOGOUT,SHOW SCOREBOARD,CHANGE PROFILE---*/

    public static final String USER_LOGOUT = "^user logout$";
    public static final String SHOW_SCOREBOARD = "^scoreboard show$";
    public static final String CHANGE_NICKNAME = "^profile change (--nickname|-n) (\\S+)$";
    public static final String CHANGE_USERNAME = "^profile change (--username|-u) (\\S+)$";
    //----------------------------------------------------------------------------------------------

    /*---CHANGE PASS---*/

    public static final String CHANGE_PASS_1 = "^profile change (--password|-p) (--current|-c) (\\S+) (--new|-n) (\\S+)$";
    public static final String CHANGE_PASS_2 = "^profile change (--password|-p) (--new|-n) (\\S+) (--current|-c) (\\S+)$";
    public static final String CHANGE_PASS_3 = "^profile change (--current|-c) (\\S+) (--password|-p) (--new|-n) (\\S+)$";
    public static final String CHANGE_PASS_4 = "^profile change (--current|-c) (\\S+) (--new|-n) (\\S+) (--password|-p)$";
    public static final String CHANGE_PASS_5 = "^profile change (--new|-n) (\\S+) (--password|-p) (--current|-c) (\\S+)$";
    public static final String CHANGE_PASS_6 = "^profile change (--new|-n) (\\S+) (--current|-c) (\\S+) (--password|-p)$";
    //----------------------------------------------------------------------------------------------

    /*---DECK CREATION,DELETION,ACTIVATION---*/

    public static final String CREATE_DECK = "^deck create (\\S+)$";
    public static final String DELETE_DECK = "^deck delete (\\S+)$";
    public static final String ACTIVATE_DECK = "^deck set-activate (\\S+)$";
    //----------------------------------------------------------------------------------------------

    /*---ADD CARD TO DECK---*/

    public static final String ADD_CARD_TO_DECK_1 = "^deck add-card (--card|-c) (.+) (--deck|-d) (\\S+)( --side| -s)?$";
    public static final String ADD_CARD_TO_DECK_2 = "^deck add-card (--card|-c) (.+)( --side| -s)? (--deck|-d) (\\S+)$";
    public static final String ADD_CARD_TO_DECK_3 = "^deck add-card (--deck|-d) (\\S+) (--card|-c) (.+)( --side| -s)?$";
    public static final String ADD_CARD_TO_DECK_4 = "^deck add-card (--deck|-d) (\\S+)( --side| -s)? (--card|-c) (.+)$";
    public static final String ADD_CARD_TO_DECK_5 = "^deck add-card( --side| -s)? (--deck|-d) (\\S+) (--card|-c) (.+)$";
    public static final String ADD_CARD_TO_DECK_6 = "^deck add-card( --side| -s)? (--card|-c) (.+) (--deck|-d) (\\S+)$";
    //----------------------------------------------------------------------------------------------

    /*---REMOVE CARD FROM DECK---*/

    public static final String REMOVE_CARD_FROM_DECK_1 = "^deck rm-card (--card|-c) (.+) (--deck|-d) (\\S+)( --side| -s)?$";
    public static final String REMOVE_CARD_FROM_DECK_2 = "^deck rm-card (--card|-c) (.+)( --side| -s)? (--deck|-d) (\\S+)$";
    public static final String REMOVE_CARD_FROM_DECK_3 = "^deck rm-card (--deck|-d) (\\S+) (--card|-c) (.+)( --side| -s)?$";
    public static final String REMOVE_CARD_FROM_DECK_4 = "^deck rm-card (--deck|-d) (\\S+)( --side| -s)? (--card|-c) (.+)$";
    public static final String REMOVE_CARD_FROM_DECK_5 = "^deck rm-card( --side| -s)? (--deck|-d) (\\S+) (--card|-c) (.+)$";
    public static final String REMOVE_CARD_FROM_DECK_6 = "^deck rm-card( --side| -s)? (--card|-c) (.+) (--deck|-d) (\\S+)$";
    //----------------------------------------------------------------------------------------------

    /*---SHOW DECK---*/

    public static final String SHOW_ALL_DECKS = "^deck show (--all|-a)$";
    public static final String SHOW_OPTIONAL_DECK_1 = "^deck show (--deck-name|-d) (\\S+)( --side| -s)?$";
    public static final String SHOW_OPTIONAL_DECK_2 = "^deck show( --side| -s)? (--deck-name|-d) (\\S+)$";
    //----------------------------------------------------------------------------------------------

    /*---SHOW CARD,BUY CARD---*/

    public static final String SHOW_ALL_USER_CARDS = "^deck show (--cards|-c)$";
    public static final String BUY_CARD = "^shop buy (.+)$";
    public static final String SHOW_CARDS_IN_SHOP = "^shop show (--all|-a)$";
    public static final String SHOP_SEE_MONEY = "shop show money";
    //----------------------------------------------------------------------------------------------

    /*---DUEL MULTI---*/

    public static final String DUEL_MULTIPLAYER_1 = "^duel (--new|-n) (--second-player|-sp) (\\S+) (--rounds|-r) (\\d+)$";
    public static final String DUEL_MULTIPLAYER_2 = "^duel (--new|-n) (--rounds|-r) (\\d+) (--second-player|-sp) (\\S+)$";
    public static final String DUEL_MULTIPLAYER_3 = "^duel (--second-player|-sp) (\\S+) (--new|-n) (--rounds|-r) (\\d+)$";
    public static final String DUEL_MULTIPLAYER_4 = "^duel (--second-player|-sp) (\\S+) (--rounds|-r) (\\d+) (--new|-n)$";
    public static final String DUEL_MULTIPLAYER_5 = "^duel (--rounds|-r) (\\d+) (--second-player|-sp) (\\S+) (--new|-n)$";
    public static final String DUEL_MULTIPLAYER_6 = "^duel (--rounds|-r) (\\d+) (--new|-n) (--second-player|-sp) (\\S+)$";
    //----------------------------------------------------------------------------------------------

    /*---DUEL SINGLE---*/

    //What is sort form of ai?:|
    public static final String DUEL_SINGLE_PLAYER_1 = "^duel (--new|-n) (--ai|-ai) (--rounds|-r) (\\d+) (--difficulty|-d) (easy|hard)$";
    public static final String DUEL_SINGLE_PLAYER_2 = "^duel (--new|-n) (--rounds|-r) (\\d+) (--ai|-ai) (--difficulty|-d) (easy|hard)$";
    public static final String DUEL_SINGLE_PLAYER_3 = "^duel (--new|-n) (--ai|-ai) (--difficulty|-d) (easy|hard) (--rounds|-r) (\\d+)$";
    public static final String DUEL_SINGLE_PLAYER_4 = "^duel (--new|-n) (--difficulty|-d) (easy|hard) (--ai|-ai) (--rounds|-r) (\\d+)$";
    public static final String DUEL_SINGLE_PLAYER_5 = "^duel (--new|-n) (--difficulty|-d) (easy|hard) (--rounds|-r) (\\d+) (--ai|-ai)$";
    public static final String DUEL_SINGLE_PLAYER_6 = "^duel (--new|-n) (--rounds|-r) (\\d+) (--difficulty|-d) (easy|hard) (--ai|-ai)$";
    //===============================
    public static final String DUEL_SINGLE_PLAYER_7 = "^duel (--ai|-ai) (--new|-n) (--rounds|-r) (\\d+) (--difficulty|-d) (easy|hard)$";
    public static final String DUEL_SINGLE_PLAYER_8 = "^duel (--ai|-ai) (--rounds|-r) (\\d+) (--new|-n) (--difficulty|-d) (easy|hard)$";
    public static final String DUEL_SINGLE_PLAYER_9 = "^duel (--ai|-ai) (--new|-n) (--difficulty|-d) (easy|hard) (--rounds|-r) (\\d+)$";
    public static final String DUEL_SINGLE_PLAYER_10 = "^duel (--ai|-ai) (--difficulty|-d) (easy|hard) (--new|-n) (--rounds|-r) (\\d+)$";
    public static final String DUEL_SINGLE_PLAYER_11 = "^duel (--ai|-ai) (--difficulty|-d) (easy|hard) (--rounds|-r) (\\d+) (--new|-n)$";
    public static final String DUEL_SINGLE_PLAYER_12 = "^duel (--ai|-ai) (--rounds|-r) (\\d+) (--difficulty|-d) (easy|hard) (--new|-n)$";
    //==============================
    public static final String DUEL_SINGLE_PLAYER_13 = "^duel (--rounds|-r) (\\d+) (--new|-n) (--ai|-ai) (--difficulty|-d) (easy|hard)$";
    public static final String DUEL_SINGLE_PLAYER_14 = "^duel (--rounds|-r) (\\d+) (--ai|-ai) (--new|-n) (--difficulty|-d) (easy|hard)$";
    public static final String DUEL_SINGLE_PLAYER_15 = "^duel (--rounds|-r) (\\d+) (--new|-n) (--difficulty|-d) (easy|hard) (--ai|-ai)$";
    public static final String DUEL_SINGLE_PLAYER_16 = "^duel (--rounds|-r) (\\d+) (--difficulty|-d) (easy|hard) (--new|-n) (--ai|-ai)$";
    public static final String DUEL_SINGLE_PLAYER_17 = "^duel (--rounds|-r) (\\d+) (--difficulty|-d) (easy|hard) (--ai|-ai) (--new|-n)$";
    public static final String DUEL_SINGLE_PLAYER_18 = "^duel (--rounds|-r) (\\d+) (--ai|-ai) (--difficulty|-d) (easy|hard) (--new|-n)$";
    //==============================
    public static final String DUEL_SINGLE_PLAYER_19 = "^duel (--difficulty|-d) (easy|hard) (--new|-n) (--ai|-ai) (--rounds|-r) (\\d+)$";
    public static final String DUEL_SINGLE_PLAYER_20 = "^duel (--difficulty|-d) (easy|hard) (--ai|-ai) (--new|-n) (--rounds|-r) (\\d+)$";
    public static final String DUEL_SINGLE_PLAYER_21 = "^duel (--difficulty|-d) (easy|hard) (--new|-n) (--rounds|-r) (\\d+) (--ai|-ai)$";
    public static final String DUEL_SINGLE_PLAYER_22 = "^duel (--difficulty|-d) (easy|hard) (--rounds|-r) (\\d+) (--new|-n) (--ai|-ai)$";
    public static final String DUEL_SINGLE_PLAYER_23 = "^duel (--difficulty|-d) (easy|hard) (--rounds|-r) (\\d+) (--ai|-ai) (--new|-n)$";
    public static final String DUEL_SINGLE_PLAYER_24 = "^duel (--difficulty|-d) (easy|hard) (--ai|-ai) (--rounds|-r) (\\d+) (--new|-n)$";
    //----------------------------------------------------------------------------------------------

    /*---SELECTION AND DESELECTION---*/
    public static final String SELECT_OWN_MONSTER = "^select (--monster|-m) (\\d)$";
    public static final String SELECT_OPPONENT_MONSTER_1 = "^select (--monster|-m) (\\d) (opponent|-o)$";
    public static final String SELECT_OPPONENT_MONSTER_2 = "^select (opponent|-o) (--monster|-m) (\\d)$";
    public static final String SELECT_HAND_CARD = "^select (--hand|-h) (\\d+)$";
    public static final String SELECT_OWN_SPELL_CARD = "^select (--spell|-s) (\\d+)$";
    public static final String SELECT_OWN_FIELD = "^select (--field|-f)$";
    public static final String SELECT_OPPONENT_FIELD_1 = "^select (--opponent|-o) (--field|-f)$";
    public static final String SELECT_OPPONENT_FIELD_2 = "^select (--field|-f) (--opponent|-o)$";
    public static final String SELECT_OPPONENT_SPELL_CARD_1 = "^select (--opponent|-o) (--spell|-s) (\\d+)$";
    public static final String SELECT_OPPONENT_SPELL_CARD_2 = "^select (--spell|-s) (\\d+) (--opponent|-o)$";
    public static final String DESELECT_CARD = "^select -d$";
    //----------------------------------------------------------------------------------------------

    /*---OTHER DUEL COMMANDS---*/

    public static final String SUMMON = "^summon$";
    public static final String TRIBUTE_SUMMON = "^tribute summon (\\d|\\d \\d)";
    public static final String SET = "^set$";
    public static final String SET_CARD_POSITION = "^set (--position|-p) (attack|defense)$";
    public static final String FLIP_SUMMON = "^flip-summon$";
    public static final String SPECIAL_SUMMON = "^special-summon$";
    public static final String RITUAL_SUMMON = "^ritual-summon ([\\d ])$";
    public static final String ATTACK_MONSTER = "^attack (\\d)$";
    public static final String ATTACK_DIRECT = "^attack direct$";
    public static final String ACTIVATE_EFFECT = "^activate effect$";
    public static final String ACTIVATE_EFFECT_ON_MONSTER = "^activate effect on monster (\\d)$";
    public static final String EQUIP_MONSTER = "^equip monster (\\d)$";
    public static final String SHOW_GRAVEYARD = "^show graveyard$";
    public static final String SHOW_SELECTED_CARD = "^card show (--selected|-s)$";
    public static final String SURRENDER = "^surrender$";
    //----------------------------------------------------------------------------------------------

    /*---CHEATS---*/

    public static final String CHEAT_INCREASE_MONEY = "^increase (--money|-m) (\\d+)$";
    public static final String CHEAT_SELECT_MORE_CARDS_1 = "^select (--hand|-h) (\\S+) (--force|-f)$";
    public static final String CHEAT_SELECT_MORE_CARDS_2 = "^select (--force|-f) (--hand|-h) (\\S+)$";
    public static final String CHEAT_INCREASE_LP = "^increase (--LP|-l) (\\d+)$";
    public static final String CHEAT_SET_WINNER = "^duel set-winner (\\S+)$";
    //----------------------------------------------------------------------------------------------

    /*---IMPORT EXPORT---*/

    public static final String IMPORT_CARD = "^import card (\\S+)$";
    public static final String EXPORT_CARD = "^export card (\\S+)$";

    //----------------------------------------------------------------------------------------------

    /*---PHASE---*/
    public static final String SWITCH_PHASE = "^next phase$";
    public static final String SWITCH_CARDS = "^switch card (\\d+) from main with card (\\d+) from side$";
    //----------------------------------------------------------------------------------------------

    public static Matcher getMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
