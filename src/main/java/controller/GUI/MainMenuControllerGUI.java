package controller.GUI;

import controller.menucontroller.*;
import javafx.event.ActionEvent;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import models.SaveData;


import java.io.File;
import java.io.IOException;

public class MainMenuControllerGUI {


    public static ProfileMenuController profileMenuController = new ProfileMenuController(LoginMenuController.currentUser);
    public static DuelMenuController duelMenuController = new DuelMenuController();
    public ImageView mainsound;

    public void initialize() {
        if (LoginControllerGUI.player.isMute()) {
            mainsound.setImage(new Image("./image/mute.jpg"));
        } else {
            mainsound.setImage(new Image("./image/unmute.jpg"));
        }
    }


    public void enterProfileMenu(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/profile_menu.fxml", actionEvent);
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        LoginMenuController.currentUser = null;
        LoginMenuController.isLoggedOn = false;
        LoginControllerGUI.player.stop();
        String musicFile = "./src/main/resources/sound/main.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        LoginControllerGUI.player = new MediaPlayer(sound);
        LoginControllerGUI.player.setCycleCount(MediaPlayer.INDEFINITE);
        LoginControllerGUI.player.play();
        SceneController.switchScene("/fxml/start.fxml", actionEvent);
    }

    public void enterShopMenu(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/shop_menu.fxml", actionEvent);
    }

    public void enterImportMenu(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/ImportExport_Scene.fxml", actionEvent);
    }

    public void enterDeckMenu(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/deck_menu.fxml", actionEvent);
    }

    public void exit() {
        LoginMenuController.currentUser = null;
        LoginMenuController.isLoggedOn = false;
        SaveData.save();
        System.exit(0);
    }


    public void enterDuelMenu(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/duel_start_view.fxml", actionEvent);
    }

    public void muteAndUnmute() {
        if (LoginControllerGUI.player.isMute()) {
            LoginControllerGUI.player.setMute(false);
            mainsound.setImage(new Image("./image/unmute.jpg"));
        } else {
            LoginControllerGUI.player.setMute(true);
            mainsound.setImage(new Image("./image/mute.jpg"));
        }
    }

    public void highlightBt(MouseEvent event) {
        String musicFile = "./src/main/resources/sound/bt.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        ImageView imageView = ((ImageView) event.getSource());
        double w = imageView.getFitWidth();
        double h = imageView.getFitHeight();
        imageView.setFitHeight(h + 5);
        imageView.setFitWidth(w + 5);


    }

    public void returnNormalBt(MouseEvent event) {
        ImageView imageView = ((ImageView) event.getSource());
        double w = imageView.getFitWidth();
        double h = imageView.getFitHeight();
        imageView.setFitHeight(h - 5);
        imageView.setFitWidth(w - 5);
    }

    public void highlightBt2(MouseEvent event) {
        String musicFile = "./src/main/resources/sound/bt.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        Button bt = ((Button) event.getSource());
        double w = bt.getWidth();
        double h = bt.getHeight();
        bt.setPrefSize(w + 5, h + 5);
    }

    public void returnNormalBt2(MouseEvent event) {
        Button bt = ((Button) event.getSource());
        double w = bt.getWidth();
        double h = bt.getHeight();
        bt.setPrefSize(w - 5, h - 5);
    }

    public void scoreboradScene(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/scoreboard.fxml", actionEvent);
    }

    public void enterMakeCardMenu(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/Make_CardScene.fxml", actionEvent);
    }
}
