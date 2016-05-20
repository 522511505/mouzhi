package cn.scau.mouzhi.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.scau.mouzhi.atys.AtyLogin;
import cn.scau.mouzhi.atys.MainActivity;
import cn.scau.mouzhi.frag.SettingFragment;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		 String account = Config.getCachedAccount(this);//获得缓存的账号
		 String password = Config.getCachedPassword(this);//获得缓存的密码
		 String nickname = Config.getCachedNickname(this);
		 String avatar_url = Config.getCachedAvatart_url(this);
//		 int uid = Config.getCachedUid(this);

		/*
		 * 假如缓存中已经有了账号密码就直接跳转到 主界面，否则就跳转到登录界面
		 */
		if (account != null && password != null) { // 假如都不为空
//			Intent i = new Intent(this, MainActivity.class);
//			i.putExtra(Config.KEY_ACCOUNT, account); // 将两个信息放进intent
//			i.putExtra(Config.KEY_PASSWORD, password);
//			startActivity(i);
			
			SettingFragment.atMainActivity = 0;
			Intent intent = new Intent(StartActivity.this, MainActivity.class);
			
			Bundle bundle = new Bundle();
			bundle.putString("nickname", nickname);
			bundle.putString("avatar_url", avatar_url);
//			bundle.putInt("uid", uid);
			
			intent.putExtra("intentName", "AtyLogin");
			intent.putExtra("bundle", bundle);
			startActivity(intent);
		}
		else {
			startActivity(new Intent(this, AtyLogin.class)); // 跳到登录界面
		}

		finish();
	}

}
