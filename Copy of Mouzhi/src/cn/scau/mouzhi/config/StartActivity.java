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

		 String account = Config.getCachedAccount(this);//��û�����˺�
		 String password = Config.getCachedPassword(this);//��û��������
		 String nickname = Config.getCachedNickname(this);
		 String avatar_url = Config.getCachedAvatart_url(this);
//		 int uid = Config.getCachedUid(this);

		/*
		 * ���绺�����Ѿ������˺������ֱ����ת�� �����棬�������ת����¼����
		 */
		if (account != null && password != null) { // ���綼��Ϊ��
//			Intent i = new Intent(this, MainActivity.class);
//			i.putExtra(Config.KEY_ACCOUNT, account); // ��������Ϣ�Ž�intent
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
			startActivity(new Intent(this, AtyLogin.class)); // ������¼����
		}

		finish();
	}

}
