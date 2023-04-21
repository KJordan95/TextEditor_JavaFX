@REM replace YOUR_PATH_TO_JAVAFX_LIB with the actual file path. For example:
@REM set PATH_TO_FX="C:\Program Files\Java\javafx-sdk-20.0.1\lib"
set PATH_TO_FX="YOUR_PATH_TO_JAVAFX_LIB"
javac --module-path %PATH_TO_FX% --add-modules javafx.controls SpellCheckerWindow.java SpellChecker.java
java --module-path %PATH_TO_FX% --add-modules javafx.controls SpellCheckerWindow