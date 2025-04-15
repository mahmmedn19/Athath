package com.project.athath.data.network;

import android.content.Context;
import okhttp3.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CookieManager implements Interceptor {
    private Context context;

    public CookieManager(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Load stored cookies
        Set<String> cookies = context.getSharedPreferences("PREF_COOKIES", Context.MODE_PRIVATE)
                .getStringSet("cookies", new HashSet<>());

        // Attach cookies to the request
        Request.Builder builder = request.newBuilder();
        for (String cookie : cookies) {
            builder.addHeader("Cookie", cookie);
        }

        Response response = chain.proceed(builder.build());

        // Save cookies from the response
        if (!response.headers("Set-Cookie").isEmpty()) {
            HashSet<String> newCookies = new HashSet<>(response.headers("Set-Cookie"));
            context.getSharedPreferences("PREF_COOKIES", Context.MODE_PRIVATE)
                    .edit()
                    .putStringSet("cookies", newCookies)
                    .apply();
        }

        return response;
    }
}
