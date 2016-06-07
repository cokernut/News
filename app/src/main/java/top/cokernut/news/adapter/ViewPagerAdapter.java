package top.cokernut.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import top.cokernut.news.model.URLModel;

/**
 * Created by Cokernut on 2016/6/7.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<URLModel> mDatas;
    private List<Fragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm, List<URLModel> mDatas, List<Fragment> mFragments) {
        super(fm);
        this.mDatas = mDatas;
        this.mFragments = mFragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDatas.get(position).getTitle();
    }

}