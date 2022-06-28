package com.calibrage.payzanconsumer.framework.networkservices;

import android.content.Context;
import android.util.Log;


import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Calibrage11 on 11/30/2017.
 */

public class CustomResponseInterceptor implements Interceptor {

    private static String newToken;
    private String bodyString;
    private Context context;

    private final String TAG = getClass().getSimpleName();

    public CustomResponseInterceptor(Context context){
        this.context =context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (response.code() != 200) {
            Response r = null;
            try
            {
                r = makeTokenRefreshCall(request, chain);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return r;
        }
        Log.d(TAG, "INTERCEPTED:$ " +response.toString());
        return response;
    }

    private Response makeTokenRefreshCall(Request req, Chain chain) throws JSONException, IOException {

        /*https://gist.github.com/alex-shpak/da1e65f52dc916716930*/
//        Log.d(TAG, "Retrying new request");
        /* fetch refreshed token, some synchronous API call, whatever */
        String newToken = SharedPrefsData.getInstance(context).getStringFromSharedPrefs("Token");
        /* make a new request which is same as the original one, except that its headers now contain a refreshed token */
        Request newRequest;
        newRequest = req.newBuilder().header("Authorization",   newToken).build();
        Response another =  chain.proceed(newRequest);
        while (another.code() == 401) {
            makeTokenRefreshCall(newRequest, chain);

        }
        return another;
    }
}
