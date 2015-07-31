package com.living.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.bmob.im.BmobChat;
import com.living.config.Config;

/**
 * ����ҳ
 * 
 * @ClassName: SplashActivity
 * @Description: TODO
 * @author smile
 * @date 2014-6-4 ����9:45:43
 */
public class SplashActivity extends BaseActivity {

	private static final int GO_HOME = 100;
	private static final int GO_LOGIN = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// �����õ���ģʽ����Ϊtrue��ʱ�򣬻���logcat��BmobChat�����һЩ��־���������ͷ����Ƿ��������У��������˷��ش���Ҳ��һ����ӡ���������㿪���ߵ���
		BmobChat.DEBUG_MODE = true;
		BmobChat.getInstance(this).init(Config.applicationId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (userManager.getCurrentUser() != null) {
			mHandler.sendEmptyMessageDelayed(GO_HOME, 2000); // ����Ѿ���¼����
		} else {
			mHandler.sendEmptyMessageDelayed(GO_LOGIN, 2000); // ����
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				startAnimActivity(MainActivity.class);
				finish();
				break;
			case GO_LOGIN:
				startAnimActivity(LoginActivity.class);
				finish();
				break;
			}
		}
	};

}
