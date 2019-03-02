package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.AuthenticationController;
import java9.util.concurrent.CompletableFuture;

/**
 * This class tests the AuthenticationController
 */
@RunWith(AndroidJUnit4.class)
public class AuthenticationControllerTests {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testCreateUser() throws InterruptedException, ExecutionException, TimeoutException {
        AuthenticationController controller = AuthenticationController.getInstance();

        String username = "test_"+UUID.randomUUID().toString().replace('-', '_');
        String password = UUID.randomUUID().toString();
        String email = username + "@example.com";
        CompletableFuture<Void> future = controller.createUser(username, email, password);

        future.get(5, TimeUnit.SECONDS);

        Assert.assertTrue(controller.isAuthenticated());
    }

    @Test(expected = ExecutionException.class)
    public void testCreateUserUsernameAlreadyTaken() throws InterruptedException, ExecutionException, TimeoutException {
        AuthenticationController controller = AuthenticationController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = UUID.randomUUID().toString();
        String email = username + "@example.com";
        CompletableFuture<Void> future = controller.createUser(username, email, password);

        future.get(5, TimeUnit.SECONDS);
    }


    @Test(expected = ExecutionException.class)
    public void testCreateUserEmailExists() throws InterruptedException, ExecutionException, TimeoutException {
        AuthenticationController controller = AuthenticationController.getInstance();

        String username = "test_"+UUID.randomUUID().toString().replace('-', '_');
        String password = UUID.randomUUID().toString();
        String email = "0457de6b_0a85_481a_9093_c73de1ba0020@example.com";
        CompletableFuture<Void> future = controller.createUser(username, email, password);

        future.get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testAuthenticate() throws InterruptedException, ExecutionException, TimeoutException {
        AuthenticationController controller = AuthenticationController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        CompletableFuture<Void> future = controller.authenticate(username, password);

        future.get(5, TimeUnit.SECONDS);

        Assert.assertTrue(controller.isAuthenticated());
    }

    @Test(expected = ExecutionException.class)
    public void testAuthenticateBadCredentials() throws InterruptedException, ExecutionException, TimeoutException {
        AuthenticationController controller = AuthenticationController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "bad password";
        CompletableFuture<Void> future = controller.authenticate(username, password);

        future.get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testDeauthenticate() throws InterruptedException, ExecutionException, TimeoutException {
        AuthenticationController controller = AuthenticationController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        CompletableFuture<Void> future = controller.authenticate(username, password);

        future.get(5, TimeUnit.SECONDS);

        Assert.assertTrue(controller.isAuthenticated());

        controller.deauthenticate();

        Assert.assertFalse(controller.isAuthenticated());
    }

    @Test
    public void testGetMyId() throws InterruptedException, ExecutionException, TimeoutException {
        AuthenticationController controller = AuthenticationController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        CompletableFuture<Void> future = controller.authenticate(username, password);

        future.get(5, TimeUnit.SECONDS);

        Assert.assertTrue(controller.isAuthenticated());
        Assert.assertEquals("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1",controller.getMyId());
    }
}