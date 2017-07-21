package top.cokernut.news.http;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/7/20.
 */

public interface ApiService {
    //Gson
//    @POST("{type}")
//    Call<Result<List<NewModel>> getNews(@Path("type") String type, @QueryMap Map<String, Object> args);

    @POST("{type}")
    Call<String> getNews(@Path("type") String type, @QueryMap Map<String, Object> args);

    @POST("{type}")
    Observable<String> getNewsRx(@Path("type") String type, @QueryMap Map<String, Object> args);
}
