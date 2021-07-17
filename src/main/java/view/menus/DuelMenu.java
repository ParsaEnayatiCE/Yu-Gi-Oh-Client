package view.menus;

import controller.menucontroller.DuelMenuController;
import controller.menucontroller.LoginMenuController;
import javafx.scene.input.MouseEvent;
import models.User;
import view.MenuEnum;
import view.ProgramController;
import view.Regex;
import view.StatusEnum;

import java.util.regex.Matcher;

public class DuelMenu {

    private final User currentUser = LoginMenuController.currentUser;

    DuelMenuController duelMenuController = new DuelMenuController();
    private boolean isCommandValid;

    public void run(String command) throws CloneNotSupportedException {
        isCommandValid = false;
        startTwoPlayerGame(command);
        startSinglePlayerGame(command);
        exitMenu(command);
        showMenu(command);
        changeMenu(command);
        if (!isCommandValid)
            System.out.println("invalid command!");
    }

    private void startTwoPlayerGame(String command) throws CloneNotSupportedException {
        Matcher matcher;
        String secondPlayer, rounds;
        if ((matcher = Regex.getMatcher(command, Regex.DUEL_MULTIPLAYER_1)).matches()) {
            secondPlayer = matcher.group(3);
            rounds = matcher.group(5);
        } else if ((matcher = Regex.getMatcher(command, Regex.DUEL_MULTIPLAYER_2)).matches()) {
            secondPlayer = matcher.group(5);
            rounds = matcher.group(3);
        } else if ((matcher = Regex.getMatcher(command, Regex.DUEL_MULTIPLAYER_3)).matches()) {
            secondPlayer = matcher.group(2);
            rounds = matcher.group(5);
        } else if ((matcher = Regex.getMatcher(command, Regex.DUEL_MULTIPLAYER_4)).matches()) {
            secondPlayer = matcher.group(2);
            rounds = matcher.group(4);
        } else if ((matcher = Regex.getMatcher(command, Regex.DUEL_MULTIPLAYER_5)).matches()) {
            secondPlayer = matcher.group(4);
            rounds = matcher.group(2);
        } else if ((matcher = Regex.getMatcher(command, Regex.DUEL_MULTIPLAYER_6)).matches()) {
            secondPlayer = matcher.group(5);
            rounds = matcher.group(2);
        } else return;
        isCommandValid = true;
        System.out.println(duelMenuController.startTwoPlayer(currentUser, secondPlayer, rounds));
    }

    private void startSinglePlayerGame(String command) throws CloneNotSupportedException {
        Matcher matcher;
        String rounds, difficulty;
        if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_1)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_7)).matches()) {
            rounds = matcher.group(4);
            difficulty = matcher.group(6);
        }
        else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_2)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_8)).matches()) {
            rounds = matcher.group(3);
            difficulty = matcher.group(6);
        }
        else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_3)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_9)).matches()) {
            rounds = matcher.group(6);
            difficulty = matcher.group(4);
        }else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_4)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_10)).matches()) {
            rounds = matcher.group(6);
            difficulty = matcher.group(3);
        }else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_5)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_11)).matches()) {
            rounds = matcher.group(5);
            difficulty = matcher.group(3);
        }else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_6)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_12)).matches()) {
            rounds = matcher.group(3);
            difficulty = matcher.group(5);
        }else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_13)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_14)).matches()) {
            rounds = matcher.group(2);
            difficulty = matcher.group(6);
        }else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_15)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_18)).matches()) {
            rounds = matcher.group(2);
            difficulty = matcher.group(5);
        }else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_16)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_17)).matches()) {
            rounds = matcher.group(2);
            difficulty = matcher.group(4);
        }else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_19)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_20)).matches()) {
            rounds = matcher.group(6);
            difficulty = matcher.group(2);
        }else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_21)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_24)).matches()) {
            rounds = matcher.group(5);
            difficulty = matcher.group(2);
        }else if (((matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_22)).matches())
                || (matcher = Regex.getMatcher(command, Regex.DUEL_SINGLE_PLAYER_23)).matches()) {
            rounds = matcher.group(4);
            difficulty = matcher.group(2);
        }else return;
        isCommandValid = true;
        //System.out.println(duelMenuController.startSinglePlayer(currentUser, rounds, difficulty);
    }

    public void showMenu(String command) {
        if (Regex.getMatcher(command, Regex.SHOW_CURRENT_MENU).matches()) {
            isCommandValid = true;
            System.out.println("Duel");
        }
    }

    private void exitMenu(String command) {
        if (Regex.getMatcher(command, Regex.EXIT_MENU).matches()) {
            isCommandValid = true;
            ProgramController.currentMenu = MenuEnum.MAIN_MENU;
        }
    }

    public void changeMenu(String command) {
        if (Regex.getMatcher(command, Regex.ENTER_MENU).matches()) {
            isCommandValid = true;
            System.out.println(StatusEnum.MENU_NAVIGATION_NOT_POSSIBLE.getStatus());
        }
    }
}