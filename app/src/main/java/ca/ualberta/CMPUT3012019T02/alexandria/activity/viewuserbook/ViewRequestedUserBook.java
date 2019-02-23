package ca.ualberta.CMPUT3012019T02.alexandria.activity.viewuserbook;

import android.os.Bundle;
import android.view.View;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class ViewRequestedUserBook extends ViewUserBook {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requested_user_book);
    }

    @Override
    protected void setStatus() {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    public void cancelRequest(View view) {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

}
