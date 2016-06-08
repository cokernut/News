package top.cokernut.news.net;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import top.cokernut.news.config.URLConfig;

/**
 * Created by Cokernut on 2016/6/8.
 */
public class NetClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params,  AsyncHttpResponseHandler responseHandler) {
        client.addHeader("apikey", URLConfig.API_KEY);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("apikey", URLConfig.API_KEY);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
      //  return BASE_URL + relativeUrl;
        return relativeUrl;
    }
}
