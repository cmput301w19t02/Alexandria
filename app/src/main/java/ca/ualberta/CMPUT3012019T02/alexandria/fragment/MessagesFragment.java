package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.os.Bundle;
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
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.ChatRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.model.ChatRoomItem;

public class MessagesFragment extends Fragment {

    private List<ChatRoomItem> chatRoomList = new ArrayList<ChatRoomItem>();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //TODO: add data to chatRooms list
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users")
                                                .child(UserController.getInstance().getMyId());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    ChatRoomItem chatRoom = childSnapshot.getValue(ChatRoomItem);
                    chatRoomList.add()
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw new RuntimeException("Could not load data from database " + databaseError);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, null);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.chat_recycler);
        ChatRecyclerViewAdapter chatAdapter = new ChatRecyclerViewAdapter(getContext(), chatRoomList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(chatAdapter);

        return rootView;
    }

}
