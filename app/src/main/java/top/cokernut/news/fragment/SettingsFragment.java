package top.cokernut.news.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import top.cokernut.news.R;
import top.cokernut.news.utils.PrefUtils;
import top.cokernut.news.utils.VersionUtils;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private CheckBoxPreference mIsAutoUpdate;
    private SwitchPreference mPush;
    private Preference mVersionPre, mSendPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        initPreference();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (mIsAutoUpdate == preference) {
            preference.getEditor().putBoolean(PrefUtils.PRE_AUTO_UPDATE, mIsAutoUpdate.isChecked());
        } else if (mSendPref == preference) {
            Intent sendTo = new Intent(Intent.ACTION_SEND);
            String[] developers = new String[]{"cokernut0628@gmail.com", "cokernut@foxmail.com"};
            sendTo.putExtra(Intent.EXTRA_EMAIL, developers);
            sendTo.putExtra(Intent.EXTRA_SUBJECT, "反馈");
            sendTo.putExtra(Intent.EXTRA_TEXT, "欢迎吐槽:\n");
            sendTo.setType("text/plain");
            startActivity(Intent.createChooser(sendTo, "请选择邮件客户端"));
        }
        return true;
    }

    private void initPreference() {
        mIsAutoUpdate = (CheckBoxPreference) findPreference("auto_update");
        mSendPref = findPreference("send");
        mSendPref.setOnPreferenceClickListener(this);
        mVersionPre = findPreference("version");
        mVersionPre.setTitle("版本：" + VersionUtils.setUpVersionName(getActivity()));
    }
}
