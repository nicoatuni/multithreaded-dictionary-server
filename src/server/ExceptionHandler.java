package server;

/**
 * TODO: add docs
 * 
 * @author Nico Dinata (770318)
 */
public class ExceptionHandler {
    // TODO: format different exception types into consistent error messages
    // in JSON.
}

class DuplicateWordException extends Exception {
}

class WordNotFoundException extends Exception {
}

class NoWordDefinitionException extends Exception {
}
