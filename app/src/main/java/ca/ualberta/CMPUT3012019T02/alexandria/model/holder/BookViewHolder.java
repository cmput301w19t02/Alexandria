package ca.ualberta.CMPUT3012019T02.alexandria.model.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

/**
 * Sets up item ID for RecyclerViews
 * Says that access can be private, even though it will break RecyclerViewAdapters
 */

public class BookViewHolder extends RecyclerView.ViewHolder{

    /**
     * The Item book.
     */
    public RelativeLayout itemBook;
    /**
     * The ImageView cover.
     */
    public ImageView ivCover;
    /**
     * The TextView title.
     */
    public TextView tvTitle;
    /**
     * The TextView author.
     */
    public TextView tvAuthor;
    /**
     * The TextView isbn.
     */
    public TextView tvISBN;
    /**
     * The ImageView status.
     */
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
