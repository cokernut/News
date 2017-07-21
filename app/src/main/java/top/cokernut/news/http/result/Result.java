package top.cokernut.news.http.result;

/**
 * Created by Administrator on 2017/7/21.
 */

public class Result<T> {
    public int code;
    public String msg;
    //public T data;
    public T newslist;
}
