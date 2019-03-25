package ca.ualberta.CMPUT3012019T02.alexandria.fragment.myBook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class MyBookAvailableFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_book_available,null);
    }
}
