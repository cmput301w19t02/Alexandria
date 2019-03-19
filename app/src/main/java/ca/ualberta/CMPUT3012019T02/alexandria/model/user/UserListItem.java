package ca.ualberta.CMPUT3012019T02.alexandria.model.user;

import android.graphics.Bitmap;

//List Item for MyBookFragment's UserRecyclerViewAdapter
public class UserListItem {

    private Bitmap borrowerPic;
    private String borrowerUsername;
    private String isbn;
    private String borrowerId;

    public UserListItem() {
    }

    public UserListItem(
            Bitmap borrowerPic, String borrowerUsername, String isbn, String borrowerId) {

        this.borrowerPic = borrowerPic;
        this.borrowerUsername = borrowerUsername;
        this.isbn = isbn;
        this.borrowerId = borrowerId;
    }

    public Bitmap getBorrowerPic() {
        return borrowerPic;
    }

    public void setBorrowerPic(Bitmap borrowerPic) {
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
