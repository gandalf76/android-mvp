package alex.com.mybooks.dagger;


import alex.com.mybooks.presenter.BookContract;
import dagger.Module;
import dagger.Provides;

@Module
public class BookPresenterModule {

    private BookContract.View view;

    public BookPresenterModule(BookContract.View view) {
        this.view = view;
    }

    @Provides
    BookContract.View provideBooksContractView() {
        return this.view;
    }
}
