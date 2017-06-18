package alex.com.mybooks.presenter;

import java.util.List;

import alex.com.mybooks.model.Book;

public interface BookContract {

    interface View {

        void showBook(Book book);

        void showError(String error);
    }

    interface Presenter {

        void start();

        void loadBook(String id);

        void stop();

    }
}
