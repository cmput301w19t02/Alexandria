package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * Sets up item ID for RecyclerViews
 * Says that access can be private, even though it will break RecyclerViewAdapters
 */


public class BookViewHolder extends RecyclerView.ViewHolder{

    public LinearLayout itemBook;
    public ImageView ivCover;
    public TextView tvTitle;
    public TextView tvAuthor;
    public TextView tvISBN;
    public ImageView ivStatus;

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);

        itemBook = itemView.findViewById(R.id.item_book);
        ivCover = itemView.findViewById(R.id.book_cover);
        tvTitle = itemView.findViewById(R.id.book_title);
        tvAuthor = itemView.findViewById(R.id.book_author);
        tvISBN = itemView.findViewById(R.id.book_isbn);
        ivStatus = itemView.findViewById(R.id.book_status);
    }
}
