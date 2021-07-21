package controller.menucontroller;


/*------------------CURRENT USER NOT FIXED-------------*/


import controller.ConnectionController;
import models.User;
import view.StatusEnum;

import java.io.IOException;


public class ProfileMenuController {


    public String changeNickname(String newNickname) {
        String forServer = "changeNickname-"+newNickname+"-"+LoginMenuController.getToken();
        try {
            ConnectionController.getDataOutputStream().writeUTF(forServer);
            ConnectionController.getDataOutputStream().flush();
            return ConnectionController.getDataInputStream().readUTF();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "Error";
    }

    public String changePass(String oldPass, String newPass) {
        String forServer = "changePassword-"+oldPass+"-"+newPass+"-"+LoginMenuController.getToken();
        try {
            ConnectionController.getDataOutputStream().writeUTF(forServer);
            ConnectionController.getDataOutputStream().flush();
            return ConnectionController.getDataInputStream().readUTF();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "Error";

    }

    public String changeUsername(String newUsername) {
        String forServer = "changeUsername-"+newUsername+"-"+LoginMenuController.getToken();
        try {
            ConnectionController.getDataOutputStream().writeUTF(forServer);
            ConnectionController.getDataOutputStream().flush();
            return ConnectionController.getDataInputStream().readUTF();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "Error";
    }
}

