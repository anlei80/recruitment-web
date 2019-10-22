package fr.d2factory.libraryapp.library;

/**
 * This exception is thrown when a member who owns late books tries to borrow another book
 */
public class HasLateBooksException extends Exception {
	
	private static final long serialVersionUID = -7511152640224894009L;

	public HasLateBooksException(String message) {
        super(message);
    }
}
