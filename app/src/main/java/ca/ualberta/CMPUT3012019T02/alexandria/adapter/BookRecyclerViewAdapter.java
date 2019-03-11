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
import ca.ualberta.CMPUT3012019T02.alexandria.model.BookList;
import ca.ualberta.CMPUT3012019T02.alexandria.model.holder.BookViewHolder;

/**
 * Set up RecyclerView for book lists that direct to UserBookFragment
 * Code based on code from https://youtu.be/T_QfRU-A3s4 on 03/04/2019
 */
public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookViewHolder> {

    private Context mContext;
    private List<BookList> mBookList;
    private String destination;

    /**
     * Instantiates a new Book recycler view adapter.
     *
     * @param mContext    the application context
     * @param mBookList   the book list
     * @param destination the destination
     */
    public BookRecyclerViewAdapter(Context mContext,
                                   List<BookList> mBookList, String destination) {
        this.mContext = mContext;
        this.mBookList = mBookList;
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
        mViewHolder.itemBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment frag = setDestination(destination);
                frag.setArguments(dataBundler(mViewHolder));

                //switch fragments with bundled data
                FragmentTransaction fragmentTransaction =
                        ((FragmentActivity) mView.getContext()).getSupportFragmentManager()
                                .beginTransaction();

                fragmentTransaction.replace(R.id.fragment_container, frag, "UserBook");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder myViewHolder, int position) {

        myViewHolder.ivCover.setImageBitmap(mBookList.get(position).getCover());
        myViewHolder.tvTitle.setText(mBookList.get(position).getTitle());
        myViewHolder.tvAuthor.setText(mBookList.get(position).getAuthor());
        myViewHolder.tvISBN.setText(mBookList.get(position).getIsbn());

        switch (mBookList.get(position).getStatus()) {
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

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    //bundles data for fragment switching
    private Bundle dataBundler(BookViewHolder mViewHolder) {
        Bundle b = new Bundle();

        b.putParcelable("cover", mBookList.get(mViewHolder.getAdapterPosition()).getCover());
        b.putString("title", mBookList.get(mViewHolder.getAdapterPosition()).getTitle());
        b.putString("author", mBookList.get(mViewHolder.getAdapterPosition()).getAuthor());
        b.putString("isbn", mBookList.get(mViewHolder.getAdapterPosition()).getIsbn());
        b.putString("status", mBookList.get(mViewHolder.getAdapterPosition()).getStatus());
        b.putString("owner", mBookList.get(mViewHolder.getAdapterPosition()).getOwner());

        return b;
    }

    //Allows Conditional Destination to Fragments, Add to cases as needed
    private Fragment setDestination(String destination){

        switch (destination){
            case "UserBookFragment":
                UserBookFragment frag = new UserBookFragment();
                return frag;
            default:
                throw new RuntimeException("Fragment Not Defined");
        }
    }
}
