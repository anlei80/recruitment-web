package fr.d2factory.libraryapp.library;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.ResidentMember;
import fr.d2factory.libraryapp.member.StudentMember;

public class LibraryTest {
    private Library library ;
    private BookRepository bookRepository;

    @Before
    public void setup(){
        //TODO instantiate the library and the repository
    	library = new MyLibrary();
    	bookRepository = BookRepository.getInstance();

        //TODO add some test books (use BookRepository#addBooks)
    	ObjectMapper mapper = new ObjectMapper();
    	try {
			Book[] books = mapper.readValue( getClass()
					.getClassLoader().getResourceAsStream("books.json"), Book[].class);
			bookRepository.addBooks(Arrays.asList(books));
		} catch (JsonParseException e) {
			fail();
		} catch (JsonMappingException e) {
			fail();
		} catch (IOException e) {
			fail();
		}
        //TODO to help you a file called books.json is available in src/test/resources
    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available() {
    	Member student = new StudentMember();
    	long isbnCode = 46578964513l;
    	ISBN isbn = new ISBN(isbnCode);
    	Book book = bookRepository.findBook(isbn);
        try {
        	if (book != null) {
        		Book borrowedBook = library.borrowBook(46578964513l, student, LocalDate.now());
        		assertEquals("Equals", isbn, borrowedBook.getIsbn());
        		assertEquals("Equals", 3, bookRepository.getAvailableBooks().size());
        		assertEquals("Equals", 1, bookRepository.getBorrowedBooks().size());
        	} else {
        		fail();
        	}
		} catch (HasLateBooksException e) {
			// TODO Auto-generated catch block
			fail();
		}
    }

    
    @Test
    public void borrowed_book_is_no_longer_available(){
    	long isbnCode = 46578964513l;
    	ISBN isbn = new ISBN(isbnCode);
    	Member student = new StudentMember();
    	try {
			library.borrowBook(46578964513l, student, LocalDate.now());
			Book book = bookRepository.findBook(isbn);
			assertNull(book);
		} catch (HasLateBooksException e) {
			// TODO Auto-generated catch block
			fail();
		}
    	
    }

    
    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book(){
        Member resident = new ResidentMember();
        long isbnCode = 46578964513l;
    	ISBN isbn = new ISBN(isbnCode);
    	try {
			library.borrowBook(46578964513l, resident, LocalDate.now());
			Book book = bookRepository.findBook(isbn);
			assertNull(book);
		} catch (HasLateBooksException e) {
			// TODO Auto-generated catch block
			fail();
		}
    }

    
    @Test
    public void students_pay_10_cents_the_first_30days(){
    	long isbnCode = 46578964513l;
    	ISBN isbn = new ISBN(isbnCode);
    	Member student = new StudentMember();
    	student.setWallet(100);
    	try {
			Book borrowedBook = library.borrowBook(46578964513l, student, LocalDate.now().minusDays(20));
			library.returnBook(borrowedBook, student);
			assertEquals((100 - 0.1f * 20), student.getWallet(), 0 );
		} catch (HasLateBooksException | WalletInsufficientException e) {
			fail();
		}
    }

    
    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days(){
    	long isbnCode = 46578964513l;
    	ISBN isbn = new ISBN(isbnCode);
    	StudentMember student = new StudentMember();
    	student.setFirstYear(true);
    	student.setWallet(100);
    	try {
			Book borrowedBook = library.borrowBook(46578964513l, student, LocalDate.now().minusDays(10));
			library.returnBook(borrowedBook, student);
			assertEquals(100, student.getWallet(), 0 );
		} catch (HasLateBooksException | WalletInsufficientException e) {
			fail();
		}
    }
    
    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days(){
    	long isbnCode = 46578964513l;
    	ISBN isbn = new ISBN(isbnCode);
    	Member student = new StudentMember();
    	student.setWallet(100);
    	try {
			Book borrowedBook = library.borrowBook(46578964513l, student, LocalDate.now().minusDays(40));
			library.returnBook(borrowedBook, student);
			assertEquals(95.5, student.getWallet(), 0 );
		} catch (HasLateBooksException | WalletInsufficientException e) {
			fail();
		}
    }
    
    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days(){
    	long isbnCode = 46578964513l;
    	ISBN isbn = new ISBN(isbnCode);
    	Member resident = new ResidentMember();
    	resident.setWallet(100);
    	try {
			Book borrowedBook = library.borrowBook(46578964513l, resident, LocalDate.now().minusDays(70));
			library.returnBook(borrowedBook, resident);
			assertEquals(92, resident.getWallet(), 0 );
		} catch (HasLateBooksException | WalletInsufficientException e) {
			fail();
		}
    }
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void members_cannot_borrow_book_if_they_have_late_books() throws HasLateBooksException{
    	thrown.expect(HasLateBooksException.class);
    	thrown.expectMessage(CoreMatchers.containsString("You can't borrow book")); 
    	 
    	long isbnOneBook = 46578964513l;
    	Member resident = new ResidentMember();
    	resident.setWallet(100);

    	library.borrowBook(isbnOneBook, resident, LocalDate.now().minusDays(70));
    	
    	long isbnAntherBook = 3326456467846l;
    	library.borrowBook(isbnAntherBook, resident, LocalDate.now());
    	
    }
    
    @Rule
    public ExpectedException thrownOther = ExpectedException.none();
    
    @Test
    public void members_should_charger_wallet_when_then_return_book() throws HasLateBooksException, WalletInsufficientException {
    	thrownOther.expect(WalletInsufficientException.class);
    	thrownOther.expectMessage(CoreMatchers.containsString("You should charge your wallet")); 
    	 
    	long isbnOneBook = 46578964513l;
    	Member resident = new ResidentMember();

    	Book borrowedBook = library.borrowBook(isbnOneBook, resident, LocalDate.now().minusDays(70));
    	library.returnBook(borrowedBook, resident);
    }
}
