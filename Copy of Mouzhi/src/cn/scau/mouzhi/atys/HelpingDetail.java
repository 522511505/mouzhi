package cn.scau.mouzhi.atys;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.Activities;
import cn.scau.mouzhi.bean.HelpingInfo;

public class HelpingDetail extends Activity {
	private TextView helping_detail_title, helping_detail_content;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.helping_detail);
		
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
		helping_detail_title = (TextView) findViewById(R.id.helping_detail_title);
		helping_detail_content = (TextView) findViewById(R.id.helping_detail_content);
	}
	
	private void setView() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		HelpingInfo helpingInfo = (HelpingInfo) intent.getSerializableExtra("Helping");
		String a = helpingInfo.getTitle();
		String b = helpingInfo.getContent();
		helping_detail_title.setText(helpingInfo.getTitle());
		helping_detail_content.setText(helpingInfo.getContent());
	}
}
