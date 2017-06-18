package alex.com.mybooks.httpapi.volley;


import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.lang.reflect.Type;

public abstract class AbstractVolleyRequest<ResponseType> extends Request<ResponseType> {

    private final Response.Listener<ResponseType> resultListener;

    public AbstractVolleyRequest(int method, String url, Response.Listener<ResponseType> resultListener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.resultListener = resultListener;
    }

    @Override
    protected Response<ResponseType> parseNetworkResponse(NetworkResponse networkResponse) {
        Log.d(AbstractVolleyRequest.class.getName(), "Response received.");

        try {

            Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(networkResponse);
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));

            StringBuilder builder = new StringBuilder();
            builder.append("Response received. Status: ").append(networkResponse.statusCode);

            Log.d(AbstractVolleyRequest.class.getName(), builder.toString());

            ResponseType response = null;

            if (!TextUtils.isEmpty(json)) {
                Gson gson = new Gson();
                response = gson.fromJson(json, getResponseType());
            }
            return Response.success(response, entry);

        } catch (Exception e) {
            Log.e(AbstractVolleyRequest.class.getName(), "Malformed response received. Error: " + e.getMessage());
            return Response.error(new NetworkError(networkResponse));

        }
    }

    @Override
    protected void deliverResponse(ResponseType response) {
        Log.d(AbstractVolleyRequest.class.getName(), "Response delivered.");
        this.resultListener.onResponse(response);
    }

    protected abstract Type getResponseType();

}
