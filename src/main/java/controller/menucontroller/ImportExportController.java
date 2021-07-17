package controller.menucontroller;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportExportController {

    public void isFileValid(File file, Label dragHereLabel, Label statusLabel) {
        if (!file.exists())
            return;
        if (file.getName().matches(".+\\.csv") || file.getName().matches(".+\\.json")) {
            dragHereLabel.setText("the file is caught");
            statusLabel.setText("press import card button to finalize import");
            return;
        }
        statusLabel.setText("Please enter a csv or a json file");
    }

    public String importCard(File file, Label statusLabel, Label dragHereLabel) {
        if (file == null) {
            statusLabel.setText("Please enter the file first");
            return null;
        }
        try {
            String fileData = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            if (file.getName().matches(".+\\.csv")) {
                String[] allData = fileData.split(",");
                if (allData.length == 9) return importMonsterCsv(fileData, allData, statusLabel);
                else if (allData.length == 6) return importSpellTrapCsv(fileData, allData, statusLabel);
                else statusLabel.setText("csv is not valid");
            } else if (file.getName().matches(".+\\.json")) {
                Matcher matcher;
                Pattern monsterPattern = Pattern.compile("\\{\"Name\": \"([^\"]+)\", \"Level\": (\\d+), \"Attribute\": " +
                        "\"([^\"])\", \"Monster Type\": \"([^\"])\", \"Card Type\": \"([^,\"])\", \"Atk\": (\\d+), " +
                        "\"Def\": (\\d+), \"Description\": \"([^\"])\", \"Price\": (\\d+)}");
                if ((matcher = monsterPattern.matcher(fileData)).find())
                    return importMonsterJason(matcher, statusLabel);
                Pattern spellTrapPattern = Pattern.compile("\\{\"Name\": \"([^\"])\", \"Type\": \"([^\"])\", \"Icon\":" +
                        " \"([^\"])\", \"Description\": \"([^\"])\", \"Status\": \"([^\"])\", \"Price\": (\\d+)}");
                if ((matcher = spellTrapPattern.matcher(fileData)).find())
                    return importSpellJason(matcher, statusLabel);
                else statusLabel.setText("jason is not valid");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String importSpellJason(Matcher matcher, Label statusLabel) {
        String csvData = "\n" + matcher.group(1) + "," + matcher.group(2) + "," + matcher.group(3) + "," +
                matcher.group(4) + "," + matcher.group(5) + "," + matcher.group(6);
        try {
            Files.write(Paths.get("SpellTrap.csv"), ("\n" + csvData).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        statusLabel.setText("card imported successfully");
        return matcher.group(1);
    }

    private String importMonsterJason(Matcher matcher, Label statusLabel) {
        String csvData = "\n" + matcher.group(1) + "," + matcher.group(2) + "," + matcher.group(3) + "," +
                matcher.group(4) + "," + matcher.group(5) + "," + matcher.group(6) + "," +
                matcher.group(7) + "," + matcher.group(8) + "," + matcher.group(9);
        try {
            Files.write(Paths.get("Monster.csv"), csvData.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        statusLabel.setText("card imported successfully");
        return matcher.group(1);
    }

    private String importSpellTrapCsv(String csvData, String[] allData, Label statusLabel) {
        if (!allData[5].matches("\\d+")) {
            statusLabel.setText("csv is invalid");
            return null;
        }
        try {
            Files.write(Paths.get("SpellTrap.csv"), ("\n" + csvData).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        statusLabel.setText("card imported successfully");
        return allData[0];
    }

    private String importMonsterCsv(String csvData, String[] allData, Label statusLabel) {
        if (!(allData[1].matches("\\d+") && allData[5].matches("\\d+") && allData[6].matches("\\d+")
                && allData[8].matches("\\d+"))) {
            statusLabel.setText("csv is invalid");
            return null;
        }
        try {
            Files.write(Paths.get("Monster.csv"), ("\n" + csvData).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        statusLabel.setText("card imported successfully");
        return allData[0];
    }

    public void exportCard(TextField cardName, ToggleButton asJsonToggleButton, Label statusLabel) {
        try {
            exportMonster(cardName, asJsonToggleButton, statusLabel);
            if (!statusLabel.getText().equals("card exported successfully"))
                exportSpellTrap(cardName, asJsonToggleButton, statusLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportSpellTrap(TextField cardName, ToggleButton asJsonToggleButton, Label statusLabel) throws IOException {
        String line;
        boolean isFirstLine = true;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("SpellTrap.csv"));
        while ((line = bufferedReader.readLine()) != null) {
            String[] values = line.split(",");
            if (isFirstLine) isFirstLine = false;
            else if (cardName.getText().equals(values[0])) {
                statusLabel.setText("card exported successfully");
                if (asJsonToggleButton.isSelected()) {
                    File file = new File(values[0] + ".json");
                    if (!file.createNewFile()) {
                        statusLabel.setText("you already exported this spell or trap as json");
                        return;
                    }
                    FileWriter myWriter = new FileWriter(file.getName());
                    myWriter.write("\\{\"Name\": \"" + values[0] + "\", \"Type\": \"" + values[1] + "\", \"Icon\":" +
                            " \"" + values[2] + "\", \"Description\": \"" + values[3] + "\", \"Status\": \"" + values[4]
                            + "\", \"Price\": " + values[5] + "}");
                    myWriter.close();
                }
                else {
                    File file = new File(values[0] + ".csv");
                    if (!file.createNewFile()) {
                        statusLabel.setText("you already exported this spell or trap as csv");
                        return;
                    }
                    FileWriter myWriter = new FileWriter(file.getName());
                    myWriter.write(values[0] + "," + values[1] + "," + values[2] + "," + values[3] + "," + values[4] + values[5]);
                    myWriter.close();
                }
                return;
            }
        }
    }

    private void exportMonster(TextField cardName, ToggleButton asJsonToggleButton, Label statusLabel) throws IOException {
        String line;
        boolean isFirstLine = true;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("Monster.csv"));
        while ((line = bufferedReader.readLine()) != null) {
            String[] values = line.split(",");
            if (isFirstLine) isFirstLine = false;
            else if (cardName.getText().equals(values[0])) {
                statusLabel.setText("card exported successfully");
                if (asJsonToggleButton.isSelected()) {
                    File file = new File(values[0] + ".json");
                    if (!file.createNewFile()) {
                        statusLabel.setText("you already exported this monster as json");
                        return;
                    }
                    FileWriter myWriter = new FileWriter(file.getName());
                    myWriter.write("\\{\"Name\": \"" + values[0] + "\", \"Level\": " + values[1] + ", \"Attribute\": " +
                            "\"" + values[2] + "\", \"Monster Type\": \"" + values[3] + "\", \"Card Type\": \""
                            + values[4] + "\", \"Atk\": " + values[5] + ", " +
                            "\"Def\": " + values[6] + ", \"Description\": \"" + values[7] + "\", \"Price\": " + values[8] + "}");
                    myWriter.close();
                }
                else {
                    File file = new File(values[0] + ".csv");
                    if (!file.createNewFile()) {
                        statusLabel.setText("you already exported this monster as csv");
                        return;
                    }
                    FileWriter myWriter = new FileWriter(file.getName());
                    myWriter.write(values[0] + "," + values[1] + "," + values[2] + "," + values[3] + "," + values[4]
                            + values[5] + "," + values[6] + "," + values[7] + "," + values[8]);
                    myWriter.close();
                }
                return;
            }
        }
    }
}
