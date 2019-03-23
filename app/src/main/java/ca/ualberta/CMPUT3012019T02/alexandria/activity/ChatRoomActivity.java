package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private DatabaseReference messagesRef;
    private ValueEventListener messagesListener;

    private List<Message> messageList = new ArrayList<>();
    private String chatId;
    private String recieverId;
    private String senderId;
    private String recieverName;
    private Bitmap recieverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        chatId = intent.getStringExtra("chatId");
        recieverId = intent.getStringExtra("recieverId");
        recieverName = intent.getStringExtra("recieverName");
        senderId = UserController.getInstance().getMyId();

        //byte[] byteArray = intent.getByteArrayExtra("image");
        //recieverImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        TextView recieverUserName = (TextView)findViewById(R.id.reciever_username);
        recieverUserName.setText(recieverName);

        //ImageView recieverImageIcon = (ImageView)findViewById(R.id.reciever_image);
        //recieverImage.setImageBitmap();

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.ChatRoom_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // messages

        messagesRef = FirebaseDatabase.getInstance().getReference().child("chatMessages").child(chatId);
        messagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
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


        ImageView sendButton = (ImageView)findViewById(R.id.image_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText)findViewById(R.id.edit_message);
                String inputText = input.getText().toString();
                onSendMessageClick(inputText, senderId);
                input.setText("");
            }
        });

        ImageView locationButton = (ImageView)findViewById(R.id.image_location);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddLocationClick();
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();

        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.message_recycler);
        MessageRecyclerViewAdapter adapter = new MessageRecyclerViewAdapter(this, messageList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.updateMessageList(messageList);
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 2000);
            }
        }, 2000);

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
                startActivity(startViewProfile);
                break;
            case R.id.block_user:
                // Block User
                //TODO implement block user

                AlertDialog.Builder blockAlert = new AlertDialog.Builder(ChatRoomActivity.this, R.style.AlertDialogTheme);

                blockAlert.setCancelable(true);
                blockAlert.setTitle("Block User?");
                String blockMessage = "Are you sure you want to block " +  "Joe123 Example";
                blockAlert.setMessage(blockMessage);

                blockAlert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                Context context = this;
                blockAlert.setPositiveButton("BLOCK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // block user
                        Toast.makeText(context , "User Blocked", Toast.LENGTH_LONG).show();
                    }
                });
                blockAlert.show();
                break;
            case R.id.messaging_setting:
                // open menu
                break;
            default:
                throw new RuntimeException("Unknown option");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On add image message to database.
     *
     * @param view the view
     */
    public void onAddPhotoClick(View view) {
        //TODO implement function
        Toast.makeText(this , "Add Photo", Toast.LENGTH_LONG).show();
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
        // TODO move this to chat controller, replace with chat controller methods
        Date date = new Date();
        TextMessage message = new TextMessage(inputText, "unread", date.getTime(), senderId);
        chatController.addTextMessage(chatId, message);
        chatController.setUserChatRoomReadStatus(chatId, recieverId, false);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent returnIntent) {
        if (requestCode == MEET_LOCATION_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                double lat = returnIntent.getExtras().getDouble("lat");
                double lng = returnIntent.getExtras().getDouble("lng");

                Location location = new Location(lat, lng);
                Date date = new Date();
                LocationMessage message = new LocationMessage(location, "unread", date.getTime(), senderId);

                chatController.addLocationMessage(chatId, message);
                chatController.setUserChatRoomReadStatus(chatId, recieverId, false);
                //TODO: transaction location
            }
            if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this , "No Location Added", Toast.LENGTH_LONG).show();
            }
        }
    }
}
