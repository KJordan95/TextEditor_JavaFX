import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SpellChecker {
    private HashMap<Integer, ArrayList<String>> map;
    private File dictionaryFile;
    private Scanner scanner;
    private final int ALPHABET_NUMS = 26;

    public SpellChecker() {
        map = new HashMap<Integer, ArrayList<String>>();
        dictionaryFile = new File("Words.txt");

        // add words in dictionary to hash map
        try {
            scanner = new Scanner(dictionaryFile);
            while (scanner.hasNext()) {
                String wordToAddToDictionary = scanner.next();
                // map.put(wordToAddToDictionary.hashCode(), wordToAddToDictionary);
                addToMap(wordToAddToDictionary);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Custom "put" method for the Map that will be the dictionary as the values for
     * the map will be stored in an ArrayList of Strings so that any duplicates keys
     * wont overwrite different like they would in a tradional Map.
     * 
     * Also, the keys for the Map are the hash code of the words in the dictionary
     * but in all lowercase (the words themsleves will be stored as they are
     * including capitalization). This is to make it easier searching for words
     * without having to worry about the case of the word(i.e. "Dog" and "doG" will
     * result in the same hash code as "dog" when being searched)
     * 
     * @param str
     */
    private void addToMap(String str) {
        if (map.containsKey(str.toLowerCase().hashCode())) {
            map.get(str.toLowerCase().hashCode()).add(str);
        }

        else {
            ArrayList<String> list = new ArrayList<String>();
            list.add(str);
            map.put(str.toLowerCase().hashCode(), list);
        }
    }

    public ArrayList<String> checkForMissingLetter(String wordToCheck) {
        ArrayList<String> suggestedWords = new ArrayList<String>();
        StringBuffer possibleWord;// = new StringBuffer(wordToCheck);
        char letter = 'a';

        for (int i = 0; i < ALPHABET_NUMS; ++i) {
            for (int j = 0; j <= wordToCheck.length(); ++j) {
                /**
                 * possible word is in lowercase as the words in the dictionary are hash coded
                 * using the all lowercase value of the word
                 */
                possibleWord = new StringBuffer(wordToCheck.toLowerCase());
                possibleWord.insert(j, (char) (letter + i));

                // check if the possible word's hash code is used in the dictionary
                if (map.containsKey(possibleWord.toString().hashCode())) {
                    // search the ArrayList in the Map for the word
                    ArrayList<String> wordsWithSameHashCode = map.get(possibleWord.toString().hashCode());
                    for (int k = 0; k < wordsWithSameHashCode.size(); ++k) {
                        /**
                         * only add the word to suggestion list if it matches a word in the dictionary
                         * (regardless of case) and is not already apart of the list
                         */
                        if (wordsWithSameHashCode.get(k).equalsIgnoreCase(possibleWord.toString())
                                && !(suggestedWords.contains(wordsWithSameHashCode.get(k)))) {
                            suggestedWords.add(wordsWithSameHashCode.get(k));
                            // System.out.print(listToUpper.get(k) + " ");
                            // System.out.println(suggestedWords.size());
                        }
                    }
                }
            }
        }

        return suggestedWords;
    }

    public boolean isWordInDictionary(String word) {
        if (map.containsKey(word.toLowerCase().hashCode())) {
            ArrayList<String> list = map.get(word.hashCode());
            for (int k = 0; k < list.size(); ++k) {
                if (list.get(k).equalsIgnoreCase(word)) {
                    return true;
                }
            }
        }

        return false;
    }

    public String listToString(ArrayList<String> list) {
        String str = "";

        for (int i = 0; i < list.size(); ++i) {
            str += list.get(i) + "\n";
        }

        return str;
    }

}
