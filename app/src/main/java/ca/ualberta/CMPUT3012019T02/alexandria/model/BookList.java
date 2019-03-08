package ca.ualberta.CMPUT3012019T02.alexandria.model;


import android.graphics.Bitmap;

/**
 * Model Class for Recyclers using BookList
 */

public class BookList {
    private Bitmap cover;
    private String title;
    private String author;
    private String isbn;
    private String status;

    public BookList() {
    }

    public BookList(Bitmap cover, String title, String author, String isbn, String status) {
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status;
    }


    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
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

}
