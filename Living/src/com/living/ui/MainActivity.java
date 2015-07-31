package com.living.ui;

import com.living.CustomApplcation;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * ������
 * 
 * @ClassName: MainActivity
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 ����2:45:35
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	// ����TabHost
	public TabHost mth;
	public static final String TAB_LEFT = "ÿ�վ�ѡ";
	public static final String TAB_RIGHT = "���ݷ���";
	public RadioGroup radioGroup;
	private RadioButton rbleft;
	private RadioButton rbright;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ʼ���ײ��˵�
		init();
		// �ײ��˵�����¼�
		clickevent();
	}

	/**
	 * ÿһ���ײ���ť����¼����л���Ӧ�Ľ���
	 */
	private void clickevent() {
		this.radioGroup = (RadioGroup) findViewById(R.id.main_radio);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// ���ݵ���İ�ť��ת����Ӧ�Ľ���
				switch (checkedId) {
				case R.id.radio_button0:
					mth.setCurrentTabByTag(TAB_LEFT);
					rbleft.setTextColor(Color.BLACK);
					rbright.setTextColor(Color.GRAY);
					break;
				case R.id.radio_button1:
					mth.setCurrentTabByTag(TAB_RIGHT);	
					rbright.setTextColor(Color.BLACK);
					rbleft.setTextColor(Color.GRAY);
					break;
				}
			}
		});
	}

	/**
	 * ʵ����TabHost,��TabHost���2������
	 */
	private void init() {
		// ʵ����TabHost
		mth = this.getTabHost();
		TabSpec ts1 = mth.newTabSpec(TAB_LEFT).setIndicator(TAB_LEFT);
		ts1.setContent(new Intent(MainActivity.this, DailyActivity.class));
		mth.addTab(ts1);// ��TabHost�е�һ���ײ��˵���ӽ���
		TabSpec ts2 = mth.newTabSpec(TAB_RIGHT).setIndicator(TAB_RIGHT);
		ts2.setContent(new Intent(MainActivity.this, AssortmentActivity.class));
		mth.addTab(ts2);
		rbleft=(RadioButton) findViewById(R.id.radio_button0);
		rbright=(RadioButton) findViewById(R.id.radio_button1);
		rbleft.setTextColor(Color.BLACK);
		rbright.setTextColor(Color.GRAY);
	}

	// �������ؼ�
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setCancelable(false)
					.setTitle("��ܰ��ʾ")
					.setMessage("��ȷ��Ҫ�˳���?")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									CustomApplcation.getInstance().logout();
									finish();
								}
							})
					.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
}
