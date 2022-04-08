package server;

import java.io.FileNotFoundException;

import org.json.simple.parser.ParseException;

/**
 * @author Nico Dinata (770318)
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

class DuplicateWordException extends Exception {
}

class WordNotFoundException extends Exception {
}

class NoWordDefinitionException extends Exception {
}
