package fr.d2factory.libraryapp.book;

import java.io.Serializable;

/**
 * A simple representation of a book
 */
public class Book implements Serializable {
	
	private static final long serialVersionUID = 4163381799727917218L;

	private String title;
    
    private String author;
    
    private ISBN isbn;
    
    public Book() {
    }

    public Book(String title, String author, ISBN isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public ISBN getIsbn() {
		return isbn;
	}

	public void setIsbn(ISBN isbn) {
		this.isbn = isbn;
	}
    
    //Depends only on isbn code
    @Override
    public int hashCode() {
        return isbn.hashCode();
    }
 

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Book other = (Book) obj;
        return this.isbn.equals(other.isbn);
    }
    
    
}
