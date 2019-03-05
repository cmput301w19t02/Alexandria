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

public class ViewUserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.view_profile_toolbar);
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
        inflater.inflate(R.menu.view_user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.message_user_option:
                // edit activity
                Toast.makeText(this , "Open messages", Toast.LENGTH_LONG).show();
                break;
            case R.id.block_user_option:
                Toast.makeText(this , "Block user implement", Toast.LENGTH_LONG).show();

                AlertDialog.Builder blockAlert = new AlertDialog.Builder(ViewUserProfileActivity.this);

                blockAlert.setCancelable(true);
                blockAlert.setTitle("Block User?");
                blockAlert.setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna wirl aliqua. " +
                        "Up exlaborum incididunt quis nostrud exercitatn.");

                blockAlert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                Context context = this;
                blockAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // block user
                        Toast.makeText(context , "User Blocked", Toast.LENGTH_LONG).show();
                    }
                });
                blockAlert.show();

                break;
            default:
                // unknown error
        }
        return super.onOptionsItemSelected(item);
    }
}
