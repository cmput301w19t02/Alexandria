package ca.ualberta.CMPUT3012019T02.alexandria.fragment.myBook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ChatRoomActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ViewUserProfileActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.UserRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.cache.ObservableUserCache;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ChatController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.UserListItem;
import java9.util.concurrent.CompletableFuture;

public class MyBookUserListFragment extends Fragment {

    private List<UserListItem> requests;
    private UserRecyclerViewAdapter userAdapter;

    private BookController bookController;
    private ImageController imageController;
    private UserController userController;
    private ChatController chatController = ChatController.getInstance();
    private ObservableUserCache userCache = ObservableUserCache.getInstance();


    private String isbn;
    private final int RESULT_ISBN = 1;

    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_book_user_list, null);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.my_book_user_list_recycler);
        userAdapter =
                new UserRecyclerViewAdapter(getContext(), requests,
                        new UserRecyclerViewAdapter.UserRecyclerListener() {

                            //Sets up onClick functions for each user
                            @Override
                            public void messageClick(int position) {
                                UserListItem item = requests.get(position);
                                String userId = item.getBorrowerId();
                                String userName = item.getBorrowerUsername();
                                String chatRoomId = userCache.getChatRoomId(userId).get() ;
                                if (chatRoomId == null) {
                                    //TODO: Start spinner
                                    CompletableFuture<String> addChatRoom = chatController.addChatRoom(userController.getMyId(), userId, userName);
                                    addChatRoom.thenAccept(chatId -> {
                                        //TODO: stop spinner
                                        Intent intentChatRoom = new Intent(getContext(), ChatRoomActivity.class);
                                        intentChatRoom.putExtra("chatId", chatId);
                                        intentChatRoom.putExtra("receiverId", userId);
                                        intentChatRoom.putExtra("receiverName", userName);
                                        startActivity(intentChatRoom);
                                    });
                                } else {
                                    Intent intentChatRoom = new Intent(getContext(), ChatRoomActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("chatId", chatRoomId);
                                    bundle.putString("recieverId", userId);
                                    intentChatRoom.putExtra("bundle", bundle);
                                    startActivity(intentChatRoom);
                                }
                            }

                            @Override
                            public void userClick(int position) {
                                Intent intentViewOwner = new Intent(activity, ViewUserProfileActivity.class);
                                intentViewOwner.putExtra("USER_ID", requests.get(position).getBorrowerId());
                                startActivity(intentViewOwner);
                            }

                            @Override
                            public void ellipsesClick(View v, int position) {
                                PopupMenu popup = new PopupMenu(getContext(), v.findViewById(R.id.item_user_ellipses));
                                popup.getMenuInflater().inflate(R.menu.menu_my_book_requested, popup.getMenu());

                                popup.setOnMenuItemClickListener((MenuItem item) -> {

                                    String isbn = requests.get(position).getIsbn();
                                    String borrowerId = requests.get(position).getBorrowerId();

                                    switch (item.getItemId()) {
                                        case R.id.option_view_user:
                                            userClick(position);
                                            break;
                                        case R.id.option_accept_request:
                                            acceptRequest(isbn, borrowerId);
                                            break;
                                        case R.id.option_reject_request:
                                            declineRequest(isbn, borrowerId);
                                            break;
                                        default:
                                            throw new RuntimeException("No Button Found");
                                    }
                                    return true;
                                });
                                popup.show();
                            }
                        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setAdapter(userAdapter);

        return rootView;
    }

    //Temp list
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        isbn = arguments.getString("isbn");

        requests = new ArrayList<>();
        bookController = BookController.getInstance();
        imageController = ImageController.getInstance();
        userController = UserController.getInstance();

        bookController.getMyOwnedBook(isbn).handleAsync(((ownedBook, bookError) -> {
            if (bookError == null) {
                for (String userId : ownedBook.get().getRequestingUsers()) {
                    userController.getUserProfile(userId).handleAsync((user, userError) -> {
                        if (userError == null) {
                            if (user.getPicture() != null) {

                                imageController.getImage(user.getPicture()).handleAsync((bitmap, imageError) -> {
                                    activity.runOnUiThread(() -> {
                                        if (imageError == null) {
                                            Bitmap squareBitmap = Bitmap.createBitmap(bitmap, 0, 0, Math.min(bitmap.getWidth(), bitmap.getHeight()), Math.min(bitmap.getWidth(), bitmap.getHeight()));

                                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), squareBitmap);
                                            drawable.setCornerRadius(Math.min(bitmap.getWidth(), bitmap.getHeight()));
                                            drawable.setAntiAlias(true);

                                            requests.add(new UserListItem(drawable, user.getUsername(), isbn, userId));
                                        } else {
                                            requests.add(new UserListItem(null, user.getUsername(), isbn, userId));
                                            showError("Failed to get user image.");
                                        }
                                        userAdapter.notifyDataSetChanged();
                                    });
                                    return null;
                                });
                            } else {
                                requests.add(new UserListItem(null, user.getUsername(), isbn, userId));
                            }

                        } else {
                            showError("Failed to get user information.");
                        }
                        return null;
                    });
                }
            } else {
                showError("Failed to get users requesting.");
            }
            return null;
        }));

    }

    /**
     * Shows an error message in toast
     *
     * @param message error message
     */
    private void showError(String message) {
        Toast.makeText(getView().getContext(), "Error: " + message, Toast.LENGTH_LONG).show();
    }

    // TODO: call test with MyBookFragment.onStatusChange()
    // Switch status to accepted, and refresh the MyBookFragment
    private void acceptRequest(String isbn, String borrowerId) {
        bookController.acceptRequest(isbn, borrowerId).handleAsync((aVoid, throwable) -> {
            activity.runOnUiThread(() -> {
                if (throwable == null) {

                    // Successfully accepted the request
                    // TODO: MyBookFragment.onStatusChange()

                } else {
                    throwable.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Unable to accept request. Please try again later.");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            return null;
        });
    }

    // Remove the user from list
    private void declineRequest(String isbn, String borrowerId) {
        bookController.declineRequest(isbn, borrowerId).handleAsync((aVoid, throwable) -> {
            activity.runOnUiThread(() -> {
                if (throwable == null) {

                    // Successfully declined the request
                    // TODO: MyBookFragment.onStatusChange()

                } else {
                    throwable.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Unable to decline request. Please try again later.");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            return null;
        });
    }
}
