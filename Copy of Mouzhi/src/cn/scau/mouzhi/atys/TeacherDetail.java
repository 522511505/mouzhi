package cn.scau.mouzhi.atys;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Window;
import android.widget.TextView;
import cn.scau.mouzhi.aty.R;
import cn.scau.mouzhi.bean.Teacher;

public class TeacherDetail extends Activity {
	private TextView teacher_name, teacher_department, teacher_tel, teacher_courses;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//		getWindow().setEnterTransition(new Slide());
		setContentView(R.layout.teacher_info);

		initView();

		// ��׿4.0�Ժ�֧�����߳�����HTTP
		if (Build.VERSION.SDK_INT >= 11) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
					.detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}

		setView();
	}

	private void initView() {
		teacher_name = (TextView) findViewById(R.id.teacher_name);
		teacher_department = (TextView) findViewById(R.id.teacher_department);
		teacher_tel = (TextView) findViewById(R.id.teacher_tel);
		teacher_courses = (TextView) findViewById(R.id.teacher_courses);
	}

	private void setView() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Teacher teacher = (Teacher) intent.getSerializableExtra("Teacher");
		
		teacher_name.setText("������" + teacher.getName());
		teacher_department.setText("ѧԺ��" + teacher.getDepartment());
		if(teacher.getTel() == null){
			teacher_tel.setText("�绰���룺��");
		}else{
			teacher_tel.setText("�绰���룺" + teacher.getTel());
		}
		String[] courses = intent.getStringArrayExtra("course");
		String course = "";
		if(courses == null){
			teacher_courses.setText("��ѧ�γ̣���");
		}else{
			for (int i = 0; i < teacher.getCourse().size(); i++) {
				course += (String) courses[i] + ",";
			}
			
			course = "��ѧ�γ̣� " + course.substring(0, course.length() - 1);
			teacher_courses.setText(course);
		}
	}
}
