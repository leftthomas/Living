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
 * 主界面
 * 
 * @ClassName: MainActivity
 * @Description: TODO
 * @author smile
 * @date 2014-5-29 下午2:45:35
 */
@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	// 创建TabHost
	public TabHost mth;
	public static final String TAB_LEFT = "每日精选";
	public static final String TAB_RIGHT = "房屋分类";
	public RadioGroup radioGroup;
	private RadioButton rbleft;
	private RadioButton rbright;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化底部菜单
		init();
		// 底部菜单点击事件
		clickevent();
	}

	/**
	 * 每一个底部按钮点击事件，切换相应的界面
	 */
	private void clickevent() {
		this.radioGroup = (RadioGroup) findViewById(R.id.main_radio);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 根据点击的按钮跳转到相应的界面
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
	 * 实例化TabHost,往TabHost添加2个界面
	 */
	private void init() {
		// 实例化TabHost
		mth = this.getTabHost();
		TabSpec ts1 = mth.newTabSpec(TAB_LEFT).setIndicator(TAB_LEFT);
		ts1.setContent(new Intent(MainActivity.this, DailyActivity.class));
		mth.addTab(ts1);// 往TabHost中第一个底部菜单添加界面
		TabSpec ts2 = mth.newTabSpec(TAB_RIGHT).setIndicator(TAB_RIGHT);
		ts2.setContent(new Intent(MainActivity.this, AssortmentActivity.class));
		mth.addTab(ts2);
		rbleft=(RadioButton) findViewById(R.id.radio_button0);
		rbright=(RadioButton) findViewById(R.id.radio_button1);
		rbleft.setTextColor(Color.BLACK);
		rbright.setTextColor(Color.GRAY);
	}

	// 监听返回键
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setCancelable(false)
					.setTitle("温馨提示")
					.setMessage("您确定要退出吗?")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									CustomApplcation.getInstance().logout();
									finish();
								}
							})
					.setNegativeButton("取消",
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
