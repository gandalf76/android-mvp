package alex.com.mybooks.httpapi.volley;


import android.content.Context;

import com.android.volley.Response;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import alex.com.mybooks.httpapi.UriBuilder;
import alex.com.mybooks.model.Book;
import alex.com.mybooks.model.BookApiObject;

public class GetVolleyBooksRequest extends AbstractVolleyRequest<BookApiObject> {

    private final static String PATH_VOLUMES_PARAM = "volumes";

    private final static String QUERY_FILTER_PARAM = "q";

    private final static String QUERY_START_INDEX_PARAM = "startIndex";

    private final static String QUERY_MAX_RESULTS_PARAM = "maxResults";

    private final static String QUERY_ORDER_BY_PARAM = "orderBy";

    public GetVolleyBooksRequest(String apiBasePath, String filter, int startIndex, int maxResults, Response.Listener<BookApiObject> resultListener, Response.ErrorListener listener) {
        super(Method.GET, composeUri(apiBasePath, filter, startIndex, maxResults), resultListener, listener);
        setTag("GET");
    }

    private static String composeUri(String apiBasePath, String filter, int startIndex, int maxResults) {
        UriBuilder uriBuilder = new UriBuilder(apiBasePath);
        uriBuilder.appendPath(PATH_VOLUMES_PARAM);
        uriBuilder.appendQueryParameter(QUERY_FILTER_PARAM, filter);
        uriBuilder.appendQueryParameter(QUERY_START_INDEX_PARAM, String.valueOf(startIndex));
        uriBuilder.appendQueryParameter(QUERY_MAX_RESULTS_PARAM, String.valueOf(maxResults));
        uriBuilder.appendQueryParameter(QUERY_ORDER_BY_PARAM, "newest");
        return uriBuilder.build();
    }

    @Override
    protected TypeReference getResponseType() {
        return new TypeReference<BookApiObject>() {};
    }
}
