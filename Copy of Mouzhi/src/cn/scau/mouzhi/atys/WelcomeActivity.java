package cn.scau.mouzhi.atys;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import cn.scau.mouzhi.adapter.GuideViewPagerAdapter;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.config.Config;

public class WelcomeActivity extends Activity implements OnClickListener {

	private ArrayList<View> guideViews;
	private GuideViewPagerAdapter guideViewPagerAdapter;

	private Button guide_start_btn;
	private ImageView[] guide_dot_iv;
	private ViewPager guide_viewpager;
	private View guideView1, guideView2, guideView3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		initViews();
		initListeners();
		initValues();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		guide_dot_iv = new ImageView[3];
		guide_dot_iv[0] = (ImageView) findViewById(R.id.guide_dot1_iv);
		guide_dot_iv[1] = (ImageView) findViewById(R.id.guide_dot2_iv);
		guide_dot_iv[2] = (ImageView) findViewById(R.id.guide_dot3_iv);
		guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);

		guideView1 = LayoutInflater.from(this).inflate(R.layout.activity_guide_view1, null);
		guideView2 = LayoutInflater.from(this).inflate(R.layout.activity_guide_view2, null);
		guideView3 = LayoutInflater.from(this).inflate(R.layout.activity_guide_view3, null);
		guide_start_btn = (Button) guideView3.findViewById(R.id.guide_start_btn);
	}

	private void initListeners() {
		// TODO Auto-generated method stub
		guide_start_btn.setOnClickListener(this);

		guide_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				selectPage(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initValues() {
		// TODO Auto-generated method stub
		guideViews = new ArrayList<View>();
		guideViews.add(guideView1);
		guideViews.add(guideView2);
		guideViews.add(guideView3);
		guideViewPagerAdapter = new GuideViewPagerAdapter(guideViews);
		guide_viewpager.setAdapter(guideViewPagerAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.guide_start_btn:
			MainActivity.flag = 0;
			Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
			intent.putExtra("intentName", "WelcomeACtivity");
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * ������ʾ����
	 * 
	 * @param current
	 */
	private void selectPage(int current) {
		for (int i = 0; i < guide_dot_iv.length; i++) {
			guide_dot_iv[current].setImageResource(R.drawable.guide_dot_pressed);
			if (current != i) {
				guide_dot_iv[i].setImageResource(R.drawable.guide_dot_normal);
			}
		}
	}

}
