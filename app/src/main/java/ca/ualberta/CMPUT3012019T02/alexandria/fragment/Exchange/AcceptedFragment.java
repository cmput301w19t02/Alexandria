package ca.ualberta.CMPUT3012019T02.alexandria.fragment.Exchange;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * Fragment for filtering book list that has the status of Accepted
 */

public class AcceptedFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange_accepted,null);
    }
}
