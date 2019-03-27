package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import android.app.Application;
import android.content.res.Resources;
import android.view.View;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import ca.ualberta.CMPUT3012019T02.alexandria.App;
import ca.ualberta.CMPUT3012019T02.alexandria.R;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import java9.util.concurrent.CompletableFuture;

import static java.lang.System.in;

/**
 * The type Search controller.
 */
public class SearchController {

    private Client client;
    private Index index;
    private Gson gson;
    private static SearchController instance;
    private final String GOOGLE_BOOK_URL = "https://www.googleapis.com/books/v1/volumes?&maxResults=1&projection=lite&q=";
    private final String booksApiKey = App.getContext().getResources().getString(R.string.google_books_api_key);

    /**
     * The Books.
     */
    ArrayList<Book> books = new ArrayList<>();


    private SearchController() {
        client = new Client("9ETLQT0YZC", App.getContext().getResources().getString(R.string.algolia_api_key));
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

    public CompletableFuture<Boolean> compareIsbn(String isbn1, String isbn2) {
        final CompletableFuture<Boolean> compareFuture = new CompletableFuture<>();

        CompletableFuture.runAsync(() -> {
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

                   if (IOUtils.contentEquals( in1, in2 )) {
                       equalIsbn = true;
                   }

//                   InputStreamReader bookInput1 = new InputStreamReader(in1);
//                   InputStreamReader bookInput2 = new InputStreamReader(in2);
//
//                   BufferedReader bookReader1 = new BufferedReader(bookInput1);
//                   BufferedReader bookReader2 = new BufferedReader(bookInput2);

               } catch (Exception e){
                   e.printStackTrace();
                   compareFuture.completeExceptionally(e);
               }
               finally {
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
        });

        return compareFuture;
    }

    public CompletableFuture<Book> searchIsbn(String isbn) {
        final CompletableFuture<Book> resultFuture = new CompletableFuture<>();

        // information retrieved from https://code.tutsplus.com/tutorials/android-sdk-create-a-book-scanning-app-interface-book-search--mobile-17790
        CompletableFuture.runAsync(() -> {
            String searchUrl = GOOGLE_BOOK_URL + isbn + "&key=" + booksApiKey;

            StringBuilder bookBuilder = new StringBuilder();
            bookBuilder.append("{'isbn':" + isbn + ",");

            Book resultBook = null;

            try {
                URL url = new URL(searchUrl);
                HttpURLConnection bookConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream in = bookConnection.getInputStream();

                    InputStreamReader bookInput = new InputStreamReader(in);
                    BufferedReader bookReader = new BufferedReader(bookInput);

                    String lineIn;
                    Boolean volumeInfo = false;
                    while ((lineIn = bookReader.readLine())!= null) {
                        if (lineIn.contains("]") && volumeInfo) {
                            volumeInfo = false;
                            bookBuilder.append("}");
                        }

                        if (volumeInfo) {
                            if (lineIn.contains("authors")) {
                                bookBuilder.append("author:");
                            } else {
                                bookBuilder.append(lineIn);
                            }
                        }

                        if (lineIn.contains("volumeInfo")) {
                            volumeInfo = true;
                        }
                    }
                    resultBook = new Gson().fromJson(bookBuilder.toString(), Book.class);
                }
                catch (Exception e){
                    e.printStackTrace();
                    resultFuture.completeExceptionally(e);
                }
                finally {
                    bookConnection.disconnect();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                resultFuture.completeExceptionally(new IOException("Failed to create URL object"));
            } catch (IOException e) {
                e.printStackTrace();
                resultFuture.completeExceptionally(new IOException("Failed to create URL connection"));
            }

            resultFuture.complete(resultBook);
        });
        return resultFuture;
    }




}
