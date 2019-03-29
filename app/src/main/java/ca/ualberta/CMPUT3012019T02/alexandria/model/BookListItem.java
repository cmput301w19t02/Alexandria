package ca.ualberta.CMPUT3012019T02.alexandria.model;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Model Class for BookRecyclerViewAdapter
 */
public class BookListItem {
    private Bitmap cover;
    private String coverId;
    private String title;
    private String author;
    private String isbn;
    private String status;
    private String ownerId;
    private Collection<String> availableOwners = new ArrayList<>();

    private static class BookListItemComparator implements Comparator<BookListItem> {
        @Override
        public int compare(BookListItem o1, BookListItem o2) {
            List<String> statuses = Arrays.asList("available", "requested", "accepted", "borrowed");
            int i1 = statuses.indexOf(o1.getStatus());
            int i2 = statuses.indexOf(o2.getStatus());
            if (i1 < i2) {
                return -1;
            } else if (i1 == i2) {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getTitle(), o2.getTitle());
            } else { // i1 > i2
                return 1;
            }
        }
    }

    /**
     * Gets a comparator for comparing two BookListItems.
     * Sorts by status first, in order of 'available', 'requested', 'accepted', 'borrowed'.
     * Then uses alphabetical order of book title to resolve ordering of books with the same status.
     *
     * @return a Comparator
     */
    public static Comparator<BookListItem> getComparator() {
        return new BookListItemComparator();
    }

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
    public BookListItem(Bitmap cover, String coverId, String title, String author,
                        String isbn, String status, String ownerId) {
        this.cover = cover;
        this.coverId = coverId;
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
     * @param availableOwners the available owners
     */
    public BookListItem(Bitmap cover, String title, String author, String isbn, Collection<String> availableOwners) {
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.availableOwners = availableOwners;
    }

    /**
     * gets the Collection of available owner ids
     *
     * @return Collection<String> availableOwners
     */
    public ArrayList<String> getAvailableOwners() { return new ArrayList<>(availableOwners); }

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

    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }
}
