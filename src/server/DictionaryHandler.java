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
    private static final String WORDS_FILE_PATH = "../../words.json";

    private static JSONObject wordJsonObject = null;

    private static void initDictionaryFromFile() throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        wordJsonObject = (JSONObject) parser.parse(new FileReader(WORDS_FILE_PATH));
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
    public synchronized static String getDefinition(String word)
            throws IOException, ParseException, FileNotFoundException {
        if (wordJsonObject == null) {
            initDictionaryFromFile();
        }

        return (String) wordJsonObject.get(word);
    }

    public synchronized static void addDefinition(String word, String definition)
            throws FileNotFoundException, IOException, ParseException, DuplicateWordException,
            NoWordDefinitionException {
        if (definition == null) {
            throw new NoWordDefinitionException();
        }

        if (wordJsonObject == null) {
            initDictionaryFromFile();
        }

        if (wordJsonObject.containsKey(word)) {
            throw new DuplicateWordException();
        }

        wordJsonObject.put(word, definition);
    }

    public synchronized static void removeWord(String word)
            throws FileNotFoundException, IOException, ParseException, WordNotFoundException {
        if (wordJsonObject == null) {
            initDictionaryFromFile();
        }

        if (!wordJsonObject.containsKey(word)) {
            throw new WordNotFoundException();
        }

        wordJsonObject.remove(word);
    }

    public synchronized static void updateDefinition(String word, String definition)
            throws FileNotFoundException, IOException, ParseException, NoWordDefinitionException,
            WordNotFoundException {
        if (definition == null) {
            throw new NoWordDefinitionException();
        }

        if (wordJsonObject == null) {
            initDictionaryFromFile();
        }

        if (!wordJsonObject.containsKey(word)) {
            throw new WordNotFoundException();
        }

        wordJsonObject.put(word, definition);
    }
}
