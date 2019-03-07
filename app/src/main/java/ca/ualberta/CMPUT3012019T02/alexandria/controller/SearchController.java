package ca.ualberta.CMPUT3012019T02.alexandria.controller;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import java9.util.concurrent.CompletableFuture;

public class SearchController {

    private Client client;
    private Index index;
    private Gson gson;
    private static SearchController instance;

    private SearchController() {
        client = new Client("9ETLQT0YZC", "7c5462a00988e4152996bac591236760");
        index = client.getIndex("search");

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
        CompletableFuture<ArrayList<Book>> resultFuture = new CompletableFuture<>();
        index.searchAsync(new Query(search), (content, error)->{
            if(error==null) {
                try {
                    ArrayList<Book> books = new ArrayList<>();
                    JSONArray jsonArray;
                    jsonArray = content.getJSONArray("hits");
                    for (int i = 0; i<jsonArray.length();i++){
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

}
