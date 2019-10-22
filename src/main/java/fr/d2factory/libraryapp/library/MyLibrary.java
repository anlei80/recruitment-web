package fr.d2factory.libraryapp.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Member;

public class MyLibrary implements Library {
	
	private BookRepository bookRepository = BookRepository.getInstance();
	
	public MyLibrary() {
		
	}

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException {
		if (member.hasLateBooks()) {
			throw new HasLateBooksException("You can't borrow book");
		}
		
		//Create un isbn object
		ISBN isbn = new ISBN(isbnCode);
		
		//Find the book with isbn code
		Book book = bookRepository.findBook(isbn);
		
		if (book != null) {
			//Persist borrowed book in the repository and memeber's information
			bookRepository.saveBookBorrow(book, borrowedAt);
			member.addBorrowedBook(book, LocalDate.now());
		}
		return book;
	}

	@Override
	public void returnBook(Book book, Member member) throws WalletInsufficientException {
		//Find the borrowed date and calculate the duration 
		LocalDate borrowedDate = bookRepository.findBorrowedBookDate(book);
		long numberOfDays = ChronoUnit.DAYS.between(borrowedDate, LocalDate.now());
		//Member pay book's fee
		member.payBook((int)numberOfDays);
		//Persiet the book in the repository
		bookRepository.returnBookBorrow(book);
		member.removeBorrowedBook(book);

	}

}
