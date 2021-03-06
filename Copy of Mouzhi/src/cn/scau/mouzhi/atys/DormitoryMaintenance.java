package cn.scau.mouzhi.atys;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Window;
import android.webkit.WebView;
import cn.scau.mouzhi.aty.R;

public class DormitoryMaintenance extends Activity {
	private WebView webView;
	private String url = "http://202.116.162.76:3000/studentlogin.html";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.show_game);

		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		
		webView.loadUrl(url);
	}
}
