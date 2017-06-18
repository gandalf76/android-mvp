package alex.com.mybooks.dagger;


import alex.com.mybooks.presenter.BooksContract;
import dagger.Module;
import dagger.Provides;

@Module
public class BooksPresenterModule {

    private BooksContract.View view;

    public BooksPresenterModule(BooksContract.View view) {
        this.view = view;
    }

    @Provides
    BooksContract.View provideBooksContractView() {
        return this.view;
    }
}
