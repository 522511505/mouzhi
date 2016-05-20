package cn.scau.mouzhi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import cn.scau.mouzhi.atys.MainActivity;
import cn.scau.mouzhi.frag.ActivitiesFragment;
import cn.scau.mouzhi.frag.NewsFragment;
import cn.scau.mouzhi.frag.ParttimeFragment;
import cn.scau.mouzhi.frag.SettingFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
	public final static int TAB_COUNT = 4;

	public FragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int id) {
		switch (id) {
		case MainActivity.TAB_NEWS:
			NewsFragment newFragment = new NewsFragment();
			return newFragment;
		case MainActivity.TAB_PARTTIME:
			ParttimeFragment parttimeFragment = new ParttimeFragment();
			return parttimeFragment;
		case MainActivity.TAB_ACTIVITIES:
			ActivitiesFragment activitiesFragment = new ActivitiesFragment();
			return activitiesFragment;
		case MainActivity.TAB_SETTING:
			SettingFragment settingFragment = SettingFragment.getInstance();
			SettingFragment.atMainActivity = 1;
			return settingFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return TAB_COUNT;
	}
}
