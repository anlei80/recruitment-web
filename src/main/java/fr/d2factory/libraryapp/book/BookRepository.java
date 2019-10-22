package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepository {
	
    private Map<ISBN, Book> availableBooks;
    
    private Map<Book, LocalDate> borrowedBooks;

    
	public BookRepository() {
		availableBooks = new HashMap<>();
		borrowedBooks = new HashMap<>();
	}
	
	private static BookRepository INSTANCE = null;
	
	public static BookRepository getInstance() {
		if (INSTANCE == null) {   
			INSTANCE = new BookRepository(); 
        }
        return INSTANCE; 
	}

    public void addBooks(List<Book> books) {
        books.stream().forEach(book -> {
        	availableBooks.put(book.getIsbn(), book);
        });
    }

    public Book findBook(ISBN isbnCode) {
        return availableBooks.get(isbnCode);
    }

    public void saveBookBorrow(Book book, LocalDate borrowedAt){
    	availableBooks.remove(book.getIsbn());
        borrowedBooks.put(book, borrowedAt);
    }
    
    public void returnBookBorrow(Book book) {
    	borrowedBooks.remove(book);
    	this.addBooks(Arrays.asList(book));
    }

    public LocalDate findBorrowedBookDate(Book book) {
        return borrowedBooks.get(book);
    }

	public Map<ISBN, Book> getAvailableBooks() {
		return availableBooks;
	}

	public Map<Book, LocalDate> getBorrowedBooks() {
		return borrowedBooks;
	}
    
    
}
