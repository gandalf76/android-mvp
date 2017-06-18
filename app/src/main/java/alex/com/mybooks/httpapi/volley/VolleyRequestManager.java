package alex.com.mybooks.httpapi.volley;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestManager {

    private static VolleyRequestManager mInstance;

    private RequestQueue requestQueue;

    private static Context context;

    private VolleyRequestManager(Context context) {
        VolleyRequestManager.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequestManager(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
