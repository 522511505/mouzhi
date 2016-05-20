package cn.scau.mouzhi.atys;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.config.Config;
import cn.scau.mouzhi.frag.SettingFragment;
import cn.scau.mouzhi.net.NetUtil;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class Regester extends Activity implements OnClickListener {
	private EditText ed_login_tel, register_ed_security_code, ed_sure_register_password, ed_register_account,
			ed_register_password;
	private Button register_text_security_code, btn_register;
	public String tel;
	EventHandler eh;
	private TimeCount time;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ��׿4.0�Ժ�֧�����߳�����HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.register);

		initSDK();

		initView();
	}

	// ��ʼ������SDK
	private void initSDK() {
		// SMSSDK.initSDK(this, "103a7e2dc93d8",
		// "fcb0d448f518dfb7df1ec65347435636");
		// ���õ��˺�
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

		SMSSDK.registerEventHandler(eh); // ע����Żص�
	}

	private void initView() {
		time = new TimeCount(30000, 1000);
		ed_login_tel = (EditText) findViewById(R.id.ed_login_tel);
		register_ed_security_code = (EditText) findViewById(R.id.register_ed_security_code);
		ed_register_account = (EditText) findViewById(R.id.ed_register_account);
		ed_sure_register_password = (EditText) findViewById(R.id.ed_sure_register_password);
		ed_register_password = (EditText) findViewById(R.id.ed_register_password);
		register_text_security_code = (Button) findViewById(R.id.register_text_security_code);
		btn_register = (Button) findViewById(R.id.btn_register);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register_text_security_code:
			// ������֤��
			send();
			break;
		case R.id.btn_register:
			// ���ӷ�����ע��
			regester();
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void regester() {
		// TODO Auto-generated method stub
		if (ed_register_account.getText().toString().equals("")) {
			Toast.makeText(Regester.this, "�������˺�", Toast.LENGTH_SHORT).show();
		} else if (ed_register_password.getText().toString().equals("")) {
			Toast.makeText(Regester.this, "���벻����Ϊ��", Toast.LENGTH_SHORT).show();
		} else if (register_ed_security_code.getText().toString().equals("")) {
			Toast.makeText(Regester.this, "��������֤��", Toast.LENGTH_SHORT).show();
		} else if (register_ed_security_code.getText().toString().trim().length() != 4) {
			Toast.makeText(Regester.this, "��֤���������������", Toast.LENGTH_SHORT).show();
		} else if (!ed_register_password.getText().toString().equals(ed_sure_register_password.getText().toString())) {
			Toast.makeText(Regester.this, "������������벻��ͬ", Toast.LENGTH_SHORT).show();
		} else if (register_ed_security_code.getText().toString().trim().length() == 4) {
			// SMSSDK.submitVerificationCode("86", tel,
			// register_ed_security_code.getText().toString().trim());
			try {
				URL url = new URL("http://121.42.189.168/mouzhi/user/register");

				Map map = new HashMap();
				map.put("password", ed_register_password.getText().toString());
				map.put("nickname", ed_register_account.getText().toString());
				map.put("phoneNumber", ed_login_tel.getText().toString());

				String str = NetUtil.submitPostData(url, map, Config.CHARSET);

				int uid = 0;
				String nickname = "";
				String avatar_url = "";
				try {
					JSONObject json = new JSONObject(str);
					JSONObject data = json.getJSONObject("data");
					uid = data.getInt("uid");
					nickname = data.getString("nickname");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int callBackCode = str.charAt(str.indexOf("error_code") + 12) - 48;

				if (0 == callBackCode) {
					Toast.makeText(getApplicationContext(), "ע��ɹ�", Toast.LENGTH_SHORT).show();

					SettingFragment.atMainActivity = 0;

					Intent intent = new Intent(Regester.this, MainActivity.class);

					Bundle bundle = new Bundle();
					bundle.putString("nickname", nickname);
					bundle.putString("avatar_url", avatar_url);
					bundle.putInt("uid", uid);

					intent.putExtra("intentName", "AtyLogin");
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					finish();
				} else if (1 == callBackCode) {
					Toast.makeText(getApplicationContext(), "���˺��ѱ�ע��", Toast.LENGTH_SHORT).show();
				} else if (2 == callBackCode) {
					Toast.makeText(getApplicationContext(), "δ֪����", Toast.LENGTH_SHORT).show();
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// ��֤ͨ��֮�󣬽�ssmssdkע�����ע��
		// SMSSDK.unregisterEventHandler(eh);
	}

	// ������֤��
	private void send() {
		// TODO Auto-generated method stub
		tel = ed_login_tel.getText().toString().trim();

		if (tel.equals("")) {
			Toast.makeText(getApplicationContext(), "�������ֻ���", Toast.LENGTH_SHORT).show();
		} else if (!tel.matches("^(13|15|18)\\d{9}$")) {
			Toast.makeText(getApplicationContext(), "��������ȷ���ֻ���", Toast.LENGTH_SHORT).show();
		} else {
			SMSSDK.getVoiceVerifyCode("86", tel);

			Toast.makeText(getApplicationContext(), "��֤���Ѿ�����", Toast.LENGTH_SHORT).show();

			time.start();
		}

	}

	// ��ʱȡ�����Žӿڻص�
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			// if (result == SMSSDK.RESULT_COMPLETE) {
			// if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// �ύ��֤��ɹ�
			// Toast.makeText(getApplicationContext(), "��֤����֤�ɹ�",
			// Toast.LENGTH_SHORT).show();
			// Intent i = new Intent(Regester.this, MainActivity.class);
			// startActivity(i);
			// } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
			// Toast.makeText(getApplicationContext(), "��֤���Ѿ�����1",
			// Toast.LENGTH_SHORT).show();
			// } else {
			// ((Throwable) data).printStackTrace();
			// Toast.makeText(Regester.this, "��֤���Ѿ�����2",
			// Toast.LENGTH_SHORT).show();
			// }}
			if (result == SMSSDK.RESULT_COMPLETE) {
				// �ص����
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					// �ύ��֤��ɹ�
					// Toast.makeText(getApplicationContext(), "��֤����֤�ɹ�",
					// Toast.LENGTH_SHORT).show();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					// ��ȡ��֤��ɹ�
					// Toast.makeText(getApplicationContext(), "��֤���Ѿ�����",
					// Toast.LENGTH_SHORT).show();
				} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
					// ����֧�ַ�����֤��Ĺ����б�
				}
			} else {
				// ((Throwable) data).printStackTrace();
				Toast.makeText(getApplicationContext(), "��֤ʧ��", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// ��������Ϊ��ʱ��,�ͼ�ʱ��ʱ����
		}

		@Override
		public void onFinish() {// ��ʱ���ʱ����
			register_text_security_code.setText("������֤");
			register_text_security_code.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// ��ʱ������ʾ
			register_text_security_code.setClickable(false);
			register_text_security_code.setText(millisUntilFinished / 1000 + "��");
		}
	}
}
