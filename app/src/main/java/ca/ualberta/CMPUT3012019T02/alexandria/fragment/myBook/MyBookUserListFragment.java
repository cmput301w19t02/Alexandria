package ca.ualberta.CMPUT3012019T02.alexandria.fragment.myBook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ChatRoomActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.activity.ViewUserProfileActivity;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.UserRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.cache.ObservableUserCache;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ChatController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.UserListItem;
import java9.util.concurrent.CompletableFuture;

public class MyBookUserListFragment extends Fragment {

    private List<UserListItem> requests;
    private UserController userController = UserController.getInstance();
    private ChatController chatController = ChatController.getInstance();
    private ObservableUserCache userCache = ObservableUserCache.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_book_user_list,null);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.my_book_user_list_recycler);
        UserRecyclerViewAdapter userAdapter =
                new UserRecyclerViewAdapter(getContext(),requests,
                        new UserRecyclerViewAdapter.UserRecyclerListener() {

            //Sets up onClick functions for each user
            @Override
            public void messageClick(int position) {
                UserListItem item = requests.get(position);
                String userId = item.getBorrowerId();
                String userName = item.getBorrowerUsername();
                String chatRoomId = userCache.getChatRoomId(userId);
                if (chatRoomId == null) {
                    //TODO: Start spinner
                    CompletableFuture<Void> addChatRoom = chatController.addChatRoom(userController.getMyId(), userId, userName);
                    addChatRoom.thenRun(()-> {
                        //TODO: stop spinner
                        Intent intentChatRoom = new Intent(getContext(), ChatRoomActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("chatId", chatRoomId);
                        bundle.putString("recieverId", userId);
                        addChatRoom.complete(null);
                        intentChatRoom.putExtra("bundle", bundle);
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
                Intent intentViewOwner = new Intent(getActivity(), ViewUserProfileActivity.class);
                intentViewOwner.putExtra("USER_ID", requests.get(position).getBorrowerId());
                startActivity(intentViewOwner);
            }

            @Override
            public void ellipsesClick(View v, int position) {
                PopupMenu popup = new PopupMenu(getContext(), v.findViewById(R.id.item_user_ellipses));
                popup.getMenuInflater().inflate(R.menu.menu_my_book_requested, popup.getMenu());

                popup.setOnMenuItemClickListener((MenuItem item) -> {
                    switch (item.getItemId()){
                        case R.id.option_view_user: userClick(position); break;
                        case R.id.option_accept_request: acceptRequest(); break;
                        case R.id.option_reject_request: rejectRequest(); break;
                        default: throw new RuntimeException("No Button Found");
                    }
                    return true;
                });
                popup.show();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(userAdapter);

        return rootView;
    }

    //Temp list
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO set up proper back end
        requests = new ArrayList<>();
        Bitmap aBitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);

        requests.add(new UserListItem(aBitmap,"Fake User 1","fake isbn", "fake borrowerId"));
        requests.add(new UserListItem(aBitmap,"Fake User 2","fake isbn", "fake borrowerId"));
        requests.add(new UserListItem(aBitmap,"Fake User 3","fake isbn", "fake borrowerId"));
        requests.add(new UserListItem(aBitmap,"Fake User 4","fake isbn", "fake borrowerId"));

    }

    //TODO implement with backend, call test with MyBookFragment.onStatusChange()
    //Switch status to accepted, and refresh the MyBookFragment
    private void acceptRequest() {

    }

    //TODO implement with backend
    //Remove the user from list
    private void rejectRequest() {

    }
}
