package ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses;

import android.graphics.drawable.Drawable;

//List Item for MyBookFragment's UserRecyclerViewAdapter
public class UserListItem {

    private Drawable borrowerPic;
    private String borrowerUsername;
    private String isbn;
    private String borrowerId;

    public UserListItem() {
    }

    public UserListItem(
            Drawable borrowerPic, String borrowerUsername, String isbn, String borrowerId) {

        this.borrowerPic = borrowerPic;
        this.borrowerUsername = borrowerUsername;
        this.isbn = isbn;
        this.borrowerId = borrowerId;
    }

    public Drawable getBorrowerPic() {
        return borrowerPic;
    }

    public void setBorrowerPic(Drawable borrowerPic) {
        this.borrowerPic = borrowerPic;
    }

    public String getBorrowerUsername() {
        return borrowerUsername;
    }

    public void setBorrowerUsername(String borrowerUsername) {
        this.borrowerUsername = borrowerUsername;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }
}
