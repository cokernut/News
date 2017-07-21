package top.cokernut.news.net;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * Created by Administrator on 2017/7/20.
 */

public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;
    public FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource buffer = Okio.buffer(value.source());
        String s = buffer.readUtf8();
        buffer.close();
        return JSON.parseObject(s, type);
    }
}
