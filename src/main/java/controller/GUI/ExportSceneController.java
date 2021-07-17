package controller.GUI;

import controller.menucontroller.ImportExportController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import models.cards.CardImage;

import java.io.IOException;

public class ExportSceneController {

    ImportExportController importExportController = new ImportExportController();

    public ToggleButton asJsonToggleButton;
    public Label statusLabel;
    public TextField cardName;
    public ImageView cardImageView;

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/ImportExport_Scene.fxml", actionEvent);
    }

    public void export() {
        importExportController.exportCard(cardName, asJsonToggleButton, statusLabel);
        if (statusLabel.getText().equals("card exported successfully"))
            cardImageView.setImage(CardImage.getImageByName(cardName.getText()));
    }
}
