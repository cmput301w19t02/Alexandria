package ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.user;

import android.support.annotation.Nullable;

abstract class MyBook extends UserBook {
    public MyBook(String isbn, String status, String owner) {
        super(isbn, status, owner);
    }

    @Nullable
    public abstract String getCoverId();
}
