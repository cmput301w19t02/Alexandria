package ca.ualberta.CMPUT3012019T02.alexandria.cache;

import java.util.HashMap;

import ca.ualberta.CMPUT3012019T02.alexandria.model.book.Book;

/**
 * BasicCache for books
 */
public class BookCache extends BasicCache {

    private static BookCache instance;
    private HashMap<String, Book> cache = new HashMap<>();

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static BookCache getInstance() {
        if (instance == null) {
            instance = new BookCache();
        }
        return instance;
    }

    private BookCache() {
    }

    /**
     * Gets book from cache
     *
     * @param isbn the isbn
     * @return the book
     */
    public Book getBook(String isbn) {
        return cache.get(isbn);
    }

    /**
     * Put book into cache
     *
     * @param book the book
     */
    public void putBook(Book book) {
        cache.put(book.getIsbn(), book);
    }

    /**
     * Delete book from cache
     *
     * @param isbn of the book
     */
    public void deleteBook(String isbn) {
        cache.remove(isbn);
    }
}
