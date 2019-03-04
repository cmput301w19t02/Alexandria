package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

public abstract class UserBook {

    private String isbn;

    public UserBook(String isbn) {
        this.isbn = isbn;
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

}
