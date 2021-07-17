package controller.menucontroller;

import view.MenuEnum;
import view.ProgramController;
import view.StatusEnum;
import models.User;

import java.util.Objects;

//-----------------------------------PLEASE LOGIN FIRST NOT FIXED-------------------

public class LoginMenuController {
    public static User currentUser;
    public static boolean isLoggedOn = false;

    private boolean doesUserExist(String username) {
        for (User user : User.allUsers) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPasswordCorrect(String username, String password) {
        return Objects.requireNonNull(User.getUserByUserName(username)).getPassword().equals(password);
    }

    public String loginUSer(String username, String password) {
        if (!doesUserExist(username))
            return "There is no user with username " + username;

        if (!isPasswordCorrect(username, password))
            return StatusEnum.USERNAME_AND_PASSWORD_MISMATCH.getStatus();


        currentUser = User.getUserByUserName(username);
        isLoggedOn = true;
        ProgramController.currentMenu = MenuEnum.MAIN_MENU;
        return StatusEnum.USER_LOGIN_SUCCESSFULLY.getStatus();
    }

    public String createUser(String username, String nickname, String password) {
        if (User.isUserNameTaken(username))
            return "user with username " + username + " already exists";

        if (User.isNickNameTaken(nickname))
            return "user with nickname " + nickname + " already exists";

        currentUser = new User(username, nickname, password);
        return StatusEnum.USER_CREATE_SUCCESSFULLY.getStatus();
    }

}
