package fr.d2factory.libraryapp.member;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.library.Library;
import fr.d2factory.libraryapp.library.WalletInsufficientException;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public abstract class Member {
    
	/**
     * An initial sum of money the member has
     */
    private float wallet;
    
    public float getWallet() {
        return wallet;
    }

    public void setWallet(float wallet) {
        this.wallet = wallet;
    }
    
    /**
     * A map to hold borrowed books
     */
    private Map<Book, LocalDate> borrowedBooks;
    
    public Member() {
    	borrowedBooks = new HashMap<Book, LocalDate>();
    }

    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
    public abstract void payBook(int numberOfDays) throws WalletInsufficientException;
    
    /**
     * The limit number of days
     * @return number of days
     */
    public abstract int getLimitDays();
    
    /**
     * Add borrowed book to the map
     * @param book the borrowed book
     * @param date the borrowed date
     */

    public void addBorrowedBook(Book book, LocalDate date) {
    	borrowedBooks.put(book, date);
    }
    
    /**
     * Remove the borrowed from the map
     * @param book the borrowed book
     */
    public void removeBorrowedBook(Book book) {
    	borrowedBooks.remove(book);
    }
    
    /**
     * Return if the member have the late books
     * @return boolean
     */
    public boolean hasLateBooks() {
    	long lateCount = borrowedBooks.values().stream().filter(date -> 
    		date.plusDays(getLimitDays()).isAfter(LocalDate.now())
    	).count();
    	
    	return lateCount > 0;
    }
}
