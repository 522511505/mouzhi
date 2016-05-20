package cn.scau.mouzhi.atys;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.Parttime;
import cn.scau.mouzhi.frag.SettingFragment;
import cn.scau.mouzhi.net.NetUtil;

public class ParttimePublish extends Activity implements OnClickListener {
	private EditText parttime_publish_title, parttime_publish_worktime, parttime_publish_workplace,
			parttime_publish_wage, parttime_publish_emnumber, parttime_publish_content;
	private Button bt_parttime_publish;
	private static final int IMAGE_SELECT = 1;
	private Parttime parttime;
	private int authorid;
	private String picturePath;
	private Bundle bundle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.parttime_publish);

		// 安卓4.0以后不支持主线程请求HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}

		Intent intent = getIntent();
		bundle = intent.getBundleExtra("bundle");
		authorid = bundle.getInt("uid");

		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void initView() {
		parttime_publish_title = (EditText) findViewById(R.id.parttime_publish_title);
		parttime_publish_worktime = (EditText) findViewById(R.id.parttime_publish_worktime);
		parttime_publish_workplace = (EditText) findViewById(R.id.parttime_publish_workplace);
		parttime_publish_wage = (EditText) findViewById(R.id.parttime_publish_wage);
		parttime_publish_emnumber = (EditText) findViewById(R.id.parttime_publish_emnumber);
		parttime_publish_content = (EditText) findViewById(R.id.parttime_publish_content);
		bt_parttime_publish = (Button) findViewById(R.id.bt_parttime_publish);

		bt_parttime_publish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (contentIsEmpty()) {
			Toast.makeText(getApplicationContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
			return;
		}
		URL url = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/recruitment/addrec");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map map = new HashMap();
		map.put("authorid", authorid);
		map.put("title", parttime_publish_title.getText().toString());
		map.put("worktime", parttime_publish_worktime.getText().toString());
		map.put("workplace", parttime_publish_workplace.getText().toString());
		map.put("wage", parttime_publish_wage.getText().toString());
		map.put("emnumber", Integer.parseInt(parttime_publish_emnumber.getText().toString()));
		map.put("content", parttime_publish_content.getText().toString());

		String str = NetUtil.submitPostData(url, map);

		JSONObject json;
		int callBack = str.charAt(str.indexOf("error_code") + 12) - 48;
		if (0 == callBack) {
			Toast.makeText(getApplicationContext(), "添加兼职成功，待管理员审核后即可显示", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(ParttimePublish.this, MainActivity.class);
			i.putExtra("intentName", "ParttimePublish");
			i.putExtra("bundle", bundle);
			SettingFragment.atMainActivity = 0;
			startActivity(i);
			finish();
		} else {

		}
	}

	private boolean contentIsEmpty() {
		// TODO Auto-generated method stub
		boolean isEmpty = false;
		if (parttime_publish_title.getText().toString().equals("")
				|| parttime_publish_worktime.getText().toString().equals("")
				|| parttime_publish_workplace.getText().toString().equals("")
				|| parttime_publish_wage.getText().toString().equals("")
				|| parttime_publish_emnumber.getText().toString().equals("")
				|| parttime_publish_content.getText().toString().equals(""))
			isEmpty = true;
		return isEmpty;
	}
}
