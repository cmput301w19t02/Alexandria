package ca.ualberta.CMPUT3012019T02.alexandria.activity.viewuserbook;

import android.os.Bundle;
import android.view.View;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class ViewBorrowedUserBookActivity extends ViewUserBookActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_borrowed_user_book);
    }

    @Override
    protected void setStatus() {
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

    public void processReturn(View view) {
        // calls scanIsbn()
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

}
