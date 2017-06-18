package alex.com.mybooks.dagger;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Singleton;

import alex.com.mybooks.httpapi.BooksHttpInteractor;
import alex.com.mybooks.httpapi.volley.VolleyBooksHttpInteractor;
import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private String apiBasePath;

    public ServiceModule(String apiBasePath) {
        this.apiBasePath = apiBasePath;
    }

    @Provides
    BooksHttpInteractor provideVolleyBookHttpInteractor(RequestQueue requestQueue) {
        return new VolleyBooksHttpInteractor(apiBasePath, requestQueue);
    }

    @Provides
    @Singleton
    RequestQueue provideVolleyRequestQueue(Application application) {
        return Volley.newRequestQueue(application.getApplicationContext());
    }
}
