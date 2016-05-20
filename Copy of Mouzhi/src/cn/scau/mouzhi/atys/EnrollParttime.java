package cn.scau.mouzhi.atys;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.Parttime;
import cn.scau.mouzhi.bean.User;
import cn.scau.mouzhi.config.SlashActivity;
import cn.scau.mouzhi.frag.SettingFragment;
import cn.scau.mouzhi.net.NetUtil;

public class EnrollParttime extends Activity{
	
	private EditText enroll_parttime_name, enroll_parttime_class, enroll_parttime_tel;
	private Button enroll;
	private int id;
	private String parttimeName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.parttime_enroll);
		
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Parttime parttime = (Parttime) intent.getSerializableExtra("parttime");
		id = parttime.getReid();
		parttimeName = parttime.getTitle();
		
		enroll_parttime_name = (EditText) findViewById(R.id.enroll_parttime_name);
		enroll_parttime_class = (EditText) findViewById(R.id.enroll_parttime_class);
		enroll_parttime_tel = (EditText) findViewById(R.id.enroll_parttime_tel);
		enroll = (Button) findViewById(R.id.enroll);
		
		enroll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				enroll();
			}
		});
	}


	private void enroll(){
		if (contentIsEmpty()) {
			Toast.makeText(getApplicationContext(), "请填写完整信息", Toast.LENGTH_SHORT).show();
			return;
		}
		String name = enroll_parttime_name.getText().toString();
		String classes = enroll_parttime_class.getText().toString();
		String tel = enroll_parttime_tel.getText().toString();
		String content = name + "<br>" +classes + "<br>" + tel + "<br>" + "兼职ID： " + id + "<br>" + "兼职名称" + parttimeName;
		URL url = null;
		try {
			url = new URL("http://121.42.189.168/mouzhi/setting/feedback");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int uid = SlashActivity.userid;
		Map map = new HashMap();
		map.put("uid", uid);
		map.put("content", content);
		map.put("email", "");
		
		String str = NetUtil.submitPostData(url, map);
		
		JSONObject json = null;
		try {
			json = new JSONObject(str);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int callBackCode = 1;
		try {
			callBackCode = json.getInt("error_code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(0 == callBackCode){
			Toast.makeText(EnrollParttime.this, "报名成功", Toast.LENGTH_SHORT).show();
			Intent intent = getIntent();
			Bundle bundle = intent.getBundleExtra("bundle");
			
			Intent i = new Intent(EnrollParttime.this, MainActivity.class);
			i.putExtra("intentName", "ParttimePublish");
			i.putExtra("bundle", bundle);
			SettingFragment.atMainActivity = 0;
			startActivity(i);
			finish();
		}else{
			Toast.makeText(EnrollParttime.this, "抱歉，您还未登录", Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean contentIsEmpty() {
		// TODO Auto-generated method stub
		boolean isEmpty = false;
		if (enroll_parttime_name.getText().toString().equals("")
				|| enroll_parttime_class.getText().toString().equals("")
				|| enroll_parttime_tel.getText().toString().equals(""))
			isEmpty = true;
		return isEmpty;
	}
}
