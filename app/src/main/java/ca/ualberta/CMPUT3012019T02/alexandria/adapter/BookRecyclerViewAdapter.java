package ca.ualberta.CMPUT3012019T02.alexandria.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.fragment.UserBookFragment;
import ca.ualberta.CMPUT3012019T02.alexandria.fragment.mybook.MyBookFragment;
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookListItem;
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.BookViewHolder;

/**
 * Set up RecyclerView for book lists
 * Code based on code from https://youtu.be/T_QfRU-A3s4 on 03/04/2019
 */
public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookViewHolder> {

    private Context mContext;
    private List<BookListItem> mBookListItem;
    private String destination;

    /**
     * Instantiates a new Book recycler view adapter.
     *
     * @param mContext    the application context
     * @param mBookListItem   the book list
     * @param destination the destination
     */
    public BookRecyclerViewAdapter(Context mContext,
                                   List<BookListItem> mBookListItem, String destination) {
        this.mContext = mContext;
        this.mBookListItem = mBookListItem;
        this.destination = destination;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView;

        //sets up view
        mView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_book, viewGroup, false);
        BookViewHolder mViewHolder = new BookViewHolder(mView);

        //list click to switch fragments
        mViewHolder.itemBook.setOnClickListener((View v) -> {
            Fragment frag = setDestination(destination);
            frag.setArguments(dataBundler(mViewHolder));

            //switch fragments with bundled data
            FragmentTransaction fragmentTransaction =
                    ((FragmentActivity) mView.getContext()).getSupportFragmentManager()
                            .beginTransaction();

            fragmentTransaction.replace(R.id.fragment_container, frag, "UserBook");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder myViewHolder, int position) {

        myViewHolder.ivCover.setImageBitmap(mBookListItem.get(position).getCover());
        myViewHolder.tvTitle.setText(mBookListItem.get(position).getTitle());
        myViewHolder.tvAuthor.setText(mBookListItem.get(position).getAuthor());
        myViewHolder.tvISBN.setText(mBookListItem.get(position).getIsbn());

        if (mBookListItem.get(position).getStatus() != null) {
            switch (mBookListItem.get(position).getStatus()) {
                case "available":
                    myViewHolder.ivStatus.setImageResource(R.drawable.ic_status_available);
                    break;
                case "requested":
                    myViewHolder.ivStatus.setImageResource(R.drawable.ic_status_requested);
                    break;
                case "accepted":
                    myViewHolder.ivStatus.setImageResource(R.drawable.ic_status_accepted);
                    break;
                case "borrowed":
                    myViewHolder.ivStatus.setImageResource(R.drawable.ic_status_borrowed);
                    break;
                default:
                    throw new RuntimeException("Status out of bounds");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mBookListItem.size();
    }

    //bundles data for fragment switching
    private Bundle dataBundler(BookViewHolder mViewHolder) {
        Bundle b = new Bundle();

        b.putParcelable("cover", mBookListItem.get(mViewHolder.getAdapterPosition()).getCover());
        b.putString("title", mBookListItem.get(mViewHolder.getAdapterPosition()).getTitle());
        b.putString("author", mBookListItem.get(mViewHolder.getAdapterPosition()).getAuthor());
        b.putString("isbn", mBookListItem.get(mViewHolder.getAdapterPosition()).getIsbn());
        if (mBookListItem.get(mViewHolder.getAdapterPosition()).getStatus() != null) {
            b.putString("status", mBookListItem.get(mViewHolder.getAdapterPosition()).getStatus());
        }
        b.putString("ownerId", mBookListItem.get(mViewHolder.getAdapterPosition()).getOwnerId());

        return b;
    }

    public void setmBookListItem(List<BookListItem> bookListItem) {
        this.mBookListItem = bookListItem;
    }

    //Allows Conditional Destination to Fragments, Add to cases as needed
    private Fragment setDestination(String destination){

        switch (destination){
            case "UserBookFragment":
                return new UserBookFragment();
            case "MyBookFragment":
                return new MyBookFragment();
            default:
                throw new RuntimeException("Fragment Not Defined");
        }
    }
}
