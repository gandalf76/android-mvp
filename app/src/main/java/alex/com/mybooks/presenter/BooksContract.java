package alex.com.mybooks.presenter;

import java.util.List;

import alex.com.mybooks.model.Book;

public interface BooksContract {

    interface View {

        void showBooks(List<Book> books, int totalPageCount);

        void showError(String error);
    }

    interface Presenter {

        void start();

        void loadBooks(String queryFilter, int startIndex, int maxResults);

        void stop();

    }
}
