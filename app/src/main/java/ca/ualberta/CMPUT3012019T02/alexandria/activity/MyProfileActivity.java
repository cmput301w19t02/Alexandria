package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class MyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.edit_profile:
                // edit activity
                Intent startEditProfile = new Intent(this, EditMyProfileActivity.class);
                startActivity(startEditProfile);
                break;
            case R.id.blocked_users:
                // black list activity
                Intent startBlockedUsers = new Intent(this, BlockedUsersActivity.class);
                startActivity(startBlockedUsers);
                break;
            case android.R.id.home:
                // go back
                finish();
            default:
                // unknown error
        }

        return super.onOptionsItemSelected(item);
    }

    public void setInfo() {
        //TODO Implement
        throw new UnsupportedOperationException("Not implemented");
    }
}
