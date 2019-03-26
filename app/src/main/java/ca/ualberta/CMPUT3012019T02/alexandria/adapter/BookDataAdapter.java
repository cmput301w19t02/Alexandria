package ca.ualberta.CMPUT3012019T02.alexandria.adapter;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.cache.ObservableUserCache;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.BookListItem;
import ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.user.UserBook;
import java9.util.concurrent.CompletableFuture;

/**
 * Functions as an adapter for BookController, ImageController, and UserController
 */
public class BookDataAdapter extends Observable {

    private List<BookListItem> myBorrowedBookList;
    private List<BookListItem> myOwnedBookList;

    private static BookDataAdapter instance;

    public static BookDataAdapter getInstance() {
        if (instance == null) {
            instance = new BookDataAdapter();
        }
        return instance;
    }

    private class CacheObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {

            new Thread(() -> {
                try {

                    myBorrowedBookList = fetchMyBorrowedBooksList().get(5, TimeUnit.SECONDS);
                    myOwnedBookList = fetchMyOwnedBooksList().get(5, TimeUnit.SECONDS);

                    Collections.sort(myBorrowedBookList, BookListItem.getComparator());
                    Collections.sort(myOwnedBookList, BookListItem.getComparator());

                    setChanged();
                    notifyObservers();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }

    private BookDataAdapter() {
        myBorrowedBookList = new ArrayList<>();
        myOwnedBookList = new ArrayList<>();
        ObservableUserCache.getInstance().addObserver(new CacheObserver());
    }

    /**
     * Gets a list of borrowed books of the current user, in a BookListItem format.
     * Items are sorted by status and then by alphabetical order.
     *
     * @return a CompletableFuture that returns a list of book list items
     */
    public List<BookListItem> getMyBorrowedBooksList() {
        return myBorrowedBookList;
    }

    /**
     * Gets a list of owned books of the current user, in a BookListItem format
     * Items are sorted by status and then by alphabetical order.
     *
     * @return a CompletableFuture that returns a list of book list items
     */
    public List<BookListItem> getMyOwnedBooksList() {
        return myOwnedBookList;
    }

    private CompletableFuture<List<BookListItem>> fetchMyBorrowedBooksList() {
        CompletableFuture<List<BookListItem>> future = new CompletableFuture<>();
        BookController.getInstance().getMyBorrowedBooks().thenAcceptAsync(
                borrowedBooks -> userBooksToBookList(borrowedBooks)
                        .thenAccept(future::complete)
                        .exceptionally(throwable -> {
                            future.completeExceptionally(throwable);
                            return null;
                        })
        ).exceptionally(throwable -> {
            future.completeExceptionally(throwable);
            return null;
        });
        return future;
    }

    private CompletableFuture<List<BookListItem>> fetchMyOwnedBooksList() {
        CompletableFuture<List<BookListItem>> future = new CompletableFuture<>();
        BookController.getInstance().getMyOwnedBooks().thenAcceptAsync(
                ownedBooks -> userBooksToBookList(ownedBooks)
                        .thenAccept(future::complete)
                        .exceptionally(throwable -> {
                            future.completeExceptionally(throwable);
                            return null;
                        })
        ).exceptionally(throwable -> {
            future.completeExceptionally(throwable);
            return null;
        });
        return future;
    }

    private <T extends UserBook> CompletableFuture<List<BookListItem>> userBooksToBookList(Collection<T> userBooks) {
        CompletableFuture<List<BookListItem>> future = new CompletableFuture<>();
        List<BookListItem> result = new ArrayList<>();

        BookController bookController = BookController.getInstance();
        ImageController imageController = ImageController.getInstance();

        List<CompletableFuture> tasks = new LinkedList<>();
        for (T userBook : userBooks) {
            CompletableFuture<Void> f = new CompletableFuture<>();
            bookController.getBook(userBook.getIsbn()).thenAcceptAsync(bookOptional -> bookOptional.ifPresentOrElse(
                    book -> {
                        Bitmap image = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
                        String imageId = book.getImageId();
                        if (imageId != null) {
                            try {
                                image = imageController.getImage(imageId).get(5, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }
                        }

                        BookListItem bookListItem = new BookListItem(image, book.getImageId(), book.getTitle(), book.getAuthor(), book.getIsbn(), userBook.getStatus(), userBook.getOwner());
                        result.add(bookListItem);
                        f.complete(null);

                    }, () -> f.completeExceptionally(new IllegalStateException("No book was found with the given isbn " + userBook.getIsbn()))));
            tasks.add(f);
        }

        CompletableFuture.runAsync(() -> {
            for (CompletableFuture f : tasks) {
                try {
                    f.get(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
            future.complete(result);
        });

        return future;
    }

}
