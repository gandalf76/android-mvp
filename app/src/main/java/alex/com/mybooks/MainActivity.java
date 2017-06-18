package alex.com.mybooks;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import alex.com.mybooks.adapters.AbstractRecyclerViewScrollingAdapter;
import alex.com.mybooks.adapters.BookAdapter;
import alex.com.mybooks.dagger.ApplicationModule;
import alex.com.mybooks.dagger.BooksPresenterModule;
import alex.com.mybooks.dagger.DaggerBooksPresenterComponent;
import alex.com.mybooks.dagger.ServiceModule;
import alex.com.mybooks.databinding.ActivityMainBinding;
import alex.com.mybooks.model.Book;
import alex.com.mybooks.presenter.BooksContract;
import alex.com.mybooks.presenter.BooksPresenter;

public class MainActivity extends AppCompatActivity implements BooksContract.View, AbstractRecyclerViewScrollingAdapter.OnLoadMoreListener, BookAdapter.OnBookAdapterListener {

    private static final int MAX_BOOKS_PER_PAGE = 10;

    private static final String BOOKS_QUERY_FILTER = "thriller";

    private ActivityMainBinding binding;

    private BookAdapter bookAdapter;

    @Inject
    BooksPresenter booksPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MainActivity.class.getName(), "onCreate");
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        DaggerBooksPresenterComponent.builder()
                .applicationModule(new ApplicationModule(this.getApplication()))
                .serviceModule(new ServiceModule(BuildConfig.API_BASE_PATH))
                .booksPresenterModule(new BooksPresenterModule(this))
                .build()
                .inject(this);

        setTitle("");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        this.binding.collapsingToolbar.setTitleEnabled(true);
        this.binding.collapsingToolbar.setTitle("My Title");

        this.binding.rvBooks.setHasFixedSize(true);
        this.binding.rvBooks.setLayoutManager(new LinearLayoutManager(this));
        this.bookAdapter = new BookAdapter(this.binding.rvBooks, this, this);
        this.binding.rvBooks.setAdapter(bookAdapter);
        this.binding.rvBooks.setVisibility(View.GONE);
        this.binding.loader.setVisibility(View.VISIBLE);

        this.booksPresenter.start();
        this.booksPresenter.loadBooks(BOOKS_QUERY_FILTER, 0, MAX_BOOKS_PER_PAGE);
    }

    @Override
    protected void onDestroy() {
        Log.d(MainActivity.class.getName(), "onDestroy");
        this.booksPresenter.stop();
        super.onDestroy();
    }

    @Override
    public void showBooks(final List<Book> books, final int totalPageCount) {
        this.binding.rvBooks.setVisibility(View.VISIBLE);
        this.binding.loader.setVisibility(View.GONE);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //remove the progress bar
                bookAdapter.removeItem(null);
                bookAdapter.addBooks(books, totalPageCount);
            }
        });
    }

    @Override
    public void showError(String error) {
        Log.e(MainActivity.class.getName(), error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        this.binding.rvBooks.setVisibility(View.VISIBLE);
        this.binding.loader.setVisibility(View.GONE);
    }

    @Override
    public void onLoadMore(int currentPage) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //add the progress bar
                bookAdapter.addItem(null);
            }
        });

        this.booksPresenter.loadBooks(BOOKS_QUERY_FILTER, currentPage, MAX_BOOKS_PER_PAGE);
    }

    @Override
    public void onLoadFinished() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //remove the progress bar
                bookAdapter.removeItem(null);
            }
        });
    }

    @Override
    public void onBookSelected(String id) {
        Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
        intent.putExtra(BookDetailActivity.ARG_BOOK_ID, id);
        startActivity(intent);
    }
}
