package cn.scau.mouzhi.atys;

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

public class Helping extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.help);

		Button send = (Button) findViewById(R.id.btnSendQuestion);
		final EditText edt = (EditText) findViewById(R.id.etQuestion);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("" == edt.getText().toString()) {
					Toast.makeText(Helping.this, "请输入查询的问题", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(Helping.this, "抱歉，您要查询的问题暂未能查到相关的消息", Toast.LENGTH_LONG).show();
					edt.clearComposingText();
				}
			}
		});

	}
}
