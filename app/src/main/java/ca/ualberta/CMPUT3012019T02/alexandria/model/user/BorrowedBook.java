package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

public class BorrowedBook extends UserBook {

    private String owner;

    public BorrowedBook() {
        super(null, null);
    }

    public BorrowedBook(String isbn, String status, String owner) {
        super(isbn, null);

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner cannot be null or empty");
        }
        
        this.owner = owner;
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
