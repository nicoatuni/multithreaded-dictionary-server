package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class drives the logic of operations of the dictionary, including
 * reading the list of words from the dictionary file into memory, querying for
 * words, and adding, updating, and removing words. All public methods of this
 * class are `synchronized`, meaning locks have to be obtained before access to
 * the dictionary object (JSONObject) is given.
 * 
 * @author Nico Eka Dinata (770318)
 * 
 */
public class DictionaryHandler {

    /** The object containing the words in the dictionary. */
    private static JSONObject wordJsonObject = null;

    /**
     * Reads dictionary words from the specified file path (in JSON format) into
     * memory.
     */
    public synchronized static void initDictionaryFile(String filePath)
            throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        wordJsonObject = (JSONObject) parser.parse(new FileReader(filePath));

        // TODO: commit changes to in-memory dictionary to disk at some point?
    }

    /**
     * Retrieves the definition of the given word. Returns `null` if the word
     * does not exist in the dictionary.
     */
    public synchronized static String getDefinition(String word) throws FileNotFoundException {
        if (wordJsonObject == null) {
            throw new FileNotFoundException();
        }

        return (String) wordJsonObject.get(word);
    }

    /**
     * Adds a new word and its associated definition into the dictionary. Will
     * throw exceptions if the new word has no definitions, if the dictionary
     * object does not exist, or if the new word already exists in the dictionary.
     */
    public synchronized static void addDefinition(String word, String definition)
            throws NoWordDefinitionException, FileNotFoundException, DuplicateWordException {
        if (definition == null) {
            throw new NoWordDefinitionException();
        }

        if (wordJsonObject == null) {
            throw new FileNotFoundException();
        }

        if (wordJsonObject.containsKey(word)) {
            throw new DuplicateWordException();
        }

        wordJsonObject.put(word, definition);
    }

    /**
     * Removes the given word (and its definition) from the dictionary. Will
     * throw exceptions if the dictionary object does not exist or if the word
     * does not already exist in the dictionary.
     */
    public synchronized static void removeWord(String word)
            throws FileNotFoundException, WordNotFoundException {
        if (wordJsonObject == null) {
            throw new FileNotFoundException();
        }

        if (!wordJsonObject.containsKey(word)) {
            throw new WordNotFoundException();
        }

        wordJsonObject.remove(word);
    }

    /**
     * Updates the definition of an existing word in the dictionary. Will
     * throw exceptions if the new definition is null, if the dictionary object
     * does not exist, or if the word does not already exist in the dictionary.
     */
    public synchronized static void updateDefinition(String word, String definition)
            throws NoWordDefinitionException, FileNotFoundException, WordNotFoundException {
        if (definition == null) {
            throw new NoWordDefinitionException();
        }

        if (wordJsonObject == null) {
            throw new FileNotFoundException();
        }

        if (!wordJsonObject.containsKey(word)) {
            throw new WordNotFoundException();
        }

        wordJsonObject.put(word, definition);
    }
}
