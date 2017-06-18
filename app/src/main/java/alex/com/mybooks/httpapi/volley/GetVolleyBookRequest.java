package alex.com.mybooks.httpapi.volley;


import com.android.volley.Response;
import com.fasterxml.jackson.core.type.TypeReference;

import alex.com.mybooks.httpapi.UriBuilder;
import alex.com.mybooks.model.Book;
import alex.com.mybooks.model.BookApiObject;

public class GetVolleyBookRequest extends AbstractVolleyRequest<Book> {

    private final static String PATH_VOLUMES_PARAM = "volumes";

    public GetVolleyBookRequest(String apiBasePath, String volumeId, Response.Listener<Book> resultListener, Response.ErrorListener listener) {
        super(Method.GET, composeUri(apiBasePath, volumeId), resultListener, listener);
        setTag("GET");
    }

    private static String composeUri(String apiBasePath, String volumeId) {
        UriBuilder uriBuilder = new UriBuilder(apiBasePath);
        uriBuilder.appendPath(PATH_VOLUMES_PARAM);
        uriBuilder.appendPath(volumeId);
        return uriBuilder.build();
    }

    @Override
    protected TypeReference getResponseType() {
        return new TypeReference<Book>() {};
    }
}
