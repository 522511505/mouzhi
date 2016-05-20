package cn.scau.mouzhi.atys;

import com.gallery.FamousPeople;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TableRow;
import cn.scau.mouzhi.aty.R;

public class Laboratory extends Activity {
	private TableRow search_teacher,game,schoolbus,empty_class,dormitory_maintenance,internet_maintenance,famous_people;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.laboratory);
		
		initView();
		
		addListener();
	}
	
	private void initView(){
		search_teacher = (TableRow) findViewById(R.id.search_teacher);
		game =  (TableRow)findViewById(R.id.game);
		schoolbus =  (TableRow)findViewById(R.id.schoolbus);
		empty_class =  (TableRow)findViewById(R.id.empty_class);
		dormitory_maintenance =  (TableRow)findViewById(R.id.dormitory_maintenance);
		internet_maintenance =  (TableRow)findViewById(R.id.internet_maintenance);
		famous_people =  (TableRow)findViewById(R.id.famous_people);
	}
	
	private void addListener() {
		// TODO Auto-generated method stub
		search_teacher.setOnClickListener(new MyClickListener());
		game.setOnClickListener(new MyClickListener());
		schoolbus.setOnClickListener(new MyClickListener());
		empty_class.setOnClickListener(new MyClickListener());
		dormitory_maintenance.setOnClickListener(new MyClickListener());
		internet_maintenance.setOnClickListener(new MyClickListener());
		famous_people.setOnClickListener(new MyClickListener());
	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.search_teacher:
				Intent i0 = new Intent();
				i0.setClass(Laboratory.this, Search.class);
				i0.putExtra("ActivityName", "SearchTeacher");
				startActivity(i0,ActivityOptions.makeSceneTransitionAnimation(Laboratory.this).toBundle());
				break;
			case R.id.game:
				startActivity(new Intent(Laboratory.this, Game.class),ActivityOptions.makeSceneTransitionAnimation(Laboratory.this).toBundle());
				break;
			case R.id.schoolbus:
				startActivity(new Intent(Laboratory.this, SchoolBus.class),ActivityOptions.makeSceneTransitionAnimation(Laboratory.this).toBundle());
				break;
			case R.id.empty_class:
				startActivity(new Intent(Laboratory.this, EmptyClass.class),ActivityOptions.makeSceneTransitionAnimation(Laboratory.this).toBundle());
				break;
			case R.id.dormitory_maintenance:
				startActivity(new Intent(Laboratory.this, DormitoryMaintenance.class),ActivityOptions.makeSceneTransitionAnimation(Laboratory.this).toBundle());
				break;
			case R.id.internet_maintenance:
				startActivity(new Intent(Laboratory.this, InternetMaintenance.class),ActivityOptions.makeSceneTransitionAnimation(Laboratory.this).toBundle());
				break;
			case R.id.famous_people:
				startActivity(new Intent(Laboratory.this, FamousPeople.class));
				break;
			default:
				break;
			}
		}
	}
}
