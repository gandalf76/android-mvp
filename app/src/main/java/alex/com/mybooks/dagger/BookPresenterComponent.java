package alex.com.mybooks.dagger;

import javax.inject.Singleton;

import alex.com.mybooks.BookDetailActivity;
import alex.com.mybooks.MainActivity;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ServiceModule.class, BookPresenterModule.class})
public interface BookPresenterComponent {

    void inject(BookDetailActivity activity);
}
