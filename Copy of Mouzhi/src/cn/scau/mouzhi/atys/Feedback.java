package cn.scau.mouzhi.atys;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.User;
import cn.scau.mouzhi.net.NetUtil;

public class Feedback extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.feedback);

		Button send = (Button) findViewById(R.id.btnSendComment);
		final EditText edt = (EditText) findViewById(R.id.etComment);
		final EditText feedbackEmail = (EditText) findViewById(R.id.feedback_email);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("" == edt.getText().toString()) {
					Toast.makeText(Feedback.this, "请输入反馈的内容", Toast.LENGTH_SHORT).show();
				} else if ("" == feedbackEmail.getText().toString().trim()) {
					Toast.makeText(Feedback.this, "请输入反馈的内容", Toast.LENGTH_SHORT).show();
				} else{
					edt.clearComposingText();
					
					URL url = null;
					try {
						url = new URL("http://121.42.189.168/mouzhi/setting/feedback");
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					int uid = User.getInstance().getUid();
					String email = feedbackEmail.getText().toString().trim();
					Map map = new HashMap();
					map.put("uid", uid);
					map.put("content", edt.getText().toString().trim());
					map.put("email", email);
					
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
						Toast.makeText(Feedback.this, "您的反馈已收到，感谢您的反馈", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(Feedback.this, "抱歉，您还未登录", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}
