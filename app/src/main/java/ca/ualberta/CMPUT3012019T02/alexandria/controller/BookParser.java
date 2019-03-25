package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.model.BookListItem;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserBook;
import java9.util.concurrent.CompletableFuture;

/**
 * Functions as an adapter for BookController, ImageController, and UserController
 * to create BookListItems
 */
public class BookParser {

    /**
     * Gets a list of borrowed books of the current user, in a BookListItem format
     *
     * @return a CompletableFuture that returns a list of book list items
     */
    public static CompletableFuture<List<BookListItem>> getMyBorrowedBooksList() {
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

    /**
     * Gets a list of owned books of the current user, in a BookListItem format
     *
     * @return a CompletableFuture that returns a list of book list items
     */
    public static CompletableFuture<List<BookListItem>> getMyOwnedBooksList() {
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

    private static <T extends UserBook> CompletableFuture<List<BookListItem>> userBooksToBookList(Collection<T> userBooks) {
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
