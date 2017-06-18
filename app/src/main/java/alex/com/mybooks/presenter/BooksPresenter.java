package alex.com.mybooks.presenter;


import android.util.Log;

import java.util.Collections;

import javax.inject.Inject;

import alex.com.mybooks.httpapi.BooksHttpInteractor;
import alex.com.mybooks.model.Book;
import alex.com.mybooks.model.BookApiObject;

public class BooksPresenter implements BooksContract.Presenter, BooksHttpInteractor.BooksHttpInteractorObserver {

    private static final String TAG = BooksPresenter.class.getName();

    private BooksContract.View view;

    private BooksHttpInteractor booksHttpInteractor;

    private int totalPageCount;

    @Inject
    public BooksPresenter(BooksContract.View view, BooksHttpInteractor booksHttpInteractor) {
        this.view = view;
        this.booksHttpInteractor = booksHttpInteractor;
    }

    @Override
    public void start() {
        Log.d(TAG, "start");
        this.booksHttpInteractor.setBooksHttpInteractorObserver(this);
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop");
        this.booksHttpInteractor.cancelPendingRequests();
        this.booksHttpInteractor.setBooksHttpInteractorObserver(null);
    }

    @Override
    public void loadBooks(String queryFilter, int currentPage, int maxResults) {
        Log.d(TAG, String.format("loadBooks (queryFilter : %s - page : %d - maxResult : %d", queryFilter, currentPage, maxResults));
        int startIndex = currentPage * maxResults;
        this.booksHttpInteractor.getBooks(queryFilter, startIndex, maxResults);
    }

    @Override
    public void onBookLoaded(Book book) {
        //not used in this presenter
    }

    @Override
    public void onBooksLoaded(BookApiObject bookApiObject, int maxResults) {
        this.totalPageCount = this.totalPageCount == 0 ? (int) Math.ceil((float)bookApiObject.getTotalItems() / (float)maxResults) : totalPageCount;
        view.showBooks(Collections.unmodifiableList(bookApiObject.getBooks()), totalPageCount);
    }

    @Override
    public void onBooksLoadingFailure(String error) {
        view.showError(error);
    }
}
