
/**
 * This program will use JavaFX to create a spell checker
 * for any file the user chooses or save to the file system
 * 
 * @author Kameron Jordan
 * COP 4027
 * Project 2
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Optional;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    private SpellChecker spellChecker = new SpellChecker();
    private String fileLoadedInTextArea = "";
    private static final String regexForSeperatingText = "[,\\s\\.\\?]+";

    @Override
    public void start(Stage stage) {
        MenuBar menuBar = new MenuBar();
        TextArea area = new TextArea();
        area.setWrapText(true);

        // Create menus
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");

        // MenuItems for File Menu
        MenuItem openFileItem = createOpenFileItem(stage, area);
        MenuItem saveItem = createSaveItem(stage, area);
        MenuItem exitItem = createExitItem();

        // MenuItems for Edit Menu
        MenuItem spellcheckItem = createSpellCheckerEditItem(area);

        // Add menuItems to the Menus
        fileMenu.getItems().addAll(openFileItem, saveItem, exitItem);
        editMenu.getItems().add(spellcheckItem);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, editMenu);

        // Place Menus and TextArea into a layout
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(area);
        Scene scene = new Scene(root, 350, 200);

        // Set and Show the Stage
        stage.setTitle("Spell Checker");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates a MenuItem for opening a file to be read into the TextArea
     * 
     * @param stage - Stage holding the text area
     * @param area  - the TextArea for displaying the text
     * @return - new MenuItem
     */
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

                // get the name of the file for saving changes
                fileLoadedInTextArea = file.toPath().toString();

                // display the text in the file
                area.clear();
                addToTextArea(file, area);
            }
        });
        return openFileItem;
    }

    /**
     * Creates a MenuItem for saving the text in the TextArea into a file
     * 
     * @param stage - Stage holding the text area
     * @param area  - the TextArea for displaying the text
     * @return - new MenuItem
     */
    private MenuItem createSaveItem(Stage stage, TextArea area) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("./"));
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

        // event to save the file using a file chooser dialog
        saveItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    /**
                     * if no file was opened to get the text, create a new file using the save file
                     * dialog
                     */
                    if (fileLoadedInTextArea.isEmpty()) {
                        File file = fc.showSaveDialog(stage);
                        if (file != null) {
                            FileWriter fw = new FileWriter(file);
                            fw.write(area.getText());
                            fw.close();
                            fileLoadedInTextArea = file.toPath().toString();
                        }
                    }

                    // save the text into the already opened file directly
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

    /**
     * Creates a MenuItem for exiting the program
     * 
     * @return - new MenuItem
     */
    private MenuItem createExitItem() {
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));

        // event to close the program
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        return exitItem;
    }

    /**
     * Creates a MenuItem for spellchecking the text in the TextArea
     * 
     * @param stage - Stage holding the text area
     * @param area  - the TextArea for displaying the text
     * @return - new MenuItem
     */
    private MenuItem createSpellCheckerEditItem(TextArea area) {
        MenuItem spellCheckEditItem = new MenuItem("Spell Check");
        spellCheckEditItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Space"));

        // event for performing the spell check
        spellCheckEditItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    int foundSuggestions = 0;
                    String possibleWords = "";

                    // get the text and split in to words using regex
                    String wordsInTextArea = area.getText();
                    String[] splitArray = wordsInTextArea.split(regexForSeperatingText);

                    // check each word in dictionary
                    for (int i = 0; i < splitArray.length; ++i) {
                        // show dialog of possible words if the word is not in the dictionary
                        if (!spellChecker.isWordInDictionary(splitArray[i])) {
                            possibleWords = "";
                            possibleWords = spellChecker.allPossibleWordsMutations(splitArray[i].toString());

                            if (possibleWords.length() > 0) {
                                foundSuggestions++;
                            }

                            // if no suggestions were found, notify the user with a dialog
                            else {
                                Alert alert = new Alert(AlertType.CONFIRMATION);
                                alert.setTitle("Suggested Words");
                                alert.setHeaderText("No Suggestions Found For \"" + splitArray[i] + "\"");
                                alert.setContentText(possibleWords);
                                Optional<ButtonType> result = alert.showAndWait();
                                // if user presses cancel button in alert dialog, stop the spell check
                                if (result.get() == ButtonType.CANCEL) {
                                    break;
                                }
                                foundSuggestions++;
                                continue;
                            }

                            // show dialog with list of suggested words
                            if (foundSuggestions > 0 && possibleWords.length() > 0) {
                                Alert alert = new Alert(AlertType.CONFIRMATION);
                                alert.setTitle("Suggested Words");
                                alert.setHeaderText("Possible Words For \"" + splitArray[i] + "\": ");
                                alert.setContentText(possibleWords);
                                Optional<ButtonType> result = alert.showAndWait();
                                // if user presses cancel button in alert dialog, stop the spell check
                                if (result.get() == ButtonType.CANCEL) {
                                    break;
                                }
                            }
                        }
                    }

                    // if no errors were found, notify the user with a dialog
                    if (foundSuggestions == 0) {
                        Alert alert = new Alert(AlertType.CONFIRMATION);
                        alert.setTitle("Suggested Words");
                        alert.setHeaderText("No Error Found");
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

    /**
     * Scans through a file and displays its text into the TextArea
     * 
     * @param file - File to get text from
     * @param area - TextArea to be dsiplayed
     */
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
        Application.launch(args);
    }
}