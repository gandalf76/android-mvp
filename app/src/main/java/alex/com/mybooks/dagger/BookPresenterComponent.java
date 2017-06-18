package alex.com.mybooks.dagger;

import javax.inject.Singleton;

import alex.com.mybooks.BookDetailActivity;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ServiceModule.class, BookPresenterModule.class})
public interface BookPresenterComponent {

    void inject(BookDetailActivity activity);
}
