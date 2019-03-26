package ca.ualberta.CMPUT3012019T02.alexandria.ModelClasses.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * Sets up item ID for RecyclerViews
 */

public class BookViewHolder extends RecyclerView.ViewHolder{

    public RelativeLayout itemBook;
    public ImageView ivCover;
    public TextView tvTitle;
    public TextView tvAuthor;
    public TextView tvISBN;
    public ImageView ivStatus;

    /**
     * Instantiates a new Book view holder.
     *
     * @param itemView the list view for the books
     */
    public BookViewHolder(@NonNull View itemView) {
        super(itemView);

        itemBook = itemView.findViewById(R.id.item_book);
        ivCover = itemView.findViewById(R.id.item_book_cover);
        tvTitle = itemView.findViewById(R.id.item_book_title);
        tvAuthor = itemView.findViewById(R.id.item_book_author);
        tvISBN = itemView.findViewById(R.id.item_book_isbn);
        ivStatus = itemView.findViewById(R.id.item_book_status);
    }
}
