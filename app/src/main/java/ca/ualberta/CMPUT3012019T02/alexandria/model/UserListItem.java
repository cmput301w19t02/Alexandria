package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.graphics.drawable.Drawable;

/**
 * The type User list item.
 */
//List Item for MyBookFragment's UserRecyclerViewAdapter
public class UserListItem {

    private Drawable borrowerPic;
    private String borrowerUsername;
    private String isbn;
    private String borrowerId;

    /**
     * Instantiates a new User list item.
     */
    public UserListItem() {
    }

    /**
     * Instantiates a new User list item.
     *
     * @param borrowerPic      the borrower pic
     * @param borrowerUsername the borrower username
     * @param isbn             the isbn
     * @param borrowerId       the borrower id
     */
    public UserListItem(
            Drawable borrowerPic, String borrowerUsername, String isbn, String borrowerId) {

        this.borrowerPic = borrowerPic;
        this.borrowerUsername = borrowerUsername;
        this.isbn = isbn;
        this.borrowerId = borrowerId;
    }

    /**
     * Gets borrower pic.
     *
     * @return the borrower pic
     */
    public Drawable getBorrowerPic() {
        return borrowerPic;
    }

    /**
     * Sets borrower pic.
     *
     * @param borrowerPic the borrower pic
     */
    public void setBorrowerPic(Drawable borrowerPic) {
        this.borrowerPic = borrowerPic;
    }

    /**
     * Gets borrower username.
     *
     * @return the borrower username
     */
    public String getBorrowerUsername() {
        return borrowerUsername;
    }

    /**
     * Sets borrower username.
     *
     * @param borrowerUsername the borrower username
     */
    public void setBorrowerUsername(String borrowerUsername) {
        this.borrowerUsername = borrowerUsername;
    }

    /**
     * Gets isbn.
     *
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets isbn.
     *
     * @param isbn the isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets borrower id.
     *
     * @return the borrower id
     */
    public String getBorrowerId() {
        return borrowerId;
    }

    /**
     * Sets borrower id.
     *
     * @param borrowerId the borrower id
     */
    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }
}
