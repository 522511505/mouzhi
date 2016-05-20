package cn.scau.mouzhi.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.atys.AtyLogin;
import cn.scau.mouzhi.atys.MainActivity;
import cn.scau.mouzhi.atys.WelcomeActivity;
import cn.scau.mouzhi.frag.SettingFragment;

public class SlashActivity extends Activity {

	public static int userid;
	public static String account;
	public static String password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.welcome);

		String account = Config.getCachedAccount(this);// 获得缓存的账号
		String password = Config.getCachedPassword(this);// 获得缓存的密码
		this.account = account;
		this.password = password;
		final String nickname = Config.getCachedNickname(this);
		final String avatar_url = Config.getCachedAvatart_url(this);
		final String str_uid = Config.getCachedUid(this);
		
		if (account != null && password != null) { // 假如都不为空
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					SettingFragment.atMainActivity = 0;
					Intent intent = new Intent(SlashActivity.this, MainActivity.class);

					Bundle bundle = new Bundle();
					bundle.putString("nickname", nickname);
					bundle.putString("avatar_url", avatar_url);
//					int uid = Integer.parseInt(str_uid);
					bundle.putString("uid", str_uid);
					userid = Integer.parseInt(str_uid);

					intent.putExtra("intentName", "AtyLogin");
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
			}, 2000);
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					MainActivity.flag = 0;
					Intent intent = new Intent(SlashActivity.this, MainActivity.class);
					intent.putExtra("intentName", "WelcomeActivity");
					startActivity(intent);
					overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					SlashActivity.this.finish();
				}
			}, 2000);
		}
	}
}