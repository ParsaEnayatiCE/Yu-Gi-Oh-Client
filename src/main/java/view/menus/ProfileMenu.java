package view.menus;

import controller.menucontroller.LoginMenuController;
import controller.menucontroller.ProfileMenuController;
import models.User;
import view.MenuEnum;
import view.ProgramController;
import view.Regex;
import view.StatusEnum;

import java.util.regex.Matcher;

public class ProfileMenu {

    User currentUser = LoginMenuController.currentUser;
    ProfileMenuController profileMenuController;

    public void run(String command){

        profileMenuController = new ProfileMenuController(currentUser);

        Matcher matcher;
        if ((matcher = Regex.getMatcher(command, Regex.CHANGE_PASS_1)).matches()){
            String oldPass = matcher.group(3);
            String newPass = matcher.group(5);
            changePass(oldPass,newPass);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.CHANGE_PASS_2)).matches()){
            String oldPass = matcher.group(5);
            String newPass = matcher.group(3);
            changePass(oldPass,newPass);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.CHANGE_PASS_3)).matches()){
            String oldPass = matcher.group(2);
            String newPass = matcher.group(5);
            changePass(oldPass,newPass);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.CHANGE_PASS_4)).matches()){
            String oldPass = matcher.group(2);
            String newPass = matcher.group(4);
            changePass(oldPass,newPass);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.CHANGE_PASS_5)).matches()){
            String oldPass = matcher.group(5);
            String newPass = matcher.group(2);
            changePass(oldPass,newPass);
        }
        else if ((matcher = Regex.getMatcher(command, Regex.CHANGE_PASS_6)).matches()){
            String oldPass = matcher.group(4);
            String newPass = matcher.group(2);
            changePass(oldPass,newPass);
        }
        else if ((matcher = Regex.getMatcher(command,Regex.CHANGE_NICKNAME)).matches()){
            String newNickname = matcher.group(2);
            changeNickname(newNickname);
        }
        else if ((matcher = Regex.getMatcher(command,Regex.CHANGE_USERNAME)).matches()){
            String newUsername = matcher.group(2);
            changeUsername(newUsername);
        }
        else if ((Regex.getMatcher(command, Regex.EXIT_MENU)).matches()){
            ProgramController.currentMenu = MenuEnum.MAIN_MENU;
        }
        else if ((Regex.getMatcher(command, Regex.ENTER_MENU)).matches()){
            System.out.println(StatusEnum.MENU_NAVIGATION_NOT_POSSIBLE.getStatus());
        }
        else if ((Regex.getMatcher(command, Regex.SHOW_CURRENT_MENU)).matches()){
            System.out.println("Profile");
        }
        else{
            System.out.println(StatusEnum.INVALID_COMMAND.getStatus());
        }
    }

    private void changePass(String oldPass, String newPass) {
        System.out.println(profileMenuController.changePass(oldPass, newPass));
    }

    private void changeNickname(String newNickname) {
        System.out.println(profileMenuController.changeNickname(newNickname));
    }

    private void changeUsername(String newUsername) {
        System.out.println(profileMenuController.changeUsername(newUsername));
    }
}
