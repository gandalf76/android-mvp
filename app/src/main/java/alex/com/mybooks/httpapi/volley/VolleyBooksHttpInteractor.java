package alex.com.mybooks.httpapi.volley;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import alex.com.mybooks.httpapi.BooksHttpInteractor;
import alex.com.mybooks.model.Book;
import alex.com.mybooks.model.BookApiObject;

public class VolleyBooksHttpInteractor implements BooksHttpInteractor {

    private String apiBasePath;

    private RequestQueue requestQueue;

    private BooksHttpInteractorObserver booksHttpInteractorObserver;

    public VolleyBooksHttpInteractor (String apiBasePath, RequestQueue requestQueue) {
        this.apiBasePath = apiBasePath;
        this.requestQueue = requestQueue;
    }

    public void setBooksHttpInteractorObserver(BooksHttpInteractorObserver booksHttpInteractorObserver) {
        this.booksHttpInteractorObserver = booksHttpInteractorObserver;
    }

    @Override
    public void cancelPendingRequests() {
        this.requestQueue.cancelAll("GET");
    }

    @Override
    public void getBooks(String filter, int startIndex, final int maxResults) {
        GetVolleyBooksRequest getVolleyBooksRequest = new GetVolleyBooksRequest(apiBasePath, filter, startIndex, maxResults, new Response.Listener<BookApiObject>() {
            @Override
            public void onResponse(BookApiObject response) {
                if (VolleyBooksHttpInteractor.this.booksHttpInteractorObserver != null) {
                    VolleyBooksHttpInteractor.this.booksHttpInteractorObserver.onBooksLoaded(response, maxResults);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(VolleyBooksHttpInteractor.class.getName(), "Get Books failed.");
                if (VolleyBooksHttpInteractor.this.booksHttpInteractorObserver != null) {
                    VolleyBooksHttpInteractor.this.booksHttpInteractorObserver.onBooksLoadingFailure(error.getMessage());
                }
            }
        });
        this.requestQueue.add(getVolleyBooksRequest);
    }

    @Override
    public void getBook(String id) {
        GetVolleyBookRequest getVolleyBooksRequest = new GetVolleyBookRequest(apiBasePath, id, new Response.Listener<Book>() {
            @Override
            public void onResponse(Book book) {
                if (VolleyBooksHttpInteractor.this.booksHttpInteractorObserver != null) {
                    VolleyBooksHttpInteractor.this.booksHttpInteractorObserver.onBookLoaded(book);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(VolleyBooksHttpInteractor.class.getName(), "Get Book failed.");
                if (VolleyBooksHttpInteractor.this.booksHttpInteractorObserver != null) {
                    VolleyBooksHttpInteractor.this.booksHttpInteractorObserver.onBooksLoadingFailure(error.getMessage());
                }
            }
        });
        this.requestQueue.add(getVolleyBooksRequest);
    }

}
