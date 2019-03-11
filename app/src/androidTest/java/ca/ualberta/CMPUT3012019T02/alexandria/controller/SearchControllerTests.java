package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.SearchController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;

/**
 * This class tests the UserController
 */
@RunWith(AndroidJUnit4.class)
public class SearchControllerTests {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSearchBooks() throws InterruptedException, ExecutionException, TimeoutException {
        SearchController controller = SearchController.getInstance();
        List<Book> books = controller.searchBooks("Harry Potter and the Philosopher's Stone").get(5, TimeUnit.SECONDS);
        Assert.assertEquals("9781781102459",books.get(0).getIsbn());
    }

    @Test
    public void testSearchBooksMultiple() throws InterruptedException, ExecutionException, TimeoutException {
        SearchController controller = SearchController.getInstance();
        List<Book> books = controller.searchBooks("wizard").get(5, TimeUnit.SECONDS);
        Assert.assertTrue(books.size()>1);
    }
}
