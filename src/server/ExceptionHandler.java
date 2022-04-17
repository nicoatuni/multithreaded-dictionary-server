package server;

import java.io.FileNotFoundException;

import org.json.simple.parser.ParseException;

/**
 * Helper class for handling the various exceptions the server and all its
 * components can throw. Formats these exceptions into an object containing an
 * error title and message.
 * 
 * @author Nico Eka Dinata (770318)
 * 
 */
public class ExceptionHandler {
    public static ErrorResponse handle(Exception e, String customErrorMsg) {
        String errorTitle = "";
        String errorMsg = "";

        if (e instanceof DuplicateWordException) {
            errorTitle = "Duplicate Word";
            errorMsg = "The word already exists in the dictionary.";
        } else if (e instanceof WordNotFoundException) {
            errorTitle = "Word Not Found";
            errorMsg = "The word is not found in the dictionary.";
        } else if (e instanceof NoWordDefinitionException) {
            errorTitle = "No Word Definition";
            errorMsg = "The word has no accompanying definition.";
        } else if (e instanceof FileNotFoundException) {
            errorTitle = "File Not Found";
            errorMsg = "The dictionary file cannot be found.";
        } else if (e instanceof ParseException) {
            errorTitle = "Parser Error";
            errorMsg = "The dictionary file cannot be parsed correctly.";
        } else {
            errorTitle = "Error";
            errorMsg = "Something has gone wrong.";
        }

        if (customErrorMsg != null) {
            errorMsg = customErrorMsg;
        }

        ErrorResponse errorObject = new ErrorResponse(errorTitle, errorMsg);
        return errorObject;
    }
}

/** Helper subclass for when duplicate words have been detected. */
class DuplicateWordException extends Exception {
}

/** Helper subclass for when a requested word does not exist. */
class WordNotFoundException extends Exception {
}

/** Helper subclass for when a new added word has no accompanying definition. */
class NoWordDefinitionException extends Exception {
}
