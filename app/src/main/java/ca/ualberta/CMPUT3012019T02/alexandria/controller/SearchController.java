package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.os.AsyncTask;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import java9.util.concurrent.CompletableFuture;

import java.net.HttpURLConnection;

/**
 * The type Search controller.
 */
public class SearchController {

    private Client client;
    private Index index;
    private Gson gson;
    private static SearchController instance;
    private final String GOOGLE_BOOK_URL = "https://www.googleapis.com/books/v1/volumes?&maxResults=1&projection=lite&q=";
    private final String apiKey = "AIzaSyA0Km9mozkCqNX7N0Sy4V_Uk1OIvxUvRn4";

    /**
     * The Books.
     */
    ArrayList<Book> books = new ArrayList<>();


    private SearchController() {
        client = new Client("9ETLQT0YZC", "7c5462a00988e4152996bac591236760");
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
                    books.clear();
                    if (error == null) {
                        try {
                            JSONArray jsonArray;
                            jsonArray = content.getJSONArray("hits");
                            for (int i = 0; i < jsonArray.length(); i++){
                                Book book = gson.fromJson(jsonArray.getString(i),Book.class);
                                books.add(book);
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

    public void searchIsbn(String isbn) {
        AsyncTask<String, Void, String> bookSearch = new SearchGoogleBooks().execute(isbn);

//        return
    }

    // information retrieved from https://code.tutsplus.com/tutorials/android-sdk-create-a-book-scanning-app-interface-book-search--mobile-17790

    public class SearchGoogleBooks extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... bookURLs) {
            //request book info
//            String book = "";
            String searchUrl = GOOGLE_BOOK_URL + bookURLs[0] + "&key=" + apiKey;

            StringBuilder bookBuilder = new StringBuilder();

            try {
                URL url = new URL(searchUrl);
                HttpURLConnection bookConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = bookConnection.getInputStream();

                    InputStreamReader bookInput = new InputStreamReader(in);
                    BufferedReader bookReader = new BufferedReader(bookInput);

                    String lineIn;
                    while ((lineIn=bookReader.readLine())!=null) {
                        bookBuilder.append(lineIn);
                    }
                } finally {
                    bookConnection.disconnect();
                }

            } catch (MalformedURLException e) {
                System.out.println("Failed to create URL object");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Failed to create URL connection");
                e.printStackTrace();
            }
            String book = bookBuilder.toString();
            return bookBuilder.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
        }

    }


}
