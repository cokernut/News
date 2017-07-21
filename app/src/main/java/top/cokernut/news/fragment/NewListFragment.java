package top.cokernut.news.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.cokernut.news.R;
import top.cokernut.news.activity.DetailActivity;
import top.cokernut.news.activity.MainActivity;
import top.cokernut.news.adapter.NewListAdapter;
import top.cokernut.news.base.OnRVScrollListener;
import top.cokernut.news.base.OnRecyclerItemClickListener;
import top.cokernut.news.config.URLConfig;
import top.cokernut.news.dialog.CustomDialog;
import top.cokernut.news.http.HttpCall;
import top.cokernut.news.model.NewModel;

public class NewListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String URL_STR = "url";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mView;
   // private ImageView mGoTopIV;

    private String urlStr;
    private NewListAdapter mAdapter;
    private List<NewModel> mDatas = new ArrayList<>();
    private int pageSize = 10;
    private int pageIndex = 1;
    private boolean isLoading = false;
    private boolean isTop = true;
    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            urlStr = getArguments().getString(URL_STR);
        }
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_list, container, false);
        initView();
        onRefresh();
        return mView;
    }
    
    private void initView() {
      //  mGoTopIV = (ImageView) mView.findViewById(R.id.iv_go_top);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.srl_view);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.wheat, R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NewListAdapter(getActivity(), mDatas);
        mRecyclerView.setAdapter(mAdapter);
    /*    mGoTopIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
                }
                mGoTopIV.setVisibility(View.GONE);
            }
        });*/
        mRecyclerView.addOnScrollListener(new OnRVScrollListener() {
            @Override
            public void onBottom() {
                getData();
            }

            @Override
            public void onCenter() {
                //mGoTopIV.setVisibility(View.VISIBLE);
                mainActivity.setFabVisible(View.VISIBLE);
                isTop = false;
            }

            @Override
            public void onTop() {
                //mGoTopIV.setVisibility(View.GONE);
                mainActivity.setFabVisible(View.GONE);
                isTop = true;
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
               /* Snackbar.make(mSwipeRefreshLayout, mDatas.get(position).getTitle(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.URL_STR, mDatas.get(position).getUrl());
                intent.putExtra(DetailActivity.URL_IMG, mDatas.get(position).getPicUrl());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh, final int position) {
                CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
                builder.setMessage("喜欢这条新闻吗？");
                builder.setTitle("提示");
                builder.setPositiveButton(R.string.dislike, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.removeItem(position);
                        dialog.dismiss();
                        Snackbar.make(mRecyclerView, "我们将会推送更多类似的内容！", Snackbar.LENGTH_LONG).show();
                              //  .setAction("Action", null).show();
                    }
                });

                builder.setNegativeButton(R.string.like, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    public void goTop() {
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(0, 0);
            isTop = true;
        }
    }

    public boolean isTop() {
        return isTop;
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        getData();
    }

    public void getData() {
        if (!isLoading) {
            Map<String, Object> params = new HashMap<>();
            params.put("key", URLConfig.API_KEY);
            params.put("num", pageSize);
            params.put("page", pageIndex);
            //RxJava
            /**
             Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
             Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
             Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
             Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
             另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。*/
            isLoading = true;
            HttpCall.getApiService().getNewsRx(urlStr, params)
                    //subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程,或者叫做事件产生的线程。
                    //subscribeOn() 的位置放在哪里都可以，但它是只能调用一次的。当使用了多个 subscribeOn() 的时候，只有第一个 subscribeOn() 起作用。
                    //虽然超过一个的 subscribeOn() 对事件处理的流程没有影响，但在流程之前却是可以利用的,它和 Subscriber.onStart() 同样是在 subscribe() 调用后而且在事件发送前执行，
                    // 但区别在于它可以指定线程。默认情况下， doOnSubscribe() 执行在 subscribe() 发生的线程；
                    // 而如果在 doOnSubscribe() 之后有 subscribeOn() 的话，它将执行在离它最近的 subscribeOn() 所指定的线程
                    // 在 doOnSubscribe()的后面跟一个 subscribeOn() ，就能指定准备工作的线程了
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    //observeOn(): 指定 Subscriber 所运行在的线程,或者叫做事件消费的线程。observeOn() 指定的是它之后的操作所在的线程。
                    //因此如果有多次切换线程的需求，只要在每个想要切换线程的位置调用一次 observeOn() 即可
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        private Disposable disposable;
                        //解除订阅
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(String s) {
                            if (TextUtils.isEmpty(s)) {
                                disposable.dispose();//当s为空时解除订阅
                            }
                            JSONObject json = JSON.parseObject(s);
                            List<NewModel> result = JSON.parseArray(json.getString("newslist"), NewModel.class);
                            if (result != null && result.size() > 0) {
                                mDatas.addAll(result);
                                pageIndex++;
                                initViewData();
                            } else {
                                Snackbar.make(mRecyclerView, "没有更多了！", Snackbar.LENGTH_SHORT).show();
                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            isLoading = false;
                        }

                        @Override
                        public void onComplete() {
                            isLoading = false;
                        }
                    });

            //FastJson
            /*Call<String> news = HttpCall.getApiService().getNews(urlStr, params);
            isLoading = true;
            news.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    JSONObject json = JSON.parseObject(response.body());
                    List<NewModel> result = JSON.parseArray(json.getString("newslist"), NewModel.class);
                    if (result != null && result.size() > 0) {
                        mDatas.addAll(result);
                        pageIndex++;
                        initViewData();
                    } else {
                        Snackbar.make(mRecyclerView, "没有更多了！", Snackbar.LENGTH_SHORT).show();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Snackbar.make(mRecyclerView, "加载失败！", Snackbar.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                }
            });*/
            //JackSon
           /* Call<String> news = HttpCall.getApiService().getNews(urlStr, params);
            isLoading = true;
            news.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<NewModel> result = new ArrayList<>();
                    try {
                        JsonNode node = mapper.readTree(response.body());
                        result = mapper.readValue(node.get("newslist").asText(), new TypeReference<List<NewModel>>() {});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (result != null && result.size() > 0) {
                        mDatas.addAll(result);
                        pageIndex++;
                        initViewData();
                    } else {
                        Snackbar.make(mRecyclerView, "没有更多了！", Snackbar.LENGTH_SHORT).show();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Snackbar.make(mRecyclerView, "加载失败！", Snackbar.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                }
            });*/
            //GSON
            /*Call<Result<List<NewModel>> news = HttpCall.getApiService().getNews(urlStr, params);
            isLoading = true;
            news.enqueue(new Callback<Result<List<NewModel>>() {
                @Override
                public void onResponse(Call<Result<List<NewModel>> call, Response<Result<List<NewModel>> response) {
                    List<NewModel> result = response.body().getNewslist();
                    if (result != null && result.size() > 0) {
                        mDatas.addAll(result);
                        pageIndex++;
                        initViewData();
                    } else {
                        Snackbar.make(mRecyclerView, "没有更多了！", Snackbar.LENGTH_SHORT).show();
                    }
                    mSwipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                }

                @Override
                public void onFailure(Call<Result<List<NewModel>> call, Throwable t) {
                    Snackbar.make(mRecyclerView, "加载失败！", Snackbar.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                }
            });*/
        }
        /*if (!isLoading) {
            RequestParams params = new RequestParams();
            params.put("key", URLConfig.API_KEY);
            params.put("num", pageSize);
            params.put("page", pageIndex);
            NetClient.get(urlStr, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject json = JSON.parseObject(new String(responseBody));
                        List<NewModel> result = JSON.parseArray(json.getString("newslist"), NewModel.class);
                        if (result != null && result.size() > 0) {
                            if (1 == pageIndex) {
                                mDatas = JSON.parseArray(json.getString("newslist"), NewModel.class);
                            } else {
                                mDatas.addAll(JSON.parseArray(json.getString("newslist"), NewModel.class));
                            }
                            pageIndex++;
                            initViewData();
                        } else {
                            Snackbar.make(mRecyclerView, "没有更多了！", Snackbar.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mSwipeRefreshLayout.setRefreshing(false);
                        isLoading = false;
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Snackbar.make(mRecyclerView, new String(responseBody), Snackbar.LENGTH_LONG).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                }
            });
        }*/
        if (!isLoading) {

        }
    }

    private void initViewData() {
        mAdapter.setData(mDatas);
    }
}
