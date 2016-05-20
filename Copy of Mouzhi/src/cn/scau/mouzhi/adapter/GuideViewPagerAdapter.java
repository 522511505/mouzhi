package cn.scau.mouzhi.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ���������������
 * 
 * @author su
 * 
 */
public class GuideViewPagerAdapter extends PagerAdapter {

	private ArrayList<View> Views;

	public GuideViewPagerAdapter(ArrayList<View> views) {
		this.Views = views;
	}

	/**
	 * ��ȡ�ܽ�����
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Views.size();
	}

	/**
	 * �ж�pager��һ��view�Ƿ��instantiateItem�������ص�object�й������������Ƿ��ɶ������ɽ���
	 */
	@Override
	public boolean isViewFromObject(View container, Object object) {
		// TODO Auto-generated method stub
		return container == object;
	}

	/**
	 * PagerAdapter������ѡ���ĸ�����
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		// TODO Auto-generated method stub
		((ViewPager) container).addView(Views.get(position));
		return Views.get(position);
	}

	/**
	 * ��ViewGroup���Ƴ���ǰView
	 */
	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView((View) object);
	}

}
