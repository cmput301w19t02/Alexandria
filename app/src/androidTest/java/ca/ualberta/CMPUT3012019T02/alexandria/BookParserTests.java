package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.cache.ObservableUserCache;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookParser;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookListItem;

public class BookParserTests {

    // TODO: book parser tests.
    // It works fine from using the app though. So this "formal testing" can be done later.

//    @Before
//    public void authenticate() throws InterruptedException, ExecutionException, TimeoutException {
//        UserController.getInstance().authenticate(null, null);
//        // TODO: setup test account with both owned books and borrowed books
//        CompletableFuture<Void> waitForData = new CompletableFuture<>();
//        ObservableUserCache.getInstance().addObserver((o, arg) -> waitForData.complete(null));
//        waitForData.get(5, TimeUnit.SECONDS);
//    }
//
//    @Test
//    public void testBorrowedBooksToBookList() throws InterruptedException, ExecutionException, TimeoutException, IOException {
//        List<BookListItem> bookList = BookParser.getMyBorrowedBooksList().get(5, TimeUnit.SECONDS);
//
////        Assert.assertEquals(1, bookListItem.size());
////
////        BookListItem item = bookListItem.get(0);
////        Assert.assertTrue(item.getIsbn().equals("9780547928227"));
////        Assert.assertTrue(item.getTitle().equals("The Hobbit"));
////        Assert.assertTrue(item.getAuthor().equals("J.R.R. Tolkien"));
////        Assert.assertTrue(item.getStatus().equals("requested"));
////
////
////        Bitmap expected = BitmapFactory.decodeStream(
////                new URL("https://firebasestorage.googleapis.com/v0/b/alexandria-a5aac.appspot.com/o/images%2Fac543736-b355-4eac-b9b8-a11165f0aa43.png?alt=media&token=02c5a1b0-ba82-4128-8665-85bb623d969b")
////                        .openConnection().getInputStream());
////
////        // Confirm they are the same
////        Assert.assertTrue(expected.sameAs(item.getCover()));
//    }
//
//    @Test
//    public void testOwnedBooksToBookList() throws InterruptedException, ExecutionException, TimeoutException {
//        List<BookListItem> bookList = BookParser.getMyOwnedBooksList().get(5, TimeUnit.SECONDS);
//    }

}
