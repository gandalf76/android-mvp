package alex.com.mybooks.presenter;


import android.util.Log;

import java.util.Collections;

import javax.inject.Inject;

import alex.com.mybooks.httpapi.BooksHttpInteractor;
import alex.com.mybooks.model.Book;
import alex.com.mybooks.model.BookApiObject;

public class BookPresenter implements BookContract.Presenter, BooksHttpInteractor.BooksHttpInteractorObserver {

    private static final String TAG = BookPresenter.class.getName();

    private BookContract.View view;

    private BooksHttpInteractor booksHttpInteractor;

    @Inject
    public BookPresenter(BookContract.View view, BooksHttpInteractor booksHttpInteractor) {
        this.view = view;
        this.booksHttpInteractor = booksHttpInteractor;
    }

    @Override
    public void start() {
        Log.d(TAG, "start");
        this.booksHttpInteractor.setBooksHttpInteractorObserver(this);
    }

    @Override
    public void loadBook(String id) {
        Log.d(TAG, String.format("loadBook (volume id : %s", id));
        this.booksHttpInteractor.getBook(id);
    }

    @Override
    public void stop() {
        Log.d(TAG, "stop");
        this.booksHttpInteractor.cancelPendingRequests();
        this.booksHttpInteractor.setBooksHttpInteractorObserver(null);
    }

    @Override
    public void onBookLoaded(Book book) {
        this.view.showBook(book);
    }

    @Override
    public void onBooksLoaded(BookApiObject bookApiObject, int maxResults) {
        //not use in this presenter}
    }

    @Override
    public void onBooksLoadingFailure(String error) {
        view.showError(error);
    }
}
