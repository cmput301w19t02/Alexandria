package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

public class BorrowedBook {

    private String isbn;
    private String status;
    private String owner;

    public BorrowedBook(String isbn, String status, String owner) {
        this.isbn = isbn;
        this.status = status;
        this.owner = owner;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
