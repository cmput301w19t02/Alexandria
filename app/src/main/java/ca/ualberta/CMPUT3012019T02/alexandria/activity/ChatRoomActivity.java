package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ChatController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.NotificationController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.MessageRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Location;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.LocationMessage;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.TextMessage;

/**
 * This activity is solely for user to user messaging.
 */
public class ChatRoomActivity extends AppCompatActivity {

    static final int MEET_LOCATION_REQUEST = 1;

    private ChatController chatController = ChatController.getInstance();
    private UserController userController = UserController.getInstance();
    private NotificationController notificationController = NotificationController.getInstance();

    private DatabaseReference messagesRef;
    private ValueEventListener messagesListener;

    private List<Message> messageList = new ArrayList<>();
    private String chatId;
    private String receiverId;
    private String senderId;
    private String receiverName;
    private MessageRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        chatId = intent.getStringExtra("chatId");
        receiverId = intent.getStringExtra("receiverId");
        receiverName = intent.getStringExtra("receiverName");
        senderId = UserController.getInstance().getMyId();

        TextView receiverUserName = findViewById(R.id.receiver_username);
        receiverUserName.setText(receiverName);

        // toolbar
        Toolbar toolbar = findViewById(R.id.ChatRoom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener((View v) -> finish());

        //edit text focus
        EditText editText = findViewById(R.id.edit_message);
        editText.requestFocus();

        // messages
        messagesRef = FirebaseDatabase.getInstance().getReference().child("chatMessages").child(chatId);
        messagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    messageList.clear();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        TextMessage message = childSnapshot.getValue(TextMessage.class);
                        messageList.add(message);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw new RuntimeException("Could not load data from database " + databaseError);
            }
        };
        messagesRef.addValueEventListener(messagesListener);


        ImageView sendButton = findViewById(R.id.image_send);
        sendButton.setOnClickListener((View v) -> {
            EditText input = findViewById(R.id.edit_message);
            String inputText = input.getText().toString();
            if (!inputText.matches("")) {
                onSendMessageClick(inputText, senderId);
                input.setText("");
            }
        });

        ImageView locationButton = findViewById(R.id.image_location);
        locationButton.setOnClickListener((View v) -> onAddLocationClick());
    }

    @Override
    public void onResume(){
        super.onResume();

        RecyclerView mRecyclerView = findViewById(R.id.message_recycler);
        adapter = new MessageRecyclerViewAdapter(this, messageList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        //runs once to get scroll to bottom
        final Handler onceHandler = new Handler();
        onceHandler.postDelayed(() -> {
            adapter.updateMessageList(messageList);
            adapter.notifyDataSetChanged();
            if (messageList.size() != 0) {
                mRecyclerView.scrollToPosition(messageList.size()-1);
            }
        }, 500);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.updateMessageList(messageList);
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 500);
            }
        }, 500);
    }

    @Override
    public void onStop(){
        super.onStop();
        messagesRef.removeEventListener(messagesListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Block the user you are messaging.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.view_profile:
                Intent startViewProfile = new Intent(this, ViewUserProfileActivity.class);
                startViewProfile.putExtra("USER_ID",receiverId);
                startActivity(startViewProfile);
            case R.id.messaging_setting:
                // open menu
                break;
            default:
                throw new RuntimeException("Unknown option");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On add location message to database.
     *
     */
    public void onAddLocationClick() {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, MEET_LOCATION_REQUEST);
    }


    /**
     * On send message click.
     *
     * @param inputText the input text
     * @param senderId  the sender id
     */
    public void onSendMessageClick(String inputText, String senderId) {
        Date date = new Date();
        TextMessage message = new TextMessage(inputText, "unread", date.getTime(), senderId);
        chatController.addTextMessage(chatId, message);
        chatController.setUserChatRoomReadStatus(chatId, receiverId, false);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent returnIntent) {
        if (requestCode == MEET_LOCATION_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                double lat = returnIntent.getExtras().getDouble("lat");
                double lng = returnIntent.getExtras().getDouble("lng");
                String imageId = returnIntent.getStringExtra("imageId");
                Location location = new Location(lat, lng);
                Date date = new Date();
                LocationMessage message = new LocationMessage(location, "unread",
                        date.getTime(), senderId, imageId);

                //setting content for RecyclerAdapter use
                String content = imageId + "," + lat + "," + lng;
                message.setContent(content);

                chatController.addLocationMessage(chatId, message);
                chatController.setUserChatRoomReadStatus(chatId, receiverId, false);
                messageList.add(message);
                adapter.updateMessageList(messageList);
                adapter.notifyDataSetChanged();

                userController.getMyProfile().handleAsync((result, error) -> {
                    if (error == null) {
                        notificationController.sendNotification(receiverId,
                                "New Meet up Location from " + result.getUsername(),
                                "Go to your messages to see the new place to meet");
                    } else {
                        //handle error
                    }
                    return null;
                });
            }
            if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this , "No Location Added", Toast.LENGTH_LONG).show();
            }
        }
    }
}
