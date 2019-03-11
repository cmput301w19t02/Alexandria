package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookList;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserBook;
import java9.util.concurrent.CompletableFuture;

/**
 * Parses UserBooks and Books and converts them into a more abstract format
 */
public class BookParser {

    /**
     * Convert a Map of String, UserBook to a list of BookList
     * @param userBookMap map of userbooks referenced by strings
     * @return an CompletableFuture with an ArrayList of BookList objects
     */
    public static <T extends UserBook> CompletableFuture<ArrayList<BookList>> UserBooksToBookList(Map<String, T> userBookMap) {
        CompletableFuture<ArrayList<BookList>> bookListFuture = new CompletableFuture<>();

        BookController bookController = BookController.getInstance();
        ImageController imageController = ImageController.getInstance();
        UserController userController = UserController.getInstance();
        ArrayList<BookList> result = new ArrayList<>();

        List<CompletableFuture> futures = new ArrayList<>();
        for (Map.Entry<String, T> entry : userBookMap.entrySet()) {

            CompletableFuture<Void> future = new CompletableFuture<>();
            CompletableFuture.runAsync(() -> {
                String isbn = entry.getKey();
                UserBook userBook = entry.getValue();

                bookController.getBook(isbn).thenCombineAsync(userController.getUserProfile(userBook.getOwner()), (book, userProfile) -> {
                    try {

                        Bitmap image = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
                        if (book.getImageId() != null) {
                            image = imageController.getImage(book.getImageId()).get(5, TimeUnit.SECONDS);
                        }

                        BookList bookList = new BookList(image, book.getTitle(), book.getAuthor(), isbn, userBook.getStatus(), userProfile.getName());

                        result.add(bookList);
                        future.complete(null);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                    return null;
                });

            });
            futures.add(future);

        }

        CompletableFuture.runAsync(() -> {
            for (CompletableFuture future : futures) {
                try {
                    future.get(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    bookListFuture.completeExceptionally(e);
                }
            }
            bookListFuture.complete(result);
        });

        return bookListFuture;
    }

}
