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

        // dont check for words that are only one letter (i.e. 'a')
        if ((wordToCheck.length() == 1)) {
            return suggestedWords;
        }

        for (int i = 0; i < ALPHABET_NUMS; ++i) {
            for (int j = 0; j <= wordToCheck.length(); ++j) {
                /**
                 * possible word is in lowercase as the words in the dictionary are hash coded
                 * using the all lowercase value of the word
                 */
                possibleWord = new StringBuffer(wordToCheck.toLowerCase());
                possibleWord.insert(j, (char) (letter + i));

                suggestedWords.addAll(getSuggestedListOfWords(possibleWord.toString()));

                // // check if the possible word's hash code is used in the dictionary
                // if (map.containsKey(possibleWord.toString().hashCode())) {
                // // search the ArrayList in the Map for the word
                // ArrayList<String> wordsWithSameHashCode =
                // map.get(possibleWord.toString().hashCode());
                // for (int k = 0; k < wordsWithSameHashCode.size(); ++k) {
                // /**
                // * only add the word to suggestion list if it matches a word in the dictionary
                // * (regardless of case) and is not already apart of the list
                // */
                // if (wordsWithSameHashCode.get(k).equalsIgnoreCase(possibleWord.toString())
                // && !(suggestedWords.contains(wordsWithSameHashCode.get(k)))) {
                // suggestedWords.add(wordsWithSameHashCode.get(k));
                // // System.out.print(listToUpper.get(k) + " ");
                // // System.out.println(suggestedWords.size());
                // }
                // }
                // }
            }
        }

        return suggestedWords;
    }

    public ArrayList<String> checkForAddedLetter(String wordToCheck) {
        ArrayList<String> suggestedWords = new ArrayList<String>();
        StringBuffer possibleWord;// = new StringBuffer(wordToCheck);

        // dont check for words that are only one letter
        if ((wordToCheck.length() == 1)) {
            return suggestedWords;
        }

        for (int j = 0; j < wordToCheck.length(); ++j) {
            /**
             * possible word is in lowercase as the words in the dictionary are hash coded
             * using the all lowercase value of the word
             */
            possibleWord = new StringBuffer(wordToCheck.toLowerCase());
            possibleWord.delete(j, (j + 1));

            suggestedWords.addAll(getSuggestedListOfWords(possibleWord.toString()));

            // // check if the possible word's hash code is used in the dictionary
            // if (map.containsKey(possibleWord.toString().hashCode())) {
            // // search the ArrayList in the Map for the word
            // ArrayList<String> wordsWithSameHashCode =
            // map.get(possibleWord.toString().hashCode());
            // for (int k = 0; k < wordsWithSameHashCode.size(); ++k) {
            // /**
            // * only add the word to suggestion list if it matches a word in the dictionary
            // * (regardless of case) and is not already apart of the list
            // */
            // if (wordsWithSameHashCode.get(k).equalsIgnoreCase(possibleWord.toString())
            // && !(suggestedWords.contains(wordsWithSameHashCode.get(k)))) {
            // suggestedWords.add(wordsWithSameHashCode.get(k));
            // }
            // }
            // }
        }
        return suggestedWords;
    }

    public ArrayList<String> checkForRevesedLetters(String wordToCheck) {
        ArrayList<String> suggestedWords = new ArrayList<String>();
        String possibleWord;// = new StringBuffer(wordToCheck);

        // dont check for words that are only one letter
        if ((wordToCheck.length() == 1)) {
            return suggestedWords;
        }

        for (int j = 0; j < wordToCheck.length() - 1; ++j) {
            /**
             * possible word is in lowercase as the words in the dictionary are hash coded
             * using the all lowercase value of the word
             */
            possibleWord = wordToCheck.toLowerCase();
            char charArray[] = possibleWord.toCharArray();
            int nextPos = j + 1;
            char temp = charArray[j];
            charArray[j] = charArray[nextPos];
            charArray[nextPos] = temp;
            possibleWord = new String(charArray);

            suggestedWords.addAll(getSuggestedListOfWords(possibleWord));

            // // check if the possible word's hash code is used in the dictionary
            // if (map.containsKey(possibleWord.toString().hashCode())) {
            // // search the ArrayList in the Map for the word
            // ArrayList<String> wordsWithSameHashCode =
            // map.get(possibleWord.toString().hashCode());
            // for (int k = 0; k < wordsWithSameHashCode.size(); ++k) {
            // /**
            // * only add the word to suggestion list if it matches a word in the dictionary
            // * (regardless of case) and is not already apart of the list
            // */
            // if (wordsWithSameHashCode.get(k).equalsIgnoreCase(possibleWord.toString())
            // && !(suggestedWords.contains(wordsWithSameHashCode.get(k)))) {
            // suggestedWords.add(wordsWithSameHashCode.get(k));
            // }
            // }
            // }

        }
        return suggestedWords;
    }

    private ArrayList<String> getSuggestedListOfWords(String possibleWord) {
        ArrayList<String> suggestedWords = new ArrayList<String>();

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
                }
            }
        }

        return suggestedWords;
    }

    public String allPossibleWordsMutations(String wordToCheck) {
        String listOfPossibleMutations = "";

        // create the final of suggest words from each possible mutation
        ArrayList<String> finalList = checkForMissingLetter(wordToCheck);
        finalList.addAll(checkForAddedLetter(wordToCheck));
        finalList.addAll(checkForRevesedLetters(wordToCheck));
        finalList = removeDuplicates(finalList);

        /**
         * convert the list into a slightly better formatted string for the spell check
         * dialog
         */
        for (int i = 0; i < finalList.size(); ++i) {
            listOfPossibleMutations = listOfPossibleMutations + finalList.get(i) + "\n";
        }

        return listOfPossibleMutations;
    }

    public boolean isWordInDictionary(String word) {
        if (map.containsKey(word.toLowerCase().hashCode())) {
            ArrayList<String> list = map.get(word.toLowerCase().hashCode());
            for (int k = 0; k < list.size(); ++k) {
                if (list.get(k).equalsIgnoreCase(word)) {
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList<String> removeDuplicates(ArrayList<String> list) {
        ArrayList<String> finalList = new ArrayList<String>();
        for (String words : list) {
            // don't add word if it is already in the list
            if (!finalList.contains(words)) {
                finalList.add(words);
            }
        }

        return finalList;
    }

}
