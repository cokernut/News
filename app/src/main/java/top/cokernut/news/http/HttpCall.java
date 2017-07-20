package top.cokernut.news.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import top.cokernut.news.config.URLConfig;

/**
 * Created by Administrator on 2017/7/20.
 */

public class HttpCall {
    private static ApiService apiService;
    public static ApiService getApiService() {
        if (apiService == null) {
//
//            //处理没有认证  http 401 Not Authorised
//            Authenticator mAuthenticator2 = new Authenticator() {
//                @Override
//                public Request authenticate(Route route, Response response) throws IOException {
//                    if (responseCount(response) >= 2) {
//                        // If both the original call and the call with refreshed token failed,it will probably keep failing, so don't try again.
//                        return null;
//                    }
//                    refreshToken();
//                    return response.request().newBuilder()
//                            .header("Authorization", TOKEN)
//                            .build();
//                }
//            };
//
//            /**
//             * 如果你的 token 是空的，就是还没有请求到 token，比如对于登陆请求，是没有 token 的，
//             * 只有等到登陆之后才有 token，这时候就不进行附着上 token。另外，如果你的请求中已经带有验证 header 了，
//             * 比如你手动设置了一个另外的 token，那么也不需要再附着这一个 token.
//             */
//
//            Interceptor mRequestInterceptor = new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//                    Request originalRequest = chain.request();
//                    if (TextUtils.isEmpty(TOKEN)) {
//                        TOKEN = SharedPreferencesDao.getInstance().getData(SPKey.KEY_ACCESS_TOKEN, "", String.class);
//                    }
//
//                    /***
//                     * TOKEN == null，Login/Register noNeed Token
//                     * noNeedAuth(originalRequest)    refreshToken api request is after log in before log out,but  refreshToken api no need auth
//                     */
//                    if (TextUtils.isEmpty(TOKEN) || alreadyHasAuthorizationHeader(originalRequest) || noNeedAuth(originalRequest)) {
//                        Response originalResponse = chain.proceed(originalRequest);
//                        return originalResponse.newBuilder()
//                                //get http request progress,et download app
////                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
//                                .build();
//                    }
//
//                    Request authorisedRequest = originalRequest.newBuilder()
//                            .header("Authorization", TOKEN)
//                            .header("Connection", "Keep-Alive") //新添加，time-out默认是多少呢？
//                            .build();
//
//                    Response originalResponse = chain.proceed(authorisedRequest);
//                    return originalResponse.newBuilder()
////                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
//                            .build();
//                }
//            };
//
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .retryOnConnectionFailure(true)
//                    .connectTimeout(11, TimeUnit.SECONDS)
//                    .addNetworkInterceptor(mRequestInterceptor)
//                    .addInterceptor(loggingInterceptor)
//                    .authenticator(mAuthenticator2)
//                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URLConfig.BASE_URL)
                    //.client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}
