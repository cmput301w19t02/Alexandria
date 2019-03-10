package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class ChatRoomActivity extends AppCompatActivity {

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

    protected void onSendMessageClick(View view) {
        //TODO implement function
        Toast.makeText(this , "Send", Toast.LENGTH_LONG).show();
    }
}
