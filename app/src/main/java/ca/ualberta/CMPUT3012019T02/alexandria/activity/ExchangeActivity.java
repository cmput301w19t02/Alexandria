package ca.ualberta.CMPUT3012019T02.alexandria.activity;

//TODO MOVE METHODS TO EXCHANGE FRAGMENT
public class ExchangeActivity {

    private enum Page {BORROWED, ACCEPTED, REQUESTED}
    private Page currentPage = Page.BORROWED;


    private void onCreate() {
        filterPage(currentPage);
    }

    private void onStart() {}

    private void filterPage(Page page) {}

    public void filterBorrowed() {
        if (currentPage != Page.BORROWED) {
            filterPage(Page.BORROWED);
        }
    }

    public void filterAccepted() {
        if (currentPage != Page.ACCEPTED) {
            filterPage(Page.ACCEPTED);
        }
    }

    public void filterRequested() {
        if (currentPage != Page.REQUESTED) {
            filterPage(Page.REQUESTED);
        }
    }

    public void SearchBar() {}
}
