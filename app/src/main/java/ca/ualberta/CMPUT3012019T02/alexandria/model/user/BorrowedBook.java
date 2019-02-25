package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

public class BorrowedBook {

    private String isbn;
    private String status;
    private String owner;

    public BorrowedBook(String isbn, String status, String owner) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn cannot be null or empty");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner cannot be null or empty");
        }
        
        this.isbn = isbn;
        this.status = status;
        this.owner = owner;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("Isbn cannot be null or empty");
        }

        this.isbn = isbn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner cannot be null or empty");
        }

        this.owner = owner;
    }

}
