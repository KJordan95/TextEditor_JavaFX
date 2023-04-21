# TextEditor in JavaFX
This is a Windows based text editor made using Java and the JavaFX library. The editor can open and save text files and has a spell checker included.

## Installation
To use this program, a Java version of 17 or above and the latest version of JavaFX is required. 

### Installing Java 17
To check if Java is on your device, open a command prompt and run

```bash
java -version
```

If an error occurs or your version of the Java SDK is below 17, you can download Java 17 using [this link](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

Afterwards, you would need to set or update your system's environment PATH variable for the new Java SDK. A tutorial for that can be found [here](https://www.geeksforgeeks.org/how-to-set-java-path-in-windows-and-linux/).

### Installing JavaFX

The latest version of JavaFX is also required for this application to run. Downloads for the compressed files library can be found [here](https://gluonhq.com/products/javafx/). Once downloaded, extract the files to anywhere on the system you please (preferably next to the Java SDK you have).

Once that's completed, the last step before running the application is to set the file path for the JavaFX lib folder. In a text or code editor such as Notepad or VS Code, open the 'run.bat' file and replace the "YOUR_PATH_TO_JAVAFX_LIB" variable with the actual file path to the 'lib' folder in the JavaFX that was downloaded.

```bash
@REM replace YOUR_PATH_TO_JAVAFX_LIB with the actual file path. For example:
@REM set PATH_TO_FX="C:\Program Files\Java\javafx-sdk-20.0.1\lib"
set PATH_TO_FX="YOUR_PATH_TO_JAVAFX_LIB"
javac --module-path %PATH_TO_FX% --add-modules javafx.controls SpellCheckerWindow.java SpellChecker.java
java --module-path %PATH_TO_FX% --add-modules javafx.controls SpellCheckerWindow
```

## License

[MIT](https://choosealicense.com/licenses/mit/)
