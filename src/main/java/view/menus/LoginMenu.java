package view.menus;

import controller.menucontroller.LoginMenuController;
import view.MenuEnum;
import view.ProgramController;
import view.Regex;
import view.StatusEnum;

import java.util.regex.Matcher;

public class LoginMenu {

    LoginMenuController loginMenuController;

    public void run(String command){

        loginMenuController = new LoginMenuController();

        Matcher matcher;
        if ((matcher = Regex.getMatcher(command,Regex.LOGIN_USER_1)).matches()){
            String username = matcher.group(2);
            String password = matcher.group(4);
            loginUSer(username,password);
        }
        else if ((matcher = Regex.getMatcher(command,Regex.LOGIN_USER_2)).matches()){
            String username = matcher.group(4);
            String password = matcher.group(2);
            loginUSer(username,password);
        }
        else if ((matcher = Regex.getMatcher(command,Regex.CREATE_USER_1)).matches()){
            String username = matcher.group(2);
            String nickname = matcher.group(4);
            String password = matcher.group(6);
            createUser(username,nickname,password);
        }
        else if ((matcher = Regex.getMatcher(command,Regex.CREATE_USER_2)).matches()){
            String username = matcher.group(2);
            String nickname = matcher.group(6);
            String password = matcher.group(4);
            createUser(username,nickname,password);
        }
        else if ((matcher = Regex.getMatcher(command,Regex.CREATE_USER_3)).matches()){
            String username = matcher.group(4);
            String nickname = matcher.group(2);
            String password = matcher.group(6);
            createUser(username,nickname,password);
        }
        else if ((matcher = Regex.getMatcher(command,Regex.CREATE_USER_4)).matches()){
            String username = matcher.group(6);
            String nickname = matcher.group(2);
            String password = matcher.group(4);
            createUser(username,nickname,password);
        }
        else if ((matcher = Regex.getMatcher(command,Regex.CREATE_USER_5)).matches()){
            String username = matcher.group(6);
            String nickname = matcher.group(4);
            String password = matcher.group(2);
            createUser(username,nickname,password);
        }
        else if ((matcher = Regex.getMatcher(command,Regex.CREATE_USER_6)).matches()){
            String username = matcher.group(4);
            String nickname = matcher.group(6);
            String password = matcher.group(2);
            createUser(username,nickname,password);
        }
        else if ((Regex.getMatcher(command,Regex.ENTER_MENU)).matches()){
            System.out.println(StatusEnum.PLEASE_LOGIN_FIRST.getStatus());
        }
        else if ((Regex.getMatcher(command,Regex.SHOW_CURRENT_MENU)).matches()){
            System.out.println("Login Menu");
        }
        else if ((Regex.getMatcher(command,Regex.EXIT_MENU)).matches()){
            ProgramController.currentMenu = MenuEnum.EXIT;
        }
        else{
            System.out.println(StatusEnum.INVALID_COMMAND.getStatus());
        }
    }

    private void loginUSer(String username, String password) {
        System.out.println(loginMenuController.loginUSer(username, password));
    }

    private void createUser(String username, String nickname, String password) {
        System.out.println(loginMenuController.createUser(username, nickname, password));
    }
}
