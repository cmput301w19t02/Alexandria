package ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses;

import android.graphics.Bitmap;

public class OwnerListItem {

    private Bitmap ownerPic;
    private String ownerUsername;
    private String ownerId;
    private String isbn;
    private String status;
    private String title;
    private String author;

    public OwnerListItem() {
    }


    public OwnerListItem(Bitmap ownerPic, String ownerUsername, String ownerId,
                         String isbn, String status, String title, String author) {
        this.ownerPic = ownerPic;
        this.ownerUsername = ownerUsername;
        this.ownerId = ownerId;
        this.isbn = isbn;
        this.status = status;
        this.title = title;
        this.author = author;
    }


    public Bitmap getOwnerPic() {
        return ownerPic;
    }

    public void setOwnerPic(Bitmap ownerPic) {
        this.ownerPic = ownerPic;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
