package ca.ualberta.CMPUT3012019T02.alexandria.activity.viewuserbook;

import android.os.Bundle;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class ViewBorrowedUserBook extends ViewUserBook {

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

    public void processReturn() {
        // calls scanIsbn()
        // TODO: Finish implementation
        throw new UnsupportedOperationException();
    }

}
