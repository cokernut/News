package top.cokernut.news.http.result;

import java.util.List;

import top.cokernut.news.model.NewModel;

/**
 * Created by Administrator on 2017/7/20.
 */

public class NewsListResult {
    private int code;
    private String msg;
    private List<NewModel> newslist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NewModel> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<NewModel> newslist) {
        this.newslist = newslist;
    }
}
