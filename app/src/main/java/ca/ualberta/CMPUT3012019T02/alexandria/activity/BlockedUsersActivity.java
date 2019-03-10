package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class BlockedUsersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.blocked_users_toolbar);
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

    //TODO remove when there is a straight pass to view user
    protected void onJoeTestClick(View view) {
        Intent startProfileActivity = new Intent(this, ChatRoomActivity.class);
        startActivity(startProfileActivity);
    }
}
