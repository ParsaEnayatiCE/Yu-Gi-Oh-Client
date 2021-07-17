package controller.menucontroller;


/*------------------CURRENT USER NOT FIXED-------------*/


import models.User;
import view.StatusEnum;


public class ProfileMenuController {

    private final User currentUser;

    public ProfileMenuController(User user) {
        this.currentUser = user;
    }

    public String changeNickname(String newNickname) {
        if (User.isNickNameTaken(newNickname))
            return "user with nickname " + newNickname + " already exists";

        currentUser.setNickName(newNickname);
        return StatusEnum.CHANGE_NICKNAME_SUCCESSFULLY.getStatus();
    }

    public String changePass(String oldPass, String newPass) {
        if (!currentUser.getPassword().equals(oldPass))
            return StatusEnum.CURRENT_PASSWORD_INVALIDITY.getStatus();

        if (currentUser.getPassword().equals(newPass))
            return StatusEnum.ENTER_A_NEW_PASSWORD.getStatus();

        currentUser.changePassword(newPass);
        return StatusEnum.CHANGE_PASSWORD_SUCCESSFULLY.getStatus();

    }

    public String changeUsername(String newUsername) {
        if (User.isUserNameTaken(newUsername))
            return "user with username " + newUsername + " already exists";

        currentUser.setUserName(newUsername);
        return StatusEnum.CHANGE_USERNAME_SUCCESSFULLY.getStatus();
    }
}

