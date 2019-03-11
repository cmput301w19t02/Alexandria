package ca.ualberta.CMPUT3012019T02.alexandria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookParser;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookList;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.BorrowedBook;

public class BookParserTests {

    @Test
    public void testBorrowedBooksToBookList() throws InterruptedException, ExecutionException, TimeoutException, IOException {
        BookController bookController = BookController.getInstance();
        HashMap<String, BorrowedBook> borrowedBookHashMap = bookController.getUserBorrowedBooks("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1").get(5, TimeUnit.SECONDS);
        List<BookList> bookList = BookParser.UserBooksToBookList(borrowedBookHashMap).get(10, TimeUnit.SECONDS);
        Assert.assertEquals(1, bookList.size());

        BookList item = bookList.get(0);
        Assert.assertTrue(item.getIsbn().equals("9780547928227"));
        Assert.assertTrue(item.getTitle().equals("The Hobbit"));
        Assert.assertTrue(item.getAuthor().equals("J.R.R. Tolkien"));
        Assert.assertTrue(item.getStatus().equals("requested"));


        Bitmap expected = BitmapFactory.decodeStream(
                new URL("https://firebasestorage.googleapis.com/v0/b/alexandria-a5aac.appspot.com/o/images%2Fac543736-b355-4eac-b9b8-a11165f0aa43.png?alt=media&token=02c5a1b0-ba82-4128-8665-85bb623d969b")
                        .openConnection().getInputStream());

        // Confirm they are the same
        Assert.assertTrue(expected.sameAs(item.getCover()));

    }
}
