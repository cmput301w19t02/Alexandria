package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.android.gms.common.util.IOUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import ca.ualberta.CMPUT3012019T02.alexandria.App;
import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import java9.util.concurrent.CompletableFuture;

/**
 * The controller for Search operations
 */
public class SearchController {

    private Index index;
    private Gson gson;
    private static SearchController instance;
    private final String GOOGLE_BOOK_URL = "https://www.googleapis.com/books/v1/volumes?&maxResults=1&projection=lite&q=";
    private final String booksApiKey = App.getContext().getResources().getString(R.string.google_books_api_key);
    private static UserController userController = UserController.getInstance();

    private SearchController() {
        Client client = new Client("9ETLQT0YZC", App.getContext().getResources().getString(R.string.algolia_api_key));
        index = client.getIndex("search");
        try {
            index.setSettingsAsync(new JSONObject().put(
                    "searchableAttributes", new JSONArray()
                            .put("title, author")
                            .put("isbn")
                    ),
                    null);
        } catch (JSONException e) {
            System.out.println("Error: JSON object not created: " + e);
        }

        gson = new GsonBuilder().create();
    }

    /**
     * Gets the singleton instance of UserController
     *
     * @return the UserController instance
     */
    public static SearchController getInstance() {
        if (instance == null) {
            instance = new SearchController();
        }
        return instance;
    }

    /**
     * Finds search results given a search string
     *
     * @param search the string being searched
     * @return the search results
     */
    public CompletableFuture<ArrayList<Book>> searchBooks(String search){
        final CompletableFuture<ArrayList<Book>> resultFuture = new CompletableFuture<>();

        index.searchAsync(new Query(search),
                (content, error) -> {
                    ArrayList<Book> books = new ArrayList<>();
                    if (error == null) {
                        try {
                            JSONArray jsonArray;
                            jsonArray = content.getJSONArray("hits");
                            for (int i = 0; i < jsonArray.length(); i++){
                                Book book = gson.fromJson(jsonArray.getString(i),Book.class);
                                // && !(book.getAvailableOwners())
                                boolean isOwner = book.getAvailableOwners().contains(userController.getMyId());

                                if (!(book.getAvailableOwners() == null)
                                        && !(book.getAvailableOwners().size() == 0)
                                        && !(isOwner && book.getAvailableOwners().size() == 1)) {
                                    books.add(book);
                                }
                            }
                            resultFuture.complete(books);
                        } catch (JSONException e) {
                            resultFuture.completeExceptionally(e);
                        }
                    }
                    else {
                        resultFuture.completeExceptionally(error);
                    }
                });

        return resultFuture;
    }

    /**
     * Compares two isbns
     *
     * @param isbn1 the isbn 1
     * @param isbn2 the isbn 2
     * @return the completable future with result
     */
    public CompletableFuture<Boolean> compareIsbn(String isbn1, String isbn2) {
        final CompletableFuture<Boolean> compareFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                String searchUrl1 = GOOGLE_BOOK_URL + isbn1 + "&key=" + booksApiKey;
                String searchUrl2 = GOOGLE_BOOK_URL + isbn2 + "&key=" + booksApiKey;

                Boolean equalIsbn = false;

                try {
                    URL url1 = new URL(searchUrl1);
                    URL url2 = new URL(searchUrl2);

                    HttpURLConnection bookConnection1 = (HttpURLConnection) url1.openConnection();
                    HttpURLConnection bookConnection2 = (HttpURLConnection) url2.openConnection();

                    try {
                        InputStream in1 = bookConnection1.getInputStream();
                        InputStream in2 = bookConnection2.getInputStream();

                        if (in1== in2) equalIsbn = true;

//                   InputStreamReader bookInput1 = new InputStreamReader(in1);
//                   InputStreamReader bookInput2 = new InputStreamReader(in2);
//
//                   BufferedReader bookReader1 = new BufferedReader(bookInput1);
//                   BufferedReader bookReader2 = new BufferedReader(bookInput2);

                    } catch (Exception e) {
                        e.printStackTrace();
                        compareFuture.completeExceptionally(e);
                    } finally {
                        bookConnection1.disconnect();
                        bookConnection2.disconnect();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    compareFuture.completeExceptionally(new IOException("Failed to create URL object"));
                } catch (IOException e) {
                    e.printStackTrace();
                    compareFuture.completeExceptionally(new IOException("Failed to create URL connection"));
                }

                compareFuture.complete(equalIsbn);
            }
        });

        return compareFuture;
    }

    /**
     * Search isbn
     *
     * @param isbn the isbn
     * @return the completable future with book information
     */
    public CompletableFuture<Book> searchIsbn(String isbn) {
        final CompletableFuture<Book> future = new CompletableFuture<>();

        // information retrieved from https://code.tutsplus.com/tutorials/android-sdk-create-a-book-scanning-app-interface-book-search--mobile-17790
        new Thread(() -> {

            String searchUrl = GOOGLE_BOOK_URL + isbn + "&key=" + booksApiKey;
            StringBuilder stringBuilder = new StringBuilder();

            try {

                URL url = new URL(searchUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                try (
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                ) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                } finally {
                    httpURLConnection.disconnect();
                }

                JSONObject volumeInfo = new JSONObject(stringBuilder.toString()).getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo");
                String title = "N/A", author = "N/A", thumbnail = null, imageId = null;
                try {
                    title = volumeInfo.getString("title");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    author = volumeInfo.getJSONArray("authors").getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    thumbnail = volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (thumbnail != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(new URL(thumbnail).openConnection().getInputStream());
                    if (bitmap != null) {
                        imageId = ImageController.getInstance().addImage(bitmap).get(5, TimeUnit.SECONDS);
                    }
                }

                Book book = new Book(isbn, title, author, null, imageId);
                future.complete(book);

            } catch (Exception e) {
                e.printStackTrace();
                future.completeExceptionally(e);
            }

        }).start();

        return future;
    }

}
