package ged.graph;

/**
 * Thrown to indicate that a DOT expression parsing error occured.
 * 
 * @author Roman Tekhov
 */
public class ParseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	
	public ParseException(String message) {
		super(message);
	}

}
