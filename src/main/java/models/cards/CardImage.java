package models.cards;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CardImage {

    private final String name;
    private final Image image;
    private static final ArrayList<CardImage> allCardImages = new ArrayList<>();

    public CardImage(String name, Image image) {
        this.name = name;
        this.image = image;
        allCardImages.add(this);
    }

    static {
        try {
            BufferedReader monsterBufferReader = new BufferedReader(new FileReader("Monster.csv"));
            addImages(monsterBufferReader);
            BufferedReader spellTrapBufferReader = new BufferedReader(new FileReader("SpellTrap.csv"));
            addImages(spellTrapBufferReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void addImages(BufferedReader bufferedReader) throws IOException {
        String line;
        boolean isFirstLine = true;
        while ((line = bufferedReader.readLine()) != null) {
            String[] values = line.split(",");
            if (isFirstLine) isFirstLine = false;
            else {
                String name = values[0].replaceAll(" ", "");
                new CardImage(values[0], new Image("./image/Cards/" + name + ".jpg"));
            }
        }
    }

    public static Image getImageByName(String name) {
        for (CardImage cardImage : allCardImages) {
            if (cardImage.name.equals(name))
                return cardImage.image;
        }
        return null;
    }

}
