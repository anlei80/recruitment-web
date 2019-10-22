package fr.d2factory.libraryapp.library;

/**
 * This exception is thrown when a member dosn't have enough money
 */
public class WalletInsufficientException extends Exception {
	

	private static final long serialVersionUID = 6366971586299443540L;

	public WalletInsufficientException(String message) {
        super(message);
    }
}
