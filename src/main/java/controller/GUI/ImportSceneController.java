package controller.GUI;

import controller.menucontroller.ImportExportController;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import models.cards.CardImage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImportSceneController {
    public Label dragHereLabel;
    private final ImportExportController importExportController = new ImportExportController();
    public ImageView cardImage;
    private File file = null;
    public Label statusLabel;

    public void handleDragDropped(DragEvent dragEvent) {
        List<File> files = dragEvent.getDragboard().getFiles();
        file = files.get(files.size() - 1);
        importExportController.isFileValid(file, dragHereLabel, statusLabel);
    }

    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles())
            dragEvent.acceptTransferModes(TransferMode.ANY);
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        SceneController.switchScene("/fxml/ImportExport_Scene.fxml", actionEvent);
    }

    public void importCard() {
        String cardName = importExportController.importCard(file, statusLabel, dragHereLabel);
        if (statusLabel.getText().equals("card imported successfully"))
            cardImage.setImage(CardImage.getImageByName(cardName));
    }
}
