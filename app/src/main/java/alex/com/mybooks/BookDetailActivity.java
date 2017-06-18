package alex.com.mybooks;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import javax.inject.Inject;

import alex.com.mybooks.dagger.ApplicationModule;
import alex.com.mybooks.dagger.BookPresenterModule;
import alex.com.mybooks.dagger.DaggerBookPresenterComponent;
import alex.com.mybooks.dagger.ServiceModule;
import alex.com.mybooks.databinding.ActivityBookDetailBinding;
import alex.com.mybooks.model.Book;
import alex.com.mybooks.presenter.BookContract;
import alex.com.mybooks.presenter.BookPresenter;
import alex.com.mybooks.view.BookBinder;

public class BookDetailActivity extends AppCompatActivity implements BookContract.View{

    public static String ARG_BOOK_ID = "bookId";

    private ActivityBookDetailBinding binding;

    @Inject
    BookPresenter bookPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(BookDetailActivity.class.getName(), "onCreate");
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_book_detail);

        DaggerBookPresenterComponent.builder()
                .applicationModule(new ApplicationModule(getApplication()))
                .serviceModule(new ServiceModule(BuildConfig.API_BASE_PATH))
                .bookPresenterModule(new BookPresenterModule(this))
                .build()
                .inject(this);

        setTitle("");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.binding.collapsingToolbar.setTitleEnabled(true);
        this.binding.collapsingToolbar.setTitle(getString(R.string.app_name));

        String bookId = getIntent().getStringExtra(ARG_BOOK_ID);

        this.bookPresenter.start();
        this.bookPresenter.loadBook(bookId);
    }

    @Override
    protected void onDestroy() {
        Log.d(BookDetailActivity.class.getName(), "onDestroy");
        this.bookPresenter.stop();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBook(Book book) {
        Log.d(BookDetailActivity.class.getName(), "Show book with id : " + book.getId());
        BookBinder bookBinder = new BookBinder();
        bookBinder.setTitle(book.getBookInfo().getTitle());
        bookBinder.setAuthors(book.getBookInfo().getAuthors());
        String description = book.getBookInfo().getDescription();
        String formatDescription = Html.fromHtml(description).toString();
        bookBinder.setDescription(formatDescription);
        this.binding.setBookdetail(bookBinder);

        Glide.with(getApplicationContext())
                .load(book.getBookInfo().getImage() != null ? book.getBookInfo().getImage().getThumbnail() : null)
                .into(binding.ivCover);

    }

    @Override
    public void showError(String error) {
        Log.e(BookDetailActivity.class.getName(), error);
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}
