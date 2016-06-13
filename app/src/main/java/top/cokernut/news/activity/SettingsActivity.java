package top.cokernut.news.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import top.cokernut.news.R;
import top.cokernut.news.base.BaseActivity;
import top.cokernut.news.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment, new SettingsFragment()).commit();
        setTitle("设置");
    }
}
