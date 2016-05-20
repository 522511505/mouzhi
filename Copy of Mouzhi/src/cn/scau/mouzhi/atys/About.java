package cn.scau.mouzhi.atys;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Window;
import cn.scau.mouzhi.aty.R;

public class About extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		
		setContentView(R.layout.about);
	}
}