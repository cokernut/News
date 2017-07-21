package top.cokernut.news.net;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2017/7/20.
 */

public class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    @Override
    public RequestBody convert(Object value) throws IOException {
        return RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), JSON.toJSONBytes(value));
    }
}
