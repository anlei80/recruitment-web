package fr.d2factory.libraryapp.book;

import java.io.Serializable;

public class ISBN implements Serializable {
	
	private static final long serialVersionUID = -927265643819624651L;
	
	long isbnCode;
	
	public ISBN() {
		
	}

    public ISBN(long isbnCode) {
        this.isbnCode = isbnCode;
    }

	public long getIsbnCode() {
		return isbnCode;
	}

	public void setIsbnCode(long isbnCode) {
		this.isbnCode = isbnCode;
	}
    
    @Override
    public int hashCode() {
        return Long.hashCode(isbnCode);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ISBN other = (ISBN) obj;
        if (this.isbnCode != other.isbnCode)
            return false;
        return true;
    }
}
