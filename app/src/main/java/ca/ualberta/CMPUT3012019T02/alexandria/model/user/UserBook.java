package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

import java.util.Arrays;
import java.util.List;

public abstract class UserBook {

    private String isbn;
    private String status;

    public UserBook(String isbn, String status) {
        this.isbn = isbn;
        this.status = status;
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
        List<String> validStatuses = Arrays.asList("available", "requested", "accepted", "exchanged");
        if (!validStatuses.contains(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status;
    }
}
