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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import top.cokernut.news.R;
import top.cokernut.news.activity.DetailActivity;
import top.cokernut.news.activity.MainActivity;
import top.cokernut.news.adapter.NewListAdapter;
import top.cokernut.news.base.OnRVScrollListener;
import top.cokernut.news.base.OnRecyclerItemClickListener;
import top.cokernut.news.config.URLConfig;
import top.cokernut.news.dialog.CustomDialog;
import top.cokernut.news.model.NewModel;
import top.cokernut.news.net.NetClient;

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
        }
        /*if (!isLoading) {
            Parameters para = new Parameters();
            para.put("num", pageSize);
            para.put("page", pageIndex);
            isLoading = true;
            if (pageIndex > 1) {
                mDatas.add(null);
                mAdapter.setData(mDatas);
            }
            ApiStoreSDK.execute(urlStr, ApiStoreSDK.GET, para, new ApiCallBack() {
                @Override
                public void onSuccess(int status, String responseString) {
                    try {
                        JSONObject json = JSON.parseObject(responseString);
                        if (1 == pageIndex) {
                            mDatas = JSON.parseArray(json.getString("newslist"), NewModel.class);
                        } else {
                            mDatas.remove(mDatas.size() - 1);
                            mDatas.addAll(JSON.parseArray(json.getString("newslist"), NewModel.class));
                        }
                        pageIndex++;
                        initViewData();
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onComplete() {
                    mSwipeRefreshLayout.setRefreshing(false);
                    isLoading = false;
                }

                @Override
                public void onError(int status, String responseString, Exception e) {
                    Snackbar.make(mSwipeRefreshLayout, responseString, Snackbar.LENGTH_LONG).show();
                }
            });
        }*/
    }

    private void initViewData() {
        mAdapter.setData(mDatas);
    }
}
