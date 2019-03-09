package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.model.ChatRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.model.ChatRoom;

public class MessagesFragment extends Fragment {

    private FirebaseDatabase database;
    private List<ChatRoomList> chatRoomList = new ArrayList<ChatRoomList>();


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //TODO: add data to chatRooms list
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
