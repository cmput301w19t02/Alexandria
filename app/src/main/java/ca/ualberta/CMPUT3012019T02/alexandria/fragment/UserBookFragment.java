package ca.ualberta.CMPUT3012019T02.alexandria.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * Implements UserBookRecyclerView
 */

/*TODO Finish Implementing class and the XML
 *might change name in further development depending on how transaction windows are built
*/
public class UserBookFragment extends Fragment {

    private Bitmap cover;
    private String title;
    private String author;
    private String isbn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_book,null);
        extractData();
        setTextViews(rootView);

        return rootView;
    }


    private void extractData() {
        Bundle arguments = getArguments();

        cover = arguments.getParcelable("cover");
        title = arguments.getString("title");
        author = arguments.getString("author");
        isbn = arguments.getString("isbn");
    }

    public void setTextViews(View v) {
        TextView tvTitle = v.findViewById(R.id.user_book_title);
        TextView tvAuthor= v.findViewById(R.id.user_book_author);
        TextView tvIsbn = v.findViewById(R.id.user_book_isbn);

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvIsbn.setText(isbn);
    }
}
