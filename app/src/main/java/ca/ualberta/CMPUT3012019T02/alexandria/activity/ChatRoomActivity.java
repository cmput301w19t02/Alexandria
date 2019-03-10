package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.connection.util.StringListReader;

import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.MessageRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.Message;
import ca.ualberta.CMPUT3012019T02.alexandria.model.message.TextMessage;

public class ChatRoomActivity extends AppCompatActivity {

    private DatabaseReference messagesRef;
    private ValueEventListener messagesListener;

    private List<Message> messageList;
    private String chatId;
    private String recieverId;
    private String senderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


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
        // TODO: get data from Firebase and set onClickListener
        // messages
        messagesRef = FirebaseDatabase.getInstance().getReference().child("chatMessages").child(chatId);
        messagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        Message message = childSnapshot.getValue(Message.class);
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

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        chatId = bundle.getString("chatId");
        recieverId = bundle.getString("recieverId");
        senderId = UserController.getInstance().getMyId();

        ImageView sendButton = (ImageView)findViewById(R.id.image_send);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText)findViewById(R.id.edit_message);
                String inputText = input.getText().toString();
                onSendMessageClick(inputText, senderId, messagesRef);
                input.setText("");
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        // TODO: set adapter for list view, fill with data from Firebase listeners
        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.message_recycler);
        MessageRecyclerViewAdapter adapter = new MessageRecyclerViewAdapter(this, messageList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
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

    protected void onAddPhotoClick(View view) {
        //TODO implement function
        Toast.makeText(this , "Add Photo", Toast.LENGTH_LONG).show();
    }

    protected void onAddLocationClick(View view) {
        //TODO implement function
        Toast.makeText(this , "Add Location", Toast.LENGTH_LONG).show();
    }

    protected void onSendMessageClick(String inputText, String senderId, DatabaseReference ref) {
        TextMessage message = new TextMessage(inputText, "unread", "", senderId);
        ref.push().setValue(message);
    }
}
