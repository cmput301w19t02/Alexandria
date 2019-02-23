package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_exchange:
                    mTextMessage.setText(R.string.nav_exchange);
                    return true;
                case R.id.nav_library:
                    mTextMessage.setText(R.string.nav_library);
                    return true;
                case R.id.nav_messages:
                    mTextMessage.setText(R.string.nav_messages);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        if (id == R.id.user_profile) {
            // open profile
            Intent startOpenMyProfile = new Intent(this, MyProfile.class);
            startActivity(startOpenMyProfile);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
