package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.graphics.Bitmap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The type Owner list item.
 */
public class OwnerListItem {

    private Bitmap ownerPic;
    private String ownerUsername;
    private String ownerId;
    private String isbn;
    private String status;

    private static class OwnerListItemComparator implements Comparator<OwnerListItem> {
        @Override
        public int compare(OwnerListItem o1, OwnerListItem o2) {
            List<String> statuses = Arrays.asList("available", "requested", "accepted", "borrowed");
            int i1 = statuses.indexOf(o1.getStatus());
            int i2 = statuses.indexOf(o2.getStatus());
            if (i1 < i2) {
                return -1;
            } else if (i1 == i2) {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getOwnerUsername(), o2.getOwnerUsername());
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
    public static Comparator<OwnerListItem> getComparator() {
        return new OwnerListItemComparator();
    }

    /**
     * Instantiates a new Owner list item.
     */
    public OwnerListItem() {
    }


    /**
     * Instantiates a new Owner list item.
     *
     * @param ownerPic      the owner pic
     * @param ownerUsername the owner username
     * @param ownerId       the owner id
     * @param isbn          the isbn
     * @param status        the status
     */
    public OwnerListItem(Bitmap ownerPic, String ownerUsername, String ownerId, String isbn, String status) {
        this.ownerPic = ownerPic;
        this.ownerUsername = ownerUsername;
        this.ownerId = ownerId;
        this.isbn = isbn;
        this.status = status;
    }


    /**
     * Gets owner pic.
     *
     * @return the owner pic
     */
    public Bitmap getOwnerPic() {
        return ownerPic;
    }

    /**
     * Sets owner pic.
     *
     * @param ownerPic the owner pic
     */
    public void setOwnerPic(Bitmap ownerPic) {
        this.ownerPic = ownerPic;
    }

    /**
     * Gets owner username.
     *
     * @return the owner username
     */
    public String getOwnerUsername() {
        return ownerUsername;
    }

    /**
     * Sets owner username.
     *
     * @param ownerUsername the owner username
     */
    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    /**
     * Gets owner id.
     *
     * @return the owner id
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Sets owner id.
     *
     * @param ownerId the owner id
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
