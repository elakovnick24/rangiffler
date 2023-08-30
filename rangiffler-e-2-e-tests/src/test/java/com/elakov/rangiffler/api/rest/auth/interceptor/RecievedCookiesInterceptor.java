package com.elakov.rangiffler.api.rest.auth.interceptor;

import com.elakov.rangiffler.api.rest.auth.context.CookieContext;
import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class RecievedCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        List<String> setCookie = response.headers("Set-Cookie");

        if (!setCookie.isEmpty()) {
            for (String cookie : setCookie) {
                String[] cookies = cookie.split(";");
                for (String c : cookies) {
                    if (c.contains("XSRF-TOKEN") || c.contains("JSESSIONID")) {
                        String[] res = c.split("=");
                        if (res.length == 2) {
                            CookieContext.getInstance().setCookie(res[0], res[1]);
                        } else {
                            CookieContext.getInstance().removeCookie(res[0]);
                        }
                    }
                }
            }
        }
        return response;
    }
}
