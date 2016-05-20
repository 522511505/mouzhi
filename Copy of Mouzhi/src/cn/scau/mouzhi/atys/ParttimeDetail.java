package cn.scau.mouzhi.atys;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.transition.Slide;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.Parttime;

public class ParttimeDetail extends Activity {
	private TextView parttime_detail_title, parttime_detail_publishtime, parttime_detail_emnumber,
			parttime_detail_worktime, parttime_detail_content,parttime_detail_workplace,parttime_detail_wage;

	private Button enroll;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.parttime_detail);
		initView();

		// 安卓4.0以后不支持主线程请求HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}

		setView();
	}

	private void initView() {
		parttime_detail_title = (TextView) findViewById(R.id.parttime_detail_title);
		parttime_detail_publishtime = (TextView) findViewById(R.id.parttime_detail_publishtime);
		parttime_detail_emnumber = (TextView) findViewById(R.id.parttime_detail_emnumber);
		parttime_detail_worktime = (TextView) findViewById(R.id.parttime_detail_worktime);
		parttime_detail_content = (TextView) findViewById(R.id.parttime_detail_content);
		parttime_detail_workplace = (TextView) findViewById(R.id.parttime_detail_workplace);
		parttime_detail_wage = (TextView) findViewById(R.id.parttime_detail_wage);
		enroll = (Button) findViewById(R.id.enroll);
		enroll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				Bundle bundle = intent.getBundleExtra("bundle");
				Parttime parttime = (Parttime) intent.getSerializableExtra("parttime");
				intent.setClass(ParttimeDetail.this,EnrollParttime.class);
				intent.putExtra("parttime", parttime);
				intent.putExtra("bundle", bundle);
				startActivity(intent);
			}
		});
	}

	private void setView() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Parttime parttime = (Parttime) intent.getSerializableExtra("parttime");
		parttime_detail_title.setText(parttime.getTitle());
		parttime_detail_publishtime.setText(parttime.getPublishtime() + "人");
		parttime_detail_emnumber.setText("招聘人数：招聘" + String.valueOf(parttime.getEmnumber()) + "人");
		parttime_detail_worktime.setText("工作时间 ：" + parttime.getWorktime());
		parttime_detail_workplace.setText("工作地点：" + parttime.getWorkplace());
		parttime_detail_wage.setText("工资待遇：" + parttime.getWage());
		parttime_detail_content.setText(parttime.getContent());
	}
}
