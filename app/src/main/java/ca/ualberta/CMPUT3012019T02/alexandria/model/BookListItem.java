package ca.ualberta.CMPUT3012019T02.alexandria.model;


import android.graphics.Bitmap;

/**
 * Model Class for BookRecyclerViewAdapter
 */
public class BookListItem {
    private Bitmap cover;
    private String title;
    private String author;
    private String isbn;
    private String status;
    private String ownerId;

    /**
     * Instantiates a new Book list item.
     */
    public BookListItem() {
    }

    /**
     * Constructor for BookListItem
     *
     * @param cover  bitmap of cover image
     * @param title  title of the book
     * @param author author of the book
     * @param isbn   isbn of the book
     * @param status status
     * @param ownerId the id of the owner
     */
    public BookListItem(Bitmap cover, String title, String author,
                        String isbn, String status, String ownerId) {
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
        this.ownerId = ownerId;
    }

    /**
     * Instantiates a new Book list item.
     *
     * @param cover  the cover
     * @param title  the title
     * @param author the author
     * @param isbn   the isbn
     */
    public BookListItem(Bitmap cover, String title, String author, String isbn) {
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    /**
     * gets Bitmap cover of the book
     *
     * @return Bitmap cover
     */
    public Bitmap getCover() {
        return cover;
    }

    /**
     * sets cover for the book
     *
     * @param cover cover
     */
    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    /**
     * gets title of the book
     *
     * @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets title for the book
     *
     * @param title title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets author of the book
     *
     * @return String author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * sets author for the book
     *
     * @param author author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * gets isbn of the book
     *
     * @return String isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * sets isbn for the book
     *
     * @param isbn isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * gets status of the book
     *
     * @return String status
     */
    public String getStatus() {
        return status;
    }

    /**
     * sets status for the book
     *
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * gets owner Id of the book
     *
     * @return String owner
     */
    public String getOwnerId() { return ownerId; }

    /**
     * sets ownerId for the book
     *
     * @param owner owner
     */
    public void setOwnerId(String owner) { this.ownerId = owner; }

}
