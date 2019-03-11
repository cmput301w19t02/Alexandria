package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.ChatRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.model.ChatRoomItem;

public class MessagesFragment extends Fragment {

    private List<ChatRoomItem> chatRoomList = new ArrayList<>();
    private DatabaseReference chatRoomListRef;
    private ValueEventListener chatListener;
    private ChatRecyclerViewAdapter adapter;

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
        if (chatRoomList.isEmpty()){
            Log.d("MESSAGE_FRAGMENT", "Chat Room List is empty");
        } else {
            Log.d("MESSAGE_FRAGMENT", "Chat Room List is NOT empty");
        }
        for (ChatRoomItem i : chatRoomList) {
            Log.d("MESSAGE_FRAGMENT","########## chatRoom List"+ i.getUser1Id());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, null);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.chat_recycler);
        adapter = new ChatRecyclerViewAdapter(getContext(), chatRoomList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);
        adapter.updateChatRoomList(chatRoomList);
        adapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.updateChatRoomList(chatRoomList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();

        chatRoomListRef.removeEventListener(chatListener);
    }

}
