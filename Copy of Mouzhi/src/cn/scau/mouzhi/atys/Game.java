package cn.scau.mouzhi.atys;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Switch;
import cn.scau.mouzhi.aty.R;

public class Game extends Activity implements OnClickListener{
	private Switch switch0,switch1,switch2,switch3,switch4,switch5,switch6,switch7;
	private Button playGame;
	private int[] selected = {0,0,0,0,0,0,0,0};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.select_game);

		initView();
		
//		playGame.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				String number = String.valueOf(getNumber());
//				Intent intent = new Intent();
//				intent.putExtra("Number", number);
//				intent.setClass(Game.this, ShowGame.class);
//				startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(Game.this).toBundle());
//			}
//
//		});

		setListener();
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		switch0 = (Switch) findViewById(R.id.switch0);
		switch1 = (Switch) findViewById(R.id.switch1);
		switch2 = (Switch) findViewById(R.id.switch2);
		switch3 = (Switch) findViewById(R.id.switch3);
		switch4 = (Switch) findViewById(R.id.switch4);
		switch5 = (Switch) findViewById(R.id.switch5);
		switch6 = (Switch) findViewById(R.id.switch6);
		switch7 = (Switch) findViewById(R.id.switch7);
		playGame = (Button) findViewById(R.id.playGame);
	}
	
	private void setListener() {
		// TODO Auto-generated method stub
		switch0.setOnClickListener(this);
		switch1.setOnClickListener(this);
		switch2.setOnClickListener(this);
		switch3.setOnClickListener(this);
		switch4.setOnClickListener(this);
		switch5.setOnClickListener(this);
		switch6.setOnClickListener(this);
		switch7.setOnClickListener(this);
		playGame.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.switch0:
			selected[0] = (selected[0] == 0)?1:0;
			break;
		case R.id.switch1:
			selected[1] = (selected[1] == 0)?1:0;
			break;
		case R.id.switch2:
			selected[2] = (selected[2] == 0)?1:0;
			break;
		case R.id.switch3:
			selected[3] = (selected[3] == 0)?1:0;
			break;
		case R.id.switch4:
			selected[4] = (selected[4] == 0)?1:0;
			break;
		case R.id.switch5:
			selected[5] = (selected[5] == 0)?1:0;
			break;
		case R.id.switch6:
			selected[6] = (selected[6] == 0)?1:0;
			break;
		case R.id.switch7:
			selected[7] = (selected[7] == 0)?1:0;
			break;
		case R.id.playGame:
			String number = String.valueOf(getNumber());
			Intent intent = new Intent();
			intent.putExtra("Number", number);
			intent.setClass(Game.this, ShowGame.class);
			startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(Game.this).toBundle());
			break;
			
		}
	}
	
	private int getNumber() {
		// TODO Auto-generated method stub
		int number = 0;
		for(int i = 0;i < 8;i++){
			if(selected[i] == 1)
			number += Math.pow(2, i);
		}
		return number;
	}
}
