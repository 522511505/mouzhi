package cn.scau.mouzhi.atys;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Window;
import android.webkit.WebView;
import cn.scau.mouzhi.aty.R;

public class ShowGame extends Activity {

	private WebView webView;
	private Map map = new HashMap();
	private String[] game = new String[18];

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.show_game);

		setMap();

		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);

		Intent intent = getIntent();
		int number = Integer.parseInt(intent.getStringExtra("Number"));

//		Random random = new Random(47);
		int index = (int) (Math.random() * 18);
		if (number >= 0) {
			String game = (String) map.get(index);
			webView.loadUrl(game);
		}
	}

	private void setMap() {

		game[0] = "http://www.h5uc.com/play-9416.html";
		game[1] = "http://baiqiang.github.io/2048-3d";
		game[2] = "http://baiqiang.github.io/2048-double";
		game[3] = "http://baiqiang.github.io/2048-cross";
		game[4] = "http://baiqiang.github.io/2048-cross2";
		game[5] = "http://baiqiang.github.io/2048-hexagon";
		game[6] = "http://baiqiang.github.io/2048-hexagon2";
		game[7] = "http://baiqiang.github.io/2048-advanced";
		game[8] = "http://yx.h5uc.com/xiaoxueyuwenceshi/";
		game[9] = "http://www.h5uc.com/play-9317.html";
		game[10] = "http://www.h5uc.com/play-9318.html";
		game[11] = "http://www.h5uc.com/play-9408.html";
		game[12] = "http://www.h5uc.com/play-9405.html";
		game[13] = "http://www.h5uc.com/play-9394.html";
		game[14] = "http://www.h5uc.com/play-9399.html";
		game[15] = "http://www.h5uc.com/play-9390.html";
		game[16] = "http://www.h5uc.com/play-9379.html";
		game[17] = "http://www.h5uc.com/play-9398.html";
		
		for(int i = 0;i < 18;i++){
			map.put(i,game[i]);
		}
	}
}
