package ca.ualberta.CMPUT3012019T02.alexandria.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.ualberta.CMPUT3012019T02.alexandria.R;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ChatRoomList> mChatRoomList;

    public ChatRecyclerViewAdapter(Context mContext, List<ChatRoomList> mChatRoomList) {
        this.mContext = mContext;
        this.mChatRoomList = mChatRoomList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView;

        mView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_chat_list, viewGroup, false);
        MyViewHolder mViewHolder = new MyViewHolder(mView);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookRecyclerViewAdapter.MyViewHolder myViewHolder, int position) {
        //TODO: replace with chat_item ids
        /**
        myViewHolder.ivCover.setImageBitmap(mBookList.get(position).getCover());
        myViewHolder.tvTitle.setText(mBookList.get(position).getTitle());
        myViewHolder.tvAuthor.setText(mBookList.get(position).getAuthor());
        myViewHolder.tvISBN.setText(mBookList.get(position).getIsbn());

        switch (mChatRoomList.get(position).getStatus()) {
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
         */

    }

    @Override
    public int getItemCount() {
        return mChatRoomList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        /**
        private ImageView ivCover;
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvISBN;
        private ImageView ivStatus;
        */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //TODO: replace these with chat_item ids
            /**
            ivCover = itemView.findViewById(R.id.book_cover);
            tvTitle = itemView.findViewById(R.id.book_title);
            tvAuthor = itemView.findViewById(R.id.book_author);
            tvISBN = itemView.findViewById(R.id.book_isbn);
            ivStatus = itemView.findViewById(R.id.book_status);
             */

        }
    }
}
