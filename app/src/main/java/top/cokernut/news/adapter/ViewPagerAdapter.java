package top.cokernut.news.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import top.cokernut.news.R;
import top.cokernut.news.fragment.NewListFragment;
import top.cokernut.news.model.URLModel;

/**
 * Created by Cokernut on 2016/6/7.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<URLModel> mDatas;
    private List<NewListFragment> mFragments;
    private Context mContext;

    public ViewPagerAdapter(Context context, FragmentManager fm, List<URLModel> mDatas, List<NewListFragment> mFragments) {
        super(fm);
        this.mContext = context;
        this.mDatas = mDatas;
        this.mFragments = mFragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public NewListFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
       // return mDatas.get(position).getTitle();
        return null;
    }

    public View getTabView(int position){
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tab, null);
        TextView tv= (TextView) view.findViewById(R.id.tv_title);
        tv.setText(mDatas.get(position).getTitle());
        return view;
    }

    public View getDefaultTabView(int position){
        TextView view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_default_tab, null);
        view.setText(mDatas.get(position).getTitle());
        return view;
    }

}