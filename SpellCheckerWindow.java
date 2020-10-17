import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SpellCheckerWindow extends Application {
    SpellChecker spellChecker = new SpellChecker();
    private String fileLoadedInTextArea = "";

    @Override
    public void start(Stage stage) {
        MenuBar menuBar = new MenuBar();
        TextArea area = new TextArea();

        // Create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");

        // Create File MenuItems
        MenuItem openFileItem = createOpenFileItem(stage, area);
        MenuItem saveItem = createSaveItem(stage, area);
        MenuItem exitItem = createExitItem();

        // Create Edit MenuItems
        MenuItem spellcheckItem = createSpellCheckerEditItem(stage, area);

        // Add menuItems to the Menus
        fileMenu.getItems().addAll(openFileItem, saveItem, exitItem);
        editMenu.getItems().add(spellcheckItem);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu);

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(area);
        Scene scene = new Scene(root, 350, 200);

        stage.setTitle("Spell Checker");
        stage.setScene(scene);
        stage.show();
    }

    private MenuItem createOpenFileItem(Stage stage, TextArea area) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("./"));
        MenuItem openFileItem = new MenuItem("Open File");
        openFileItem.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));

        // event to open the file using a file chooser dialog
        openFileItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                File file = fc.showOpenDialog(stage);

                // exit the event if no file is choosen
                if (file == null) {
                    return;
                }
                fileLoadedInTextArea = file.toPath().toString();
                area.clear();
                addToTextArea(file, area);
            }
        });
        return openFileItem;
    }

    private MenuItem createSaveItem(Stage stage, TextArea area) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("./"));
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

        saveItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (fileLoadedInTextArea.isEmpty()) {
                        File file = fc.showSaveDialog(stage);
                        if (file != null) {
                            FileWriter fw = new FileWriter(file);
                            fw.write(area.getText());
                            fw.close();
                            fileLoadedInTextArea = file.toPath().toString();
                        }
                    }

                    else {
                        FileWriter fw = new FileWriter(fileLoadedInTextArea, false);
                        fw.write(area.getText());
                        fw.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return saveItem;

    }

    private MenuItem createExitItem() {
        MenuItem exitItem = new MenuItem("Exit");
        // Set Accelerator for Exit MenuItem.
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        // When user click on the Exit item
        exitItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        return exitItem;
    }

    private MenuItem createSpellCheckerEditItem(Stage stage, TextArea area) {
        MenuItem spellCheckEditItem = new MenuItem("Spell Check");
        spellCheckEditItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    String wordsInTextArea = area.getText();
                    String possibleWords = "";

                    String[] splitArray = wordsInTextArea.split("\\s+");
                    for (int i = 0; i < splitArray.length; ++i) {
                        // show dialog of possible words if the word is not in the dictionary
                        if (!spellChecker.isWordInDictionary(splitArray[i])) {
                            possibleWords = "";
                            // TODO: Implement Spell Check
                            //TODO: RETURN ONLY STRING TO "possibleWords", NOT LIST
                            possibleWords = spellChecker.checkForMissingLetter(splitArray[i]).toString();
                            Alert alert = new Alert(AlertType.CONFIRMATION);
                            alert.setTitle("Suggested Words");
                            alert.setHeaderText("Possible Words For \"" + splitArray[i] + "\": ");
                            alert.setContentText(possibleWords);
                            alert.showAndWait();
                        }
                    }

                    // if no suggestions were found, notify the user with a dialog
                    if (possibleWords.isEmpty()) {
                        Alert alert = new Alert(AlertType.CONFIRMATION);
                        alert.setTitle("Suggested Words");
                        alert.setHeaderText("No Suggestions Found");
                        alert.setContentText(possibleWords);
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return spellCheckEditItem;
    }

    public void addToTextArea(File file, TextArea area) {
        Scanner scanner;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNext()) {
                area.appendText(scanner.next() + " ");
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO:
        // SpellChecker spellChecker = new SpellChecker();
        // spellChecker.checkForMissingLetter("zZ");
        // System.exit(0);
        Application.launch(args);
    }
}