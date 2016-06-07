package top.cokernut.news.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import java.util.List;

import top.cokernut.news.R;
import top.cokernut.news.adapter.NewListAdapter;
import top.cokernut.news.base.OnRVScrollListener;
import top.cokernut.news.base.OnRecyclerItemClickListener;
import top.cokernut.news.model.NewModel;

public class NewListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String URL = "url";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mView;
    private ImageView mGoTopIV;

    private String url;
    private NewListAdapter mAdapter;
    private List<NewModel> mDatas;
    private int pageSize = 10;
    private int pageIndex = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_new_list, container, false);
        initView();
        initData();
        return mView;
    }
    
    private void initView() {
        mGoTopIV = (ImageView) mView.findViewById(R.id.iv_go_top);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.srl_view);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.rv_view);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(new OnRVScrollListener() {
            @Override
            public void onBottom() {
              //  getData();
            }

            @Override
            public void onTop() {
                mGoTopIV.setVisibility(View.GONE);
                mSwipeRefreshLayout.setEnabled(true);
            }

            @Override
            public void onCenter() {
                mGoTopIV.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setEnabled(false);
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh, int position) {
                Snackbar.make(mRecyclerView, mDatas.get(position).getTitle(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initData() {
        getData();
    }
    
    @Override
    public void onRefresh() {
        pageIndex = 1;
      //  getData();
    }

    public void getData() {
        Parameters para = new Parameters();
        para.put("num", pageSize);
        para.put("page", pageIndex);
        ApiStoreSDK.execute(url, ApiStoreSDK.GET, para, new ApiCallBack() {
            @Override
            public void onSuccess(int status, String responseString) {
                JSONObject json = JSON.parseObject(responseString);
                if (1 == pageIndex) {
                    mDatas = JSON.parseArray(json.getString("newslist"), NewModel.class);
                } else {
                    mDatas.addAll(JSON.parseArray(json.getString("newslist"), NewModel.class));
                }
                pageIndex++;
                initViewData();
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(int status, String responseString, Exception e) {

            }
        });
    }

    private void initViewData() {
        if (pageIndex <= 2 && mAdapter == null && mDatas != null && mDatas.size() > 0) {
            mAdapter = new NewListAdapter(getActivity(), mDatas);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(mDatas);
        }
    }
}
