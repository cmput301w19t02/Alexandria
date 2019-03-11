package ca.ualberta.CMPUT3012019T02.alexandria;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.MainActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;
import java9.util.concurrent.CompletableFuture;

/**
 * This class tests the UserController
 */
@RunWith(AndroidJUnit4.class)
public class UserControllerTests {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testCreateUser() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "test_" + UUID.randomUUID().toString().replace('-', '_');
        String password = UUID.randomUUID().toString();
        String email = username + "@example.com";
        CompletableFuture<Void> future = controller.createUser("John Smith", email, "7801234567", null, username, password);

        future.get(5, TimeUnit.SECONDS);

        Assert.assertTrue(controller.isAuthenticated());
    }

    @Test(expected = ExecutionException.class)
    public void testCreateUserUsernameAlreadyTaken() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = UUID.randomUUID().toString();
        String email = username + "@example.com";
        CompletableFuture<Void> future = controller.createUser("John Smith", email, "7801234567", null, username, password);

        future.get(5, TimeUnit.SECONDS);
    }


    @Test(expected = ExecutionException.class)
    public void testCreateUserEmailExists() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "test_" + UUID.randomUUID().toString().replace('-', '_');
        String password = UUID.randomUUID().toString();
        String email = "0457de6b_0a85_481a_9093_c73de1ba0020@example.com";
        CompletableFuture<Void> future = controller.createUser("John Smith", email, "7801234567", null, username, password);

        future.get(5, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testCreateUserInvalidEmail() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "test_" + UUID.randomUUID().toString().replace('-', '_');
        String password = UUID.randomUUID().toString();
        CompletableFuture<Void> future = controller.createUser("John Smith", null, "7801234567", null, username, password);

        future.get(5, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testCreateUserInvalidPhone() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "test_" + UUID.randomUUID().toString().replace('-', '_');
        String password = UUID.randomUUID().toString();
        String email = username + "@example.com";
        CompletableFuture<Void> future = controller.createUser("John Smith", email, null, null, username, password);

        future.get(5, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testCreateUserInvalidUsername() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "test_" + UUID.randomUUID().toString().replace('-', '_');
        String password = UUID.randomUUID().toString();
        String email = username + "@example.com";
        CompletableFuture<Void> future = controller.createUser("John Smith", email, "7801234567", null, null, password);

        future.get(5, TimeUnit.SECONDS);
    }

    @Test(expected = ExecutionException.class)
    public void testCreateUserInvalidName() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "test_" + UUID.randomUUID().toString().replace('-', '_');
        String password = UUID.randomUUID().toString();
        String email = username + "@example.com";
        CompletableFuture<Void> future = controller.createUser(null, email, "7801234567", null, username, password);

        future.get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testAuthenticate() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        CompletableFuture<Void> future = controller.authenticate(username, password);

        future.get(5, TimeUnit.SECONDS);

        Assert.assertTrue(controller.isAuthenticated());
    }

    @Test(expected = ExecutionException.class)
    public void testAuthenticateBadCredentials() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "bad password";
        CompletableFuture<Void> future = controller.authenticate(username, password);

        future.get(5, TimeUnit.SECONDS);
    }

    @Test
    public void testDeauthenticate() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

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
        UserController controller = UserController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        CompletableFuture<Void> future = controller.authenticate(username, password);

        future.get(5, TimeUnit.SECONDS);

        Assert.assertTrue(controller.isAuthenticated());
        Assert.assertEquals("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1", controller.getMyId());
    }

    @Test
    public void testGetUserProfile() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        CompletableFuture<UserProfile> future = controller.getUserProfile("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1");

        UserProfile profile = future.get(5, TimeUnit.SECONDS);

        future.get(5, TimeUnit.SECONDS);

        Assert.assertEquals("0457de6b_0a85_481a_9093_c73de1ba0020", profile.getUsername());
    }

    @Test
    public void testGetMyProfile() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        CompletableFuture<Void> authFuture = controller.authenticate(username, password);

        authFuture.get(5, TimeUnit.SECONDS);

        CompletableFuture<UserProfile> profileFuture = controller.getMyProfile();

        UserProfile profile = profileFuture.get(5, TimeUnit.SECONDS);

        Assert.assertEquals("0457de6b_0a85_481a_9093_c73de1ba0020", profile.getUsername());
    }

    @Test
    public void testUpdateMyProfile() throws InterruptedException, ExecutionException, TimeoutException {
        UserController controller = UserController.getInstance();

        String username = "0457de6b_0a85_481a_9093_c73de1ba0020";
        String password = "4b5e9592-8c9e-4c37-b7d6-f5aed797e791";
        CompletableFuture<Void> authFuture = controller.authenticate(username, password);

        authFuture.get(5, TimeUnit.SECONDS);

        CompletableFuture<UserProfile> oldProfileFuture = controller.getMyProfile();

        UserProfile oldProfile = oldProfileFuture.get(5, TimeUnit.SECONDS);

        Random random = new Random();
        String phoneNumber = "780" + (random.nextInt(899) + 100) + "" + (random.nextInt(8999) + 1000);
        oldProfile.setPhone(phoneNumber);

        CompletableFuture<Void> updateProfileFuture = controller.updateMyProfile(oldProfile);

        updateProfileFuture.get(5, TimeUnit.SECONDS);

        CompletableFuture<UserProfile> newProfileFuture = controller.getMyProfile();

        UserProfile newProfile = newProfileFuture.get(5, TimeUnit.SECONDS);

        Assert.assertEquals(phoneNumber, newProfile.getPhone());
    }
}