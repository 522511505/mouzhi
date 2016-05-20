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
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.config.Config;
import cn.scau.mouzhi.net.NetUtil;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class Forget_password extends Activity implements OnClickListener {
	private EditText forget_password_tel, forget_password_icode, forget_password_new_password;
	private Button security_code, btn_login;
	public String tel;
	EventHandler eh;
	private int callBackCode = 1;
	private int uid = 0;
	private String nickname = null;
	private String avatar_url = null;
	private int groupid = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.forget_password);
		
		initSDK();

		initView();

	}

	// 初始化短信SDK
	private void initSDK() {
		// SMSSDK.initSDK(this, "103a7e2dc93d8",
		// "fcb0d448f518dfb7df1ec65347435636");
		SMSSDK.initSDK(this, "10d39a762d0b0", "6f7049fb3a675917afb0dd73752db819");

		eh = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};

		SMSSDK.registerEventHandler(eh); // 注册短信回调
	}

	private void initView() {
		// TODO Auto-generated method stub
		forget_password_tel = (EditText) findViewById(R.id.forget_password_tel);
		forget_password_icode = (EditText) findViewById(R.id.forget_password_icode);
		forget_password_new_password = (EditText) findViewById(R.id.forget_password_new_password);
		security_code = (Button) findViewById(R.id.security_code);
		btn_login = (Button) findViewById(R.id.btn_login);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.security_code:
			send();
			break;
		case R.id.btn_login:
			turnToMianActivity();
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void turnToMianActivity() {
		// TODO Auto-generated method stub
		if (forget_password_tel.getText().toString().equals("")) {
			Toast.makeText(Forget_password.this, "请输入账号", Toast.LENGTH_SHORT).show();
		} else if (forget_password_icode.getText().toString().equals("")) {
			Toast.makeText(Forget_password.this, "请输入验证码", Toast.LENGTH_SHORT).show();
		} else if (forget_password_new_password.getText().toString().equals("")) {
			Toast.makeText(Forget_password.this, "请输入新密码", Toast.LENGTH_SHORT).show();
		} else if (forget_password_icode.getText().toString().trim().length() == 4) {
			SMSSDK.submitVerificationCode("86", tel, forget_password_icode.getText().toString().trim());
			try {
				URL url = new URL("http://121.42.189.168/mouzhi/user/recallpassword");

				Map map = new HashMap();
				map.put("phoneNumber", forget_password_tel.getText().toString());
				map.put("newPassword", forget_password_new_password.getText().toString());

				String str = NetUtil.submitPostData(url, map, Config.CHARSET);

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
					JSONObject data = json.getJSONObject("data");
					uid = data.getInt("uid");
					nickname = data.getString("nickname");
					callBackCode = json.getInt("error_code");
					avatar_url = data.getString("avatar_url");
					groupid = data.getInt("groupid");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (0 == callBackCode) {
					Toast.makeText(Forget_password.this, "修改密码成功", Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(Forget_password.this, MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("nickname", nickname);
					bundle.putString("avatar_url", avatar_url);
					bundle.putInt("uid", uid);

					intent.putExtra("intentName", "AtyLogin");
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					finish();

				} else {
					Toast.makeText(Forget_password.this, "修改密码失败", Toast.LENGTH_SHORT).show();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// 验证通过之后，将ssmssdk注册代码注销
		SMSSDK.unregisterEventHandler(eh);
	}

	// 发送验证码
	private void send() {
		// TODO Auto-generated method stub
		tel = forget_password_tel.getText().toString().trim();

		if (tel.equals("")) {
			Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
		} else if (!tel.matches("^(13|15|18)\\d{9}$")) {
			Toast.makeText(getApplicationContext(), "请输入正确的手机号格式", Toast.LENGTH_SHORT).show();
		} else {
			SMSSDK.getVoiceVerifyCode("86", tel);

			Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();

			forget_password_icode.setClickable(false);
		}

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			// if (result == SMSSDK.RESULT_COMPLETE) {
			// if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
			// Toast.makeText(getApplicationContext(), "验证码验证成功",
			// Toast.LENGTH_SHORT).show();
			// Intent i = new Intent(Regester.this, MainActivity.class);
			// startActivity(i);
			// } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
			// Toast.makeText(getApplicationContext(), "验证码已经发送1",
			// Toast.LENGTH_SHORT).show();
			// } else {
			// ((Throwable) data).printStackTrace();
			// Toast.makeText(Regester.this, "验证码已经发送2",
			// Toast.LENGTH_SHORT).show();
			// }}
			if (result == SMSSDK.RESULT_COMPLETE) {
				// 回调完成
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					// 提交验证码成功
					// Toast.makeText(getApplicationContext(), "验证码验证成功",
					// Toast.LENGTH_SHORT).show();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					// 获取验证码成功
					// Toast.makeText(getApplicationContext(), "验证码已经发送",
					// Toast.LENGTH_SHORT).show();
				} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
					// 返回支持发送验证码的国家列表
				}
			} else {
				((Throwable) data).printStackTrace();
				// Toast.makeText(getApplicationContext(), "验证失败",
				// Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}