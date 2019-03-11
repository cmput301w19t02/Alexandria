package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.ChatRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.model.chatroom.ChatRoomItem;

/**
 * Fragment that shows a list of clickable chat rooms to chat with another user.
 * Implements Chat
 */
public class MessagesFragment extends Fragment {

    private List<ChatRoomItem> chatRoomList = new ArrayList<>();
    private DatabaseReference chatRoomListRef;
    private ValueEventListener chatListener;
    private ChatRecyclerViewAdapter adapter;

    /**
     * set data listener on fragment creation
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //myRef = FirebaseDatabase.getInstance().getReference().child("users").child(UserController.getInstance().getMyId());
        // TODO remove when getMyId is working
        chatRoomListRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child("eQgZfhN2Yng9TPHcXvfBZs5ZKxj1").child("chatRoomList");

        // adding current users' chatRooms to chatRoomList
        chatListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        ChatRoomItem chatRoom = childSnapshot.getValue(ChatRoomItem.class);
                        chatRoomList.add(chatRoom);
                    }
                } else {
                    chatRoomListRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw new RuntimeException("Could not load data from database " + databaseError);
            }
        };
        chatRoomListRef.addValueEventListener(chatListener);
    }

    /**
     * Assigns ChatRecyclerViewAdapter to the chat recycler view, and creates the view
     *
     * {@inheritDoc}
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view of each chat room item in recycler view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, null);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.chat_recycler);
        adapter = new ChatRecyclerViewAdapter(getContext(), chatRoomList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }

    /**
     * refreshes the chat room list every 2 seconds, with a .5 second delay on fragment start.
     * {@inheritDoc}
     */
    @Override
    public void onStart(){
        super.onStart();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.updateChatRoomList(chatRoomList);
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 2000);
            }
        }, 500);
        //adapter.updateChatRoomList(chatRoomList);
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();

        chatRoomListRef.removeEventListener(chatListener);
    }

}
