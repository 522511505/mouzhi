package cn.scau.mouzhi.atys;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.User;
import cn.scau.mouzhi.config.Config;
import cn.scau.mouzhi.frag.SettingFragment;
import cn.scau.mouzhi.net.NetUtil;

public class AtyLogin extends Activity implements OnClickListener {
	private EditText etAccount, etPassword;
	private Button loginButton;
	TextView regester, forget_passwd;
	ImageView icon_login;
	URL url = null;
	private User user = null;
	private int callBackCode = 1;
	private int uid = 0;
	private String nickname = null;
	private String avatar_url = null;
	private int groupid = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.aty_login);
		
		// ��׿4.0�Ժ�֧�����߳�����HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}

		initView();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			login();
			break;
		case R.id.app_login_regester:
			turn_to_regester();
			break;
		case R.id.app_login_forget_password:
			forget_password();
			break;
		}
	}

	private void login() {
		if (TextUtils.isEmpty(etAccount.getText())) {
			Toast.makeText(AtyLogin.this, R.string.account_empty, Toast.LENGTH_LONG).show();
		} else if (TextUtils.isEmpty(etPassword.getText())) {
			Toast.makeText(AtyLogin.this, R.string.password_empty, Toast.LENGTH_LONG).show();
		} else {
			final ProgressDialog pd = ProgressDialog.show(AtyLogin.this, getResources().getString(R.string.connecting),
					getResources().getString(R.string.connecting_to_server));

			// �����˺�����
			try {
				url = new URL("http://121.42.189.168/mouzhi/user/login");

				Map map = new HashMap();
				map.put("phoneNumber", etAccount.getText().toString());
				map.put("password", etPassword.getText().toString());
				String str = NetUtil.submitPostData(url, map, Config.CHARSET);
				
				try {
					JSONObject json = new JSONObject(str);
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
					user = User.getInstance(uid, avatar_url, groupid, nickname);
					
					Toast.makeText(getApplicationContext(), "��¼�ɹ�", Toast.LENGTH_SHORT).show();
					// ������Ҫ�����˺�����
					Config.cacheAccount(this, etAccount.getText().toString());
					Config.cachePassword(this,etPassword.getText().toString());
					Config.cacheNickname(this,nickname);
					Config.cacheUid(this, String.valueOf(uid));
					Config.cacheAvatart_url(this, avatar_url);
					
					SettingFragment.atMainActivity = 0;
					Intent intent = new Intent(AtyLogin.this, MainActivity.class);
					
					Bundle bundle = new Bundle();
					bundle.putString("nickname", nickname);
					bundle.putString("avatar_url", avatar_url);
					bundle.putInt("uid", uid);
					
					intent.putExtra("intentName", "AtyLogin");
					intent.putExtra("bundle", bundle);
					startActivity(intent);
					finish();
				} else if (1 == callBackCode) {
					pd.dismiss();
					Toast.makeText(getApplicationContext(), "�û������� ", Toast.LENGTH_SHORT).show();
				} else if (2 == callBackCode) {
					pd.dismiss();
					Toast.makeText(getApplicationContext(), "�������", Toast.LENGTH_SHORT).show();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void turn_to_regester() {
		Intent i = new Intent(AtyLogin.this, Regester.class);
		startActivity(i);
		finish();
	}

	private void forget_password() {
		Intent intent = new Intent(AtyLogin.this, Forget_password.class);
		startActivity(intent);
		finish();// �ѵ�ǰ�Ľ���
	}

	private void initView() {
		// TODO Auto-generated method stub
		icon_login = (ImageView) findViewById(R.id.icon_login);
		etAccount = (EditText) findViewById(R.id.app_login_account);
		etPassword = (EditText) findViewById(R.id.app_login_password);
		loginButton = (Button) findViewById(R.id.btn_login);
		regester = (TextView) findViewById(R.id.app_login_regester);
		forget_passwd = (TextView) findViewById(R.id.app_login_forget_password);
	}

}
