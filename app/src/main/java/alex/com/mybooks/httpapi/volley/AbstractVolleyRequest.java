package alex.com.mybooks.httpapi.volley;


import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public abstract class AbstractVolleyRequest<ResponseType> extends Request<ResponseType> {

    private final Response.Listener<ResponseType> resultListener;

    public AbstractVolleyRequest(int method, String url, Response.Listener<ResponseType> resultListener, Response.ErrorListener listener) {
        super(method, url, listener);
        this.resultListener = resultListener;
    }

    @Override
    protected Response<ResponseType> parseNetworkResponse(NetworkResponse networkResponse) {
        Log.d(AbstractVolleyRequest.class.getName(), "Response received.");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {

            TypeReference typeReference = getResponseType();
            ObjectReader objectReader = objectMapper.readerFor(typeReference);
            Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(networkResponse);
            String json = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));

            StringBuilder builder = new StringBuilder();
            builder.append("Response received. Status: ").append(networkResponse.statusCode);

            Log.d(AbstractVolleyRequest.class.getName(), builder.toString());

            ResponseType response = null;
            if (!TextUtils.isEmpty(json)) {
                response = objectReader.readValue(json);
            }
            return Response.success(response, entry);

        } catch (Exception e) {
            Log.e(AbstractVolleyRequest.class.getName(), "Malformed response received. Error: " + e.getMessage());
            return Response.error(new NetworkError(networkResponse));

        }
    }

    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    protected void deliverResponse(ResponseType response) {
        Log.d(AbstractVolleyRequest.class.getName(), "Response delivered.");
        this.resultListener.onResponse(response);
    }

    protected abstract TypeReference getResponseType();

}
