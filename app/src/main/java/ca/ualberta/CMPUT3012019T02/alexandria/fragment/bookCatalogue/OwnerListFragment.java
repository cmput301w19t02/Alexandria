package ca.ualberta.CMPUT3012019T02.alexandria.fragment.bookCatalogue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.adapter.OwnerRecyclerViewAdapter;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.BookController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.ImageController;
import ca.ualberta.CMPUT3012019T02.alexandria.controller.UserController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.OwnerListItem;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.OwnedBook;
import ca.ualberta.CMPUT3012019T02.alexandria.model.user.UserProfile;

public class OwnerListFragment extends Fragment {

    private List<OwnerListItem> owners = new ArrayList<>();
    private String isbn;

    private Activity activity;
    private OwnerRecyclerViewAdapter userAdapter;

    private BookController bookController = BookController.getInstance();
    private UserController userController = UserController.getInstance();
    private ImageController imageController = ImageController.getInstance();

    private ChildEventListener childEventListener;
    private DatabaseReference databaseReference;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_book_catalogue_owners, null);

        Bundle bundle = getArguments();
        isbn = bundle.getString("isbn");

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                new Thread(() -> {
                    if (!isAdded()) {
                        return;
                    }
                    String ownerId = dataSnapshot.getKey();
                    try {
                        OwnedBook ownedBook = bookController.getUserOwnedBook(isbn, ownerId).get(5, TimeUnit.SECONDS).get();
                        UserProfile userProfile = userController.getUserProfile(ownerId).get(5, TimeUnit.SECONDS);
                        Bitmap profilePic = null;
                        if (userProfile.getPicture() != null) {
                            profilePic = imageController.getImage(userProfile.getPicture()).get(5, TimeUnit.SECONDS);
                        }
                        Bitmap finalProfilePic = profilePic;
                        activity.runOnUiThread(() -> {
                            OwnerListItem ownerListItem = new OwnerListItem(finalProfilePic, userProfile.getUsername(), ownerId, isbn, ownedBook.getStatus());
                            owners.add(ownerListItem);
                            Collections.sort(owners, OwnerListItem.getComparator());
                            userAdapter.notifyDataSetChanged();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String ownerId = dataSnapshot.getKey();
                for (OwnerListItem ownerListItem : owners) {
                    if (ownerListItem.getOwnerId().equals(ownerId)) {
                        owners.remove(ownerListItem);
                        userAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        databaseReference = FirebaseDatabase.getInstance().getReference("books/" + isbn + "/availableFrom");
        databaseReference.addChildEventListener(childEventListener);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.book_catalogue_recycler);
        userAdapter = new OwnerRecyclerViewAdapter(getContext(), owners);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(userAdapter);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        databaseReference.removeEventListener(childEventListener);
    }
}

