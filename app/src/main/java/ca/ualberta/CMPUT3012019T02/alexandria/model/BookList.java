package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.net.Uri;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class BookList {
    private int cover;
    private String title;
    private String author;
    private String isbn;
    private int status;

    public BookList() {
    }

    public BookList(int cover, String title, String author, String isbn, int status) {
        this.cover = cover;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = status; //1 available, 2 requested, 3 accepted, 4 borrowed
    }


    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
