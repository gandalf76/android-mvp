package alex.com.mybooks.dagger;

import javax.inject.Singleton;

import alex.com.mybooks.MainActivity;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ServiceModule.class, BooksPresenterModule.class})
public interface BooksPresenterComponent {

    void inject(MainActivity activity);
}
