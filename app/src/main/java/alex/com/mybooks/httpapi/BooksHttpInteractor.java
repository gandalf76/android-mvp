package alex.com.mybooks.httpapi;


import alex.com.mybooks.model.Book;
import alex.com.mybooks.model.BookApiObject;

public interface BooksHttpInteractor {

    void setBooksHttpInteractorObserver(BooksHttpInteractorObserver booksHttpInteractorObserver);

    void cancelPendingRequests();
        /**
         * Get a list of books with the total number of items found
         * @param filter : a word for filtering books request
         * @param startIndex : the starting index of the book for paging
         * @param maxResults : max number of the books retrieved
         */
    void getBooks(String filter, int startIndex, int maxResults);

    /**
     * Get details of a single book
     * @param id . the book id
     */
    void getBook(String id);

    interface BooksHttpInteractorObserver {

        void onBookLoaded(Book book);

        void onBooksLoaded(BookApiObject bookApiObject, int maxResults);

        void onBooksLoadingFailure(String error);
    }
}
