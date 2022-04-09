package server;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Nico Dinata (770318)
 */
public class DictionaryHandler {
    private static JSONObject wordJsonObject = null;

    /**
     * Reads dictionary words from the specified file path (in JSON format) into
     * memory.
     * 
     * @param filePath to read words from
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ParseException
     */
    public synchronized static void initDictionaryFile(String filePath)
            throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        wordJsonObject = (JSONObject) parser.parse(new FileReader(filePath));

        // TODO: commit changes to in-memory dictionary to disk at some point?
    }

    /**
     * Retrieves the definition for a given word. Returns `null` if the word
     * does not exist in the dictionary.
     * 
     * @param word to look up the definition of
     * @return the definition of `word` if it exists, `null` otherwise
     * @throws IOException
     * @throws ParseException
     * @throws FileNotFoundException
     */
    public synchronized static String getDefinition(String word) throws FileNotFoundException {
        if (wordJsonObject == null) {
            throw new FileNotFoundException();
        }

        return (String) wordJsonObject.get(word);
    }

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
